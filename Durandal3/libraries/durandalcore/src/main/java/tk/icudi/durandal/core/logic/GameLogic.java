package tk.icudi.durandal.core.logic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.util.Log;
import tk.icudi.durandal.core.logic.BoardModel.GameResult;
import tk.icudi.durandal.core.logic.ShortMessage.Identifier;
import tk.icudi.durandal.core.logic.player.Player;


public class GameLogic implements Serializable {
	
	public static Random rand = new Random(1234567);
	
	private static final int ticksAfterGame = 30;

	private static final long serialVersionUID = 1L;

	private int tick = -1; // -1 = noch nicht startbereit
	
	private List<BoardController> boards = new ArrayList<BoardController>();
	
	private int waitTimeout;

	private boolean isPaused;

	private StartOptions options;
		
	public GameLogic(StartOptions options) {

		System.out.println(" --- GameLogic: " + this);

		this.options = options;
		
		for(Player player : options.getPlayers()){
			final BoardController board = new BoardController(player);
			boards.add(board);
		}
		
		reset();
	}
	
	public void appendPlayer(Player player, String username) {
		
		final BoardController board = new BoardController(player);
		boards.add(board);
		
		reset();
	}
	
	public void reset() {
		
		System.out.println(" --- reset --- ");
		
		this.tick = 0;
		this.waitTimeout = -1;
		this.isPaused = false;
				
		for(BoardController board : boards){
			board.reset();
		}
				
		if(options.isReleaseCreepAtStart()){
			Monster creep = new Monster();
			creep.setLifepoints(1);
			
			for(BoardController board : boards){
				board.creepSpawned(creep.replikate());
			}
		}

	}
	
    public boolean tick() {
    	
    	if(isPaused == true){
    		
            boolean changed = false;
            
    		for(BoardController board : boards){
    			changed |= board.hasChanged();
    		}
    		
    		return changed;
    	}
    	
    	if(waitTimeout >= 0){
    		
    		if(waitTimeout > 0){
    			waitTimeout--;
    			if(waitTimeout == 0){
    				Log.d("---", "Waiting finished");
    			}
    		} else {
    			doBoardCommands();
    		}
    		
    		if(waitTimeout == ticksAfterGame-1){
    			return true;
    		} else {
    			return false;
    		}
    		
    	}
    	
    	tick++;
    	
    	if(tick % 10 == 0){
    		for(BoardController board : boards){
    			if(board.getModel().isFinished() == false){
    				board.giveMoney();
    			}
    		}
    	}
    	
		doBoardCommands();
		
        boolean changed = false;
        
		for(BoardController board : boards){
			changed |= board.tick();
		}
		
    	return changed;
    }

	private void doBoardCommands() {
		for(BoardController board : boards){
			List<ShortMessage> msgs = board.fetchMessages();
			if(msgs.isEmpty() == false){
				doCommands(msgs, board);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void doCommands(List<ShortMessage> msgs, BoardController board) {
		for (ShortMessage message : msgs) {

			if(message.identifier == Identifier.resetGame){
				System.out.println(" --- resetGame --- ");
				this.reset();
				continue;
			}
			
			if(board.getModel().isFinished()){
				continue;
			}
			
			switch (message.identifier) {

			case build_tower:
				board.commandBuildTower((Tower) message.object);
				break;

			case build_creep:
				this.newCreep((Monster) message.object, board);
				break;
				
			case set_killer:
				Monster killer = (Monster) message.object;
				this.reportLooser(board, killer);
				break;
				
			case set_dead_creeps:
				board.commandDeadMonsters((List<Monster>) message.object);
				break;
				
			case set_dead_towers:
				board.commandDeadTowers((List<Tower>) message.object);
				break;
			
			case resetGame:				
			default:
				Log.d(" --- ", "unknown message: " + message.identifier);
			}
		}
	}

	public boolean isFinished() {
		return waitTimeout >= 0;
	}

	public void newCreep(Monster creep, BoardController origin) {
		
		for(BoardController board : boards){
			Monster spawn = creep.replikate();
			if(board == origin){
				spawn.setLifepoints(creep.orgLifepoints / 3);
			}
			board.creepSpawned(spawn);
		}
	}

	public void reportLooser(BoardController looser, Monster killer) {
		
		Log.d(" --- ", "killer received: " + killer);
		
		looser.setGameResult(GameResult.loose);
		looser.setKiller(killer);
		
		BoardController winner = null;
		
		for(BoardController board : boards){
			
			if(board.getModel().getGameResult()==GameResult.undecided){
				if(winner == null){
					winner = board;
				} else {
					winner = null;
					break;
				}
			}
		}
		
		if(winner != null){
			winner.setGameResult(GameResult.win);
			this.waitTimeout = ticksAfterGame;
		}

	}

	public boolean isPaused() {
		return isPaused;
	}

	public void setPaused(boolean isPaused) {
		this.isPaused = isPaused;
		for(BoardController board : boards){
			board.setPaused(isPaused);
		}
	}

	public int getWaitTimeout() {
		return waitTimeout;
	}

	public List<BoardController> getBoards() {
		return boards;
	}

	public int getTick() {
		return tick;
	}

}
