package tk.icudi.durandal;

import java.io.IOException;

import tk.icudi.durandal.bluetooth2.BTConnection;
import tk.icudi.durandal.bluetooth2.BTConnectionHandler;
import tk.icudi.durandal.core.logic.ShortMessage;
import tk.icudi.durandal.core.logic.player.PlayerHuman;
import android.util.Log;

/** Represents a player on an other device */
public class PlayerBlueToothRemote extends PlayerHuman implements BTConnectionHandler<ShortMessage> {

	private static final long serialVersionUID = 1L;
	
	private String name;
	
	public PlayerBlueToothRemote(String name) {
		this.name = name;
	}
		
	@Override
	public void tick() {
	}

	@Override
	public void reset() {

	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public void onConnectionEstablished(BTConnection<ShortMessage> connection, boolean youAreTheServer) {
		Log.d(" --- ", "onConnectionEstablished. youAreTheServer: " + youAreTheServer);
	}

	@Override
	public void onConnectionError(IOException e) {
		Log.d(" --- ", "onConnectionError", e);
	}

	@Override
	public void obtainedMessage(ShortMessage msg) {
		boardController.addPrivateCommand(msg);
	}

}
