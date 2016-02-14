package tk.icudi.durandal.core.logic.player;

import tk.icudi.durandal.core.logic.BoardController;

public class Scoreboard implements Player {
	
	private static final long serialVersionUID = 1L;
	
	protected BoardController boardController;

	@Override
	public void setBoardController(BoardController boardController) {
		this.boardController = boardController;
	}
	
	public boolean isRemoteControlled(){
		return true;
	}

	@Override
	public boolean isLogicMaster() {
		return false;
	}

	
	@Override
	public void tick() {
		// do nothing
	}

	@Override
	public void reset() {
		// do nothing
	}

	@Override
	public String getName() {
		return "Scoreboard";
	}

}
