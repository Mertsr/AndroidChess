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
        // Medium: Prioritize captures and basic tactics
        val scoredMoves = moves.map { move ->
            var score = 0
            
            // Strongly prefer capturing moves
            val capturedPiece = board.getPiece(move.to)
            if (capturedPiece != com.github.bhlangonijr.chesslib.Piece.NONE) {
                score += getPieceValue(capturedPiece) * 2
            }
            
            // Prefer center control
            if (move.to in listOf(Square.E4, Square.E5, Square.D4, Square.D5, 
                                   Square.C4, Square.C5, Square.F4, Square.F5)) {
                score += 15
            }
            
            // Add some randomness to make it less predictable
            score += Random.nextInt(20)
            
            move to score
        }
        
        val sortedMoves = scoredMoves.sortedByDescending { it.second }
        // Pick from top 5 moves with some randomness
        val topMoves = sortedMoves.take(5)
        return topMoves[Random.nextInt(topMoves.size)].first
    }
    
    private fun selectHardMove(moves: List<Move>): Move {
        // Hard: Better evaluation with threats and piece development
        val scoredMoves = moves.map { move ->
            var score = 0
            
            // Evaluate capture value
            val capturedPiece = board.getPiece(move.to)
            score += getPieceValue(capturedPiece) * 3
            
            // Simulate move and check for threats
            board.doMove(move)
            
            // Bonus for checks
            if (board.isKingAttacked) {
                score += 40
            }
            
            // Evaluate material balance
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
            score += materialBalance / 5
            
            // Center control
            if (move.to in listOf(Square.E4, Square.E5, Square.D4, Square.D5)) {
                score += 25
            }
            
            // Piece development (move pieces from back rank)
            if (move.from.rank == com.github.bhlangonijr.chesslib.Rank.RANK_8 || 
                move.from.rank == com.github.bhlangonijr.chesslib.Rank.RANK_7) {
                score += 10
            }
            
            board.undoMove()
            
            // Add small randomness
            score += Random.nextInt(10)
            
            move to score
        }
        
        val sortedMoves = scoredMoves.sortedByDescending { it.second }
        // Pick from top 2 moves
        val topMoves = sortedMoves.take(2)
        return topMoves[Random.nextInt(topMoves.size)].first
    }
    
    private fun selectExpertMove(moves: List<Move>): Move {
        // Expert: Deep evaluation with positional understanding
        val scoredMoves = moves.map { move ->
            var score = evaluateMoveDeep(move)
            
            // Additional expert-level analysis
            board.doMove(move)
            
            // Look ahead one more move for opponent responses
            val opponentMoves = MoveGenerator.generateLegalMoves(board)
            if (opponentMoves.isNotEmpty()) {
                // Find opponent's best response
                val bestOpponentScore = opponentMoves.maxOfOrNull { oppMove ->
                    board.doMove(oppMove)
                    val capturedValue = getPieceValue(board.getPiece(oppMove.to))
                    board.undoMove()
                    capturedValue
                } ?: 0
                // Penalize moves that allow strong opponent responses
                score -= bestOpponentScore / 2
            }
            
            // King safety
            val kingSquare = if (board.sideToMove == Side.WHITE) {
                board.getPieceLocation(com.github.bhlangonijr.chesslib.Piece.WHITE_KING).firstOrNull()
            } else {
                board.getPieceLocation(com.github.bhlangonijr.chesslib.Piece.BLACK_KING).firstOrNull()
            }
            
            if (kingSquare != null) {
                // Prefer keeping king safe
                val kingFile = kingSquare.file.ordinal
                if (kingFile in 0..2 || kingFile in 5..7) {
                    score += 15  // King on side is safer
                }
            }
            
            board.undoMove()
            
            move to score
        }
        
        val sortedMoves = scoredMoves.sortedByDescending { it.second }
        // Always pick the best move (no randomness at expert level)
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
        
        // Capture value with multiplier
        val capturedPiece = board.getPiece(move.to)
        score += getPieceValue(capturedPiece) * 4
        
        // Simulate move and evaluate position
        board.doMove(move)
        
        // Material count with better weighting
        var materialBalance = 0
        var pieceActivity = 0
        
        for (square in Square.values()) {
            val piece = board.getPiece(square)
            if (piece != com.github.bhlangonijr.chesslib.Piece.NONE) {
                val value = getPieceValue(piece)
                
                if (piece.pieceSide == Side.BLACK) {
                    materialBalance += value
                    // Reward active piece placement
                    if (square.rank.ordinal >= 3 && square.rank.ordinal <= 5) {
                        pieceActivity += 5  // Black pieces advancing
                    }
                } else {
                    materialBalance -= value
                    // Penalize opponent active pieces
                    if (square.rank.ordinal >= 2 && square.rank.ordinal <= 4) {
                        pieceActivity -= 3
                    }
                }
            }
        }
        
        score += materialBalance / 3
        score += pieceActivity
        
        // Check if move gives check (strong bonus)
        if (board.isKingAttacked) {
            score += 50
        }
        
        // Bonus for controlling center squares
        val controlledCenterSquares = listOf(
            Square.E4, Square.E5, Square.D4, Square.D5,
            Square.C4, Square.C5, Square.F4, Square.F5
        ).count { sq -> 
            val piece = board.getPiece(sq)
            piece != com.github.bhlangonijr.chesslib.Piece.NONE && 
            piece.pieceSide == Side.BLACK
        }
        score += controlledCenterSquares * 8
        
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
