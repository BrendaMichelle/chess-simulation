package chess;
/**
 * 
 *<h1>BoardCell class: an individual unit on a chess board</h1>
 *<b>Details:</b> This class represents a single unit on a chess board.
 * This is why the Board class contains an array of these.
 * 
 * @author royjacome
 * @author BrendaRios
 * @since   2018-02-23
 */
public class BoardCell {
	
	boolean empty = true;	// Defines if a cell on the chess board contains a chessPiece or not
	int whiteAttackCell;		// Counter for how many White pieces are attacking a cell
	int blackAttackCell;		// Counter for how many Black pieces are attacking a cell
	Piece piece;	// The general object that each chess piece is contained as
	
	boolean 
	whiteKingAttacking, 
	whiteQueenAttacking,
	whiteBishopAttacking,
	whiteKnightAttacking,
	whiteRookAttacking,
	
	blackKingAttacking,
	blackQueenAttacking,
	blackBishopAttacking,
	blackKnightAttacking,
	blackRookAttacking = false;
	
	public  BoardCell() {
		
	}
}
