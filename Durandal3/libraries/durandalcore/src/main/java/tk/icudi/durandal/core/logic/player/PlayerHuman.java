package tk.icudi.durandal.core.logic.player;

import tk.icudi.durandal.core.logic.BoardController;
import tk.icudi.durandal.core.logic.NameGenerator;

public class PlayerHuman implements Player {
	
	private static final long serialVersionUID = 1L;
	
	protected BoardController boardController;

	@Override
	public void setBoardController(BoardController boardController) {
		this.boardController = boardController;
	}
	
	public boolean isRemoteControlled(){
		return false;
	}

	@Override
	public boolean isLogicMaster() {
		return true;
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
		return NameGenerator.getName();
	}

}
