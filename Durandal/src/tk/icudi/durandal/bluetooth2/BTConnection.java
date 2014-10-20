package tk.icudi.durandal.bluetooth2;

import tk.icudi.durandal.core.logic.BTMessage;


public interface BTConnection<T extends BTMessage> {
 
    /** Call this from the main activity to shutdown the connection */
    public void cancel();

    /** Call this from the main activity to send data to the remote device */
	public void write(T msg);

	/** Returns the device Name */
	public String getDeviceName();
}
