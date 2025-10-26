package com.chessrobot

import com.github.bhlangonijr.chesslib.Board
import com.github.bhlangonijr.chesslib.Side
import com.github.bhlangonijr.chesslib.Square
import com.github.bhlangonijr.chesslib.move.Move
import com.github.bhlangonijr.chesslib.move.MoveGenerator
import kotlin.random.Random

/**
 * Simple chess engine implementation based on the Python code
 * Uses basic AI with configurable difficulty levels
 */
class SimpleChessEngine {
    
    companion object {
        private const val STANDARD_CHESS_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"
    }
    
    val board = Board()
    private var skillLevel = 1 // 0 = easy, 1 = medium, 2 = hard, 3 = expert
    
    fun reset() {
        board.loadFromFen(STANDARD_CHESS_FEN)
    }
    
    fun setDifficulty(level: Int) {
        skillLevel = when (level) {
            0 -> 0 // Easy
            1 -> 1 // Medium
            2 -> 2 // Hard
            3 -> 3 // Expert
            else -> 1
        }
    }
    
    fun isPlayerTurn(): Boolean {
        return board.sideToMove == Side.WHITE
    }
    
    fun makePlayerMove(fromSquare: Int, toSquare: Int): Boolean {
        val from = Square.squareAt(fromSquare)
        val to = Square.squareAt(toSquare)
        val move = Move(from, to)
        
        // Check for promotion - default to queen
        val legalMoves = MoveGenerator.generateLegalMoves(board)
        val actualMove = legalMoves.firstOrNull { 
            it.from == from && it.to == to 
        }
        
        if (actualMove != null) {
            board.doMove(actualMove)
            return true
        }
        
        return false
    }
    
    fun makeRobotMove() {
        if (board.isDraw || board.isMated) return
        
        val legalMoves = MoveGenerator.generateLegalMoves(board)
        if (legalMoves.isEmpty()) return
        
        val bestMove = when (skillLevel) {
            0 -> selectRandomMove(legalMoves)
            1 -> selectMediumMove(legalMoves)
            2 -> selectHardMove(legalMoves)
            3 -> selectExpertMove(legalMoves)
            else -> selectRandomMove(legalMoves)
        }
        
        board.doMove(bestMove)
    }
    
    private fun selectRandomMove(moves: List<Move>): Move {
        return moves[Random.nextInt(moves.size)]
    }
    
    private fun selectMediumMove(moves: List<Move>): Move {
        // Try to find capturing moves or good positional moves
        val captureMoves = moves.filter { move ->
            board.getPiece(move.to) != com.github.bhlangonijr.chesslib.Piece.NONE
        }
        
        if (captureMoves.isNotEmpty() && Random.nextFloat() > 0.3f) {
            return captureMoves[Random.nextInt(captureMoves.size)]
        }
        
        return selectRandomMove(moves)
    }
    
    private fun selectHardMove(moves: List<Move>): Move {
        // Evaluate moves with basic scoring
        val scoredMoves = moves.map { move ->
            val score = evaluateMove(move)
            move to score
        }
        
        val sortedMoves = scoredMoves.sortedByDescending { it.second }
        
        // Pick from top 3 moves with some randomness
        val topMoves = sortedMoves.take(3)
        return topMoves[Random.nextInt(topMoves.size)].first
    }
    
    private fun selectExpertMove(moves: List<Move>): Move {
        // More sophisticated evaluation
        val scoredMoves = moves.map { move ->
            val score = evaluateMoveDeep(move)
            move to score
        }
        
        val sortedMoves = scoredMoves.sortedByDescending { it.second }
        return sortedMoves.first().first
    }
    
    private fun evaluateMove(move: Move): Int {
        var score = 0
        
        // Capture value
        val capturedPiece = board.getPiece(move.to)
        score += getPieceValue(capturedPiece)
        
        // Center control
        if (move.to in listOf(Square.E4, Square.E5, Square.D4, Square.D5)) {
            score += 10
        }
        
        return score
    }
    
    private fun evaluateMoveDeep(move: Move): Int {
        var score = evaluateMove(move)
        
        // Simulate move and evaluate position
        board.doMove(move)
        
        // Simple material count
        for (square in Square.values()) {
            val piece = board.getPiece(square)
            if (piece != com.github.bhlangonijr.chesslib.Piece.NONE) {
                val value = getPieceValue(piece)
                if (piece.pieceSide == Side.BLACK) {
                    score += value
                } else {
                    score -= value / 2  // Penalize opponent's material less
                }
            }
        }
        
        // Check if move gives check
        if (board.isKingAttacked) {
            score += 20
        }
        
        board.undoMove()
        
        return score
    }
    
    private fun getPieceValue(piece: com.github.bhlangonijr.chesslib.Piece): Int {
        return when (piece) {
            com.github.bhlangonijr.chesslib.Piece.WHITE_PAWN,
            com.github.bhlangonijr.chesslib.Piece.BLACK_PAWN -> 10
            
            com.github.bhlangonijr.chesslib.Piece.WHITE_KNIGHT,
            com.github.bhlangonijr.chesslib.Piece.BLACK_KNIGHT,
            com.github.bhlangonijr.chesslib.Piece.WHITE_BISHOP,
            com.github.bhlangonijr.chesslib.Piece.BLACK_BISHOP -> 30
            
            com.github.bhlangonijr.chesslib.Piece.WHITE_ROOK,
            com.github.bhlangonijr.chesslib.Piece.BLACK_ROOK -> 50
            
            com.github.bhlangonijr.chesslib.Piece.WHITE_QUEEN,
            com.github.bhlangonijr.chesslib.Piece.BLACK_QUEEN -> 90
            
            com.github.bhlangonijr.chesslib.Piece.WHITE_KING,
            com.github.bhlangonijr.chesslib.Piece.BLACK_KING -> 900
            
            else -> 0
        }
    }
    
    fun isGameOver(): Boolean {
        return board.isDraw || board.isMated
    }
    
    fun getGameResult(): String {
        return when {
            board.isMated && board.sideToMove == Side.BLACK -> "1-0"  // White wins
            board.isMated && board.sideToMove == Side.WHITE -> "0-1"  // Black wins
            board.isDraw -> "1/2-1/2"  // Draw
            else -> "*"  // Game not over
        }
    }
}
