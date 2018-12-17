package chess;

/**
 * <h1>Rook class: represents a rook piece </h1>
 * <b>Details:</b> This class extends the class Piece.
 * 
 * @author royjacome
 * @since   2018-02-23
 */
public class Rook extends Piece {
	
	public Rook(String player, String pieceName, int xCoordinate, int yCoordinate, BoardCell[][] chessBoard) {
		super(player, pieceName);
		this.piece_xCoordinate = xCoordinate;
		this.piece_yCoordinate = yCoordinate;
		
		incrementAttackPath(chessBoard);
		chessBoard[xCoordinate][yCoordinate].empty = false;
	}
	/* (non-Javadoc)
	 * @see chess.PieceAttribute#eating(chess.board, int, int)
	 */
	@Override
	public boolean eating(Board board, int new_xCoordinate, int new_yCoordinate) {
		
		Rook rook = this;
		
		Piece pieceToBeEaten = board.board[new_xCoordinate][new_yCoordinate].piece;
		String pieceOwner = pieceToBeEaten.getPlayer();
		String currentPlayer = getPlayer();
		
		if(!pieceOwner.equals(currentPlayer)) {
			int originalX = rook.piece_xCoordinate;
			int originalY = rook.piece_yCoordinate;
			board.prepareUpdatingBoard(originalX, originalY, new_xCoordinate, new_yCoordinate); 
			rook.decrementAttackPath(board.board);
			//pieceToBeEaten.decrementAttackPath(board.board);
			board.board[originalX][originalY].empty = true;
			board.board[originalX][originalY].piece = null;
			rook.setMoved(true);
			board.board[new_xCoordinate][new_yCoordinate].empty = false;
			board.board[new_xCoordinate][new_yCoordinate].piece = rook;
			rook.piece_xCoordinate = new_xCoordinate;
			rook.piece_yCoordinate = new_yCoordinate;
			rook.incrementAttackPath(board.board);
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
		
		Rook rook = this;
		
		if(!checkValidMove(board.board, new_xCoordinate, new_yCoordinate)) {
			return false;
		}
		
		if(!board.board[new_xCoordinate][new_yCoordinate].empty) {
			if(eating(board, new_xCoordinate, new_yCoordinate)) {
				
				return true;
			}
		}
		// If not then just move Rook
		else {
			int originalX = rook.piece_xCoordinate;
			int originalY = rook.piece_yCoordinate;
			board.prepareUpdatingBoard(originalX, originalY, new_xCoordinate, new_yCoordinate); 
			decrementAttackPath(board.board);
			board.board[originalX][originalY].empty = true;
			board.board[originalX][originalY].piece = null;
			rook.setMoved(true);
			board.board[new_xCoordinate][new_yCoordinate].empty = false;
			board.board[new_xCoordinate][new_yCoordinate].piece = rook;
			rook.piece_xCoordinate = new_xCoordinate;
			rook.piece_yCoordinate = new_yCoordinate;
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
		Rook rook = this;
		int rookX = rook.piece_xCoordinate;
		int rookY = rook.piece_yCoordinate;
		boolean 
		decreasedX = false,
		increasedX = false,
		decreasedY = false,
		increasedY = false;
		
		if(rookX - new_xCoordinate > 0) {
			decreasedX = true;
		}
		else {
			increasedX = true;
		}
		
		if(rookY - new_yCoordinate > 0) {
			decreasedY = true;
		}
		else {
			increasedY = true;
		}
		
		if(new_xCoordinate + new_yCoordinate > 16) {
			//System.out.println("Invalid move");
			return false;
		}
		if(rook.piece_xCoordinate != new_xCoordinate && rook.piece_yCoordinate == new_yCoordinate) {	// Move horizontally
			
			if(decreasedX) {		// Going left of board
				for(int xCell = rookX - 1, yCell = rookY ; xCell > new_xCoordinate ; xCell--) {	// check no piece is in the way
					if(!chessBoard[xCell][yCell].empty) {
						return false;
					}
				}
				return true;
			}
			else if(increasedX) {	// Going right of board
				for(int xCell = rookX + 1, yCell = rookY ; xCell < new_xCoordinate ; xCell++) {		// Check no piece is in the way
					if(!chessBoard[xCell][yCell].empty) {
						return false;
					}
				}
				return true;
			}
		}
		
		if(rook.piece_xCoordinate == new_xCoordinate && rook.piece_yCoordinate != new_yCoordinate) {
			
			if(decreasedY) {		// Going down the Board
				for(int xCell = rookX, yCell = rookY - 1; yCell > new_yCoordinate ; yCell--) {
					if(!chessBoard[xCell][yCell].empty) {
						return false;
					}
				}
				return true;
			}
			else if(increasedY) {	// Going up the board
				for(int xCell = rookX, yCell = rookY + 1; yCell < new_yCoordinate ; yCell++) {
					if(!chessBoard[xCell][yCell].empty) {
						return false;
					}
				}
				return true;
			}
			return true;
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see chess.PieceAttribute#incrementAttackPath(BoardCell[][] chessBoard)
	 */
	@Override
	public void incrementAttackPath(BoardCell[][] chessBoard) {
		
		Rook rook = this;
		
		if(rook.getAttackPathVisible()) {
			return;
		}
		
		String currentPlayer = rook.getPlayer();
		
		if(currentPlayer.equals("white")) {									// Rook is a White Piece
			
			for(int rookX = rook.piece_xCoordinate - 1; rookX > -1 ; rookX--) {						// Check cells to left of Rook
				if(chessBoard[rookX][rook.piece_yCoordinate].empty == true) {
					chessBoard[rookX][rook.piece_yCoordinate].whiteAttackCell++;
				}
				else {
					if(chessBoard[rookX][rook.piece_yCoordinate].piece instanceof King) {				// Continue attack if enemy King
						if(chessBoard[rookX][rook.piece_yCoordinate].piece.getPlayer().equals("black")) {
							chessBoard[rookX][rook.piece_yCoordinate].whiteAttackCell++;
							continue;
						}
					}
					chessBoard[rookX][rook.piece_yCoordinate].whiteAttackCell++;
					break;
				}
			}
			
			for(int rookX = rook.piece_xCoordinate + 1 ; rookX < 8 ; rookX++) {						// Check cells to right of Rook
				if(chessBoard[rookX][rook.piece_yCoordinate].empty == true) {
					chessBoard[rookX][rook.piece_yCoordinate].whiteAttackCell++;
				}
				else {
					if(chessBoard[rookX][rook.piece_yCoordinate].piece instanceof King) {				// Continue attack if enemy King
						if(chessBoard[rookX][rook.piece_yCoordinate].piece.getPlayer().equals("black")) {
							chessBoard[rookX][rook.piece_yCoordinate].whiteAttackCell++;
							continue;
						}
					}
					chessBoard[rookX][rook.piece_yCoordinate].whiteAttackCell++;
					break;
				}
			}
			
			for(int rookY = rook.piece_yCoordinate + 1 ; rookY < 8 ; rookY++) {						// Check all cells above Rook
				if(chessBoard[rook.piece_xCoordinate][rookY].empty == true) {
					chessBoard[rook.piece_xCoordinate][rookY].whiteAttackCell++;
				}
				else {
					if(chessBoard[rook.piece_xCoordinate][rookY].piece instanceof King) {				// Continue attack if enemy King
						if(chessBoard[rook.piece_xCoordinate][rookY].piece.getPlayer().equals("black")) {
							chessBoard[rook.piece_xCoordinate][rookY].whiteAttackCell++;
							continue;
						}
					}
					chessBoard[rook.piece_xCoordinate][rookY].whiteAttackCell++;
					break;
				}
			}
			
			for(int rookY = rook.piece_yCoordinate - 1 ; rookY > -1 ; rookY--) {						// Check all cells below Rook
				if(chessBoard[rook.piece_xCoordinate][rookY].empty == true) {
					chessBoard[rook.piece_xCoordinate][rookY].whiteAttackCell++;
				}
				else {
					if(chessBoard[rook.piece_xCoordinate][rookY].piece instanceof King) {				// Continue attack if enemy King
						if(chessBoard[rook.piece_xCoordinate][rookY].piece.getPlayer().equals("black")) {
							chessBoard[rook.piece_xCoordinate][rookY].whiteAttackCell++;
							continue;
						}
					}
					chessBoard[rook.piece_xCoordinate][rookY].whiteAttackCell++;
					break;
				}
			}
			
			rook.setAttackPathVisible(true);
			return;
		}
		
		if(currentPlayer.equals("black")) {										// Rook is a Black piece
			
			for(int rookX = rook.piece_xCoordinate - 1; rookX > -1 ; rookX--) {						// Check cells to left of Rook
				if(chessBoard[rookX][rook.piece_yCoordinate].empty == true) {
					chessBoard[rookX][rook.piece_yCoordinate].blackAttackCell++;
				}
				else {
					if(chessBoard[rookX][rook.piece_yCoordinate].piece instanceof King) {				// Continue attack if enemy King
						if(chessBoard[rookX][rook.piece_yCoordinate].piece.getPlayer().equals("white")) {
							chessBoard[rookX][rook.piece_yCoordinate].blackAttackCell++;
							continue;
						}
					}
					chessBoard[rookX][rook.piece_yCoordinate].blackAttackCell++;
					break;
				}
			}
			
			for(int rookX = rook.piece_xCoordinate + 1 ; rookX < 8 ; rookX++) {						// Check cells to right of Rook
				if(chessBoard[rookX][rook.piece_yCoordinate].empty == true) {
					chessBoard[rookX][rook.piece_yCoordinate].blackAttackCell++;
				}
				else {
					if(chessBoard[rookX][rook.piece_yCoordinate].piece instanceof King) {				// Continue attack if enemy King
						if(chessBoard[rookX][rook.piece_yCoordinate].piece.getPlayer().equals("white")) {
							chessBoard[rookX][rook.piece_yCoordinate].blackAttackCell++;
							continue;
						}
					}
					chessBoard[rookX][rook.piece_yCoordinate].blackAttackCell++;
					break;
				}
			}
			
			for(int rookY = rook.piece_yCoordinate + 1 ; rookY < 8 ; rookY++) {						// Check all cells above Rook
				if(chessBoard[rook.piece_xCoordinate][rookY].empty == true) {
					chessBoard[rook.piece_xCoordinate][rookY].blackAttackCell++;
				}
				else {
					if(chessBoard[rook.piece_xCoordinate][rookY].piece instanceof King) {				// Continue attack if enemy King
						if(chessBoard[rook.piece_xCoordinate][rookY].piece.getPlayer().equals("white")) {
							chessBoard[rook.piece_xCoordinate][rookY].blackAttackCell++;
							continue;
						}
					}
					chessBoard[rook.piece_xCoordinate][rookY].blackAttackCell++;
					break;
				}
			}
			
			for(int rookY = rook.piece_yCoordinate - 1 ; rookY > -1 ; rookY--) {						// Check all cells below Rook
				if(chessBoard[rook.piece_xCoordinate][rookY].empty == true) {
					chessBoard[rook.piece_xCoordinate][rookY].blackAttackCell++;
				}
				else {
					if(chessBoard[rook.piece_xCoordinate][rookY].piece instanceof King) {				// Continue attack if enemy King
						if(chessBoard[rook.piece_xCoordinate][rookY].piece.getPlayer().equals("white")) {
							chessBoard[rook.piece_xCoordinate][rookY].blackAttackCell++;
							continue;
						}
					}
					chessBoard[rook.piece_xCoordinate][rookY].blackAttackCell++;
					break;
				}
			}
			
			rook.setAttackPathVisible(true);
			return;
		}
		
		return;
	}
	
	/* (non-Javadoc)
	 * @see chess.PieceAttribute#decrementAttackPath(BoardCell[][] chessBoard)
	 */
	@Override
	public void decrementAttackPath(BoardCell[][] chessBoard) {
		
		Rook rook = this;
		
		if(!rook.getAttackPathVisible()) {
			return;
		}
		
		String currentPlayer = rook.getPlayer();
		
		if(currentPlayer.equals("white")) {									// Rook is a White Piece
			
			for(int rookX = rook.piece_xCoordinate - 1; rookX > -1 ; rookX--) {						// Check cells to left of Rook
				if(chessBoard[rookX][rook.piece_yCoordinate].empty == true) {
					chessBoard[rookX][rook.piece_yCoordinate].whiteAttackCell--;
				}
				else {
					if(chessBoard[rookX][rook.piece_yCoordinate].piece instanceof King) {				// Continue attack if enemy King
						if(chessBoard[rookX][rook.piece_yCoordinate].piece.getPlayer().equals("black")) {
							chessBoard[rookX][rook.piece_yCoordinate].whiteAttackCell--;
							continue;
						}
					}
					chessBoard[rookX][rook.piece_yCoordinate].whiteAttackCell--;
					break;
				}
			}
			
			for(int rookX = rook.piece_xCoordinate + 1 ; rookX < 8 ; rookX++) {						// Check cells to right of Rook
				if(chessBoard[rookX][rook.piece_yCoordinate].empty == true) {
					chessBoard[rookX][rook.piece_yCoordinate].whiteAttackCell--;
				}
				else {
					if(chessBoard[rookX][rook.piece_yCoordinate].piece instanceof King) {				// Continue attack if enemy King
						if(chessBoard[rookX][rook.piece_yCoordinate].piece.getPlayer().equals("black")) {
							chessBoard[rookX][rook.piece_yCoordinate].whiteAttackCell--;
							continue;
						}
					}
					chessBoard[rookX][rook.piece_yCoordinate].whiteAttackCell--;
					break;
				}
			}
			
			for(int rookY = rook.piece_yCoordinate + 1 ; rookY < 8 ; rookY++) {						// Check all cells above Rook
				if(chessBoard[rook.piece_xCoordinate][rookY].empty == true) {
					chessBoard[rook.piece_xCoordinate][rookY].whiteAttackCell--;
				}
				else {
					if(chessBoard[rook.piece_xCoordinate][rookY].piece instanceof King) {				// Continue attack if enemy King
						if(chessBoard[rook.piece_xCoordinate][rookY].piece.getPlayer().equals("black")) {
							chessBoard[rook.piece_xCoordinate][rookY].whiteAttackCell--;
							continue;
						}
					}
					chessBoard[rook.piece_xCoordinate][rookY].whiteAttackCell--;
					break;
				}
			}
			
			for(int rookY = rook.piece_yCoordinate - 1 ; rookY > -1 ; rookY--) {						// Check all cells below Rook
				if(chessBoard[rook.piece_xCoordinate][rookY].empty == true) {
					chessBoard[rook.piece_xCoordinate][rookY].whiteAttackCell--;
				}
				else {
					chessBoard[rook.piece_xCoordinate][rookY].whiteAttackCell--;
					break;
				}
			}
			rook.setAttackPathVisible(false);
			return;
		}
		
		if(currentPlayer.equals("black")) {										// Rook is a Black piece
			
			for(int rookX = rook.piece_xCoordinate - 1; rookX > -1 ; rookX--) {						// Check cells to left of Rook
				if(chessBoard[rookX][rook.piece_yCoordinate].empty == true) {
					chessBoard[rookX][rook.piece_yCoordinate].blackAttackCell--;
				}
				else {
					if(chessBoard[rookX][rook.piece_yCoordinate].piece instanceof King) {				// Continue attack if enemy King
						if(chessBoard[rookX][rook.piece_yCoordinate].piece.getPlayer().equals("white")) {
							chessBoard[rookX][rook.piece_yCoordinate].blackAttackCell--;
							continue;
						}
					}
					chessBoard[rookX][rook.piece_yCoordinate].blackAttackCell--;
					break;
				}
			}
			
			for(int rookX = rook.piece_xCoordinate + 1 ; rookX < 8 ; rookX++) {						// Check cells to right of Rook
				if(chessBoard[rookX][rook.piece_yCoordinate].empty == true) {
					chessBoard[rookX][rook.piece_yCoordinate].blackAttackCell--;
				}
				else {
					if(chessBoard[rookX][rook.piece_yCoordinate].piece instanceof King) {				// Continue attack if enemy King
						if(chessBoard[rookX][rook.piece_yCoordinate].piece.getPlayer().equals("white")) {
							chessBoard[rookX][rook.piece_yCoordinate].blackAttackCell--;
							continue;
						}
					}
					chessBoard[rookX][rook.piece_yCoordinate].blackAttackCell--;
					break;
				}
			}
			
			for(int rookY = rook.piece_yCoordinate + 1 ; rookY < 8 ; rookY++) {						// Check all cells above Rook
				if(chessBoard[rook.piece_xCoordinate][rookY].empty == true) {
					chessBoard[rook.piece_xCoordinate][rookY].blackAttackCell--;
				}
				else {
					if(chessBoard[rook.piece_xCoordinate][rookY].piece instanceof King) {				// Continue attack if enemy King
						if(chessBoard[rook.piece_xCoordinate][rookY].piece.getPlayer().equals("white")) {
							chessBoard[rook.piece_xCoordinate][rookY].blackAttackCell--;
							continue;
						}
					}
					chessBoard[rook.piece_xCoordinate][rookY].blackAttackCell--;
					break;
				}
			}
			
			for(int rookY = rook.piece_yCoordinate - 1 ; rookY > -1 ; rookY--) {						// Check all cells below Rook
				if(chessBoard[rook.piece_xCoordinate][rookY].empty == true) {
					chessBoard[rook.piece_xCoordinate][rookY].blackAttackCell--;
				}
				else {
					if(chessBoard[rook.piece_xCoordinate][rookY].piece instanceof King) {				// Continue attack if enemy King
						if(chessBoard[rook.piece_xCoordinate][rookY].piece.getPlayer().equals("white")) {
							chessBoard[rook.piece_xCoordinate][rookY].blackAttackCell--;
							continue;
						}
					}
					chessBoard[rook.piece_xCoordinate][rookY].blackAttackCell--;
					break;
				}
			}
			rook.setAttackPathVisible(false);
			return;
		}
		
		return;
	}

}