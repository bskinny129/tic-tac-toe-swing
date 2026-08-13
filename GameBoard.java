import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

public class GameBoard extends JPanel {
		
		private int BOARD_SIDE_PIXELS;
		private int BOARD_SIDE_SQUARES;
		private int SQUARE_PIXELS;
		private int BOARD_PIECES[][];
		private String labelValue = "";

		public GameBoard(int pixels_per_side, int squares_per_side, int boardPieces[][]){
			
			BOARD_SIDE_PIXELS = pixels_per_side;
			BOARD_SIDE_SQUARES = squares_per_side;
			SQUARE_PIXELS = pixels_per_side / squares_per_side;
			BOARD_PIECES = boardPieces;

			//how big this panel wants to be: the board, plus a strip along the
			//bottom for the "X Turn" / "O wins!" label
			setPreferredSize(new Dimension(pixels_per_side, pixels_per_side + 25));
			setBackground(Color.WHITE);

		}
		
		public void paintComponent(Graphics g) { 

			super.paintComponent(g);
			
			drawSeparatorLines(g);
			drawPieces(g);
			drawLabel(g);

	    }
		
		public void setLabel(String newValue) {
			labelValue = newValue;
		}
		
		private void drawLabel(Graphics g){
			g.setColor(Color.BLACK);

			//line at bottom of board
			g.drawLine(0, BOARD_SIDE_PIXELS, BOARD_SIDE_PIXELS, BOARD_SIDE_PIXELS);
			
			g.drawString(labelValue, 10, BOARD_SIDE_PIXELS + 18);
			
		}
		
		//not used for checkers, will be used for other games
		private void drawSeparatorLines(Graphics g){
			
			//horizontal lines
			for(int i=1; i<BOARD_SIDE_SQUARES; i++){
				//x is always 0 to board size
				g.drawLine(0, i*SQUARE_PIXELS, BOARD_SIDE_PIXELS, i*SQUARE_PIXELS);
			}
			
			//vertical lines
			for(int i=1; i<BOARD_SIDE_SQUARES; i++){
				//y is always 0 to board size
				g.drawLine(i*SQUARE_PIXELS, 0, i*SQUARE_PIXELS, BOARD_SIDE_PIXELS);
			}
			
		}
		
		private void drawPieces(Graphics g){
			
			//leave a little space between the edge of the square
			int pixelBuffer = 8;
			
			for(int i=0; i<BOARD_SIDE_SQUARES; i++){
				for(int j=0; j<BOARD_SIDE_SQUARES; j++){
					
					int piece = BOARD_PIECES[i][j];
					if(piece != 0){
						
						if(piece == 1){
							g.setColor(Color.RED);
							g.drawLine(i * SQUARE_PIXELS + pixelBuffer, j*SQUARE_PIXELS + pixelBuffer, i * SQUARE_PIXELS + SQUARE_PIXELS - pixelBuffer, j * SQUARE_PIXELS + SQUARE_PIXELS - pixelBuffer);
							g.drawLine(i * SQUARE_PIXELS + pixelBuffer, j * SQUARE_PIXELS + SQUARE_PIXELS - pixelBuffer, i * SQUARE_PIXELS + SQUARE_PIXELS - pixelBuffer, j*SQUARE_PIXELS + pixelBuffer);
						}
						else if(piece == 2){
							g.setColor(Color.BLUE);
							g.drawOval(i * SQUARE_PIXELS + pixelBuffer, j*SQUARE_PIXELS + pixelBuffer, SQUARE_PIXELS - pixelBuffer*2, SQUARE_PIXELS - pixelBuffer*2);
						}

					}
					
				}
			}
			
		}
		
}

