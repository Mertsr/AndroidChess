package com.chessrobot

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.github.bhlangonijr.chesslib.Board
import com.github.bhlangonijr.chesslib.Piece
import com.github.bhlangonijr.chesslib.Square

class ChessBoardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var board: Board? = null
    private var selectedSquare: Int? = null
    private var moveCallback: ((Int, Int) -> Unit)? = null

    private val lightSquarePaint = Paint().apply {
        color = Color.parseColor("#FFCE9E")
        style = Paint.Style.FILL
    }

    private val darkSquarePaint = Paint().apply {
        color = Color.parseColor("#D18B47")
        style = Paint.Style.FILL
    }

    private val selectedSquarePaint = Paint().apply {
        color = Color.parseColor("#88FF00")
        alpha = 128
        style = Paint.Style.FILL
    }

    private val textPaint = Paint().apply {
        color = Color.BLACK
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }

    private val piecePaint = Paint().apply {
        color = Color.BLACK
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }

    private var squareSize = 0f

    fun setBoard(board: Board) {
        this.board = board
        selectedSquare = null
        invalidate()
    }

    fun setMoveCallback(callback: (Int, Int) -> Unit) {
        this.moveCallback = callback
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val size = Math.min(
            MeasureSpec.getSize(widthMeasureSpec),
            MeasureSpec.getSize(heightMeasureSpec)
        )
        setMeasuredDimension(size, size)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        squareSize = w / 8f
        textPaint.textSize = squareSize * 0.3f
        piecePaint.textSize = squareSize * 0.7f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (board == null) return

        // Draw squares
        for (rank in 0..7) {
            for (file in 0..7) {
                val isLight = (rank + file) % 2 == 0
                val paint = if (isLight) lightSquarePaint else darkSquarePaint
                
                val left = file * squareSize
                val top = (7 - rank) * squareSize
                val rect = RectF(left, top, left + squareSize, top + squareSize)
                
                canvas.drawRect(rect, paint)

                // Highlight selected square
                val squareIndex = rank * 8 + file
                if (squareIndex == selectedSquare) {
                    canvas.drawRect(rect, selectedSquarePaint)
                }

                // Draw piece
                val square = Square.squareAt(squareIndex)
                val piece = board!!.getPiece(square)
                
                if (piece != Piece.NONE) {
                    val pieceSymbol = getPieceSymbol(piece)
                    canvas.drawText(
                        pieceSymbol,
                        left + squareSize / 2,
                        top + squareSize / 2 + piecePaint.textSize / 3,
                        piecePaint
                    )
                }
            }
        }

        // Draw coordinates
        for (i in 0..7) {
            // Files (a-h)
            val file = ('a' + i).toString()
            canvas.drawText(
                file,
                i * squareSize + squareSize / 2,
                height - 10f,
                textPaint.apply { textSize = squareSize * 0.2f }
            )

            // Ranks (1-8)
            val rank = (i + 1).toString()
            canvas.drawText(
                rank,
                10f,
                (7 - i) * squareSize + squareSize / 2,
                textPaint.apply { textSize = squareSize * 0.2f }
            )
        }
        
        // Restore text size
        piecePaint.textSize = squareSize * 0.7f
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isEnabled || board == null) return false

        if (event.action == MotionEvent.ACTION_DOWN) {
            val file = (event.x / squareSize).toInt()
            val rank = 7 - (event.y / squareSize).toInt()

            if (file in 0..7 && rank in 0..7) {
                val squareIndex = rank * 8 + file

                if (selectedSquare == null) {
                    // Select piece
                    val square = Square.squareAt(squareIndex)
                    val piece = board!!.getPiece(square)
                    
                    if (piece != Piece.NONE && piece.pieceSide == board!!.sideToMove) {
                        selectedSquare = squareIndex
                        invalidate()
                    }
                } else {
                    // Make move
                    if (selectedSquare != squareIndex) {
                        moveCallback?.invoke(selectedSquare!!, squareIndex)
                    }
                    selectedSquare = null
                    invalidate()
                }
            }
            return true
        }

        return super.onTouchEvent(event)
    }

    private fun getPieceSymbol(piece: Piece): String {
        return when (piece) {
            Piece.WHITE_PAWN -> "♙"
            Piece.WHITE_KNIGHT -> "♘"
            Piece.WHITE_BISHOP -> "♗"
            Piece.WHITE_ROOK -> "♖"
            Piece.WHITE_QUEEN -> "♕"
            Piece.WHITE_KING -> "♔"
            Piece.BLACK_PAWN -> "♟"
            Piece.BLACK_KNIGHT -> "♞"
            Piece.BLACK_BISHOP -> "♝"
            Piece.BLACK_ROOK -> "♜"
            Piece.BLACK_QUEEN -> "♛"
            Piece.BLACK_KING -> "♚"
            else -> ""
        }
    }
}
