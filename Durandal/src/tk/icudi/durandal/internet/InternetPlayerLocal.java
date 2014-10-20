//package tk.icudi.durandal.internet;
//
//import tk.icudi.durandal.core.logic.BoardController;
//import tk.icudi.durandal.core.logic.CommandObserver;
//import tk.icudi.durandal.core.logic.Message;
//import tk.icudi.durandal.core.logic.player.PlayerHuman;
//
//public class InternetPlayerLocal extends PlayerHuman implements CommandObserver {
//	
//	private static final long serialVersionUID = 1L;
//	private String name;
//	
//	public InternetPlayerLocal(String name) {
//		this.name = name;
//		InternetMessageHandler.getInstance().registerPlayer(name);
//	}
//	
//	@Override
//	public void setBoardController(BoardController boardController) {
//		super.setBoardController(boardController);
//		boardController.addObserver(this);
//	}
//	
//	@Override
//	public void tick() {
//	}
//
//
//	@Override
//	public void reset() {
//
//	}
//
//	@Override
//	public void onLocalCommand(Message message) {
//		InternetMessageHandler.getInstance().uploadMessage(message);
//	}
//
//	@Override
//	public String getName() {
//		return name;
//	}
//
//}
