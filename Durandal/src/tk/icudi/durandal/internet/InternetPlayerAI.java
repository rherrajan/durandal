//package tk.icudi.durandal.internet;
//
//import tk.icudi.durandal.core.logic.BoardController;
//import tk.icudi.durandal.core.logic.CommandObserver;
//import tk.icudi.durandal.core.logic.Message;
//import tk.icudi.durandal.core.logic.player.Player;
//import tk.icudi.durandal.core.logic.player.PlayerAI;
//
//public class InternetPlayerAI extends PlayerAI implements Player, CommandObserver {
//	
//	private static final long serialVersionUID = 1L;
//	
//	private String name;
//	
//	public InternetPlayerAI(int level, String name) {
//		super(level);
//		this.name = name;
//	}
//	
//	
//	@Override
//	public void setBoardController(BoardController boardController) {
//		super.setBoardController(boardController);
//		boardController.addObserver(this);
//	}
//	
//	public boolean isRemoteControlled(){
//		return true;
//	}
//
//	@Override
//	public boolean isLogicMaster() {
//		return false;
//	}
//	
//	@Override
//	public void tick() {
//		super.tick();
//	}
//
//	@Override
//	public void onLocalCommand(Message message) {
//		InternetMessageHandler.getInstance().uploadMessage(message);
//	}
//
//	@Override
//	public void reset() {
//		InternetMessageHandler.getInstance().reset();
//	}
//	
//	@Override
//	public String getName() {
//		return name;
//	}
//	
//}
