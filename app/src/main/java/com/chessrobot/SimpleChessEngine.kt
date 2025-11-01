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
        // Medium: Strong material evaluation with basic tactics
        val scoredMoves = moves.map { move ->
            var score = 0
            
            // Heavily prioritize good captures
            val capturedPiece = board.getPiece(move.to)
            val captureValue = getPieceValue(capturedPiece)
            
            board.doMove(move)
            
            // Check if our piece is now attacked
            val movingPiece = board.getPiece(move.to)
            val ourPieceValue = getPieceValue(movingPiece)
            
            // Simple exchange evaluation: capture value - risk
            if (captureValue > 0) {
                // Good capture if we gain material
                if (captureValue >= ourPieceValue) {
                    score += captureValue * 10  // Winning or equal exchange
                } else {
                    score += (captureValue - ourPieceValue / 2)  // Losing exchange is bad
                }
            }
            
            // Check bonus
            if (board.isKingAttacked) {
                score += 80
            }
            
            // Mobility bonus (number of legal moves after this move)
            val mobilityAfter = MoveGenerator.generateLegalMoves(board).size
            score += mobilityAfter
            
            board.undoMove()
            
            // Center control
            if (move.to in listOf(Square.E4, Square.E5, Square.D4, Square.D5)) {
                score += 30
            }
            
            // Small randomness for variety
            score += Random.nextInt(5)
            
            move to score
        }
        
        val sortedMoves = scoredMoves.sortedByDescending { it.second }
        // Pick from top 3 moves
        val topMoves = sortedMoves.take(3).ifEmpty { scoredMoves.take(1) }
        return topMoves[Random.nextInt(topMoves.size)].first
    }
    
    private fun selectHardMove(moves: List<Move>): Move {
        // Hard: Full position evaluation with tactical awareness
        val scoredMoves = moves.map { move ->
            var score = evaluatePositionAfterMove(move)
            
            // Minimal randomness
            score += Random.nextInt(3)
            
            move to score
        }
        
        val sortedMoves = scoredMoves.sortedByDescending { it.second }
        // Always pick best move or second best
        return if (sortedMoves.size > 1 && Random.nextFloat() < 0.2f) {
            sortedMoves[1].first  // 20% chance to pick second best for variety
        } else {
            sortedMoves.first().first
        }
    }
    
    private fun evaluatePositionAfterMove(move: Move): Int {
        var score = 0
        
        // Capture value
        val capturedPiece = board.getPiece(move.to)
        val captureValue = getPieceValue(capturedPiece)
        score += captureValue * 10
        
        board.doMove(move)
        
        // Check bonus
        if (board.isKingAttacked) {
            score += 100
        }
        
        // Material count (total material advantage)
        var materialBalance = 0
        for (square in Square.values()) {
            val piece = board.getPiece(square)
            if (piece != com.github.bhlangonijr.chesslib.Piece.NONE) {
                val value = getPieceValue(piece)
                if (piece.pieceSide == Side.BLACK) {
                    materialBalance += value
                } else {
                    materialBalance -= value
                }
            }
        }
        score += materialBalance
        
        // Mobility (how many moves do we have)
        val mobility = MoveGenerator.generateLegalMoves(board).size
        score += mobility * 2
        
        // Center control
        val centerSquares = listOf(Square.E4, Square.E5, Square.D4, Square.D5)
        for (sq in centerSquares) {
            val piece = board.getPiece(sq)
            if (piece != com.github.bhlangonijr.chesslib.Piece.NONE) {
                if (piece.pieceSide == Side.BLACK) {
                    score += 20
                } else {
                    score -= 10
                }
            }
        }
        
        // Piece development (pieces off back rank)
        var developedPieces = 0
        for (file in listOf(com.github.bhlangonijr.chesslib.File.FILE_B, 
                           com.github.bhlangonijr.chesslib.File.FILE_C,
                           com.github.bhlangonijr.chesslib.File.FILE_D,
                           com.github.bhlangonijr.chesslib.File.FILE_E,
                           com.github.bhlangonijr.chesslib.File.FILE_F,
                           com.github.bhlangonijr.chesslib.File.FILE_G)) {
            val backRankSquare = Square.encode(com.github.bhlangonijr.chesslib.Rank.RANK_8, file)
            val piece = board.getPiece(backRankSquare)
            if (piece == com.github.bhlangonijr.chesslib.Piece.NONE || 
                piece == com.github.bhlangonijr.chesslib.Piece.BLACK_KING ||
                piece == com.github.bhlangonijr.chesslib.Piece.BLACK_ROOK) {
                developedPieces++
            }
        }
        score += developedPieces * 5
        
        board.undoMove()
        
        return score
    }
    
    private fun selectExpertMove(moves: List<Move>): Move {
        // Expert: Minimax with alpha-beta pruning (2 ply search)
        var bestMove = moves.first()
        var bestScore = Int.MIN_VALUE
        
        for (move in moves) {
            board.doMove(move)
            
            // Evaluate opponent's best response
            val score = -minimaxSearch(1, Int.MIN_VALUE, Int.MAX_VALUE)
            
            board.undoMove()
            
            if (score > bestScore) {
                bestScore = score
                bestMove = move
            }
        }
        
        return bestMove
    }
    
    private fun minimaxSearch(depth: Int, alpha: Int, beta: Int): Int {
        if (depth == 0 || board.isDraw || board.isMated) {
            return evaluatePosition()
        }
        
        val moves = MoveGenerator.generateLegalMoves(board)
        if (moves.isEmpty()) return evaluatePosition()
        
        var maxScore = Int.MIN_VALUE
        var alphaValue = alpha
        
        for (move in moves) {
            board.doMove(move)
            val score = -minimaxSearch(depth - 1, -beta, -alphaValue)
            board.undoMove()
            
            if (score > maxScore) {
                maxScore = score
            }
            
            alphaValue = maxOf(alphaValue, score)
            if (alphaValue >= beta) {
                break  // Beta cutoff
            }
        }
        
        return maxScore
    }
    
    private fun evaluatePosition(): Int {
        // Complete position evaluation
        var score = 0
        
        // Check for game over
        if (board.isMated) {
            return if (board.sideToMove == Side.BLACK) -100000 else 100000
        }
        if (board.isDraw) return 0
        
        // Material count
        for (square in Square.values()) {
            val piece = board.getPiece(square)
            if (piece != com.github.bhlangonijr.chesslib.Piece.NONE) {
                val value = getPieceValue(piece)
                if (piece.pieceSide == Side.BLACK) {
                    score += value
                } else {
                    score -= value
                }
            }
        }
        
        // Mobility
        val mobility = MoveGenerator.generateLegalMoves(board).size
        score += if (board.sideToMove == Side.BLACK) mobility * 3 else -mobility * 3
        
        // Center control
        for (sq in listOf(Square.E4, Square.E5, Square.D4, Square.D5)) {
            val piece = board.getPiece(sq)
            if (piece != com.github.bhlangonijr.chesslib.Piece.NONE) {
                val bonus = 15
                if (piece.pieceSide == Side.BLACK) {
                    score += bonus
                } else {
                    score -= bonus
                }
            }
        }
        
        // King safety
        val blackKing = board.getPieceLocation(com.github.bhlangonijr.chesslib.Piece.BLACK_KING).firstOrNull()
        val whiteKing = board.getPieceLocation(com.github.bhlangonijr.chesslib.Piece.WHITE_KING).firstOrNull()
        
        if (blackKing != null) {
            val file = blackKing.file.ordinal
            if (file in 0..2 || file in 5..7) score += 10
        }
        if (whiteKing != null) {
            val file = whiteKing.file.ordinal
            if (file in 0..2 || file in 5..7) score -= 10
        }
        
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
