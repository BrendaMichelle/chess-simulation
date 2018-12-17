package chess;
/**
 * <h1>Piece class: a super class </h1>
 * <b>Details:</b> This class is a super class for Knight,
 * Pawn, Queen, Rook, King and Bishop.
 * 
 * @author BrendaRios
 * @author royjacome
 * @since   2018-02-23
 */

public abstract class Piece {
	 String splayer; //player "black" versus player "white"
	 int moves_made = 0; //the number of moves made in the game
	 String piece_name; //how it's represented on the board
	 private boolean moved = false; // For Castling or En Passant
	 private boolean attackPathVisible; 
	 int piece_xCoordinate, piece_yCoordinate;

	
	public Piece (String splayer, String player_piece) {
		this.splayer = splayer;
		this.piece_name = player_piece;
	}

	public boolean equals(Object o) {
		if(o != null || !(o instanceof Piece)) {
			return false;
		}
		return true;
	}
	
	/*
	 * This method returns the player;
	 * Whether it is white or black.
	 * 
	 */
	public String getPlayer() {
		return splayer;
	}
	
	public boolean getMoved() {
		return moved;
	}
	
	/*
	 * This method describes whether or
	 * not the piece has been moved yet. 
	 * Moved is automatically set to false
	 * upon the creation of a piece.
	 * 
	 * @param cond This is set to true upon movement.
	 */
	public void setMoved(boolean cond) {
		moved = cond;
	}


	public boolean getAttackPathVisible() {
		return attackPathVisible;
	}
	/*
	 * This method determines whether or not a 
	 * piece's attack path is to be visible or not.
	 * 
	 * @param cond This is true if you desire for it to be invisible, false otherwise
	 */
	public void setAttackPathVisible(boolean cond) {
		attackPathVisible = cond;
	}
	
	/*This method checks whether or not a piece has eaten
	 * another piece based on its move.
	 * 
	 * @param chessBoard This is board's cells'
	 * @param new_xCoordinate This is the x coordinate that the piece is moving to.
	 * @param new_yCoordinate This is the y coordinate that the piece is moving to.
	 * @return boolean True if the piece at another piece, false otherwise.
	 */
	public abstract boolean eating(Board board, int new_xCoordinate, int new_yCoordinate);
	
	/*
	 * This method does the actually move handling for each piece.
	 * 
	 * @param board This is just the actual board from which we access its cells
	 * @param new_xCoordinate This is the x coordinate that the piece is moving to.
	 * @param new_yCoordinate This is the y coordinate that the piece is moving to.
	 * @return boolean True if the move was made successfully, false otherwise.
	 */
	public abstract boolean moving(Board board, int new_xCoordinate, int new_yCoordinate);
	
	/*
	 * This method checks whether a piece's desired move is legal based
	 * on the piece's allowances and position.
	 * 
	 * @param chessBoard This is board's cells'
	 * @param new_xCoordinate This is the x coordinate that the piece is moving to.
	 * @param new_yCoordinate This is the y coordinate that the piece is moving to.
	 * @return boolean True if the move is valid, false otherwise.
	 */
	public abstract boolean checkValidMove(BoardCell[][] chessBoard, int new_xCoordinate, int new_yCoordinate);
	
	/*
	 * This method increments a piece's attack path based on its new position.
	 * 
	 *  @param chessBoard This is board's cells'
	 *  @return Nothing.
	 */
	public abstract void incrementAttackPath(BoardCell[][] chessBoard);
	
	/*
	 * This method decrements a piece's attack path based on its departure from
	 * its former position.
	 * 
	 * @param chessBoard This is board's cells'
	 * @return Nothing.
	 */
	public abstract void decrementAttackPath(BoardCell[][] chessBoard);

}
