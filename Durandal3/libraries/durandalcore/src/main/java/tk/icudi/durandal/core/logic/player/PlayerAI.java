package tk.icudi.durandal.core.logic.player;

import android.util.SparseArray;
import tk.icudi.durandal.core.logic.BoardController;
import tk.icudi.durandal.core.logic.BoardModel;
import tk.icudi.durandal.core.logic.GameLogic;
import tk.icudi.durandal.core.logic.Monster;
import tk.icudi.durandal.core.logic.Point;
import tk.icudi.durandal.core.logic.Tower;

public class PlayerAI implements Player {

	private static final long serialVersionUID = 1L;

	enum Level2State {
		buildOrderdTowers, buildOrderdCreeps,
	}
	
	enum Level4State {
		front, back, attack_for_level3, save, buildOrderdTowers, buildOrderdCreeps, back2, attack_for_income, emergency, waitOrderdTicks, front3, front2, attack_for_income2, save_for_level3, buildLvl3Middle,
	}
	
	protected BoardController boardController;

	private Level2State level2State;
	private Level4State level4State;
	
	private int orderd;

	private int level;

	private SparseArray<Surveillance> traced;
	
	public PlayerAI(int level) {
		this.level = level;
	}
	
	@Override
	public void reset() {
		level4State = Level4State.front;
		level2State = Level2State.buildOrderdTowers;
		orderd = 50;
		traced = new SparseArray<Surveillance>();
	}
	
	@Override
	public boolean isLogicMaster() {
		return true;
	}
	
	@Override
	public boolean isRemoteControlled(){
		return true;
	}
	
	@Override
	public void tick() {

		if(getModel().getResources().getMoney() <= getModel().getResources().getIncome()){
			// no spamming
			return ;
		}
		
		if(level == 1){
			buildRandomTower();
		} else if(level == 2){
			level2();
		} else if(level == 3){
			level3();
		} else if(level == 4){
			level4();
		} else {
			level5();
		}
		
	}
	
	
	class Surveillance {

		public int expectedLive;
		
	}
	
	private void level5() {
		
		SparseArray<Monster> monsters = getModel().getMonsters();
		
		if(monsters.size() == 0){
			traced = new SparseArray<Surveillance>(); // stop Surveillance
			return;
		}
		
		for(int i=0; i<monsters.size(); i++){
			
			Monster monster = monsters.valueAt(i);
			Surveillance surv = traced.get(monster.getID());
			if(surv == null){
				surv = new Surveillance();
				surv.expectedLive = -1;
				traced.put(monster.getID(), surv);
			}
			
			int defTowers = getModel().getTowers().size() - getModel().getEaten().size();
			double frontline = defTowers/100f;
			if(frontline > 0.5){
				frontline = 0.5f;
			}
			
			if(monster.getPlace().y > frontline * BoardModel.height){
				
				if(surv.expectedLive == -1){
					surv.expectedLive = monster.getLifepoints() - guessLPReducement(monster, surv);
					buildStopTower(monster);
					return;
				} else if(surv.expectedLive > 0){
					int dec = guessLPReducement(monster, surv);
					surv.expectedLive -= dec;
					buildStopTower(monster);
					return;
				} else {
					// will soon be dead
				}
			}
        }
		
	}

	private int guessLPReducement(Monster monster, Surveillance surv) {
		int usedMoney = (int)(getModel().getResources().getMoney() * BoardModel.moneyForTower);
		int lvl = Tower.calculateLVL(usedMoney);
		int killPower = 25 * lvl;
		
		return killPower;
	}

	private void buildStopTower(Monster monster) {
		
		Point firstMonsterLocation = monster.getPlace();
		Point place = new Point();
		place.x = firstMonsterLocation.x;
		place.y = firstMonsterLocation.y+75;
		
		if(place.y > BoardModel.height - 30){
			place.y = BoardModel.height - 30;
		}
		
		buildTower(place);
	}
	
	private void level2() {

		if(level2State == Level2State.buildOrderdTowers){
			buildRandomTower();
			orderd--;
			
			if(orderd <= 0){
				orderd = 5;
				level2State = Level2State.buildOrderdCreeps;
			}
			
		} else if(level2State == Level2State.buildOrderdCreeps){
			
			buildCreep();
			orderd--;
			
			if(orderd <= 0){
				orderd = 20;
				level2State = Level2State.buildOrderdTowers;
			}
		}
	}

	private void level4() {
		
		if(level4State == Level4State.front){
			buildFrontTower();
			if(getModel().getTowers().size() > 25){
				level4State = Level4State.back;
			}
		} else if(level4State == Level4State.back){
			buildBackTower();
			if(getModel().getTowers().size() > 75){
				level4State = Level4State.attack_for_income;
			}
		} else if(level4State == Level4State.attack_for_income){
			
//			int usedMoney = (int)(getModel().getResources().getIncome() * BoardModel.moneyForTower);

//			System.out.println(" --- income: " + getModel().getResources().getIncome() + " => " + usedMoney + " => " + Tower.calculateLVL(usedMoney));
			
			if(getModel().getResources().getIncome() >= 33){
				orderd = 300;
				level4State = Level4State.front2;
			} else {
				buildCreep();
			}
		
		} else if(level4State == Level4State.front2){
			
//			int usedMoney = (int)(getModel().getResources().getMoney() * BoardModel.moneyForTower);
//			int lvl = Tower.calculateLVL(usedMoney);

//			System.out.println(" --- front2: " + orderd + " => " + getModel().getResources().getMoney() + " => " + lvl);
			
			buildFrontTower();

			orderd--;
			
			if(orderd <= 0){
				level4State = Level4State.attack_for_income2;
			}
			
		} else if(level4State == Level4State.attack_for_income2){

//			int usedMoney = (int)(getModel().getResources().getIncome() * BoardModel.moneyForTower);
//			int lvl = Tower.calculateLVL(usedMoney);

//			System.out.println(" --- income2: " + getModel().getResources().getIncome() + " => " + usedMoney + " => " + lvl);

			if(getModel().getResources().getIncome() >= 330){
				buildMiddleTower();
				level4State = Level4State.buildLvl3Middle;
			} else {
				buildCreep();
			}
			
		} else if(level4State == Level4State.buildLvl3Middle){
			
			int usedMoney = (int)(getModel().getResources().getMoney() * BoardModel.moneyForTower);
			int lvl = Tower.calculateLVL(usedMoney);

//			System.out.println(" --- save middle: " + getModel().getResources().getMoney() + " => " + usedMoney + " => " + lvl);
			
			if(lvl >= 3){
				buildMiddleTower();
				orderd = 10;
				level4State = Level4State.emergency;
			}
			
		} else if(level4State == Level4State.emergency){
			
//			int usedMoney = (int)(getModel().getResources().getMoney() * BoardModel.moneyForTower);
//			int lvl = Tower.calculateLVL(usedMoney);

//			System.out.println(" --- emergency tower: " + getModel().getResources().getMoney() + " => " + usedMoney + " => " + lvl);
			
			buildEmergencyTower();
			orderd--;
			
			if(orderd <= 0){
				level4State = Level4State.save_for_level3;
			}
			
		} else if(level4State == Level4State.buildOrderdTowers){
			
//			int usedMoney = (int)(getModel().getResources().getMoney() * BoardModel.moneyForTower);
//			int lvl = Tower.calculateLVL(usedMoney);
//			System.out.println(" --- random tower: " + getModel().getResources().getMoney() + " => " + usedMoney + " => " + lvl);
			
			
			buildBorderTower();
			orderd--;
			
			if(orderd <= 0){
				level4State = Level4State.save_for_level3;
			}
			
		} else if(level4State == Level4State.save_for_level3){
			
			int usedMoney = (int)(getModel().getResources().getMoney() * BoardModel.moneyForTower);
			int lvl = Tower.calculateLVL(usedMoney);

//			System.out.println(" --- save: " + getModel().getResources().getMoney() + " => " + usedMoney + " => " + lvl);
			
			if(lvl >= 3){
				buildRandomTower();
				orderd = (int)(5 * GameLogic.rand.nextFloat());
				level4State = Level4State.buildOrderdTowers;
			}
		}
	
	}


	private void level3() {
		
		if(getModel().getTowers().size() < 30){
			buildRandomTower();
			return;
		}
		
		if(getModel().getEaten().size() > 15){
			buildEmergencyTower();			
			return;
		}
		
		buildCreep();
		buildRandomTower();
	}
	
	private BoardModel getModel(){
		return boardController.getModel();
	}

	private void buildCreep() {
		boardController.initiateCreepBuild();
	}

	private void buildTower(Point place) {
		boardController.initiateTowerBuild(place);
	}
	
	private void buildEmergencyTower() {

		double x,y;
		if(GameLogic.rand.nextBoolean()){
			// Border Guard
			x = 0.3 * GameLogic.rand.nextFloat();
			if(GameLogic.rand.nextBoolean()){
				x = 1-x;
			}
			y = 0.5 + 0.5 * GameLogic.rand.nextFloat();
		} else {
			// Last defence
			x = 0.3 + 0.4 * GameLogic.rand.nextFloat();
			y = 0.8 + 0.2 * GameLogic.rand.nextFloat();
		}

		Point place = new Point();
		place.x = (int)(x * BoardModel.height );
		place.y = (int)(y * BoardModel.width);
		buildTower(place);
	}

	private void buildBorderTower() {

		double x,y;

		x = 0.3 * GameLogic.rand.nextFloat();
		if(GameLogic.rand.nextBoolean()){
			x = 1-x;
		}
		y = GameLogic.rand.nextFloat();


		Point place = new Point();
		place.x = (int)(x * BoardModel.height );
		place.y = (int)(y * BoardModel.width);
		buildTower(place);
	}
	
	private void buildFrontTower() {

		double x,y;

		x = GameLogic.rand.nextFloat();
		y = 0.4 * GameLogic.rand.nextFloat();

		Point place = new Point();
		place.x = (int)(x * BoardModel.height );
		place.y = (int)(y * BoardModel.width);
		buildTower(place);
	}
	
	private void buildBackTower() {

		double x,y;

		x = GameLogic.rand.nextFloat();
		y = 0.8 + 0.2 * GameLogic.rand.nextFloat();

		Point place = new Point();
		place.x = (int)(x * BoardModel.height );
		place.y = (int)(y * BoardModel.width);
		buildTower(place);
	}
	
	private void buildMiddleTower() {
		
		double x,y;

		x = 0.3 + 0.4 * GameLogic.rand.nextFloat();
		y = 0.3 + 0.4 * GameLogic.rand.nextFloat();

		Point place = new Point();
		place.x = (int)(x * BoardModel.height);
		place.y = (int)(y * BoardModel.width);
		buildTower(place);
	}
	
	private void buildRandomTower() {
		Point place = new Point();
		place.x = (int)(BoardModel.height * GameLogic.rand.nextFloat());
		place.y = (int)(BoardModel.width * GameLogic.rand.nextFloat());
		buildTower(place);
	}

	@Override
	public void setBoardController(BoardController boardController) {
		this.boardController = boardController;
	}

	@Override
	public String getName() {
		return "AI" + randomInt(0,1000);
	}
	
	private static int randomInt(int min, int max) {
		return (int) (min + (Math.random() * (max + 1 - min)));
	}
	
}
