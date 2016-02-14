package tk.icudi.durandal.view.fragments;

import tk.icudi.durandal.R;
import tk.icudi.durandal.bluetoothimpl.DeviceListActivity;
import tk.icudi.durandal.core.SimpleMenuView;
import tk.icudi.durandal.core.logic.StartOptions;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MenuView extends SimpleMenuView {
	
//	private static final String CONNECT = "bluetooth: connect";
//	private static final String SEARCHING = "Searching...";
//	private static final String SEARCH_AGAIN = "search again";

	// Intent request codes
	static final int REQUEST_CONNECT_DEVICE = 2;
	static final int REQUEST_ENABLE_BT = 3;
	
//	private BTConnector<ShortMessage> btConnector;
	
	public static String deviceIdentifier = android.os.Build.MODEL;

	private Button internetButton;
//	private Button bluetoothButtonAccept;
//	private Button bluetoothButtonConnect;
	
//	private BluetoothMatchMaker matchMaker;
	
	public MenuView(Context context, AttributeSet attrs) {
		super(context, attrs);
//		deviceIdentifier = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
		
//		matchMaker = new BluetoothMatchMaker(){
//			protected void startGame(StartOptions options) {
//				MenuView.this.startGame(options);
//			}
//		};
		 
	}
		
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		
		Button button;
		button = (Button) findViewById(R.id.pvp_device);
        button.setShadowLayer(10.0f, 0.0f, 2.0f, 0xFF000000); 
		
		internetButton = (Button) findViewById(R.id.pvp_internet);
		internetButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {            	
    			showDeviceList();
            }

        });
		
//		bluetoothButtonAccept = (Button) findViewById(R.id.pvp_bluetooth_accept);
//		bluetoothButtonAccept.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//            	matchMaker.startAsServer();
//            	bluetoothButtonAccept.setText("waiting for clients");
//            	bluetoothButtonAccept.setEnabled(false);
//            }
//        });
//        
//		bluetoothButtonConnect = (Button) findViewById(R.id.pvp_bluetooth_connect);
//		bluetoothButtonConnect.setOnClickListener(new View.OnClickListener() {
//			
//            private BluetoothDevice pairedDevice;
//
//			public void onClick(View v) {
//            	
//            	if(bluetoothButtonConnect.getText().equals(CONNECT) || bluetoothButtonConnect.getText().equals(SEARCH_AGAIN)){
//                	bluetoothButtonConnect.setText(SEARCHING);
//                	bluetoothButtonConnect.setEnabled(false);
//                	
//                	pairedDevice = matchMaker.findPairedDevice();
//                	
//                	if(pairedDevice != null){
//                    	bluetoothButtonConnect.setText(pairedDevice.getName());
//                	} else {
//                    	bluetoothButtonConnect.setText(SEARCH_AGAIN);
//                	}
//                	
//                	bluetoothButtonConnect.setEnabled(true);
//                	
//            	} else {
//            		// User wants to connect to a server
//            		matchMaker.startAsClient(pairedDevice);
//            	}
//            }
//        });
	}
	
	void showDeviceList() {
		BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
		
		if(btAdapter == null){
			Toast.makeText(getContext(), "Gerät unterstützt kein Bluetooth", Toast.LENGTH_LONG).show();
        	return;
		}
		
		if (!btAdapter.isEnabled()) {
			Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startIntent(enableIntent, REQUEST_ENABLE_BT);
			return;
		} 
		
		Intent serverIntent = new Intent(getContext(), DurandalMenuActivity.class);
		startIntent(serverIntent, REQUEST_CONNECT_DEVICE);
	}
	
	@Override
	protected void onVisibilityChanged(View changedView, int visibility) {
		super.onVisibilityChanged(changedView, visibility);
	}
	
	@Override
	protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
		super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
		
		internetButton.setEnabled(false);
//		internetButton.setEnabled(InternetMessageHandler.getInstance().isListening());
		
//		bluetoothButtonAccept.setEnabled(btConnector.isBluetoothActive());
//		bluetoothButtonConnect.setEnabled(btConnector.isBluetoothActive());
	}

	@Override
	protected void startGame(StartOptions options) {

//        final int oneSecond = 1000; 
//        InternetMessageHandler.getInstance().setIntervall(oneSecond);
        
		super.startGame(options);
	}
}
