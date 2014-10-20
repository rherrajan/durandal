package tk.icudi.durandal.core.logic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Rect;
import android.util.Log;
import android.util.SparseArray;


public class BoardModel implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public static final double moneyForTower = 0.3;
	public static final double moneyForCreep = 0.3;

	public enum GameResult {
		undecided, win, loose
	}
	
	public static final int height = 1000;
	public static final int width = 1000;

	private static final int monsterSpeed = 5;
	
	private SparseArray<Monster> monsters;	
	private List<Tower> towers;
	private List<Tower> eaten;
	
	/** save shots so we can display them */
	private List<Rect> shots;

	GameResult gameResult;
	
	Resources resources;
	
	private Monster killer;
	
	private String name;
	
	public BoardModel() {
		this(NameGenerator.getName());
	}

	public BoardModel(String name) {
		this.name = name;
	}
	
	public void reset() {
		gameResult = GameResult.undecided;
		
		killer = null;
		
		resources = new Resources();
		
		resources.money = 100;
		resources.income = 10;
		
		towers = new ArrayList<Tower>();
		eaten = new ArrayList<Tower>();
		monsters = new SparseArray<Monster>();
		shots = new ArrayList<Rect>();
	}
	
	boolean moveTowers() {
		
		for(Tower tower : eaten){
			tower.move();
		}
		
		return eaten.isEmpty() == false;
	}
	
	protected Monster createCreep() {
				
		resources.income += (int)Math.ceil(resources.money/100.0);

		int lifepoints = (int)(resources.money * moneyForCreep);
		resources.money -= lifepoints;

		
		Monster monster = new Monster();
		monster.setLifepoints(lifepoints);
		
		return monster;
	}


	protected Tower createTower(Point place) {
		
		int usedMoney = (int)(resources.getMoney() * moneyForTower);
		resources.money -= usedMoney;

		Tower created = new Tower();
		created.place = place;
		
		created.setUsedMoney(usedMoney);
		created.setRange(75);
		return created;
	}

	void eatTowers(Tower created) {
		
		for(Tower tower : towers){
			if(tower.eater == null && tower.getLvl() < created.getLvl()){
				tower.setEater(created);
				this.eaten.add(tower);
			}
		}

	}

	protected void giveMoney() {
		resources.money += resources.income;
	}

	protected boolean moveMonsters() {
		
		for(int i=0; i<monsters.size(); i++){
			Monster monster = monsters.valueAt(i);
//			if(monster.getPlace().y <= BoardModel.height){
			
			
			monster.getPlace().y += BoardModel.monsterSpeed;
		}
        
        return monsters.size() > 0;
	}

	protected boolean shootMonsters() {
		
		boolean hasChanged = (shots.isEmpty() == false);
		
        shots = new ArrayList<Rect>();
        for(Tower tower : towers){
        	
    		for(int i=0; i<monsters.size(); i++){
    			Monster monster = monsters.valueAt(i);
            	
            	double diffplus = (monster.getPlace().x - tower.place.x) + (monster.getPlace().y - tower.place.y);

            	if(diffplus < tower.getRange() && monster.lifepoints >= 0){
                	double diff = Math.sqrt(Math.pow(monster.getPlace().x - tower.place.x, 2) + Math.pow(monster.getPlace().y - tower.place.y, 2));
                	
                	if(diff < tower.getRange()){
                		monster.lifepoints -= tower.damage;
                		
                		Rect shot = new Rect(tower.place.x, tower.place.y, monster.getPlace().x, monster.getPlace().y);
                		shots.add(shot);
                		
                		break;
                	}
            	}
            }
        }
        
        return hasChanged;
	}

	void removeDeadMonsters(List<Monster> dead) {
		for(Monster monster : dead){
			Monster toRemove = monsters.get(monster.id);
			
			if(toRemove != null){
				monsters.remove(monster.id);
			} else {
				System.out.println(" --- couldnt remove Monster Nr." + monster.id);
			}

        }
	}

	protected Monster findKiller() {
		for(int i=0; i<monsters.size(); i++){
			Monster monster = monsters.valueAt(i);
			if(monster.getPlace().y > height){
				Log.d("---", "Game Over. Monster: " + monster);
				// Das Spiel ist beendet
				return monster;
			}
		}
		
		return null;
	}

	protected List<Monster> findDeadMonsters() {
		List<Monster> dead = new ArrayList<Monster>();
        
		for(int i=0; i<monsters.size(); i++){
			Monster monster = monsters.valueAt(i);
        	if(monster.lifepoints < 0){
        		dead.add(monster);
        	}
        }
		return dead;
	}
	
	void removeDeadTowers(List<Tower> dead) {
		for(Tower tower : dead){
        	tower.eater.eat(tower);
        	towers.remove(tower);
        	eaten.remove(tower);
        }
	}

	protected List<Tower> findDeadTowers() {
		List<Tower> dead = new ArrayList<Tower>();
        for(Tower tower : towers){
        	if(tower.isEaten()){
        		dead.add(tower);
//        	} else if(tower.eater != null && tower.eater.isEaten()){
//        		dead.add(tower);
        	}
        	
        }
		return dead;
	}

	public Monster getKiller() {
		return killer;
	}

	public GameResult getGameResult() {
		return gameResult;
	}

	public SparseArray<Monster> getMonsters() {
		return monsters;
	}

	public List<Tower> getTowers() {
		return towers;
	}

	public List<Rect> getShots() {
		return shots;
	}

	void setKiller(Monster killer) {
		this.killer = killer;
	}

	public boolean isFinished() {
		return gameResult != GameResult.undecided;
	}

	public void buildTower(Tower created) {
		this.towers.add(created);
	}

	public List<Tower> getEaten() {
		return eaten;
	}

	public Resources getResources() {
		return resources;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name=name;
	}


	public List<Monster> findRunnawayMonsters() {

		List<Monster> dead = new ArrayList<Monster>();
        
		for(int i=0; i<monsters.size(); i++){
			Monster monster = monsters.valueAt(i);
			if(monster.getPlace().y > height + 1000){
        		dead.add(monster);
        	}
        }
		return dead;
	}
}
