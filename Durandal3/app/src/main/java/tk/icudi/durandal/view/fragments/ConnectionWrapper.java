package tk.icudi.durandal.view.fragments;

import java.io.Serializable;

import tk.icudi.durandal.bluetooth.BluetoothConnectionService;
import tk.icudi.durandal.controller.BTConnection;
import tk.icudi.durandal.core.logic.ShortMessage;

public class ConnectionWrapper implements BTConnection<ShortMessage>, Serializable {

	private static final long serialVersionUID = 1L;
	
	private BluetoothConnectionService mConnectionService;

    public ConnectionWrapper(BluetoothConnectionService mConnectionService) {
        this.mConnectionService = mConnectionService;
    }

    @Override
    public void cancel() {
        mConnectionService.stop();
    }

    @Override
    public void write(ShortMessage msg) {
        mConnectionService.write(msg.getBytes());
    }

    @Override
    public String getDeviceName() {
        return "<DeviceName>";
    }
}
