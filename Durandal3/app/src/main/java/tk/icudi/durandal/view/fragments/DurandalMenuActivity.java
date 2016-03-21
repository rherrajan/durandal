package tk.icudi.durandal.view.fragments;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import tk.icudi.durandal.R;
import tk.icudi.durandal.bluetooth.DeviceListActivity;
import tk.icudi.durandal.bluetoothimpl.BluetoothService;
import tk.icudi.durandal.controller.BTConnection;
import tk.icudi.durandal.controller.PlayerBlueToothLocal;
import tk.icudi.durandal.controller.PlayerBlueToothRemote;
import tk.icudi.durandal.core.DurandalCoreActivity;
import tk.icudi.durandal.core.logic.Serializer;
import tk.icudi.durandal.core.logic.ShortMessage;
import tk.icudi.durandal.core.logic.StartOptions;

public class DurandalMenuActivity extends Activity {

	// TODO: Text Size depends on Screen Size
	// TODO: 3 Player
	// TODO: Hidden Device ID
	// TODO: Bluetooth
	// TODO: Statistic
	// TODO: Notification-System for challenges
	// TODO: Kornkammer, Waffenkammer, Mauer

	private static final String TAG = " --- ";

//	private BroadcastReceiver actionFoundReceiver = new BTBroadcastReceiver();
	private BluetoothService btService;

	private PlayerBlueToothLocal local;
	private PlayerBlueToothRemote remote;
	
	public DurandalMenuActivity() {
		this.local = new PlayerBlueToothLocal("You");
		this.remote = new PlayerBlueToothRemote("unknown");
		boolean youAreTheServer = true;
		BTConnection<ShortMessage> connection = btService;
		local.onConnectionEstablished(connection, youAreTheServer);
	}
	
	// The Handler that gets information back from the BluetoothService
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			String mConnectedDeviceName = "unknown";
			switch (msg.what) {
			case BluetoothService.MESSAGE_STATE_CHANGE:
					Log.i(" --- ", "MESSAGE_STATE_CHANGE: " + msg.arg1);
				switch (msg.arg1) {
				case BluetoothService.STATE_CONNECTED:
					setStatus(getString(R.string.title_connected_to, mConnectedDeviceName));
//					mConversationArrayAdapter.clear();
					toast("mConversationArrayAdapter.clear();");
					
					StartOptions options = new StartOptions();
					options.setReleaseCreepAtStart(false);
					options.addPlayer(local);
					options.addPlayer(remote);
					
					startGame(options);

					break;
				case BluetoothService.STATE_CONNECTING:
					setStatus(R.string.title_connecting);				
					break;
				case BluetoothService.STATE_LISTEN:
				case BluetoothService.STATE_NONE:
					setStatus(R.string.title_not_connected);
					break;
				}
				break;
			case BluetoothService.MESSAGE_WRITE:
				byte[] writeBuf = (byte[]) msg.obj;
				// construct a string from the buffer
				String writeMessage = new String(writeBuf);
//				mConversationArrayAdapter.add("Me:  " + writeMessage);
				toast("Me:  " + writeMessage);
				break;
			case BluetoothService.MESSAGE_READ:
				byte[] readBuf = (byte[]) msg.obj;
				// construct a string from the valid bytes in the buffer
				String readMessage = new String(readBuf, 0, msg.arg1);
				toast(mConnectedDeviceName + ":  " + readMessage);
//				mConversationArrayAdapter.add(mConnectedDeviceName + ":  " + readMessage);
				
				ShortMessage shortMsg = createMessageFromBytes(readBuf);
				remote.obtainedMessage(shortMsg);
				
				break;
			case BluetoothService.MESSAGE_DEVICE_NAME:
				// save the connected device's name
				mConnectedDeviceName = msg.getData().getString(BluetoothService.MESSAGE_PARAMETER_DEVICE_NAME);

				
				toast("Connected to " + mConnectedDeviceName);
				break;
			case BluetoothService.MESSAGE_TOAST:
				toast(msg.getData().getString(BluetoothService.MESSAGE_PARAMETER_TOAST));
				break;
			}
		}

		private void toast(String string) {
			Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT).show();
		}
		
		private ShortMessage createMessageFromBytes(byte[] buffer) {
		
			String stringMessage = new String(buffer);
			ShortMessage msg = Serializer.objectFromString(ShortMessage.class, stringMessage);

			return msg;
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fullscreen);

		// Register the BroadcastReceiver
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		filter.addAction(BluetoothDevice.ACTION_UUID);
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
//		registerReceiver(actionFoundReceiver, filter); // Don't forget to
//														// unregister during
//														// onDestroy
		
		
	}

	@Override
	protected void onStart() {
		super.onStart();
		
		if(btService != null){
			return;
		}
		
		// Initialize the BluetoothChatService to perform bluetooth connections
		this.btService = new BluetoothService(this, this.mHandler);

	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		Log.d(" --- ", "onActivityResult requestCode: " + requestCode + ", resultCode: " + resultCode + ", data: " + data);

		switch (requestCode) {
		case MenuView.REQUEST_CONNECT_DEVICE:
			// When DeviceListActivity returns with a device to connect
			if (resultCode == Activity.RESULT_OK) {
//				connectDevice(data);
				String macAddress = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
				
				Toast.makeText(this, "connect to Device " + macAddress + "...", Toast.LENGTH_LONG).show();
				
				this.btService.connect(macAddress);			
			}
			break;
		case MenuView.REQUEST_ENABLE_BT:
			// When the request to enable Bluetooth returns
			if (resultCode == Activity.RESULT_OK) {
				Toast.makeText(this, "Bluetooth enabled", Toast.LENGTH_SHORT).show();
			} else {
				// User did not enable Bluetooth or an error occurred
				Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
				finish();
			}
		}
		
	}

	@Override
	protected void onResume() {
//		if (InternetMessageHandler.isInternetAvailable(this)) {
//			final int fiveSeconds = 1000 * 5;
//			InternetMessageHandler.getInstance().startListening(fiveSeconds);
//		}

		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();

//		final int fiveMinutes = 1000 * 60 * 5;
//		InternetMessageHandler.getInstance().setIntervall(fiveMinutes);

	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		// if (btAdapter != null) {
		// btAdapter.cancelDiscovery();
		// }
//		unregisterReceiver(actionFoundReceiver);
	}

	private final void setStatus(int resId) {
//		final ActionBar actionBar = getActionBar();
		Log.i(TAG, "setSubtitle: " + resId);
//		actionBar.setSubtitle(resId);
	}

	private final void setStatus(CharSequence subTitle) {
//		final ActionBar actionBar = getActionBar();
		Log.i(TAG, "setSubtitle: " + subTitle);
//		actionBar.setSubtitle(subTitle);
	}

	
	protected void startGame(StartOptions options) {
		Log.d(" --- ", " Game started: " + options);
	    Intent intent = new Intent(this, DurandalCoreActivity.class);
	    intent.putExtra(StartOptions.START_OPTIONS_MESSAGE, options);
	    
	    startActivityForResult(intent, 0);
	}
	

	
}
