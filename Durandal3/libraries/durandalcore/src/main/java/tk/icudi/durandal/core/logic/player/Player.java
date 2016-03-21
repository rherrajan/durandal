package tk.icudi.durandal.core.logic.player;

import java.io.Serializable;

import tk.icudi.durandal.core.logic.BoardController;

public interface Player extends Serializable {

	boolean isRemoteControlled();

	/** calculations are done on this machine */
	boolean isLogicMaster();
	
	void setBoardController(BoardController boardController);

	void tick();

	void reset();

	String getName();

//	void onTapForCreep();
//
//	void onTapForTower(Point place);



}
