import java.awt.Font;
import java.io.IOException;
import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class ConnectFour implements GameState {
	
	private static final int PLAYER = 0;
	private static final int COMPUTER = 1;
	private static final int EASY = 2;
	private static final int MEDIUM = 3;
	private static final int HARD = 4;
	
	private int [][] board;
	
	private static final int BOARDWIDTH = 6;
	private static final int BOARDHEIGHT = 7;
	
	Texture boardTexture;
	Texture playerOne;
	Texture computer;
	
	private int [][] bestMove = {
		{-1,-1}
	};

	private Random generator = new Random();
	
	private int turn;
	
	private int winner;
	
	private boolean isPaused;
	
	Menu pauseMenu;
	MenuItem chooseDifficultyPause;
	MenuItem quitPause;
	MenuItem newGamePause;
	
	Menu endGameMenu;
	MenuItem playAgain;
	MenuItem quit;
	
	Menu chooseDifficulty;
	MenuItem easy;
	MenuItem medium;
	MenuItem hard;
	
	int colFallingPiece;
	int rowFallingPiece;
	double fallingPieceX;
	double fallingPieceY;
	boolean fallingPiece;
	int fallingPieceTurn;
	boolean isDraw;
	
	
	private boolean difficultyChoosen;
	private int difficulty;
	
	public ConnectFour() {
		winner = 0;
		isDraw = false;
		difficulty = EASY;
		difficultyChoosen = false;
		
		
		board = new int [BOARDWIDTH][BOARDHEIGHT];
		for(int row = 0;row < BOARDWIDTH;row++) {
			for(int col = 0;col < BOARDHEIGHT;col++) {
				board[row][col] = 0;
			}
		}
		
		isPaused = false;
		Keyboard.enableRepeatEvents(false);

		try {
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			boardTexture = TextureLoader.getTexture("PNG",ResourceLoader.getResourceAsStream("Graphics/Board1.png"));
			playerOne = TextureLoader.getTexture("PNG",ResourceLoader.getResourceAsStream("Graphics/PlayerOne.png"));
			computer = TextureLoader.getTexture("PNG",ResourceLoader.getResourceAsStream("Graphics/Computer.png"));
			
		} catch(IOException e) {
			System.err.println(e.getMessage());
		}
		
		turn = generator.nextInt(2);
		
		endGameMenu = new Menu();
		
		Font font = new Font("Times New Roman",Font.PLAIN,24);
		
		playAgain = new MenuItem("Play Again",new java.awt.Color(0.0f,0.0f,0.0f,1.0f),new java.awt.Color(1.0f,1.0f,1.0f,1.0f),
				new java.awt.Color(0.0f,0.0f,1.0f,1.0f),new java.awt.Color(1.0f,1.0f,1.0f,1.0f),500,200,200,50,font,24,false,false);
		
		quit = new MenuItem("Quit",new java.awt.Color(0.0f,0.0f,0.0f,1.0f),new java.awt.Color(1.0f,1.0f,1.0f,1.0f),
				new java.awt.Color(0.0f,0.0f,1.0f,1.0f),new java.awt.Color(1.0f,1.0f,1.0f,1.0f),500,250,200,50,font,24,false,false);
		
		
		endGameMenu.addItem(playAgain);
		endGameMenu.addItem(quit);
		
		pauseMenu = new Menu();
		
		
		newGamePause = new MenuItem("New Game",new java.awt.Color(0.0f,0.0f,0.0f,1.0f),new java.awt.Color(1.0f,1.0f,1.0f,1.0f),
				new java.awt.Color(0.0f,0.0f,1.0f,1.0f),new java.awt.Color(1.0f,1.0f,1.0f,1.0f),300,200,200,50,font,24,false,false);
		
		chooseDifficultyPause = new MenuItem("Choose Difficulty",new java.awt.Color(0.0f,0.0f,0.0f,1.0f),new java.awt.Color(1.0f,1.0f,1.0f,1.0f),
				new java.awt.Color(0.0f,0.0f,1.0f,1.0f),new java.awt.Color(1.0f,1.0f,1.0f,1.0f),300,250,200,50,font,24,false,false);
		
		
		quitPause = new MenuItem("Quit",new java.awt.Color(0.0f,0.0f,0.0f,1.0f),new java.awt.Color(1.0f,1.0f,1.0f,1.0f),
				new java.awt.Color(0.0f,0.0f,1.0f,1.0f),new java.awt.Color(1.0f,1.0f,1.0f,1.0f),300,300,200,50,font,24,false,false);
		
		pauseMenu.addItem(newGamePause);
		pauseMenu.addItem(chooseDifficultyPause);
		pauseMenu.addItem(quitPause);
		
		chooseDifficulty = new Menu();
		
		easy = new MenuItem("Easy",new java.awt.Color(0.0f,0.0f,0.0f,1.0f),new java.awt.Color(1.0f,1.0f,1.0f,1.0f),
				new java.awt.Color(0.0f,0.0f,1.0f,1.0f),new java.awt.Color(1.0f,1.0f,1.0f,1.0f),300,200,200,50,font,24,false,false);
		
		medium = new MenuItem("Medium",new java.awt.Color(0.0f,0.0f,0.0f,1.0f),new java.awt.Color(1.0f,1.0f,1.0f,1.0f),
				new java.awt.Color(0.0f,0.0f,1.0f,1.0f),new java.awt.Color(1.0f,1.0f,1.0f,1.0f),300,250,200,50,font,24,false,false);
		
		hard = new MenuItem("Hard",new java.awt.Color(0.0f,0.0f,0.0f,1.0f),new java.awt.Color(1.0f,1.0f,1.0f,1.0f),
				new java.awt.Color(0.0f,0.0f,1.0f,1.0f),new java.awt.Color(1.0f,1.0f,1.0f,1.0f),300,300,200,50,font,24,false,false);
		
		chooseDifficulty.addItem(easy);
		chooseDifficulty.addItem(medium);
		chooseDifficulty.addItem(hard);
	}
	
	public ConnectFour(int diff) {
		winner = 0;
		isDraw = false;
		difficulty = diff;
		difficultyChoosen = true;
		
		
		board = new int [BOARDWIDTH][BOARDHEIGHT];
		for(int row = 0;row < BOARDWIDTH;row++) {
			for(int col = 0;col < BOARDHEIGHT;col++) {
				board[row][col] = 0;
			}
		}
		
		isPaused = false;
		Keyboard.enableRepeatEvents(false);

		try {
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			boardTexture = TextureLoader.getTexture("PNG",ResourceLoader.getResourceAsStream("Graphics\\Board1.png"));
			playerOne = TextureLoader.getTexture("PNG",ResourceLoader.getResourceAsStream("Graphics\\PlayerOne.png"));
			computer = TextureLoader.getTexture("PNG",ResourceLoader.getResourceAsStream("Graphics\\Computer.png"));
			
		} catch(IOException e) {
			System.err.println(e.getMessage());
		}
		
		turn = generator.nextInt(2);
		
		endGameMenu = new Menu();
		
		Font font = new Font("Times New Roman",Font.PLAIN,24);
		
		playAgain = new MenuItem("Play Again",new java.awt.Color(0.0f,0.0f,0.0f,1.0f),new java.awt.Color(1.0f,1.0f,1.0f,1.0f),
				new java.awt.Color(0.0f,0.0f,1.0f,1.0f),new java.awt.Color(1.0f,1.0f,1.0f,1.0f),500,200,200,50,font,24,false,false);
		
		quit = new MenuItem("Quit",new java.awt.Color(0.0f,0.0f,0.0f,1.0f),new java.awt.Color(1.0f,1.0f,1.0f,1.0f),
				new java.awt.Color(0.0f,0.0f,1.0f,1.0f),new java.awt.Color(1.0f,1.0f,1.0f,1.0f),500,250,200,50,font,24,false,false);
		
		
		endGameMenu.addItem(playAgain);
		endGameMenu.addItem(quit);
		
		pauseMenu = new Menu();
		
		
		newGamePause = new MenuItem("New Game",new java.awt.Color(0.0f,0.0f,0.0f,1.0f),new java.awt.Color(1.0f,1.0f,1.0f,1.0f),
				new java.awt.Color(0.0f,0.0f,1.0f,1.0f),new java.awt.Color(1.0f,1.0f,1.0f,1.0f),300,200,200,50,font,24,false,false);
		
		chooseDifficultyPause = new MenuItem("Choose Difficulty",new java.awt.Color(0.0f,0.0f,0.0f,1.0f),new java.awt.Color(1.0f,1.0f,1.0f,1.0f),
				new java.awt.Color(0.0f,0.0f,1.0f,1.0f),new java.awt.Color(1.0f,1.0f,1.0f,1.0f),300,250,200,50,font,24,false,false);
		
		
		quitPause = new MenuItem("Quit",new java.awt.Color(0.0f,0.0f,0.0f,1.0f),new java.awt.Color(1.0f,1.0f,1.0f,1.0f),
				new java.awt.Color(0.0f,0.0f,1.0f,1.0f),new java.awt.Color(1.0f,1.0f,1.0f,1.0f),300,300,200,50,font,24,false,false);
		
		pauseMenu.addItem(newGamePause);
		pauseMenu.addItem(chooseDifficultyPause);
		pauseMenu.addItem(quitPause);
		
		chooseDifficulty = new Menu();
		
		easy = new MenuItem("Easy",new java.awt.Color(0.0f,0.0f,0.0f,1.0f),new java.awt.Color(1.0f,1.0f,1.0f,1.0f),
				new java.awt.Color(0.0f,0.0f,1.0f,1.0f),new java.awt.Color(1.0f,1.0f,1.0f,1.0f),300,200,200,50,font,24,false,false);
		
		medium = new MenuItem("Medium",new java.awt.Color(0.0f,0.0f,0.0f,1.0f),new java.awt.Color(1.0f,1.0f,1.0f,1.0f),
				new java.awt.Color(0.0f,0.0f,1.0f,1.0f),new java.awt.Color(1.0f,1.0f,1.0f,1.0f),300,250,200,50,font,24,false,false);
		
		hard = new MenuItem("Hard",new java.awt.Color(0.0f,0.0f,0.0f,1.0f),new java.awt.Color(1.0f,1.0f,1.0f,1.0f),
				new java.awt.Color(0.0f,0.0f,1.0f,1.0f),new java.awt.Color(1.0f,1.0f,1.0f,1.0f),300,300,200,50,font,24,false,false);
		
		chooseDifficulty.addItem(easy);
		chooseDifficulty.addItem(medium);
		chooseDifficulty.addItem(hard);
	}
	
	public boolean makeMove(int c) {
		if(c < 0 || c > 6) {
			System.out.println("This is an invalid move.");
			return false;
		} else {
			for(int i = 5;i >= 0;i--) {
				if(board[i][c] == 0) {
					board[i][c] = turn + 1;
					return true;
				}
			}
			return false;
		}
	}
	
	public boolean validMove(int c) {
		if(c < 0 || c > 6) {
			System.out.println("This is an invalid move");
			return false;
		} else {
			for(int i = 5;i >= 0;i--) {
				if(board[i][c] == 0) {
					return true;
				}
			}
			return false;
		}
	}
	
	public int findRowInMove(int c) {
		if(c < 0 || c > 6) {
			System.out.println("This is an invalid move.");
			return -1;
		} else {
			for(int i = 5;i >= 0;i--) {
				if(board[i][c] == 0) {
					return i;
				}
			}
			return -1;
		}
	}
	
	public void events() {
		if(difficultyChoosen == true) {
			winner = testForWinner();
			
				isDraw = isDraw();
			
			if(isPaused == false) {
				if(fallingPiece == false) {
					if(winner == 0 && isDraw == false) {
						if(turn == PLAYER) {
							int xm = Mouse.getX();
							int ym = Mouse.getY();
							int colm = -1;
							colm = (xm / 65) + 1;
							if(colm >= 1 && colm <= 8 && ym >= 208 && ym <= 600) {
								if(Mouse.next()) {
									if(Mouse.isButtonDown(0) && Mouse.getEventButtonState() == true) {
											if(validMove(colm - 1)) {
												colFallingPiece = colm - 1;
												rowFallingPiece = findRowInMove(colm - 1);
												fallingPieceX = (colm - 1) * 65;
												fallingPieceY = -65;
												fallingPiece = true;
												fallingPieceTurn = PLAYER;
											}
										}
									}
								}
						} else if(turn == COMPUTER) {
							int i = minimax(0,-1000000,1000000);
							int col = bestMove[0][1];
							colFallingPiece = col;
							rowFallingPiece = findRowInMove(col);
							fallingPieceX = col * 65;
							fallingPieceY = -65;
							fallingPiece = true;
							fallingPieceTurn = COMPUTER;
						}
					}
				}
			}
		}
		
		if(difficultyChoosen == true && winner == 0) {
			while(Keyboard.next()) {
				if(Keyboard.getEventKey() == Keyboard.KEY_P) {
					if(Keyboard.getEventKeyState()) {
						if(isPaused == true) {
							isPaused = false;
						} else {
							isPaused = true;
						}
					}
				}
			}
		}
		
		winner = testForWinner();
		
	    isDraw = isDraw();
		
		
		if(winner != 0 || isDraw == true) {
			endGameMenu.events();
		}
		
		if(isPaused == true && difficultyChoosen == true) {
			pauseMenu.events();
		}
		
		if(difficultyChoosen == false && isPaused == false) {
			chooseDifficulty.events();
		}
	}
	
	public void logic() {
		if(winner != 0 || isDraw == true) {
			if(endGameMenu.getItem(0).isClicked()) {
				GameStateManager.getInstance().setNextGameState(new ConnectFour(difficulty));
			}
			if(endGameMenu.getItem(1).isClicked()) {
				System.exit(0);
			}
		}
		
		if(difficultyChoosen == false) {
			if(chooseDifficulty.getItem(0).isClicked()) {
				difficulty = EASY;
				difficultyChoosen = true;
			}
			if(chooseDifficulty.getItem(1).isClicked()) {
				difficulty = MEDIUM;
				difficultyChoosen = true;
			}
			if(chooseDifficulty.getItem(2).isClicked()) {
				difficulty = HARD;
				difficultyChoosen = true;
			}
		}
		
		if(isPaused == true && difficultyChoosen == true) {
			if(pauseMenu.getItem(0).isClicked()) {
				GameStateManager.getInstance().setNextGameState(new ConnectFour(difficulty));
			}
			if(pauseMenu.getItem(1).isClicked()) {
				difficultyChoosen = false;
				isPaused = false;
			}
			if(pauseMenu.getItem(2).isClicked()) {
				System.exit(0);
			}
		}
	}
	
	public boolean isDraw() {
		for(int row = 0;row < 5;row++) {
			for(int col = 0;col < 7;col++) {
				if(board[row][col] == 0) {
					return false;
				}
			}
		}
		return true;
	}
	
	public void render() {
		
		//rendering falling piece
		Color.white.bind();
		
		if(fallingPiece == true) {
			if(fallingPieceTurn == PLAYER) {
				playerOne.bind();
				double increment = 0.5;
				
				if(fallingPieceY == (0 + (rowFallingPiece * 65))) {
					fallingPiece = false;
					turn = COMPUTER;
					board[rowFallingPiece][colFallingPiece] = 1;
				}
				
				if(fallingPieceY > ((0 + (rowFallingPiece * 65)) + increment)) {
					fallingPiece = false;
					fallingPieceY = (0 + (rowFallingPiece * 65));
					turn = COMPUTER;
					board[rowFallingPiece][colFallingPiece] = 1;
				}
				
				else {
					fallingPieceY = fallingPieceY + increment;
				}
				
				GL11.glBegin(GL11.GL_QUADS);
				
		        GL11.glTexCoord2f(0.0f,0.0f);
		        GL11.glVertex2d(fallingPieceX,fallingPieceY);
		        GL11.glTexCoord2f(0.0f,1.0f);
		        GL11.glVertex2d(fallingPieceX,fallingPieceY + 65);
		        GL11.glTexCoord2f(1.0f,1.0f);
		        GL11.glVertex2d(fallingPieceX + 65,fallingPieceY + 65);
		        GL11.glTexCoord2f(1.0f,0.0f);
		        GL11.glVertex2d(fallingPieceX + 65,fallingPieceY);
		        GL11.glEnd();	
			}
			
			if(fallingPieceTurn == COMPUTER) {
				computer.bind();
				double increment = 0.5;
				
				if(fallingPieceY == (0 + (rowFallingPiece * 65))) {
					fallingPiece = false;
					turn = PLAYER;
					board[rowFallingPiece][colFallingPiece] = 2;
				}
				
				if(fallingPieceY > ((0 + (rowFallingPiece * 65)) + increment)) {
					fallingPiece = false;
					fallingPieceY = (0 + (rowFallingPiece * 65));
					turn = PLAYER;
					board[rowFallingPiece][colFallingPiece] = 2;
				}
				
				else {
					fallingPieceY = fallingPieceY + increment;
				}
				
				GL11.glBegin(GL11.GL_QUADS);
				
		        GL11.glTexCoord2f(0.0f,0.0f);
		        GL11.glVertex2d(fallingPieceX,fallingPieceY);
		        GL11.glTexCoord2f(0.0f,1.0f);
		        GL11.glVertex2d(fallingPieceX,fallingPieceY + 65);
		        GL11.glTexCoord2f(1.0f,1.0f);
		        GL11.glVertex2d(fallingPieceX + 65,fallingPieceY + 65);
		        GL11.glTexCoord2f(1.0f,0.0f);
		        GL11.glVertex2d(fallingPieceX + 65,fallingPieceY);
		        GL11.glEnd();	
			}
		}
				
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		boardTexture.bind();
		
		GL11.glBegin(GL11.GL_QUADS);
		
	        GL11.glTexCoord2f(0.0f,0.0f);
	        GL11.glVertex2f(0,0);
	        GL11.glTexCoord2f(0.0f,1.0f);
	        GL11.glVertex2f(0,512);
	        GL11.glTexCoord2f(1.0f,1.0f);
	        GL11.glVertex2f(512,512);
	        GL11.glTexCoord2f(1.0f,0.0f);
	        GL11.glVertex2f(512,0);
		GL11.glEnd();	
		
		//render the board
		for(int row = 0;row < BOARDWIDTH;row++) {
			for(int col = 0;col < BOARDHEIGHT;col++) {
				if(board[row][col] == 1) {
					playerOne.bind();
					int y = row * 65;
					int x = col * 65;
					
					GL11.glBegin(GL11.GL_QUADS);
					
			        GL11.glTexCoord2f(0.0f,0.0f);
			        GL11.glVertex2f(x,y);
			        GL11.glTexCoord2f(0.0f,1.0f);
			        GL11.glVertex2f(x,y + 65);
			        GL11.glTexCoord2f(1.0f,1.0f);
			        GL11.glVertex2f(x + 65,y + 65);
			        GL11.glTexCoord2f(1.0f,0.0f);
			        GL11.glVertex2f(x + 65,y);
			        GL11.glEnd();	
					
				} else if(board[row][col] == 2) {
					computer.bind();
					int y = row * 65;
					int x = col * 65;
					
					GL11.glBegin(GL11.GL_QUADS);
					
			        GL11.glTexCoord2f(0.0f,0.0f);
			        GL11.glVertex2f(x,y);
			        GL11.glTexCoord2f(0.0f,1.0f);
			        GL11.glVertex2f(x,y + 65);
			        GL11.glTexCoord2f(1.0f,1.0f);
			        GL11.glVertex2f(x + 65,y + 65);
			        GL11.glTexCoord2f(1.0f,0.0f);
			        GL11.glVertex2f(x + 65,y);
			        GL11.glEnd();	
				}
			}
		}
		
		
		
		if(winner != 0 || isDraw == true) {
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			endGameMenu.render();
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			if(winner != 0) {
				
				//get winning moves
				int [][] winningMoves = getWinningMoves();
				//
				
				//highlight winningMoves
				GL11.glColor4f(0.0f, 1.0f, 0.0f,1.0f);
				GL11.glLineWidth(5.0f);
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				GL11.glDisable(GL11.GL_BLEND);
				GL11.glBegin(GL11.GL_LINES);
					GL11.glVertex2f(((winningMoves[0][1] + 1) * 65) - (65/2),((winningMoves[0][0] + 1) * 65) - (65/2));
					GL11.glVertex2f(((winningMoves[3][1] + 1) * 65) - (65/2),((winningMoves[3][0]) * 65) + (65/2));
				GL11.glEnd();
				GL11.glLineWidth(1.0f);
				GL11.glEnable(GL11.GL_TEXTURE_2D);
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			}
		}
	
		if(isPaused == true && difficultyChoosen == true) {
		
			GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.5f);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex2f(0.0f,600.0f);
			GL11.glVertex2f(800.0f,600.0f);
			GL11.glVertex2f(800.0f,0.0f);
			GL11.glVertex2f(0.0f,0.0f);
			GL11.glEnd();
			
			GL11.glColor4f(1.0f,1.0f,1.0f,1.0f);
			
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			pauseMenu.render();
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
		}
		
		if(difficultyChoosen == false) {
			GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.5f);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex2f(0.0f,600.0f);
			GL11.glVertex2f(800.0f,600.0f);
			GL11.glVertex2f(800.0f,0.0f);
			GL11.glVertex2f(0.0f,0.0f);
			GL11.glEnd();
			
			GL11.glColor4f(1.0f,1.0f,1.0f,1.0f);
			
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			chooseDifficulty.render();
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
		}
	}
	
	/**
	 * @return: 0 if no winner, 1 if player wins, 2 if computer wins
	 */
	public int testForWinner() {
		
		//Check for vertical win
		for(int c = 0;c < 7;c++) {
			for(int r = 5;r >= 3;r--) {
				if(board[r][c] == 1 && board[r - 1][c] == 1 && board[r - 2][c] == 1 && board[r - 3][c] == 1) {
					return -500000;
				} else if(board[r][c] == 2 && board[r - 1][c] == 2 && board[r - 2][c] == 2 && board[r - 3][c] == 2) {
					return 500000;
				}
			}
		}
		
		//check for horizontal win
		for(int r = 0;r < 6;r++) {
			for(int c = 0;c <= 3;c++) {
				if(board[r][c] == 1 && board[r][c + 1] == 1 && board[r][c + 2] == 1 && board[r][c + 3] == 1) {
					return -500000;
				} else if(board[r][c] == 2 && board[r][c + 1] == 2 && board[r][c + 2] == 2 && board[r][c + 3] == 2) {
					return 500000;
				}
			}
		}
		
		//check for diagonal win
		for(int r = 0;r <= 2;r++) {
		        for(int c = 0;c < 4;c++) {
		            if(board[r][c] == 1 && board[r + 1][c + 1] == 1 && board[r + 2][c + 2] == 1 && board[r + 3][c + 3] == 1) {
		                return -500000;
		            } else if(board[r][c] == 2 && board[r + 1][c + 1] == 2 && board[r + 2][c + 2] == 2 && board[r + 3][c + 3] == 2) {
		            	return 500000;
		            }
		        }
		}
		
		for(int r = 0;r <= 2;r++) {
			for(int c = 6;c >= 3;c--) {
				if(board[r][c] == 1 && board[r + 1][c - 1] == 1 && board[r + 2][c - 2] == 1 && board[r + 3][c - 3] == 1) {
	                return -500000;
	            } else if(board[r][c] == 2 && board[r + 1][c - 1] == 2 && board[r + 2][c - 2] == 2 && board[r + 3][c - 3] == 2) {
	            	return 500000;
	            }
			}
		}
		
		return 0;
	}
	
	public int [][] getWinningMoves() {
		int [][] winningMoves = new int[4][2];
		//Check for vertical win
		for(int c = 0;c < 7;c++) {
			for(int r = 5;r >= 3;r--) {
				if(board[r][c] == 1 && board[r - 1][c] == 1 && board[r - 2][c] == 1 && board[r - 3][c] == 1) {
					winningMoves[0][0] = r;
					winningMoves[0][1] = c;
					winningMoves[1][0] = r - 1;
					winningMoves[1][1] = c;
					winningMoves[2][0] = r - 2;
					winningMoves[2][1] = c;
					winningMoves[3][0] = r - 3;
					winningMoves[3][1] = c;
					return winningMoves;
				} else if(board[r][c] == 2 && board[r - 1][c] == 2 && board[r - 2][c] == 2 && board[r - 3][c] == 2) {
					winningMoves[0][0] = r;
					winningMoves[0][1] = c;
					winningMoves[1][0] = r - 1;
					winningMoves[1][1] = c;
					winningMoves[2][0] = r - 2;
					winningMoves[2][1] = c;
					winningMoves[3][0] = r - 3;
					winningMoves[3][1] = c;
					return winningMoves;
				}
			}
		}
		
		//check for horizontal win
		for(int r = 0;r < 6;r++) {
			for(int c = 0;c <= 3;c++) {
				if(board[r][c] == 1 && board[r][c + 1] == 1 && board[r][c + 2] == 1 && board[r][c + 3] == 1) {
					winningMoves[0][0] = r;
					winningMoves[0][1] = c;
					winningMoves[1][0] = r;
					winningMoves[1][1] = c + 1;
					winningMoves[2][0] = r;
					winningMoves[2][1] = c + 2;
					winningMoves[3][0] = r;
					winningMoves[3][1] = c + 3;
					return winningMoves;
				} else if(board[r][c] == 2 && board[r][c + 1] == 2 && board[r][c + 2] == 2 && board[r][c + 3] == 2) {
					winningMoves[0][0] = r;
					winningMoves[0][1] = c;
					winningMoves[1][0] = r;
					winningMoves[1][1] = c + 1;
					winningMoves[2][0] = r;
					winningMoves[2][1] = c + 2;
					winningMoves[3][0] = r;
					winningMoves[3][1] = c + 3;
					return winningMoves;
				}
			}
		}
		
		//check for diagonal win
		for(int r = 0;r <= 2;r++) {
		        for(int c = 0;c < 4;c++) {
		            if(board[r][c] == 1 && board[r + 1][c + 1] == 1 && board[r + 2][c + 2] == 1 && board[r + 3][c + 3] == 1) {
		            	winningMoves[0][0] = r;
						winningMoves[0][1] = c;
						winningMoves[1][0] = r + 1;
						winningMoves[1][1] = c + 1;
						winningMoves[2][0] = r + 2;
						winningMoves[2][1] = c + 2;
						winningMoves[3][0] = r + 3;
						winningMoves[3][1] = c + 3;
						return winningMoves;
		            } else if(board[r][c] == 2 && board[r + 1][c + 1] == 2 && board[r + 2][c + 2] == 2 && board[r + 3][c + 3] == 2) {
		            	winningMoves[0][0] = r;
						winningMoves[0][1] = c;
						winningMoves[1][0] = r + 1;
						winningMoves[1][1] = c + 1;
						winningMoves[2][0] = r + 2;
						winningMoves[2][1] = c + 2;
						winningMoves[3][0] = r + 3;
						winningMoves[3][1] = c + 3;
						return winningMoves;
		            }
		        }
		}
		
		for(int r = 0;r <= 2;r++) {
			for(int c = 6;c >= 3;c--) {
				if(board[r][c] == 1 && board[r + 1][c - 1] == 1 && board[r + 2][c - 2] == 1 && board[r + 3][c - 3] == 1) {
					winningMoves[0][0] = r;
					winningMoves[0][1] = c;
					winningMoves[1][0] = r + 1;
					winningMoves[1][1] = c - 1;
					winningMoves[2][0] = r + 2;
					winningMoves[2][1] = c - 2;
					winningMoves[3][0] = r + 3;
					winningMoves[3][1] = c - 3;
					return winningMoves;
	            } else if(board[r][c] == 2 && board[r + 1][c - 1] == 2 && board[r + 2][c - 2] == 2 && board[r + 3][c - 3] == 2) {
	            	winningMoves[0][0] = r;
					winningMoves[0][1] = c;
					winningMoves[1][0] = r + 1;
					winningMoves[1][1] = c - 1;
					winningMoves[2][0] = r + 2;
					winningMoves[2][1] = c - 2;
					winningMoves[3][0] = r + 3;
					winningMoves[3][1] = c - 3;
					return winningMoves;
	            }
			}
		}
		return winningMoves;
	}   
	
	public int minimax(int depth,int alpha,int beta) {
		 return (maxMove(depth,alpha,beta));
	}
	
	public int maxMove(int depth,int alpha,int beta) {
		int max = -500025;
		int m = testForWinner();
		if(m != 0) {
			return m;
		}
		if(depth >= 6 || isDraw()) {
			return analysis();
		}
		
		int [][] lMoves = findAllLegalMoves();
		
		for(int move = 0;move < 7;move++) {
			if(lMoves[move][0] == -1 || lMoves[move][1] == -1) {
				continue;
			} else {
				board[lMoves[move][0]][lMoves[move][1]] = 2;
				int temp = minMove(depth + 1,alpha,beta);
				board[lMoves[move][0]][lMoves[move][1]] = 0;
				if(temp > max) {
					max = temp;
						if(depth == 0) {
							bestMove[0][0] = lMoves[move][0];
							bestMove[0][1] = lMoves[move][1];
						}
				}
				if(temp > alpha) {
				   alpha = temp; 
				}
				if(alpha >= beta) {
				   return alpha;
				}
			}
		}
		return max;
	}
	
	public int minMove(int depth,int alpha,int beta) {
		int min = 500025;
		int m = testForWinner();
		if(m != 0) {
			return m;
		}
		if(depth >= 6 || isDraw()) {
			return analysis();
		}
		
		int [][] lMoves = findAllLegalMoves();
		
		for(int move = 0;move < 7;move++) {
			if(lMoves[move][0] == -1 || lMoves[move][1] == -1) {
				continue;
			} else {
				board[lMoves[move][0]][lMoves[move][1]] = 1;
				int temp = maxMove(depth + 1,alpha,beta);
				board[lMoves[move][0]][lMoves[move][1]] = 0;
				if(temp < min) {
					min = temp;
					if(depth == 0) {
						bestMove[0][0] = lMoves[move][0];
						bestMove[0][1] = lMoves[move][1];
					}
				}
				if(temp < beta) {
				   beta = temp;
				}
				if(alpha >= beta) {
				   return beta;
			    }
			}
		}
		return min;
	}
	
	public int analysis() {
		int whoWon = 0;
		
		if(difficulty == MEDIUM || difficulty == HARD) {
			// Horizontal one moves Player 1
			for(int col = 0;col <= 3;col++) {
				for(int row = 0;row < 6;row++) {
					if(board[row][col] == 1 && board[row][col + 1] == 0 && board[row][col + 2] == 0 && board[row][col + 3] == 0) {
						whoWon = whoWon - 125;
					}
				}
			}
			
			for(int col = 0;col <= 3;col++) {
				for(int row = 0;row < 6;row++) {
					if(board[row][col] == 0 && board[row][col + 1] == 1 && board[row][col + 2] == 0 && board[row][col + 3] == 0) {
						whoWon = whoWon - 125;
					}
				}
			}
			
			for(int col = 0;col <= 3;col++) {
				for(int row = 0;row < 6;row++) {
					if(board[row][col] == 0 && board[row][col + 1] == 0 && board[row][col + 2] == 1 && board[row][col + 3] == 0) {
						whoWon = whoWon - 125;
					}
				}
			}
			
			for(int col = 0;col <= 3;col++) {
				for(int row = 0;row < 6;row++) {
					if(board[row][col] == 0 && board[row][col + 1] == 0 && board[row][col + 2] == 0 && board[row][col + 3] == 1) {
						whoWon = whoWon - 125;
					}
				}
			}
			//Horizontal One Moves End Player One
			
			// Horizontal one moves Player Two
			for(int col = 0;col <= 3;col++) {
				for(int row = 0;row < 6;row++) {
					if(board[row][col] == 2 && board[row][col + 1] == 0 && board[row][col + 2] == 0 && board[row][col + 3] == 0) {
						whoWon = whoWon + 125;
					}
				}
			}
			
			for(int col = 0;col <= 3;col++) {
				for(int row = 0;row < 6;row++) {
					if(board[row][col] == 0 && board[row][col + 1] == 2 && board[row][col + 2] == 0 && board[row][col + 3] == 0) {
						whoWon = whoWon + 125;
					}
				}
			}
			
			for(int col = 0;col <= 3;col++) {
				for(int row = 0;row < 6;row++) {
					if(board[row][col] == 0 && board[row][col + 1] == 0 && board[row][col + 2] == 2 && board[row][col + 3] == 0) {
						whoWon = whoWon + 125;
					}
				}
			}
			
			for(int col = 0;col <= 3;col++) {
				for(int row = 0;row < 6;row++) {
					if(board[row][col] == 0 && board[row][col + 1] == 0 && board[row][col + 2] == 0 && board[row][col + 3] == 2) {
						whoWon = whoWon + 125;
					}
				}
			}
			//Horizontal One Moves End Player Two
			
			//Horizontal Two Moves Player One
			for(int col = 0;col <= 3;col++) {
				for(int row = 0;row < 6;row++) {
					if(board[row][col] == 1 && board[row][col + 1] == 1 && board[row][col + 2] == 0 && board[row][col + 3] == 0) {
						whoWon = whoWon - 250;
					}
				}
			}
			
			for(int col = 0;col <= 3;col++) {
				for(int row = 0;row < 6;row++) {
					if(board[row][col] == 0 && board[row][col + 1] == 1 && board[row][col + 2] == 1 && board[row][col + 3] == 0) {
						whoWon = whoWon - 250;
					}
				}
			}
			
			for(int col = 0;col <= 3;col++) {
				for(int row = 0;row < 6;row++) {
					if(board[row][col] == 0 && board[row][col + 1] == 0 && board[row][col + 2] == 1 && board[row][col + 3] == 1) {
						whoWon = whoWon - 250;
					}
				}
			}
			
			for(int col = 0;col <= 3;col++) {
				for(int row = 0;row < 6;row++) {
					if(board[row][col] == 1 && board[row][col + 1] == 0 && board[row][col + 2] == 1 && board[row][col + 3] == 0) {
						whoWon = whoWon - 250;
					}
				}
			}
			
			for(int col = 0;col <= 3;col++) {
				for(int row = 0;row < 6;row++) {
					if(board[row][col] == 0 && board[row][col + 1] == 1 && board[row][col + 2] == 0 && board[row][col + 3] == 1) {
						whoWon = whoWon - 250;
					}
				}
			}
			
			for(int col = 0;col <= 3;col++) {
				for(int row = 0;row < 6;row++) {
					if(board[row][col] == 1 && board[row][col + 1] == 0 && board[row][col + 2] == 0 && board[row][col + 3] == 1) {
						whoWon = whoWon - 250;
					}
				}
			}
			
			//Horizontal Two Moves Player One End
			
			//Horizontal Two Moves Player Two 
			for(int col = 0;col <= 3;col++) {
				for(int row = 0;row < 6;row++) {
					if(board[row][col] == 1 && board[row][col + 1] == 1 && board[row][col + 2] == 0 && board[row][col + 3] == 0) {
						whoWon = whoWon - 250;
					}
				}
			}
			
			for(int col = 0;col <= 3;col++) {
				for(int row = 0;row < 6;row++) {
					if(board[row][col] == 0 && board[row][col + 1] == 2 && board[row][col + 2] == 2 && board[row][col + 3] == 0) {
						whoWon = whoWon + 250;
					}
				}
			}
			
			for(int col = 0;col <= 3;col++) {
				for(int row = 0;row < 6;row++) {
					if(board[row][col] == 0 && board[row][col + 1] == 0 && board[row][col + 2] == 2 && board[row][col + 3] == 2) {
						whoWon = whoWon + 250;
					}
				}
			}
			
			for(int col = 0;col <= 3;col++) {
				for(int row = 0;row < 6;row++) {
					if(board[row][col] == 2 && board[row][col + 1] == 0 && board[row][col + 2] == 2 && board[row][col + 3] == 0) {
						whoWon = whoWon + 250;
					}
				}
			}
			
			for(int col = 0;col <= 3;col++) {
				for(int row = 0;row < 6;row++) {
					if(board[row][col] == 0 && board[row][col + 1] == 2 && board[row][col + 2] == 0 && board[row][col + 3] == 2) {
						whoWon = whoWon + 250;
					}
				}
			}
			
			for(int col = 0;col <= 3;col++) {
				for(int row = 0;row < 6;row++) {
					if(board[row][col] == 2 && board[row][col + 1] == 0 && board[row][col + 2] == 0 && board[row][col + 3] == 2) {
						whoWon = whoWon + 250;
					}
				}
			}
			//Horizontal Two Moves Player Two End
			if(difficulty == HARD) {
				//Horizontal Three Moves Player One
				
				for(int col = 0;col <= 3;col++) {
					for(int row = 0;row < 6;row++) {
						if(board[row][col] == 1 && board[row][col + 1] == 1 && board[row][col + 2] == 1 && board[row][col + 3] == 0) {
							whoWon = whoWon - 1000;
						}
					}
				}
				
				for(int col = 0;col <= 3;col++) {
					for(int row = 0;row < 6;row++) {
						if(board[row][col] == 0 && board[row][col + 1] == 1 && board[row][col + 2] == 1 && board[row][col + 3] == 1) {
							whoWon = whoWon - 1000;
						}
					}
				}
				
				for(int col = 0;col <= 3;col++) {
					for(int row = 0;row < 6;row++) {
						if(board[row][col] == 1 && board[row][col + 1] == 0 && board[row][col + 2] == 1 && board[row][col + 3] == 1) {
							whoWon = whoWon - 1000;
						}
					}
				}
				
				for(int col = 0;col <= 3;col++) {
					for(int row = 0;row < 6;row++) {
						if(board[row][col] == 1 && board[row][col + 1] == 1 && board[row][col + 2] == 0 && board[row][col + 3] == 1) {
							whoWon = whoWon - 1000;
						}
					}
				}
				//Horizontal Three Moves Player One End
				
				//Horizontal Three Moves Player Two 
				
				for(int col = 0;col <= 3;col++) {
					for(int row = 0;row < 6;row++) {
						if(board[row][col] == 2 && board[row][col + 1] == 2 && board[row][col + 2] == 2 && board[row][col + 3] == 0) {
							whoWon = whoWon + 1000;
						}
					}
				}
				
				for(int col = 0;col <= 3;col++) {
					for(int row = 0;row < 6;row++) {
						if(board[row][col] == 0 && board[row][col + 1] == 2 && board[row][col + 2] == 2 && board[row][col + 3] == 2) {
							whoWon = whoWon + 1000;
						}
					}
				}
				
				for(int col = 0;col <= 3;col++) {
					for(int row = 0;row < 6;row++) {
						if(board[row][col] == 2 && board[row][col + 1] == 0 && board[row][col + 2] == 2 && board[row][col + 3] == 2) {
							whoWon = whoWon + 1000;
						}
					}
				}
				
				for(int col = 0;col <= 3;col++) {
					for(int row = 0;row < 6;row++) {
						if(board[row][col] == 2 && board[row][col + 1] == 2 && board[row][col + 2] == 0 && board[row][col + 3] == 2) {
							whoWon = whoWon + 1000;
						}
					}
				}
			}
			//Horizontal Three Moves Player Two End
	   }
		
	   return whoWon;
	}
	
	public int [][] findAllLegalMoves() {
		int [][] legalMove = {
			{-1,-1},
			{-1,-1},
			{-1,-1},
			{-1,-1},
			{-1,-1},
			{-1,-1},
			{-1,-1}
		 };
		
		for(int c = 0;c < 7;c++) {
			for(int r = 5;r >= 0;r--) {
				if(board[r][c] == 0) {
					legalMove[c][0] = r;
					legalMove[c][1] = c;
					break;
				}
			}
		}
		return legalMove;
	}
	
	public void printBoard() {
		for(int i = 0;i < 6;i++) {
			if(i != 0) {
				System.out.println();
			}
			for(int j = 0;j < 7;j++) {
				System.out.print(board[i][j] + " ");
			}
		}
		System.out.println();
	}
}