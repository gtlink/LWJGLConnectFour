
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

public class ConnectFourMain {
		
	/** time at last frame */
	long lastFrame;
	 
	/** frames per second */
	int fps;
	/** last fps time */
	long lastFPS;
	
	
	public static void main(String [] args) {
		ConnectFourMain connectFourMain = new ConnectFourMain();
		connectFourMain.run();
	}
	
	public void run() {
		
		initGL(800,600);
		
		GameStateManager.getInstance().setNextGameState(new MainMenu());
		
		if(GameStateManager.getInstance().isNextGameStateSet()) {
			GameStateManager.getInstance().updateCurrentState();
		}
		
		
		getDelta(); // call once before loop to initialise lastFrame
		lastFPS = getTime(); // call before loop to initialise fps timer
		
		//Game Loop
		while(!Display.isCloseRequested()) {
			
			int delta = getDelta();
			System.out.println(delta);
			
			GameStateManager.getInstance().getCurrentGameState().events();
			
			GameStateManager.getInstance().getCurrentGameState().logic();
			
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT); 
			
			updateFPS(); // update FPS Counter
			
			GameStateManager.getInstance().getCurrentGameState().render();
			
			if(GameStateManager.getInstance().isNextGameStateSet()) {
				GameStateManager.getInstance().updateCurrentState();
			}
			
			Display.update();
		}
		
		Display.destroy();
		ErrorLog.getInstance().close();
	}
	
	public void initGL(int width,int height) {
		try {
			Display.setDisplayMode(new DisplayMode(width,height));
			Display.create();
			Display.setVSyncEnabled(true);
			Display.setTitle("Connect Four!!");
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		GL11.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		GL11.glViewport(0, 0, width, height);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, width, height, 0, 1, -1);
		
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}
	
	public long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
	
	public int getDelta() {
		long time = getTime();
		int delta = (int) (time - lastFrame);
		lastFrame = time;
		return delta;
	}
	
	public void updateFPS() {
		if (getTime() - lastFPS > 1000) {
		Display.setTitle("FPS: " + fps);
		fps = 0;
		lastFPS += 1000;
		}
		fps++;
		}
}

