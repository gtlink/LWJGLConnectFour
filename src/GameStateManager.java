
public class GameStateManager {
	
	private GameState currentGameState = null;
	
	private GameState nextGameState = null;
	
	private static final GameStateManager instance = new GameStateManager();

	private GameStateManager() {
		
	}

	public static GameStateManager getInstance() {
	   return instance;
	}

	public GameState getCurrentGameState() {
	   return currentGameState;
	}

	public void setNextGameState(GameState nextGameState) {
	   this.nextGameState = nextGameState;
	}
	
	public boolean isNextGameStateSet() {
		if(nextGameState == null) {
			return false;
		} else {
			return true;
		}
	}
	
	public void updateCurrentState() {
		if(nextGameState != null) {
			currentGameState = null;
			currentGameState = nextGameState;
			nextGameState = null;
		}
	}
	
}
