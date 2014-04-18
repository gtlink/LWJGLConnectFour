import java.awt.Color;
import java.awt.Font;
import java.io.IOException;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;


public class MainMenu implements GameState {

	private Menu menu;
	private MenuItem newGame;
	private MenuItem quit;
	private Texture logo;
	private Texture redPiece;
	private Texture yellowPiece;
	
	public MainMenu() {
		menu = new Menu();
		
		Font font = new Font("Times New Roman",Font.PLAIN,24);
		
		newGame = new MenuItem("New Game",new Color(0.0f,0.0f,0.0f,1.0f),new Color(1.0f,1.0f,1.0f,1.0f),
				new Color(1.0f,0.0f,0.0f,1.0f),new Color(1.0f,1.0f,1.0f,1.0f),300,200,200,50,font,24,false,false);
		
		quit = new MenuItem("Quit",new Color(0.0f,0.0f,0.0f,1.0f),new Color(1.0f,1.0f,1.0f,1.0f),
				new Color(1.0f,0.0f,0.0f,1.0f),new Color(1.0f,1.0f,1.0f,1.0f),300,250,200,50,font,24,false,false);
		
		
		menu.addItem(newGame);
		menu.addItem(quit);
		
		try {
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			logo = TextureLoader.getTexture("PNG",ResourceLoader.getResourceAsStream("Graphics/FourInaRowLogo.png"));
			redPiece = TextureLoader.getTexture("PNG",ResourceLoader.getResourceAsStream("Graphics/Computer.png"));
			yellowPiece = TextureLoader.getTexture("PNG",ResourceLoader.getResourceAsStream("Graphics/PlayerOne.png"));
			
		} catch(IOException e) {
			System.err.println(e.getMessage());
		}
	}
	public void events() {
		menu.events();
	}
	
	public void logic() {
		if(menu.getItem(0).isClicked()) {
			GameStateManager.getInstance().setNextGameState(new ConnectFour());
		}
		if(menu.getItem(1).isClicked()) {
			System.exit(0);
		}
	}
	
	public void render() {
		menu.render();
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		org.newdawn.slick.Color.white.bind();
		
		logo.bind();
		
		GL11.glBegin(GL11.GL_QUADS);
		
		 GL11.glTexCoord2f(0.0f,0.0f);
	        GL11.glVertex2d(275,125);
	        GL11.glTexCoord2f(0.0f,1.0f);
	        GL11.glVertex2d(275,125 + 64);
	        GL11.glTexCoord2f(1.0f,1.0f);
	        GL11.glVertex2d(275 + 256,125 + 64);
	        GL11.glTexCoord2f(1.0f,0.0f);
	        GL11.glVertex2d(275 + 256,125);
	        GL11.glEnd();	
		
	}
}
