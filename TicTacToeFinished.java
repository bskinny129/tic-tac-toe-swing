import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class TicTacToeFinished extends JFrame implements MouseListener {	

	int boardPieces[][];
	int title_bar_height = 23;
	int squares_per_side = 3;
	int pixels_per_side = 600;
	int label_height = 25;
	
	private boolean isXTurn = true;
	private boolean isGameOver = false;
			
	//Number of pixels wide the square is
	private int sq_pixels = pixels_per_side / squares_per_side;
	GameBoard gb;
	
	//used for randomly selecting a move
    private Random randomGenerator = new Random();
    
    public class TreeNode {
    	private Point moveLocation;
    	private boolean moveIsX;
        private TreeNode parentMove;
        private ArrayList<TreeNode> childrenMoves;
    }

	public static void main(String[] args) {  
		new TicTacToeFinished();  
	}
	
	public TicTacToeFinished(){  
		
		//set up our game variables
		boardPieces = new int[squares_per_side][squares_per_side];
				
		gb = new GameBoard(pixels_per_side, squares_per_side, boardPieces);
		gb.setLabel("X Turn");
		add(gb);
		
		//set a few things for our JFrame
		//pack() sizes the window around the board, so the board is exactly
		//pixels_per_side wide no matter how thick the window border is.
		pack();
		setVisible(true);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		//listen for clicks on the BOARD, not the whole window. That way the
		//click coordinates already start at the top-left of the board and we
		//don't have to subtract the height of the title bar (which is a
		//different size on Mac, Windows, and Linux).
		gb.addMouseListener(this);

	}
	

	public void mouseClicked(MouseEvent e) {
		//Does Integer Division by the size of a square in order to get the number square
		//(i.e. the top leftmost square is square (0, 0), bottom rightmost square is square(2,2)
		
		int newX = e.getX() / sq_pixels;
		int newY = e.getY() / sq_pixels;

		//ignore clicks that land outside the 3x3 grid (like on the label strip
		//along the bottom) - otherwise the game crashes
		if(newX < 0 || newX >= squares_per_side || newY < 0 || newY >= squares_per_side){
			return;
		}

		//only allowed if spot is open
		if(!isGameOver && isXTurn && boardPieces[newX][newY] == 0){
		
			boardPieces[newX][newY] = 1;								
			checkGameOver();
			this.repaint();
			
			if(!isGameOver){
				isXTurn = false;
				gb.setLabel("O Turn");
				this.repaint();
				try {
		            Thread.sleep(20);
		        } catch (InterruptedException f) {
		            f.printStackTrace();
		        }
				doComputerTurn();
			}
		
		}

	}
	
	private void doComputerTurn(){
		
		System.out.println("Computer turn");
		
		Point move = determineMove(boardPieces);
				
		//update the board with the move
		boardPieces[move.x][move.y] = 2;
		
		isXTurn = true;
		gb.setLabel("X Turn");
		checkGameOver();
		this.repaint();
	}
	
	//there would be 9! = 362,880 possible orders for moves. First move is human though, so only 8! = 40,320 after that
	private Point determineMove(int[][] currentBoard){
		
		//get all the possible moves
		ArrayList<Point> possibleMoves = getOpenMoves(currentBoard);
		
		TreeNode root = new TreeNode();		
		generateMoveTree(root, possibleMoves, false);
		
		Point winningMove = hasWinningMove(root, currentBoard);
		if(winningMove != null){
			//do winning move
			System.out.println("O can win, do it");
			return winningMove;
		}
		
		Point preventLosingMove = hasPreventLosingMove(root, currentBoard);
		if(preventLosingMove != null){
			//do prevent losing move
			System.out.println("X can win, prevent");
			return preventLosingMove;
		}
		
		//neither, just do random move
		System.out.println("Make random move");
		int index = randomGenerator.nextInt(possibleMoves.size());
		return possibleMoves.get(index);
			
	}

	
	private Point hasWinningMove(TreeNode root, int[][] currentBoard){
		
		//create copy in case we mess it up
		int [][] boardCopy = copyBoard(currentBoard);
		
		//the first level down will be O computer moves, see if you can win
		for(int i=0; i<root.childrenMoves.size(); i++){
			TreeNode currentMove = root.childrenMoves.get(i);
						
			boardCopy[currentMove.moveLocation.x][currentMove.moveLocation.y] = 2; //2 is O, computer move
			
			if(doesPlayerWin(boardCopy, 2)){
				return currentMove.moveLocation;
			}
			
			//undo move
			boardCopy[currentMove.moveLocation.x][currentMove.moveLocation.y] = 0;
			
		}
		
		return null; //no winning move found
		
	}
	
	private Point hasPreventLosingMove(TreeNode root, int[][] currentBoard){
		
		//the first level down will be O computer moves
		for(int i=0; i<root.childrenMoves.size(); i++){
			TreeNode currentOMove = root.childrenMoves.get(i);
			
			//create copy in case we mess it up
			int [][] boardCopy = copyBoard(currentBoard);
						
			//make the O move
			boardCopy[currentOMove.moveLocation.x][currentOMove.moveLocation.y] = 2; //2 is O, computer move
			
			//check all the X moves now
			for(int j=0; j<currentOMove.childrenMoves.size(); j++){
				
				TreeNode currentXMove = currentOMove.childrenMoves.get(j);
				
				//make the O move
				boardCopy[currentXMove.moveLocation.x][currentXMove.moveLocation.y] = 1; //1 is X, human move
				
				if(doesPlayerWin(boardCopy, 1)){
					return currentXMove.moveLocation;
				}
				
				//undo move
				boardCopy[currentXMove.moveLocation.x][currentXMove.moveLocation.y] = 0;
				
			}
			
			//undo the O move
			boardCopy[currentOMove.moveLocation.x][currentOMove.moveLocation.y] = 0;
			
		}
		
		return null; //no prevent losing move found
		
	}
	
	private void generateMoveTree(TreeNode parent, ArrayList<Point> openMoves, boolean isXTurn){
		
		if(openMoves.size() == 0){
			parent.childrenMoves = null;
			return;
		}
		
		//determine all child moves
		ArrayList<TreeNode> childMoves = new ArrayList<TreeNode>();
		
		for(int i=0; i<openMoves.size(); i++){
			
			Point move = openMoves.get(i);
			
			TreeNode newChild = new TreeNode();
			newChild.moveLocation = move;
			newChild.moveIsX = isXTurn;
			newChild.parentMove = parent;
			
			//generate new moves list
			ArrayList<Point> nextMoves = (ArrayList<Point>) openMoves.clone();
			nextMoves.remove(move);
		
			generateMoveTree(newChild, nextMoves, !isXTurn);
			
			childMoves.add(newChild);

		}
		
		parent.childrenMoves = childMoves;
		
	}
	
	private int[][] copyBoard(int[][] original){
		//create a new 2D array and clone the original arrays into it
		int [][] copiedBoard = new int[original.length][];
		for(int i = 0; i < original.length; i++){
			copiedBoard[i] = original[i].clone();
		}
		
		return copiedBoard;
	}
	
	private void checkGameOver(){
		
		if(doesPlayerWin(boardPieces, 1)){
			gb.setLabel("X wins!");
			isGameOver = true;
		}
		
		else if(doesPlayerWin(boardPieces, 2)){
			gb.setLabel("O wins!");
			isGameOver = true;
		}
		
		else if(isCatsGame(boardPieces)){
			gb.setLabel("Cats game");
			isGameOver = true;
		}
		
	}
	
	private boolean isCatsGame(int[][] boardState){
		//if all the spots are filled
		if(boardState[0][0]!=0 && boardState[1][0]!=0 && boardState[2][0]!=0 && boardState[0][1]!=0 && boardState[1][1]!=0 && boardState[2][1]!=0 && boardState[0][2]!=0 && boardState[1][2]!=0 && boardState[2][2]!=0){
			return true;
		}
		
		return false;
	}
	
	private ArrayList<Point> getOpenMoves(int[][] boardState){
		
		ArrayList<Point> moves = new ArrayList<Point>();
		
		//loop over the 3x3 grid
		for(int i=0; i<3; i++){
			for(int j=0; j<3; j++){
				//if the spot is open, add to the arraylist
				if(boardState[i][j] == 0){
					moves.add(new Point(i, j));
				}
			}
		}
		
		return moves;
	}
	
	private boolean doesPlayerWin(int[][] boardState, int playerNum){
		//check for 3 in a rows vertical
		if( (boardState[0][0]==playerNum && boardState[0][0]==boardState[0][1] && boardState[0][1]==boardState[0][2]) || 
			(boardState[1][0]==playerNum && boardState[1][0]==boardState[1][1] && boardState[1][1]==boardState[1][2]) || 
			(boardState[2][0]==playerNum && boardState[2][0]==boardState[2][1] && boardState[2][1]==boardState[2][2]) ){
				return true;
		}
		
		//check for 3 in a rows horizontal
		if( (boardState[0][0]==playerNum && boardState[0][0]==boardState[1][0] && boardState[1][0]==boardState[2][0]) || 
			(boardState[0][1]==playerNum && boardState[0][1]==boardState[1][1] && boardState[1][1]==boardState[2][1]) || 
			(boardState[0][2]==playerNum && boardState[0][2]==boardState[1][2] && boardState[1][2]==boardState[2][2]) ){
				return true;
		}
		
		//check for diagonals
		if( (boardState[1][1]==playerNum && boardState[0][0]==boardState[1][1] && boardState[1][1]==boardState[2][2]) || 
			(boardState[1][1]==playerNum && boardState[0][2]==boardState[1][1] && boardState[1][1]==boardState[2][0]) ){
				return true;
		}
		
		return false;
	}
	
	
	
	/* not using the following mouse listener methods, but need these here */
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
	}
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub		
	}
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub	
	}
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub	
	}


}
