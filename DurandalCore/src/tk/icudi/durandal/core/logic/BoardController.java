package tk.icudi.durandal.core.logic;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import tk.icudi.durandal.core.logic.BoardModel.GameResult;
import tk.icudi.durandal.core.logic.ShortMessage.Identifier;
import tk.icudi.durandal.core.logic.player.Player;
import android.util.Log;

public class BoardController {
	
	protected BoardModel model;
	
	private List<ShortMessage> playerCommands;
	private boolean hasChanged;

	private boolean isPaused;

	private Player player;
	
	private List<CommandObserver> observers = new ArrayList<CommandObserver>();

	private int tick;

	/** Player taps have affect */
	private boolean isRemoteControlled;

	/** calculation is done locally */
	private boolean isLogicMaster;
	
	BoardController(Player player){
		
		
		
		this.model = new BoardModel(player.getName());
		this.player = player;
		this.player.setBoardController(this);
		
		this.isRemoteControlled = player.isRemoteControlled();
		this.isLogicMaster = player.isLogicMaster();
	}
	
	public void addObserver(CommandObserver observer){
		observers.add(observer);
	}
	
	public void reset() {
		this.hasChanged = true;
		this.playerCommands = new ArrayList<ShortMessage>();
		model.reset();
		this.player.reset();
		this.tick = 0;
	}
	
	boolean tick() {
		
		tick++;
		
		if(model.isFinished()){
			return false;
		}
				
		hasChanged |= model.moveTowers();
		hasChanged |= model.moveMonsters();
		hasChanged |= model.shootMonsters();
		
		this.treatDeadMonsters();
		this.treatDeadTowers();
    	
		player.tick();
		
    	boolean hasChangedTemp = this.hasChanged;
    	
    	this.hasChanged = false;
    	
    	return hasChangedTemp;
	}
	
	void commandDeadMonsters(List<Monster> dead) {
		
		model.removeDeadMonsters(dead);
		this.hasChanged = true;
	}
	
	void commandBuildTower(Tower created) {
		
		if(this.isLogicMaster){
			model.eatTowers(created);
		}
		
		model.buildTower(created);
		this.hasChanged = true;
	}
	
	void commandDeadTowers(List<Tower> dead) {
		model.removeDeadTowers(dead);
		this.hasChanged = true;
	}
	
	public void setKiller(Monster killer) {
		model.setKiller(killer);
		this.hasChanged = true;
	}
	
	public List<ShortMessage> fetchMessages(){
		List<ShortMessage> msgs = new ArrayList<ShortMessage>(playerCommands);
		playerCommands.clear();
		return msgs;
	}
	
	public void addPrivateCommand(ShortMessage cmd){
		playerCommands.add(cmd);
	}
	
	private void addCommand(ShortMessage cmd) {
		playerCommands.add(cmd);
		notifyObserver(cmd);
	}

	private void notifyObserver(ShortMessage msg) {
		
		if(observers.size() == 0){
			return;
		}
		

		
		Message message = new Message();
		
		message.setTick(tick);
		message.setUser(model.getName());
		message.setCommand(msg.identifier.toString());
		message.setDate(new SimpleDateFormat("dd-MM-yyyy hh:mm", Locale.GERMAN).format(new Date()));
		
		try{
			message.setMsg(msg);
		} catch (IOException e) {
			Log.d(" --- ", msg.identifier.toString() + " is not serializable", e);
			return;
		}
			
		for(CommandObserver observer : observers){
			observer.onLocalCommand(message);
		}
	}

	protected void treatDeadMonsters() {
		
		List<Monster> dead = model.findDeadMonsters();
		if(dead.isEmpty() == false){
			addPrivateCommand(new ShortMessage(Identifier.set_dead_creeps, dead));
		}
		
		if(this.isLogicMaster){
			if(model.getKiller() == null){
			    Monster killer = model.findKiller();
			    if(killer != null){
			    	addCommand(new ShortMessage(Identifier.set_killer, killer));
			    }
			}
		} else {
			dead = model.findRunnawayMonsters();
			if(dead.isEmpty() == false){
				Log.d(" --- ", "Monster werent recognised as dead an run away: " + dead.size());
				addPrivateCommand(new ShortMessage(Identifier.set_dead_creeps, dead));
			}
		}
	
	}
	
	public void treatDeadTowers() {
		
		if(this.isLogicMaster == false){
			return;
		}
		
		List<Tower> dead = model.findDeadTowers();

		if (dead.isEmpty() == false) {
			ShortMessage msg = new ShortMessage(Identifier.set_dead_towers, dead);
			addCommand(msg);
		}

	}
	
	// Command received
	void creepSpawned(Monster creep) {
		
		if(model.isFinished()){
			return;
		}
		
//		System.out.println(" --- creep spawned: " + creep.getID());
		
		model.getMonsters().put(creep.getID(), creep);
		
		this.hasChanged = true;
	}

	void setGameResult(GameResult gameResult) {
		if(model.gameResult != GameResult.loose){
			model.gameResult = gameResult;
			this.hasChanged = true;
			Log.d(" --- ", "Game result: '" + gameResult + "'");
		} else {
			Log.d(" --- ", "Cannot move to stat '" + gameResult + "' cause the game has state '" + model.gameResult + "' ");
		}
	}

	public void giveMoney() {
		model.giveMoney();
	}

	public BoardModel getModel() {
		return model;
	}

	public void setPaused(boolean isPaused) {
		this.isPaused = isPaused;
		this.hasChanged = true;
	}
	
	public boolean isPaused() {
		return isPaused;
	}

	public boolean hasChanged() {
		return this.hasChanged;
	}
	
	public void tapForCreep() {
		if(isRemoteControlled){
			return;
		}
		initiateCreepBuild();
	}

	public void initiateCreepBuild() {
		Monster creep = model.createCreep();
		if(creep != null){
			ShortMessage cmd = new ShortMessage(ShortMessage.Identifier.build_creep, creep);
			this.addCommand(cmd);
		}
	}
	
	public void tapForTower(Point place) {
		if(isRemoteControlled){
			return;
		}
		initiateTowerBuild(place);
	}

	public void initiateTowerBuild(Point place) {
		Tower created = model.createTower(place);
		if(created != null){
			initiateTowerBuild(created);
		}
	}

	public void initiateTowerBuild(Tower created) {
		ShortMessage cmd = new ShortMessage(Identifier.build_tower, created);
		this.addCommand(cmd);
	}
	
	public void tapForReset() {
		ShortMessage cmd = new ShortMessage(ShortMessage.Identifier.resetGame, null);
		this.addCommand(cmd);
	}
	
	public boolean isRemoteControlled(){
		return isRemoteControlled;
	}


}
