package tk.icudi.durandal.bluetooth2;

import java.io.IOException;

import tk.icudi.durandal.core.logic.BTMessage;


/** Can receive Data from other devices */
public interface BTConnectionHandler<T extends BTMessage> {

	void onConnectionEstablished(BTConnection<T> connection, boolean youAreTheServer);

	void onConnectionError(IOException e);

	void obtainedMessage(T msg);


}
