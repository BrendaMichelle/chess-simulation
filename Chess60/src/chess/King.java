package chess;
/**
 * <h1>King class: represents a king piece </h1>
 * <b>Details:</b> This class extends the class Piece.
 * 
 *@author royjacome
 *@since   2018-02-23
 */
public class King extends Piece {
	

	
	public King(String player, String pieceName, int xCoordinate, int yCoordinate, BoardCell[][] chessBoard) {
		super(player, pieceName);
		this.piece_xCoordinate = xCoordinate;
		this.piece_yCoordinate = yCoordinate;
		
		incrementAttackPath(chessBoard);
		chessBoard[xCoordinate][yCoordinate].empty = false;
	}
	/*
	 * @see chess.PieceAttribute#eating(chess.board, int, int)
	 */
	@Override
	public boolean eating(Board board, int new_xCoordinate, int new_yCoordinate) {

		King king = this;
		
		Piece piece = board.board[new_xCoordinate][new_yCoordinate].piece;
		String pieceOwner = board.board[new_xCoordinate][new_yCoordinate].piece.getPlayer();
		String currentPlayer = getPlayer();
		
		if(currentPlayer.equals("white") && board.board[new_xCoordinate][new_yCoordinate].blackAttackCell > 0) {
			// Move puts white king in Check
			return false;
		}
		if(currentPlayer.equals("black") && board.board[new_xCoordinate][new_yCoordinate].whiteAttackCell > 0) {
			// Move puts black King in Check
			return false;
		}
		
		if(!pieceOwner.equals(currentPlayer)) {
			int originalX = king.piece_xCoordinate;
			int originalY = king.piece_yCoordinate;
			board.prepareUpdatingBoard(king.piece_xCoordinate, king.piece_yCoordinate, new_xCoordinate, new_yCoordinate);
			//piece.decrementAttackPath(board.board);
			decrementAttackPath(board.board);
			board.board[king.piece_xCoordinate][king.piece_yCoordinate].empty = true;
			board.board[king.piece_xCoordinate][king.piece_yCoordinate].piece = null;
			king.setMoved(true);
			board.board[new_xCoordinate][new_yCoordinate].piece = king;
			board.board[new_xCoordinate][new_yCoordinate].empty = false;
			// decrement attack path
			king.piece_xCoordinate = new_xCoordinate;
			king.piece_yCoordinate = new_yCoordinate;
			// increment after movement the attack path
			incrementAttackPath(board.board);
			board.updateBoardAttackPaths(originalX, originalY, new_xCoordinate, new_yCoordinate);
			return true;
		}
		else {
			// Can't eat your own piece
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see chess.PieceAttribute#moving(chess.BoardCell[][], int, int)
	 */
	@Override

	public boolean moving(Board board, int new_xCoordinate, int new_yCoordinate) {
		
		//System.out.println("Works?");
		King king = this;
		// check if trying to Castle
		if((checkIfCastling(board, new_xCoordinate, new_yCoordinate))) {						// Successfully Castled
			//System.out.println("Castled!");
			return true;
		}
		
		//System.out.println("Could not castle");
		
		// else check if new position is valid for King
		if(!checkValidMove(board.board, new_xCoordinate, new_yCoordinate)) {						// Not castling, King has valid move
			return false;
		}
		
		// Check if King will be eating
		if(!board.board[new_xCoordinate][new_yCoordinate].empty) {
			if(eating(board, new_xCoordinate, new_yCoordinate)){														// King will eat opposing team
				return true;
			}
		}
		// if not then just move king
		else {
			String currentPlayer = king.getPlayer();
			
			if(currentPlayer.equals("white")) {
				if(board.board[new_xCoordinate][new_yCoordinate].blackAttackCell > 0) {
					// Can't move white King into check
					return false;
				}
			}
			if(currentPlayer.equals("black")) {
				if(board.board[new_xCoordinate][new_yCoordinate].whiteAttackCell > 0) {
					// Can't move black King into check
					return false;
				}
			}
			//System.out.println("I get here");
			int originalX = king.piece_xCoordinate;
			int originalY = king.piece_yCoordinate;
			board.prepareUpdatingBoard(king.piece_xCoordinate, king.piece_yCoordinate, new_xCoordinate, new_yCoordinate);
			// decrement attack path
			decrementAttackPath(board.board);
			board.board[king.piece_xCoordinate][king.piece_yCoordinate].empty = true;
			board.board[king.piece_xCoordinate][king.piece_yCoordinate].piece = null;
			king.setMoved(true);
			board.board[new_xCoordinate][new_yCoordinate].piece = king;
			board.board[new_xCoordinate][new_yCoordinate].empty = false;
			king.piece_xCoordinate = new_xCoordinate;
			king.piece_yCoordinate = new_yCoordinate;
			// increment after movement the attack path
			incrementAttackPath(board.board);
			board.updateBoardAttackPaths(originalX, originalY, new_xCoordinate, new_yCoordinate);
			return true;
		}
		
		return false;
	}
	
	/* (non-Javadoc)
	 * @see chess.PieceAttribute#checkValidMove(int, int)
	 */
	@Override
	public boolean checkValidMove(BoardCell[][] chessBoard, int new_xCoordinate, int new_yCoordinate) {
		/*Must check if it also an out of bounds move*/
		if(new_xCoordinate + new_yCoordinate > 16) {
			//System.out.println("Invalid move");
			return false;
		}
		
		if(this.piece_xCoordinate == new_xCoordinate && this.piece_yCoordinate == new_yCoordinate) {			// no movement at all, invalid

			return false;
		}
		if(this.piece_xCoordinate == new_xCoordinate) {						// no horizontal movement
			if(Math.abs(this.piece_yCoordinate - new_yCoordinate) == 1) {
				return true;
			}
		}
		if(this.piece_yCoordinate == new_yCoordinate) {						// no vertical movement
			if(Math.abs(this.piece_xCoordinate - new_xCoordinate) == 1) {
				return true;
			}
		}
		
		if(Math.abs((this.piece_xCoordinate - new_xCoordinate) / (this.piece_yCoordinate - new_yCoordinate)) == 1){		// check diagonal movement
			return true;
		}
		// No condition has been met for King
		return false;
	}
	
	/* (non-Javadoc)
	 * @see chess.PieceAttribute#incrementAttackPath(BoardCell[][] chessBoard)
	 */
	@Override
	public void incrementAttackPath(BoardCell[][] chessBoard) {
		King king = this;
		String currentPlayer = king.getPlayer();
		//chessBoard[king.piece_xCoordinate][king.piece_yCoordinate].empty = false;
		//System.out.println("Increment Attack path for King: " + currentPlayer);
		if(king.getAttackPathVisible()) {
			//System.out.println("I come out");
			return;
		}
		
		// King belongs to player White
		if(currentPlayer.equals("white")) {
			// Check bottom left cell
			if(king.piece_xCoordinate - 1 != -1 && king.piece_yCoordinate - 1 != -1)
				chessBoard[king.piece_xCoordinate - 1][king.piece_yCoordinate - 1].whiteAttackCell++;
			// Check left cell
			if(king.piece_xCoordinate - 1 != -1)
				chessBoard[king.piece_xCoordinate - 1][king.piece_yCoordinate].whiteAttackCell++;
			// Ceck upper left cell
			if(king.piece_xCoordinate - 1 != -1 && king.piece_yCoordinate + 1 != chessBoard.length)
				chessBoard[king.piece_xCoordinate - 1][king.piece_yCoordinate + 1].whiteAttackCell++;
			// Check top cell
			if(king.piece_yCoordinate + 1 != chessBoard.length)
				chessBoard[king.piece_xCoordinate][king.piece_yCoordinate + 1].whiteAttackCell++;
			// Check upper right cell
			if(king.piece_xCoordinate + 1 != chessBoard.length && king.piece_yCoordinate + 1 != chessBoard.length)
				chessBoard[king.piece_xCoordinate + 1][king.piece_yCoordinate + 1].whiteAttackCell++;
			// Check right cell
			if(king.piece_xCoordinate + 1 != chessBoard.length)
				chessBoard[king.piece_xCoordinate + 1][king.piece_yCoordinate].whiteAttackCell++;
			// Check bottom right cell
			if(king.piece_xCoordinate + 1 != chessBoard.length && king.piece_yCoordinate - 1 != -1)
				chessBoard[king.piece_xCoordinate + 1][king.piece_yCoordinate - 1].whiteAttackCell++;
			// Check bottom cell
			if(king.piece_yCoordinate - 1 != -1)
				chessBoard[king.piece_xCoordinate][king.piece_yCoordinate - 1].whiteAttackCell++;
			
			king.setAttackPathVisible(true);
			return;
		}
		
		if(currentPlayer.equals("black")) {
			if(king.piece_xCoordinate - 1 != -1 && king.piece_yCoordinate - 1 != -1)
				chessBoard[king.piece_xCoordinate - 1][king.piece_yCoordinate - 1].blackAttackCell++;
			if(king.piece_xCoordinate - 1 != -1)
				chessBoard[king.piece_xCoordinate - 1][king.piece_yCoordinate].blackAttackCell++;
			if(king.piece_xCoordinate - 1 != -1 && king.piece_yCoordinate + 1 != chessBoard.length)
				chessBoard[king.piece_xCoordinate - 1][king.piece_yCoordinate + 1].blackAttackCell++;
			if(king.piece_yCoordinate + 1 != chessBoard.length)
				chessBoard[king.piece_xCoordinate][king.piece_yCoordinate + 1].blackAttackCell++;
			if(king.piece_xCoordinate + 1 != chessBoard.length && king.piece_yCoordinate + 1 != chessBoard.length)
				chessBoard[king.piece_xCoordinate + 1][king.piece_yCoordinate + 1].blackAttackCell++;
			if(king.piece_xCoordinate + 1 != chessBoard.length)
				chessBoard[king.piece_xCoordinate + 1][king.piece_yCoordinate].blackAttackCell++;
			if(king.piece_xCoordinate + 1 != chessBoard.length && king.piece_yCoordinate - 1 != -1)
				chessBoard[king.piece_xCoordinate + 1][king.piece_yCoordinate - 1].blackAttackCell++;
			if(king.piece_yCoordinate - 1 != -1)
				chessBoard[king.piece_xCoordinate][king.piece_yCoordinate - 1].blackAttackCell++;
			
			king.setAttackPathVisible(true);
			return;
		}
		
		return;
		
	}
	
	/* (non-Javadoc)
	 * @see chess.PieceAttribute#decrementAttackPath(BoardCell[][] chessBoard)
	 */
	@Override
	public void decrementAttackPath(BoardCell[][] chessBoard) {
		
		King king = this;
		String currentPlayer = this.getPlayer();
		//chessBoard[king.piece_xCoordinate][king.piece_yCoordinate].empty = true;
		
		if(!king.getAttackPathVisible()) {
			return;
		}
		
		// King belongs to player White
		if(currentPlayer.equals("white")) {
			if(king.piece_xCoordinate - 1 != -1 && king.piece_yCoordinate - 1 != -1)
				chessBoard[king.piece_xCoordinate - 1][king.piece_yCoordinate - 1].whiteAttackCell--;
			if(king.piece_xCoordinate - 1 != -1)
				chessBoard[king.piece_xCoordinate - 1][king.piece_yCoordinate].whiteAttackCell--;
			if(king.piece_xCoordinate - 1 != -1 && king.piece_yCoordinate + 1 != chessBoard.length)
				chessBoard[king.piece_xCoordinate - 1][king.piece_yCoordinate + 1].whiteAttackCell--;
			if(king.piece_yCoordinate + 1 != chessBoard.length)
				chessBoard[king.piece_xCoordinate][king.piece_yCoordinate + 1].whiteAttackCell--;
			if(king.piece_xCoordinate + 1 != chessBoard.length && king.piece_yCoordinate + 1 != chessBoard.length)
				chessBoard[king.piece_xCoordinate + 1][king.piece_yCoordinate + 1].whiteAttackCell--;
			if(king.piece_xCoordinate + 1 != chessBoard.length)
				chessBoard[king.piece_xCoordinate + 1][king.piece_yCoordinate].whiteAttackCell--;
			if(king.piece_xCoordinate + 1 != chessBoard.length && king.piece_yCoordinate - 1 != -1)
				chessBoard[king.piece_xCoordinate + 1][king.piece_yCoordinate - 1].whiteAttackCell--;
			if(king.piece_yCoordinate - 1 != -1)
				chessBoard[king.piece_xCoordinate][king.piece_yCoordinate - 1].whiteAttackCell--;
			
			king.setAttackPathVisible(false);
			return;
		}
		
		if(currentPlayer.equals("black")) {
			if(king.piece_xCoordinate - 1 != -1 && king.piece_yCoordinate - 1 != -1)
				chessBoard[king.piece_xCoordinate - 1][king.piece_yCoordinate - 1].blackAttackCell--;
			if(king.piece_xCoordinate - 1 != -1)
				chessBoard[king.piece_xCoordinate - 1][king.piece_yCoordinate].blackAttackCell--;
			if(king.piece_xCoordinate - 1 != -1 && king.piece_yCoordinate + 1 != chessBoard.length)
				chessBoard[king.piece_xCoordinate - 1][king.piece_yCoordinate + 1].blackAttackCell--;
			if(king.piece_yCoordinate + 1 != chessBoard.length)
				chessBoard[king.piece_xCoordinate][king.piece_yCoordinate + 1].blackAttackCell--;
			if(king.piece_xCoordinate + 1 != chessBoard.length && king.piece_yCoordinate + 1 != chessBoard.length)
				chessBoard[king.piece_xCoordinate + 1][king.piece_yCoordinate + 1].blackAttackCell--;
			if(king.piece_xCoordinate + 1 != chessBoard.length)
				chessBoard[king.piece_xCoordinate + 1][king.piece_yCoordinate ].blackAttackCell--;
			if(king.piece_xCoordinate + 1 != chessBoard.length && king.piece_yCoordinate - 1 != -1)
				chessBoard[king.piece_xCoordinate + 1][king.piece_yCoordinate - 1].blackAttackCell--;
			if(king.piece_yCoordinate - 1 != -1)
				chessBoard[king.piece_xCoordinate][king.piece_yCoordinate - 1].blackAttackCell--;
			
			king.setAttackPathVisible(false);
			return;
		}
		
		return;
		
	}
	
	public boolean checkIfCastling(Board board, int new_xCoordinate, int new_yCoordinate) {
		
		King king = this;

		boolean kingMoved = king.getMoved();
		
		if(kingMoved) {																					// Check if King moved
			return false;
		}
		
		int castleKingSideIndex = new_yCoordinate - 2;
		int castleQueenSideIndex = new_yCoordinate + 2;
		
		//System.out.println("King x is '" + king.piece_xCoordinate +"' but new X is '" + new_xCoordinate +"'");
		
		//System.out.println("King y is '" + king.piece_yCoordinate +"' but castle King Y is '" + castleKingSideIndex +"'");
		
		//System.out.println("King y is '" + king.piece_yCoordinate +"' but castle Queen Y is '" + castleQueenSideIndex +"'");
		
		// Check King side Castling
		if(new_xCoordinate == king.piece_xCoordinate && castleKingSideIndex == king.piece_yCoordinate) {	// Attempting to Castle King side
			//System.out.println("Something wrong here");
			if(!(board.board[new_xCoordinate][new_yCoordinate + 1].piece instanceof Rook)) {	// Rook is not in place
				return false;
			}
			

			//System.out.println("So far so good");
			Rook kingSideRook = (Rook)board.board[new_xCoordinate][new_yCoordinate + 1].piece;
			boolean kingSideRookMoved = kingSideRook.getMoved();
			
			if(kingSideRookMoved) {																		// Rook on King's side moved
				return false;
			}
			
			//System.out.println("Good here");
			// check Attack Paths and check if spaces are open
			int checkSpace = new_yCoordinate;
			int counter = 2;
			String player = null;
			
			if(king.getPlayer().equals("white")) {
				while(counter > 0) {
					if(!board.board[king.piece_xCoordinate][checkSpace].empty || board.board[king.piece_xCoordinate][checkSpace].blackAttackCell > 0) {
						return false;
					}
					counter--;
					checkSpace--;
				}
				player = "white";
			}
			else if(king.getPlayer().equals("black")) {
				while(counter > 0) {
					if(!board.board[king.piece_xCoordinate][checkSpace].empty || board.board[king.piece_xCoordinate][checkSpace].whiteAttackCell > 0) {
						return false;
					}
					counter--;
					checkSpace--;
				}
				player = "black";
			}
			
			// If all checks out, try to Castle King side
			if(!(castleKingSide(board, kingSideRook, player))) {
				return false;
			}
		}
		
		// Check Queen side Castling
		//System.out.println("queen side");
		if(new_xCoordinate == king.piece_xCoordinate && castleQueenSideIndex == king.piece_yCoordinate) {	// Attempting to Castle Queen side
			//System.out.println("Got through");
			if(!(board.board[new_xCoordinate][new_yCoordinate - 2].piece instanceof Rook)) {
				return false;
			}
			
			Rook queenSideRook = (Rook)board.board[new_xCoordinate][new_yCoordinate - 2].piece;
			boolean queenSideRookMoved = queenSideRook.getMoved();
			
			if(queenSideRookMoved) {																		// Rook on Queen side moved
				return false;
			}
			
			// Check if the extra space on Queen side is empty, i.e the knight moved and cell stayed empty
			if(!board.board[new_xCoordinate][new_yCoordinate - 1].empty) {
				return false;
			}

			//System.out.println("Def good here");
			// Check Attack Paths and check if spaces are open
			int checkSpace = new_yCoordinate;
			int counter = 2;
			String player = null;
			
			if(king.getPlayer().equals("white")) {
				while(counter > 0) {
					if(!board.board[king.piece_xCoordinate][checkSpace].empty || board.board[king.piece_xCoordinate][checkSpace].blackAttackCell > 0) {
						return false;
					}
					counter--;
					checkSpace++;
				}
				player = "white";
			}
			else if(king.getPlayer().equals("black")) {
				while (counter > 0) {
					if(!board.board[king.piece_xCoordinate][checkSpace].empty || board.board[king.piece_xCoordinate][checkSpace].whiteAttackCell > 0) {
						return false;
					}
					counter--;
					checkSpace++;
				}
				player = "black";
			}
			
			// If all checks out, try to Castle King Side
			if(!(castleQueenSide(board, queenSideRook, player))) {
				return false;
			}
			
		}
		return true;
	}
	
	public boolean castleKingSide(Board board, Rook rook, String player) {
		
		King king = this;
		int originalKingX = king.piece_xCoordinate;
		int originalKingY = king.piece_yCoordinate;
		int originalRookx = rook.piece_xCoordinate;
		int originalRookY = rook.piece_yCoordinate;

		board.prepareUpdatingBoard(king.piece_xCoordinate, king.piece_yCoordinate, king.piece_xCoordinate, king.piece_yCoordinate + 2);
		board.prepareUpdatingBoard(rook.piece_xCoordinate, rook.piece_yCoordinate, rook.piece_xCoordinate, king.piece_yCoordinate + 1);
		rook.decrementAttackPath(board.board);
		king.decrementAttackPath(board.board);
		
		//System.out.println("Rook X is: " + rook.piece_xCoordinate);
		//System.out.println("Rook Y is: " + rook.piece_yCoordinate);
		board.board[rook.piece_xCoordinate][rook.piece_yCoordinate].piece = null;				//Remove Rook
		board.board[king.piece_xCoordinate][king.piece_yCoordinate].piece = null; 			//Remove King
		board.board[rook.piece_xCoordinate][rook.piece_yCoordinate].empty = true;		//Rook Cell is empty
		board.board[king.piece_xCoordinate][king.piece_yCoordinate].empty = true; 		//King cell is empty
		
		rook.piece_yCoordinate = king.piece_yCoordinate + 1;
		rook.setMoved(true);
		board.board[rook.piece_xCoordinate][rook.piece_yCoordinate].piece = rook;
		board.board[rook.piece_xCoordinate][rook.piece_yCoordinate].empty = false;
		
		king.piece_yCoordinate = king.piece_yCoordinate + 2;
		king.setMoved(true);
		board.board[king.piece_xCoordinate][king.piece_yCoordinate].piece = king;
		board.board[king.piece_xCoordinate][king.piece_yCoordinate].empty = false;
		king.incrementAttackPath(board.board);
		rook.incrementAttackPath(board.board);
		board.updateBoardAttackPaths(originalKingX, originalKingY, king.piece_xCoordinate, king.piece_yCoordinate);
		board.updateBoardAttackPaths(originalRookx, originalRookY, rook.piece_xCoordinate, rook.piece_yCoordinate);
		
		return true;
	}
	
	public boolean castleQueenSide(Board board, Rook rook, String player) {
		
		King king = this;
		int originalKingX = king.piece_xCoordinate;
		int originalKingY = king.piece_yCoordinate;
		int originalRookx = rook.piece_xCoordinate;
		int originalRookY = rook.piece_yCoordinate;
		board.prepareUpdatingBoard(king.piece_xCoordinate, king.piece_yCoordinate, king.piece_xCoordinate, king.piece_yCoordinate + 2);
		board.prepareUpdatingBoard(rook.piece_xCoordinate, rook.piece_yCoordinate, rook.piece_xCoordinate, king.piece_yCoordinate + 1);
		rook.decrementAttackPath(board.board);
		king.decrementAttackPath(board.board);
			
		board.board[rook.piece_xCoordinate][rook.piece_yCoordinate].piece = null;
		board.board[king.piece_xCoordinate][king.piece_yCoordinate].piece = null;
		board.board[rook.piece_xCoordinate][rook.piece_yCoordinate].empty = true;
		board.board[king.piece_xCoordinate][king.piece_yCoordinate].empty = true;
		
		rook.piece_yCoordinate = king.piece_yCoordinate - 1;
		rook.setMoved(true);
		board.board[rook.piece_xCoordinate][rook.piece_yCoordinate].piece = rook;
		board.board[rook.piece_xCoordinate][rook.piece_yCoordinate].empty = false;
		
		king.piece_yCoordinate = king.piece_yCoordinate - 2;
		king.setMoved(true);
		board.board[king.piece_xCoordinate][king.piece_yCoordinate].piece = king;
		board.board[king.piece_xCoordinate][king.piece_yCoordinate].empty = false;
		king.incrementAttackPath(board.board);
		rook.incrementAttackPath(board.board);
		board.updateBoardAttackPaths(originalKingX, originalKingY, king.piece_xCoordinate, king.piece_yCoordinate);
		board.updateBoardAttackPaths(originalRookx, originalRookY, rook.piece_xCoordinate, rook.piece_yCoordinate);
		
		return true;
	}
	
	public boolean testWhiteKingCheck(BoardCell[][] chessBoard) {
		
		King whiteKing = this;
		
		if(chessBoard[whiteKing.piece_xCoordinate][whiteKing.piece_yCoordinate].blackAttackCell > 0) {
			return true;
		}
		
		return false;
	}
	
	public boolean testBlackKingCheck(BoardCell[][] chessBoard) {
		
		King blackKing = this;
		
		if(chessBoard[blackKing.piece_xCoordinate][blackKing.piece_yCoordinate].whiteAttackCell > 0) {
			return true;
		}
		
		
		return false;
	}
	
	public boolean testCheckMateWhiteKing(BoardCell[][] chessBoard) {
		
		King whiteKing = this;
		
		int kingX = whiteKing.piece_xCoordinate;
		int kingY = whiteKing.piece_yCoordinate;
		int blackAttacking = 0;
		boolean whitePieceCanBlock = false;
		
		// Check all cells King can move into to avoid check mate
		if(kingX - 1 > -1 && kingY - 1 > -1) {									// Check bottom left cells of King
			if(chessBoard[kingX - 1][kingY - 1].empty) {			// Open cell
				if(chessBoard[kingX - 1][kingY - 1].blackAttackCell == 0) {		// Safe for King to move into
					return false;
				}
			}
			else {
				String pieceInQuestion = chessBoard[kingX - 1][kingY - 1].piece.getPlayer();
				if(pieceInQuestion.equals("black")) {				// Cell has black Piece
					if(chessBoard[kingX - 1][kingY - 1].blackAttackCell == 0) {		// Safe for King to eat
						return false;
					}
				}
			}
		}
		
		if(kingX - 1 > -1) {									// Check left cells of King
			if(chessBoard[kingX - 1][kingY].empty) {			// Open cell
				if(chessBoard[kingX - 1][kingY].blackAttackCell == 0) {		// Safe for King to move into
					return false;
				}
			}
			else {
				String pieceInQuestion = chessBoard[kingX - 1][kingY].piece.getPlayer();
				if(pieceInQuestion.equals("black")) {				// Cell has black Piece
					if(chessBoard[kingX - 1][kingY].blackAttackCell == 0) {		// Safe for King to eat
						return false;
					}
				}
			}
		}
		
		if(kingX - 1 > -1 && kingY + 1 < 8) {									// Check top left cells of King
			if(chessBoard[kingX - 1][kingY + 1].empty) {			// Open cell
				if(chessBoard[kingX - 1][kingY + 1].blackAttackCell == 0) {		// Safe for King to move into
					return false;
				}
			}
			else {
				String pieceInQuestion = chessBoard[kingX - 1][kingY + 1].piece.getPlayer();
				if(pieceInQuestion.equals("black")) {				// Cell has black Piece
					if(chessBoard[kingX - 1][kingY + 1].blackAttackCell == 0) {		// Safe for King to eat
						return false;
					}
				}
			}
		}
		
		if(kingY + 1 < 8) {									// Check above cells of King
			if(chessBoard[kingX][kingY + 1].empty) {			// Open cell
				if(chessBoard[kingX][kingY + 1].blackAttackCell == 0) {		// Safe for King to move into
					return false;
				}
			}
			else {
				String pieceInQuestion = chessBoard[kingX][kingY + 1].piece.getPlayer();
				if(pieceInQuestion.equals("black")) {				// Cell has black Piece
					if(chessBoard[kingX][kingY + 1].blackAttackCell == 0) {		// Safe for King to eat
						return false;
					}
				}
			}
		}
		
		if(kingX + 1 < 8 && kingY + 1 < 8) {									// Check top right cells of King
			if(chessBoard[kingX + 1][kingY + 1].empty) {			// Open cell
				if(chessBoard[kingX + 1][kingY + 1].blackAttackCell == 0) {		// Safe for King to move into
					return false;
				}
			}
			else {
				String pieceInQuestion = chessBoard[kingX + 1][kingY + 1].piece.getPlayer();
				if(pieceInQuestion.equals("black")) {				// Cell has black Piece
					if(chessBoard[kingX + 1][kingY + 1].blackAttackCell == 0) {		// Safe for King to eat
						return false;
					}
				}
			}
		}
		
		if(kingX + 1 < 8) {									// Check right cells of King
			if(chessBoard[kingX + 1][kingY].empty) {			// Open cell
				if(chessBoard[kingX + 1][kingY].blackAttackCell == 0) {		// Safe for King to move into
					return false;
				}
			}
			else {
				String pieceInQuestion = chessBoard[kingX + 1][kingY].piece.getPlayer();
				if(pieceInQuestion.equals("black")) {				// Cell has black Piece
					if(chessBoard[kingX + 1][kingY].blackAttackCell == 0) {		// Safe for King to eat
						return false;
					}
				}
			}
		}
		
		if(kingX + 1 < 8 && kingY - 1 > -1) {									// Check bottom right cells of King
			if(chessBoard[kingX + 1][kingY - 1].empty) {			// Open cell
				if(chessBoard[kingX + 1][kingY - 1].blackAttackCell == 0) {		// Safe for King to move into
					return false;
				}
			}
			else {
				String pieceInQuestion = chessBoard[kingX + 1][kingY - 1].piece.getPlayer();
				if(pieceInQuestion.equals("black")) {				// Cell has black Piece
					if(chessBoard[kingX + 1][kingY - 1].blackAttackCell == 0) {		// Safe for King to eat
						return false;
					}
				}
			}
		}
		
		if(kingY - 1 > -1) {									// Check bottom cells of King
			if(chessBoard[kingX][kingY - 1].empty) {			// Open cell
				if(chessBoard[kingX][kingY - 1].blackAttackCell == 0) {		// Safe for King to move into
					return false;
				}
			}
			else {
				String pieceInQuestion = chessBoard[kingX][kingY - 1].piece.getPlayer();
				if(pieceInQuestion.equals("black")) {				// Cell has black Piece
					if(chessBoard[kingX][kingY - 1].blackAttackCell == 0) {		// Safe for King to eat
						return false;
					}
				}
			}
		}
		
		
		/*White King cannot run, must check if can be saved by a friendly piece*/
		
		// Check if attacked by Pawn and can be saved
		
		if(kingX - 1 > -1 && kingY - 1 > -1) {				// Check bottom left
			if(!chessBoard[kingX - 1][kingY - 1].empty) {	// Piece found
				if(chessBoard[kingX - 1][kingY - 1].piece instanceof Pawn) {		// Its a pawn
					String pawnPiece = chessBoard[kingX - 1][kingY - 1].piece.getPlayer();
					if(pawnPiece.equals("black")) {										// Its an opposing Pawn
						if(chessBoard[kingX - 1][kingY - 1].whiteAttackCell > 0) {		// It can be eaten
							return false;
						}
						else {															// Cant save king
							return true;
						}
					}
				}
			}
		}
		
		if(kingX - 1 > -1 && kingY + 1 < 8) {				// Check top right
			if(!chessBoard[kingX - 1][kingY + 1].empty) {	// Piece found
				if(chessBoard[kingX - 1][kingY + 1].piece instanceof Pawn) {		// Its a pawn
					String pawnPiece = chessBoard[kingX - 1][kingY + 1].piece.getPlayer();
					if(pawnPiece.equals("black")) {										// Its an opposing Pawn
						if(chessBoard[kingX - 1][kingY + 1].whiteAttackCell > 0) {		// It can be eaten
							return false;
						}
						else {															// Cant save king
							return true;
						}
					}
				}
			}
		}
		
		
		// Check if attacked by Knight and can be saved
		
		if(kingX - 2 > -1 && kingY - 1 > -1) {				// Left down
			if(!chessBoard[kingX - 2][kingY - 1].empty) {	// Piece found
				if(chessBoard[kingX - 2][kingY - 1].piece instanceof Knight) {	// Its a knight
					String knightPiece = chessBoard[kingX - 2][kingY - 1].piece.getPlayer();
					if(knightPiece.equals("black")) {										// Its an opposing knight
						if(chessBoard[kingX - 2][kingY - 1].whiteAttackCell > 0) {			// It can be eaten
							return false;
						}
						else {																// Cant save king
							return true;
						}
					}
				}
			}
		}
		
		if(kingX - 2 > -1 && kingY + 1 < 8) {				// Left up
			if(!chessBoard[kingX - 2][kingY + 1].empty) {	// Piece found
				if(chessBoard[kingX - 2][kingY + 1].piece instanceof Knight) {	// Its a knight
					String knightPiece = chessBoard[kingX - 2][kingY + 1].piece.getPlayer();
					if(knightPiece.equals("black")) {										// Its an opposing knight
						if(chessBoard[kingX - 2][kingY + 1].whiteAttackCell > 0) {			// It can be eaten
							return false;
						}
						else {																// Cant save king
							return true;
						}
					}
				}
			}
		}
		
		if(kingX + 2 < 8 && kingY - 1 > -1) {				// Right down
			if(!chessBoard[kingX + 2][kingY - 1].empty) {	// Piece found
				if(chessBoard[kingX + 2][kingY - 1].piece instanceof Knight) {	// Its a knight
					String knightPiece = chessBoard[kingX + 2][kingY - 1].piece.getPlayer();
					if(knightPiece.equals("black")) {										// Its an opposing knight
						if(chessBoard[kingX + 2][kingY - 1].whiteAttackCell > 0) {			// It can be eaten
							return false;
						}
						else {																// Cant save king
							return true;
						}
					}
				}
			}
		}
		
		if(kingX + 2 < 8 && kingY + 1 < 8) {				// Right Up
			if(!chessBoard[kingX + 2][kingY + 1].empty) {	// Piece found
				if(chessBoard[kingX + 2][kingY + 1].piece instanceof Knight) {	// Its a knight
					String knightPiece = chessBoard[kingX + 2][kingY + 1].piece.getPlayer();
					if(knightPiece.equals("black")) {										// Its an opposing knight
						if(chessBoard[kingX + 2][kingY + 1].whiteAttackCell > 0) {			// It can be eaten
							return false;
						}
						else {																// Cant save king
							return true;
						}
					}
				}
			}
		}
		
		if(kingX - 1 > -1 && kingY + 2  < 8) {				// Up Left
			if(!chessBoard[kingX - 1][kingY + 2].empty) {	// Piece found
				if(chessBoard[kingX - 1][kingY + 2].piece instanceof Knight) {	// Its a knight
					String knightPiece = chessBoard[kingX - 1][kingY + 2].piece.getPlayer();
					if(knightPiece.equals("black")) {										// Its an opposing knight
						if(chessBoard[kingX - 1][kingY + 2].whiteAttackCell > 0) {			// It can be eaten
							return false;
						}
						else {																// Cant save king
							return true;
						}
					}
				}
			}
		}
		
		if(kingX + 1 < 8 && kingY + 2  < 8) {				// Up Right
			if(!chessBoard[kingX + 1][kingY + 2].empty) {	// Piece found
				if(chessBoard[kingX + 1][kingY + 2].piece instanceof Knight) {	// Its a knight
					String knightPiece = chessBoard[kingX + 1][kingY + 2].piece.getPlayer();
					if(knightPiece.equals("black")) {										// Its an opposing knight
						if(chessBoard[kingX + 1][kingY + 2].whiteAttackCell > 0) {			// It can be eaten
							return false;
						}
						else {																// Cant save king
							return true;
						}
					}
				}
			}
		}
		
		if(kingX - 1 > -1 && kingY - 2  > -1) {				// Down Left
			if(!chessBoard[kingX - 1][kingY - 2].empty) {	// Piece found
				if(chessBoard[kingX - 1][kingY - 2].piece instanceof Knight) {	// Its a knight
					String knightPiece = chessBoard[kingX - 1][kingY - 2].piece.getPlayer();
					if(knightPiece.equals("black")) {										// Its an opposing knight
						if(chessBoard[kingX - 1][kingY - 2].whiteAttackCell > 0) {			// It can be eaten
							return false;
						}
						else {																// Cant save king
							return true;
						}
					}
				}
			}
		}
		
		if(kingX + 1 < 8 && kingY - 2 > -1) {				// Down Right
			if(!chessBoard[kingX + 1][kingY - 2].empty) {	// Piece found
				if(chessBoard[kingX + 1][kingY - 2].piece instanceof Knight) {	// Its a knight
					String knightPiece = chessBoard[kingX + 1][kingY - 2].piece.getPlayer();
					if(knightPiece.equals("black")) {										// Its an opposing knight
						if(chessBoard[kingX + 1][kingY - 2].whiteAttackCell > 0) {			// It can be eaten
							return false;
						}
						else {																// Cant save king
							return true;
						}
					}
				}
			}
		}
		
		
		
		// Now for any other pieces
		
		// Check horizontal and vertical before diagonal if saving king is possible
		
		// Check King's immediate surroundings first if rescue is possible
		
		if(kingX - 1 > -1) {								// Left of king
			if(!chessBoard[kingX - 1][kingY].empty) {	// Found piece
				Piece piece = chessBoard[kingX - 1][kingY].piece;
				String pieceTeam = piece.getPlayer();
				if(pieceTeam.equals("black")) {			// Its an opposing team
					if(piece instanceof Rook || piece instanceof Queen) {			// Its either an enemy rook or queen
						if(chessBoard[kingX - 1][kingY].whiteAttackCell > 1) {	// It can be eaten
							return false;
						}
						else {													// Cant save king
							return true;
						}
					}
				}
			}
		}
		
		if(kingX + 1 < 8) {								// Right of king
			if(!chessBoard[kingX + 1][kingY].empty) {	// Found piece
				Piece piece = chessBoard[kingX + 1][kingY].piece;
				String pieceTeam = piece.getPlayer();
				if(pieceTeam.equals("black")) {			// Its an opposing team
					if(piece instanceof Rook || piece instanceof Queen) {			// Its either an enemy rook or queen
						if(chessBoard[kingX + 1][kingY].whiteAttackCell > 1) {	// It can be eaten
							return false;
						}
						else {													// Cant save king
							return true;
						}
					}
				}
			}
		}
		
		if(kingY - 1 > -1) {								// Below of king
			if(!chessBoard[kingX][kingY - 1].empty) {	// Found piece
				Piece piece = chessBoard[kingX][kingY - 1].piece;
				String pieceTeam = piece.getPlayer();
				if(pieceTeam.equals("black")) {			// Its an opposing team
					if(piece instanceof Rook || piece instanceof Queen) {			// Its either an enemy rook or queen
						if(chessBoard[kingX][kingY - 1].whiteAttackCell > 1) {	// It can be eaten
							return false;
						}
						else {													// Cant save king
							return true;
						}
					}
				}
			}
		}
		
		if(kingY + 1 < 8) {								// Above of king
			if(!chessBoard[kingX][kingY + 1].empty) {	// Found piece
				Piece piece = chessBoard[kingX][kingY + 1].piece;
				String pieceTeam = piece.getPlayer();
				if(pieceTeam.equals("black")) {			// Its an opposing team
					if(piece instanceof Rook || piece instanceof Queen) {			// Its either an enemy rook or queen
						if(chessBoard[kingX][kingY + 1].whiteAttackCell > 1) {	// It can be eaten
							return false;
						}
						else {													// Cant save king
							return true;
						}
					}
				}
			}
		}
		
		if(kingX - 1 > -1 && kingY - 1 > -1) {								// Bottom left of king
			if(!chessBoard[kingX - 1][kingY - 1].empty) {	// Found piece
				Piece piece = chessBoard[kingX - 1][kingY - 1].piece;
				String pieceTeam = piece.getPlayer();
				if(pieceTeam.equals("black")) {			// Its an opposing team
					if(piece instanceof Rook || piece instanceof Queen) {			// Its either an enemy bishop or queen
						if(chessBoard[kingX - 1][kingY - 1].whiteAttackCell > 1) {	// It can be eaten
							return false;
						}
						else {													// Cant save king
							return true;
						}
					}
				}
			}
		}
		
		if(kingX - 1 > -1 && kingY + 1 < 8) {								// Top left of king
			if(!chessBoard[kingX - 1][kingY + 1].empty) {	// Found piece
				Piece piece = chessBoard[kingX - 1][kingY + 1].piece;
				String pieceTeam = piece.getPlayer();
				if(pieceTeam.equals("black")) {			// Its an opposing team
					if(piece instanceof Rook || piece instanceof Queen) {			// Its either an enemy bishop or queen
						if(chessBoard[kingX - 1][kingY + 1].whiteAttackCell > 1) {	// It can be eaten
							return false;
						}
						else {													// Cant save king
							return true;
						}
					}
				}
			}
		}
		
		if(kingX + 1 < 8 && kingY - 1 > -1) {								// Bottom Right of king
			if(!chessBoard[kingX + 1][kingY - 1].empty) {	// Found piece
				Piece piece = chessBoard[kingX + 1][kingY - 1].piece;
				String pieceTeam = piece.getPlayer();
				if(pieceTeam.equals("black")) {			// Its an opposing team
					if(piece instanceof Rook || piece instanceof Queen) {			// Its either an enemy bishop or queen
						if(chessBoard[kingX + 1][kingY - 1].whiteAttackCell > 1) {	// It can be eaten
							return false;
						}
						else {													// Cant save king
							return true;
						}
					}
				}
			}
		}
		
		if(kingX + 1 < 8 && kingY + 1 < 8) {								// Top right of king
			if(!chessBoard[kingX + 1][kingY + 1].empty) {	// Found piece
				Piece piece = chessBoard[kingX + 1][kingY + 1].piece;
				String pieceTeam = piece.getPlayer();
				if(pieceTeam.equals("black")) {			// Its an opposing team
					if(piece instanceof Rook || piece instanceof Queen) {			// Its either an enemy bishop or queen
						if(chessBoard[kingX + 1][kingY + 1].whiteAttackCell > 1) {	// It can be eaten
							return false;
						}
						else {													// Cant save king
							return true;
						}
					}
				}
			}
		}
		
		// No pieces found next to king, search outside immediate area
		// Search horizontal and vertical first
		
		for(int xCell = kingX - 2, yCell = kingY ; xCell > -1 ; xCell--) {		// Check left of king
			if(chessBoard[xCell][yCell].whiteAttackCell > 0) {					// Possible block by ally
				whitePieceCanBlock = true;
			}
			if(!chessBoard[xCell][yCell].empty) {								// Found piece
				Piece piece = chessBoard[xCell][yCell].piece;
				String pieceTeam = piece.getPlayer();
				if(pieceTeam.equals("black")) {									// It is an opposing piece
					if(piece instanceof Rook || piece instanceof Queen) {			// Piece is either rook or queen
						if(whitePieceCanBlock) {									// It can be blocked, saves king
							return false;
						}
						else {													// Cant save king
							return true;
						}
					}
					else {														// Not attacking piece
						break;
					}
				}
				else {															// Friendly piece
					break;
				}
			}
		}
		
		whitePieceCanBlock = false;
		
		for(int xCell = kingX + 2, yCell = kingY ; xCell < 8 ; xCell++) {			// Check right of king
			if(chessBoard[xCell][yCell].whiteAttackCell > 0) {					// Possible block by ally
				whitePieceCanBlock = true;
			}
			if(!chessBoard[xCell][yCell].empty) {								// Found piece
				Piece piece = chessBoard[xCell][yCell].piece;
				String pieceTeam = piece.getPlayer();
				if(pieceTeam.equals("black")) {									// It is an opposing piece
					if(piece instanceof Rook || piece instanceof Queen) {			// Piece is either rook or queen
						if(whitePieceCanBlock) {									// It can be blocked, saves king
							return false;
						}
						else {													// Cant save king
							return true;
						}
					}
					else {														// Not attacking piece
						break;
					}
				}
				else {															// Friendly piece
					break;
				}
			}
		}
		
		whitePieceCanBlock = false;
		
		for(int xCell = kingX, yCell = kingY + 2; yCell < 8 ; yCell++) {			// Check Above of king
			if(chessBoard[xCell][yCell].whiteAttackCell > 0) {					// Possible block by ally
				whitePieceCanBlock = true;
			}
			if(!chessBoard[xCell][yCell].empty) {								// Found piece
				Piece piece = chessBoard[xCell][yCell].piece;
				String pieceTeam = piece.getPlayer();
				if(pieceTeam.equals("black")) {									// It is an opposing piece
					if(piece instanceof Rook || piece instanceof Queen) {			// Piece is either rook or queen
						if(whitePieceCanBlock) {									// It can be blocked, saves king
							return false;
						}
						else {													// Cant save king
							return true;
						}
					}
					else {														// Not attacking piece
						break;
					}
				}
				else {															// Friendly piece
					break;
				}
			}
		}
		
		whitePieceCanBlock = false;
		
		for(int xCell = kingX, yCell = kingY - 2; yCell > -1 ; xCell--) {			// Check below of king
			if(chessBoard[xCell][yCell].whiteAttackCell > 0) {					// Possible block by ally
				whitePieceCanBlock = true;
			}
			if(!chessBoard[xCell][yCell].empty) {								// Found piece
				Piece piece = chessBoard[xCell][yCell].piece;
				String pieceTeam = piece.getPlayer();
				if(pieceTeam.equals("black")) {									// It is an opposing piece
					if(piece instanceof Rook || piece instanceof Queen) {			// Piece is either rook or queen
						if(whitePieceCanBlock) {									// It can be blocked, saves king
							return false;
						}
						else {													// Cant save king
							return true;
						}
					}
					else {														// Not attacking piece
						break;
					}
				}
				else {															// Friendly piece
					break;
				}
			}
		}
		
		
		// Not being attacked horizontal or vertical, search diagonal now
		
		whitePieceCanBlock = false;
		
		for(int xCell = kingX - 2, yCell = kingY - 2 ; xCell > -1 && yCell > -1 ; xCell--, yCell--) {	// Bottom left of king
			if(chessBoard[xCell][yCell].whiteAttackCell > 0) {					// Possible block by ally
				whitePieceCanBlock = true;
			}
			if(!chessBoard[xCell][yCell].empty) {								// Found piece
				Piece piece = chessBoard[xCell][yCell].piece;
				String pieceTeam = piece.getPlayer();
				if(pieceTeam.equals("black")) {									// It is an opposing piece
					if(piece instanceof Rook || piece instanceof Queen) {			// Piece is either rook or queen
						if(whitePieceCanBlock) {									// It can be blocked, saves king
							return false;
						}
						else {													// Cant save king
							return true;
						}
					}
					else {														// Not attacking piece
						break;
					}
				}
				else {															// Friendly piece
					break;
				}
			}
		}
		
		whitePieceCanBlock = false;
		
		for(int xCell = kingX - 2, yCell = kingY + 2 ; xCell > -1 && yCell < 8 ; xCell--, yCell++) {	// Top left of king
			if(chessBoard[xCell][yCell].whiteAttackCell > 0) {					// Possible block by ally
				whitePieceCanBlock = true;
			}
			if(!chessBoard[xCell][yCell].empty) {								// Found piece
				Piece piece = chessBoard[xCell][yCell].piece;
				String pieceTeam = piece.getPlayer();
				if(pieceTeam.equals("black")) {									// It is an opposing piece
					if(piece instanceof Rook || piece instanceof Queen) {			// Piece is either rook or queen
						if(whitePieceCanBlock) {									// It can be blocked, saves king
							return false;
						}
						else {													// Cant save king
							return true;
						}
					}
					else {														// Not attacking piece
						break;
					}
				}
				else {															// Friendly piece
					break;
				}
			}
		}
		
		whitePieceCanBlock = false;
		
		for(int xCell = kingX + 2, yCell = kingY + 2 ; xCell < 8 && yCell < 8 ; xCell++, yCell++) {	// Top right of king
			if(chessBoard[xCell][yCell].whiteAttackCell > 0) {					// Possible block by ally
				whitePieceCanBlock = true;
			}
			if(!chessBoard[xCell][yCell].empty) {								// Found piece
				Piece piece = chessBoard[xCell][yCell].piece;
				String pieceTeam = piece.getPlayer();
				if(pieceTeam.equals("black")) {									// It is an opposing piece
					if(piece instanceof Rook || piece instanceof Queen) {			// Piece is either rook or queen
						if(whitePieceCanBlock) {									// It can be blocked, saves king
							return false;
						}
						else {													// Cant save king
							return true;
						}
					}
					else {														// Not attacking piece
						break;
					}
				}
				else {															// Friendly piece
					break;
				}
			}
		}
		
		whitePieceCanBlock = false;
		
		for(int xCell = kingX + 2, yCell = kingY - 2 ; xCell < 8 && yCell > -1 ; xCell++, yCell--) {	// Bottom right of king
			if(chessBoard[xCell][yCell].whiteAttackCell > 0) {					// Possible block by ally
				whitePieceCanBlock = true;
			}
			if(!chessBoard[xCell][yCell].empty) {								// Found piece
				Piece piece = chessBoard[xCell][yCell].piece;
				String pieceTeam = piece.getPlayer();
				if(pieceTeam.equals("black")) {									// It is an opposing piece
					if(piece instanceof Rook || piece instanceof Queen) {			// Piece is either rook or queen
						if(whitePieceCanBlock) {									// It can be blocked, saves king
							return false;
						}
						else {													// Cant save king
							return true;
						}
					}
					else {														// Not attacking piece
						break;
					}
				}
				else {															// Friendly piece
					break;
				}
			}
		}
		
		//System.out.println("Didn't account for something for King being checked mate");
		return true;
	}
	
	public boolean testCheckMateBlackKing(BoardCell[][] chessBoard) {
		
		King blackKing = this;
		
		int kingX = blackKing.piece_xCoordinate;
		int kingY = blackKing.piece_yCoordinate;
		int whiteAttacking = 0;
		boolean blackPieceCanBlock = false;
		
		// Check all cells King can move into to avoid check mate
		if(kingX - 1 > -1 && kingY - 1 > -1) {									// Check bottom left cells of King
			if(chessBoard[kingX - 1][kingY - 1].empty) {			// Open cell
				if(chessBoard[kingX - 1][kingY - 1].whiteAttackCell == 0) {		// Safe for King to move into
					return false;
				}
			}
			String pieceInQuestion = chessBoard[kingX - 1][kingY - 1].piece.getPlayer();
			if(pieceInQuestion.equals("black")) {				// Cell has black Piece
				if(chessBoard[kingX - 1][kingY - 1].whiteAttackCell == 0) {		// Safe for King to eat
					return false;
				}
			}
		}
		
		if(kingX - 1 > -1) {									// Check left cells of King
			if(chessBoard[kingX - 1][kingY].empty) {			// Open cell
				if(chessBoard[kingX - 1][kingY].whiteAttackCell == 0) {		// Safe for King to move into
					return false;
				}
			}
			String pieceInQuestion = chessBoard[kingX - 1][kingY].piece.getPlayer();
			if(pieceInQuestion.equals("black")) {				// Cell has black Piece
				if(chessBoard[kingX - 1][kingY].whiteAttackCell == 0) {		// Safe for King to eat
					return false;
				}
			}
		}
		
		if(kingX - 1 > -1 && kingY + 1 < 8) {									// Check top left cells of King
			if(chessBoard[kingX - 1][kingY + 1].empty) {			// Open cell
				if(chessBoard[kingX - 1][kingY + 1].whiteAttackCell == 0) {		// Safe for King to move into
					return false;
				}
			}
			String pieceInQuestion = chessBoard[kingX - 1][kingY + 1].piece.getPlayer();
			if(pieceInQuestion.equals("black")) {				// Cell has black Piece
				if(chessBoard[kingX - 1][kingY + 1].whiteAttackCell == 0) {		// Safe for King to eat
					return false;
				}
			}
		}
		
		if(kingY + 1 < 8) {									// Check above cells of King
			if(chessBoard[kingX][kingY + 1].empty) {			// Open cell
				if(chessBoard[kingX][kingY + 1].whiteAttackCell == 0) {		// Safe for King to move into
					return false;
				}
			}
			String pieceInQuestion = chessBoard[kingX][kingY + 1].piece.getPlayer();
			if(pieceInQuestion.equals("black")) {				// Cell has black Piece
				if(chessBoard[kingX][kingY + 1].whiteAttackCell == 0) {		// Safe for King to eat
					return false;
				}
			}
		}
		
		if(kingX + 1 < 8 && kingY + 1 < 8) {									// Check top right cells of King
			if(chessBoard[kingX + 1][kingY + 1].empty) {			// Open cell
				if(chessBoard[kingX + 1][kingY + 1].whiteAttackCell == 0) {		// Safe for King to move into
					return false;
				}
			}
			String pieceInQuestion = chessBoard[kingX + 1][kingY + 1].piece.getPlayer();
			if(pieceInQuestion.equals("black")) {				// Cell has black Piece
				if(chessBoard[kingX + 1][kingY + 1].whiteAttackCell == 0) {		// Safe for King to eat
					return false;
				}
			}
		}
		
		if(kingX + 1 < 8) {									// Check right cells of King
			if(chessBoard[kingX + 1][kingY].empty) {			// Open cell
				if(chessBoard[kingX + 1][kingY].whiteAttackCell == 0) {		// Safe for King to move into
					return false;
				}
			}
			String pieceInQuestion = chessBoard[kingX + 1][kingY].piece.getPlayer();
			if(pieceInQuestion.equals("black")) {				// Cell has black Piece
				if(chessBoard[kingX + 1][kingY].whiteAttackCell == 0) {		// Safe for King to eat
					return false;
				}
			}
		}
		
		if(kingX + 1 < 8 && kingY - 1 > -1) {									// Check bottom right cells of King
			if(chessBoard[kingX + 1][kingY - 1].empty) {			// Open cell
				if(chessBoard[kingX + 1][kingY - 1].whiteAttackCell == 0) {		// Safe for King to move into
					return false;
				}
			}
			String pieceInQuestion = chessBoard[kingX + 1][kingY - 1].piece.getPlayer();
			if(pieceInQuestion.equals("black")) {				// Cell has black Piece
				if(chessBoard[kingX + 1][kingY - 1].whiteAttackCell == 0) {		// Safe for King to eat
					return false;
				}
			}
		}
		
		if(kingY - 1 > -1) {									// Check bottom cells of King
			if(chessBoard[kingX][kingY - 1].empty) {			// Open cell
				if(chessBoard[kingX][kingY - 1].whiteAttackCell == 0) {		// Safe for King to move into
					return false;
				}
			}
			String pieceInQuestion = chessBoard[kingX][kingY - 1].piece.getPlayer();
			if(pieceInQuestion.equals("black")) {				// Cell has black Piece
				if(chessBoard[kingX][kingY - 1].whiteAttackCell == 0) {		// Safe for King to eat
					return false;
				}
			}
		}
		
		
		/*White King cannot run, must check if can be saved by a friendly piece*/
		
		// Check if attacked by Pawn and can be saved
		
		if(kingX - 1 > 0 && kingY - 1 > -1) {				// Check bottom left
			if(!chessBoard[kingX - 1][kingY - 1].empty) {	// Piece found
				if(chessBoard[kingX - 1][kingY - 1].piece instanceof Pawn) {		// Its a pawn
					String pawnPiece = chessBoard[kingX - 1][kingY - 1].piece.getPlayer();
					if(pawnPiece.equals("black")) {										// Its an opposing Pawn
						if(chessBoard[kingX - 1][kingY - 1].blackAttackCell > 0) {		// It can be eaten
							return false;
						}
						else {															// Cant save king
							return true;
						}
					}
				}
			}
		}
		
		if(kingX - 1 > 0 && kingY + 1 < 8) {				// Check top right
			if(!chessBoard[kingX - 1][kingY + 1].empty) {	// Piece found
				if(chessBoard[kingX - 1][kingY + 1].piece instanceof Pawn) {		// Its a pawn
					String pawnPiece = chessBoard[kingX - 1][kingY + 1].piece.getPlayer();
					if(pawnPiece.equals("black")) {										// Its an opposing Pawn
						if(chessBoard[kingX - 1][kingY + 1].blackAttackCell > 0) {		// It can be eaten
							return false;
						}
						else {															// Cant save king
							return true;
						}
					}
				}
			}
		}
		
		// Check if attacked by Knight and can be saved
		
		if(kingX - 2 > -1 && kingY - 1 > -1) {				// Left down
			if(!chessBoard[kingX - 2][kingY - 1].empty) {	// Piece found
				if(chessBoard[kingX - 2][kingY - 1].piece instanceof Knight) {	// Its a knight
					String knightPiece = chessBoard[kingX - 2][kingY - 1].piece.getPlayer();
					if(knightPiece.equals("black")) {										// Its an opposing knight
						if(chessBoard[kingX - 2][kingY - 1].blackAttackCell > 0) {			// It can be eaten
							return false;
						}
						else {																// Cant save king
							return true;
						}
					}
				}
			}
		}
		
		if(kingX - 2 > -1 && kingY + 1 < 8) {				// Left up
			if(!chessBoard[kingX - 2][kingY + 1].empty) {	// Piece found
				if(chessBoard[kingX - 2][kingY + 1].piece instanceof Knight) {	// Its a knight
					String knightPiece = chessBoard[kingX - 2][kingY + 1].piece.getPlayer();
					if(knightPiece.equals("black")) {										// Its an opposing knight
						if(chessBoard[kingX - 2][kingY + 1].blackAttackCell > 0) {			// It can be eaten
							return false;
						}
						else {																// Cant save king
							return true;
						}
					}
				}
			}
		}
		
		if(kingX + 2 < 8 && kingY - 1 > -1) {				// Right down
			if(!chessBoard[kingX + 2][kingY - 1].empty) {	// Piece found
				if(chessBoard[kingX + 2][kingY - 1].piece instanceof Knight) {	// Its a knight
					String knightPiece = chessBoard[kingX + 2][kingY - 1].piece.getPlayer();
					if(knightPiece.equals("black")) {										// Its an opposing knight
						if(chessBoard[kingX + 2][kingY - 1].blackAttackCell > 0) {			// It can be eaten
							return false;
						}
						else {																// Cant save king
							return true;
						}
					}
				}
			}
		}
		
		if(kingX + 2 < 8 && kingY + 1 < 8) {				// Right Up
			if(!chessBoard[kingX + 2][kingY + 1].empty) {	// Piece found
				if(chessBoard[kingX + 2][kingY + 1].piece instanceof Knight) {	// Its a knight
					String knightPiece = chessBoard[kingX + 2][kingY + 1].piece.getPlayer();
					if(knightPiece.equals("black")) {										// Its an opposing knight
						if(chessBoard[kingX + 2][kingY + 1].blackAttackCell > 0) {			// It can be eaten
							return false;
						}
						else {																// Cant save king
							return true;
						}
					}
				}
			}
		}
		
		if(kingX - 1 > -1 && kingY + 2  < 8) {				// Up Left
			if(!chessBoard[kingX - 1][kingY + 2].empty) {	// Piece found
				if(chessBoard[kingX - 1][kingY + 2].piece instanceof Knight) {	// Its a knight
					String knightPiece = chessBoard[kingX - 1][kingY + 2].piece.getPlayer();
					if(knightPiece.equals("black")) {										// Its an opposing knight
						if(chessBoard[kingX - 1][kingY + 2].blackAttackCell > 0) {			// It can be eaten
							return false;
						}
						else {																// Cant save king
							return true;
						}
					}
				}
			}
		}
		
		if(kingX + 1 < 8 && kingY + 2  < 8) {				// Up Right
			if(!chessBoard[kingX + 1][kingY + 2].empty) {	// Piece found
				if(chessBoard[kingX + 1][kingY + 2].piece instanceof Knight) {	// Its a knight
					String knightPiece = chessBoard[kingX + 1][kingY + 2].piece.getPlayer();
					if(knightPiece.equals("black")) {										// Its an opposing knight
						if(chessBoard[kingX + 1][kingY + 2].blackAttackCell > 0) {			// It can be eaten
							return false;
						}
						else {																// Cant save king
							return true;
						}
					}
				}
			}
		}
		
		if(kingX - 1 > -1 && kingY - 2  > -1) {				// Down Left
			if(!chessBoard[kingX - 1][kingY - 2].empty) {	// Piece found
				if(chessBoard[kingX - 1][kingY - 2].piece instanceof Knight) {	// Its a knight
					String knightPiece = chessBoard[kingX - 1][kingY - 2].piece.getPlayer();
					if(knightPiece.equals("black")) {										// Its an opposing knight
						if(chessBoard[kingX - 1][kingY - 2].blackAttackCell > 0) {			// It can be eaten
							return false;
						}
						else {																// Cant save king
							return true;
						}
					}
				}
			}
		}
		
		if(kingX + 1 < 8 && kingY - 2 > -1) {				// Down Right
			if(!chessBoard[kingX + 1][kingY - 2].empty) {	// Piece found
				if(chessBoard[kingX + 1][kingY - 2].piece instanceof Knight) {	// Its a knight
					String knightPiece = chessBoard[kingX + 1][kingY - 2].piece.getPlayer();
					if(knightPiece.equals("black")) {										// Its an opposing knight
						if(chessBoard[kingX + 1][kingY - 2].blackAttackCell > 0) {			// It can be eaten
							return false;
						}
						else {																// Cant save king
							return true;
						}
					}
				}
			}
		}
		
		// Now for any other pieces
		
		// Check horizontal and vertical before diagonal if saving king is possible
		
		// Check King's immediate surroundings first if rescue is possible
		
		if(kingX - 1 > -1) {								// Left of king
			if(!chessBoard[kingX - 1][kingY].empty) {	// Found piece
				Piece piece = chessBoard[kingX - 1][kingY].piece;
				String pieceTeam = piece.getPlayer();
				if(pieceTeam.equals("black")) {			// Its an opposing team
					if(piece instanceof Rook || piece instanceof Queen) {			// Its either an enemy rook or queen
						if(chessBoard[kingX - 1][kingY].blackAttackCell > 1) {	// It can be eaten
							return false;
						}
						else {													// Cant save king
							return true;
						}
					}
				}
			}
		}
		
		if(kingX + 1 < 8) {								// Right of king
			if(!chessBoard[kingX + 1][kingY].empty) {	// Found piece
				Piece piece = chessBoard[kingX + 1][kingY].piece;
				String pieceTeam = piece.getPlayer();
				if(pieceTeam.equals("black")) {			// Its an opposing team
					if(piece instanceof Rook || piece instanceof Queen) {			// Its either an enemy rook or queen
						if(chessBoard[kingX + 1][kingY].blackAttackCell > 1) {	// It can be eaten
							return false;
						}
						else {													// Cant save king
							return true;
						}
					}
				}
			}
		}
		
		if(kingY - 1 > -1) {								// Below of king
			if(!chessBoard[kingX][kingY - 1].empty) {	// Found piece
				Piece piece = chessBoard[kingX][kingY - 1].piece;
				String pieceTeam = piece.getPlayer();
				if(pieceTeam.equals("black")) {			// Its an opposing team
					if(piece instanceof Rook || piece instanceof Queen) {			// Its either an enemy rook or queen
						if(chessBoard[kingX][kingY - 1].blackAttackCell > 1) {	// It can be eaten
							return false;
						}
						else {													// Cant save king
							return true;
						}
					}
				}
			}
		}
		
		if(kingY + 1 < 8) {								// Above of king
			if(!chessBoard[kingX][kingY + 1].empty) {	// Found piece
				Piece piece = chessBoard[kingX][kingY + 1].piece;
				String pieceTeam = piece.getPlayer();
				if(pieceTeam.equals("black")) {			// Its an opposing team
					if(piece instanceof Rook || piece instanceof Queen) {			// Its either an enemy rook or queen
						if(chessBoard[kingX][kingY + 1].blackAttackCell > 1) {	// It can be eaten
							return false;
						}
						else {													// Cant save king
							return true;
						}
					}
				}
			}
		}
		
		if(kingX - 1 > -1 && kingY - 1 > -1) {								// Bottom left of king
			if(!chessBoard[kingX - 1][kingY - 1].empty) {	// Found piece
				Piece piece = chessBoard[kingX - 1][kingY - 1].piece;
				String pieceTeam = piece.getPlayer();
				if(pieceTeam.equals("black")) {			// Its an opposing team
					if(piece instanceof Rook || piece instanceof Queen) {			// Its either an enemy bishop or queen
						if(chessBoard[kingX - 1][kingY - 1].blackAttackCell > 1) {	// It can be eaten
							return false;
						}
						else {													// Cant save king
							return true;
						}
					}
				}
			}
		}
		
		if(kingX - 1 > -1 && kingY + 1 < 8) {								// Top left of king
			if(!chessBoard[kingX - 1][kingY + 1].empty) {	// Found piece
				Piece piece = chessBoard[kingX - 1][kingY + 1].piece;
				String pieceTeam = piece.getPlayer();
				if(pieceTeam.equals("black")) {			// Its an opposing team
					if(piece instanceof Rook || piece instanceof Queen) {			// Its either an enemy bishop or queen
						if(chessBoard[kingX - 1][kingY + 1].blackAttackCell > 1) {	// It can be eaten
							return false;
						}
						else {													// Cant save king
							return true;
						}
					}
				}
			}
		}
		
		if(kingX + 1 < 8 && kingY - 1 > -1) {								// Bottom Right of king
			if(!chessBoard[kingX + 1][kingY - 1].empty) {	// Found piece
				Piece piece = chessBoard[kingX + 1][kingY - 1].piece;
				String pieceTeam = piece.getPlayer();
				if(pieceTeam.equals("black")) {			// Its an opposing team
					if(piece instanceof Rook || piece instanceof Queen) {			// Its either an enemy bishop or queen
						if(chessBoard[kingX + 1][kingY - 1].blackAttackCell > 1) {	// It can be eaten
							return false;
						}
						else {													// Cant save king
							return true;
						}
					}
				}
			}
		}
		
		if(kingX + 1 < 8 && kingY + 1 < 8) {								// Top right of king
			if(!chessBoard[kingX + 1][kingY + 1].empty) {	// Found piece
				Piece piece = chessBoard[kingX + 1][kingY + 1].piece;
				String pieceTeam = piece.getPlayer();
				if(pieceTeam.equals("black")) {			// Its an opposing team
					if(piece instanceof Rook || piece instanceof Queen) {			// Its either an enemy bishop or queen
						if(chessBoard[kingX + 1][kingY + 1].blackAttackCell > 1) {	// It can be eaten
							return false;
						}
						else {													// Cant save king
							return true;
						}
					}
				}
			}
		}
		
		// No pieces found next to king, search outside immediate area
		// Search horizontal and vertical first
		
		for(int xCell = kingX - 2, yCell = kingY ; xCell > -1 ; xCell--) {		// Check left of king
			if(chessBoard[xCell][yCell].blackAttackCell > 0) {					// Possible block by ally
				blackPieceCanBlock = true;
			}
			if(!chessBoard[xCell][yCell].empty) {								// Found piece
				Piece piece = chessBoard[xCell][yCell].piece;
				String pieceTeam = piece.getPlayer();
				if(pieceTeam.equals("black")) {									// It is an opposing piece
					if(piece instanceof Rook || piece instanceof Queen) {			// Piece is either rook or queen
						if(blackPieceCanBlock) {									// It can be blocked, saves king
							return false;
						}
						else {													// Cant save king
							return true;
						}
					}
					else {														// Not attacking piece
						break;
					}
				}
				else {															// Friendly piece
					break;
				}
			}
		}
		
		blackPieceCanBlock = false;
		
		for(int xCell = kingX + 2, yCell = kingY ; xCell < 8 ; xCell++) {			// Check right of king
			if(chessBoard[xCell][yCell].blackAttackCell > 0) {					// Possible block by ally
				blackPieceCanBlock = true;
			}
			if(!chessBoard[xCell][yCell].empty) {								// Found piece
				Piece piece = chessBoard[xCell][yCell].piece;
				String pieceTeam = piece.getPlayer();
				if(pieceTeam.equals("black")) {									// It is an opposing piece
					if(piece instanceof Rook || piece instanceof Queen) {			// Piece is either rook or queen
						if(blackPieceCanBlock) {									// It can be blocked, saves king
							return false;
						}
						else {													// Cant save king
							return true;
						}
					}
					else {														// Not attacking piece
						break;
					}
				}
				else {															// Friendly piece
					break;
				}
			}
		}
		
		blackPieceCanBlock = false;
		
		for(int xCell = kingX, yCell = kingY + 2; yCell < 8 ; yCell++) {			// Check Above of king
			if(chessBoard[xCell][yCell].blackAttackCell > 0) {					// Possible block by ally
				blackPieceCanBlock = true;
			}
			if(!chessBoard[xCell][yCell].empty) {								// Found piece
				Piece piece = chessBoard[xCell][yCell].piece;
				String pieceTeam = piece.getPlayer();
				if(pieceTeam.equals("black")) {									// It is an opposing piece
					if(piece instanceof Rook || piece instanceof Queen) {			// Piece is either rook or queen
						if(blackPieceCanBlock) {									// It can be blocked, saves king
							return false;
						}
						else {													// Cant save king
							return true;
						}
					}
					else {														// Not attacking piece
						break;
					}
				}
				else {															// Friendly piece
					break;
				}
			}
		}
		
		blackPieceCanBlock = false;
		
		for(int xCell = kingX, yCell = kingY - 2; yCell > -1 ; xCell--) {			// Check below of king
			if(chessBoard[xCell][yCell].blackAttackCell > 0) {					// Possible block by ally
				blackPieceCanBlock = true;
			}
			if(!chessBoard[xCell][yCell].empty) {								// Found piece
				Piece piece = chessBoard[xCell][yCell].piece;
				String pieceTeam = piece.getPlayer();
				if(pieceTeam.equals("black")) {									// It is an opposing piece
					if(piece instanceof Rook || piece instanceof Queen) {			// Piece is either rook or queen
						if(blackPieceCanBlock) {									// It can be blocked, saves king
							return false;
						}
						else {													// Cant save king
							return true;
						}
					}
					else {														// Not attacking piece
						break;
					}
				}
				else {															// Friendly piece
					break;
				}
			}
		}
		
		
		// Not being attacked horizontal or vertical, search diagonal now
		
		blackPieceCanBlock = false;
		
		for(int xCell = kingX - 2, yCell = kingY - 2 ; xCell > -1 && yCell > -1 ; xCell--, yCell--) {	// Bottom left of king
			if(chessBoard[xCell][yCell].blackAttackCell > 0) {					// Possible block by ally
				blackPieceCanBlock = true;
			}
			if(!chessBoard[xCell][yCell].empty) {								// Found piece
				Piece piece = chessBoard[xCell][yCell].piece;
				String pieceTeam = piece.getPlayer();
				if(pieceTeam.equals("black")) {									// It is an opposing piece
					if(piece instanceof Rook || piece instanceof Queen) {			// Piece is either rook or queen
						if(blackPieceCanBlock) {									// It can be blocked, saves king
							return false;
						}
						else {													// Cant save king
							return true;
						}
					}
					else {														// Not attacking piece
						break;
					}
				}
				else {															// Friendly piece
					break;
				}
			}
		}
		
		blackPieceCanBlock = false;
		
		for(int xCell = kingX - 2, yCell = kingY + 2 ; xCell > -1 && yCell < 8 ; xCell--, yCell++) {	// Top left of king
			if(chessBoard[xCell][yCell].blackAttackCell > 0) {					// Possible block by ally
				blackPieceCanBlock = true;
			}
			if(!chessBoard[xCell][yCell].empty) {								// Found piece
				Piece piece = chessBoard[xCell][yCell].piece;
				String pieceTeam = piece.getPlayer();
				if(pieceTeam.equals("black")) {									// It is an opposing piece
					if(piece instanceof Rook || piece instanceof Queen) {			// Piece is either rook or queen
						if(blackPieceCanBlock) {									// It can be blocked, saves king
							return false;
						}
						else {													// Cant save king
							return true;
						}
					}
					else {														// Not attacking piece
						break;
					}
				}
				else {															// Friendly piece
					break;
				}
			}
		}
		
		blackPieceCanBlock = false;
		
		for(int xCell = kingX + 2, yCell = kingY + 2 ; xCell < 8 && yCell < 8 ; xCell++, yCell++) {	// Top right of king
			if(chessBoard[xCell][yCell].blackAttackCell > 0) {					// Possible block by ally
				blackPieceCanBlock = true;
			}
			if(!chessBoard[xCell][yCell].empty) {								// Found piece
				Piece piece = chessBoard[xCell][yCell].piece;
				String pieceTeam = piece.getPlayer();
				if(pieceTeam.equals("black")) {									// It is an opposing piece
					if(piece instanceof Rook || piece instanceof Queen) {			// Piece is either rook or queen
						if(blackPieceCanBlock) {									// It can be blocked, saves king
							return false;
						}
						else {													// Cant save king
							return true;
						}
					}
					else {														// Not attacking piece
						break;
					}
				}
				else {															// Friendly piece
					break;
				}
			}
		}
		
		blackPieceCanBlock = false;
		
		for(int xCell = kingX + 2, yCell = kingY - 2 ; xCell < 8 && yCell > -1 ; xCell++, yCell--) {	// Bottom right of king
			if(chessBoard[xCell][yCell].blackAttackCell > 0) {					// Possible block by ally
				blackPieceCanBlock = true;
			}
			if(!chessBoard[xCell][yCell].empty) {								// Found piece
				Piece piece = chessBoard[xCell][yCell].piece;
				String pieceTeam = piece.getPlayer();
				if(pieceTeam.equals("black")) {									// It is an opposing piece
					if(piece instanceof Rook || piece instanceof Queen) {			// Piece is either rook or queen
						if(blackPieceCanBlock) {									// It can be blocked, saves king
							return false;
						}
						else {													// Cant save king
							return true;
						}
					}
					else {														// Not attacking piece
						break;
					}
				}
				else {															// Friendly piece
					break;
				}
			}
		}

		//System.out.println("Didn't account for something for King being checked mate");
		return true;
	}
	
	public boolean equals(Object o) {
		
		if(o != null || !(o instanceof Piece)) {
			return false;
		}
		
		King other = (King)o;
		
		return true;
	}
}
