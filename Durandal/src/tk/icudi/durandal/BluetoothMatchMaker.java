//package tk.icudi.durandal;
//
//import java.io.IOException;
//import java.util.HashSet;
//import java.util.Set;
//import java.util.UUID;
//
//import tk.icudi.durandal.bluetooth.BTConnector;
//import tk.icudi.durandal.bluetooth2.BTConnection;
//import tk.icudi.durandal.bluetooth2.BTConnectionHandler;
//import tk.icudi.durandal.core.logic.ShortMessage;
//import tk.icudi.durandal.core.logic.StartOptions;
//import android.bluetooth.BluetoothDevice;
//import android.os.ParcelUuid;
//import android.util.Log;
//
//public class BluetoothMatchMaker {
//
//	private class MatchMaker implements BTConnectionHandler<ShortMessage> {
//		
//		@Override
//		public void onConnectionEstablished(BTConnection<ShortMessage> connection, boolean youAreTheServer) {
//    		
//			if(local.isiAmTheServer()){
//				Log.d(" --- ", "Our server " + deviceIdentifier + " connected successfully to client  " + connection.getDeviceName());
//				remote.setName(connection.getDeviceName());
//			} else {
//				Log.d(" --- ", "Our client " + deviceIdentifier + " connected successfully to server  " + connection.getDeviceName());
//			}
//
//    	    StartOptions options = new StartOptions();
//    	    options.setReleaseCreepAtStart(false);
//	    	options.addPlayer(local);
//	    	options.addPlayer(remote);
//	    	
//	    	startGame(options);
//		}
//		
//		@Override
//		public void onConnectionError(IOException e) {
//			Log.d(" --- ", "onConnectionError", e);
//		}
//		
//		@Override
//		public void obtainedMessage(ShortMessage msg) {
//			// Nothing to do
//		}
//	}
//	
//	private String deviceIdentifier = android.os.Build.MODEL;
//	private BTConnector<ShortMessage> btConnector;
//	private PlayerBlueToothLocal local;
//	final PlayerBlueToothRemote remote;
//	private MatchMaker maker;
//
//
//	private final static UUID DURANDAL_UUID = UUID.fromString("42db5411-7ae7-466d-ac39-755fbba70b1a");
//	
//	public BluetoothMatchMaker() {
//		btConnector = new BTConnector<ShortMessage>(){
//
//			private static final long serialVersionUID = 1L;
//			
//			@Override
//			protected Class<ShortMessage> getMessageClass() {
//				return ShortMessage.class;
//			}
//			public UUID getUUID(){
//				return DURANDAL_UUID;
//			}
//		};
//		
//		this.local = new PlayerBlueToothLocal(deviceIdentifier);
//		this.remote = new PlayerBlueToothRemote("unknown");
//		this.maker = new MatchMaker();
//	}
//	
//	public void startAsServer() {
//		
//		Log.d(" --- ", deviceIdentifier + " waits for clients");
//
//		local.setiAmTheServer(true);
//		
//		// Remote Player has connected to our server 
//		
//		final Set<BTConnectionHandler<ShortMessage>> btHandler = new HashSet<BTConnectionHandler<ShortMessage>>();
//		btHandler.add(maker);
//		btHandler.add(local);
//		btHandler.add(remote);
//    	
//		btConnector.actAsServer(btHandler);
//	}
//
//	protected void startGame(StartOptions options){
//		// To be overridden
//	}
//
//	public BluetoothDevice findPairedDevice() {
//		
//    	Set<BluetoothDevice> allDevices = btConnector.getDevices();
//    	
//    	for(BluetoothDevice device : allDevices){
//    		device.fetchUuidsWithSdp();
//    	}
//    	
//    	Set<BluetoothDevice> devicesWithService = new HashSet<BluetoothDevice>();
//    	
//    	UUID durandalUUID = btConnector.getUUID();
//    	
//    	for(BluetoothDevice device : allDevices){
//    		
//    		ParcelUuid[] uuids = device.getUuids();
//    		
//    		for(ParcelUuid serviceOnDevice : uuids){
//    			boolean areEqual = durandalUUID.compareTo(serviceOnDevice.getUuid()) == 0;	
//    			if(areEqual){
//    				devicesWithService.add(device);
//    			}
//    		}
//    	}
//    	
//		Log.d(" --- ", "Servers found: " + devicesWithService);
//		
//    	if(devicesWithService.isEmpty() == false){
//    		return devicesWithService.iterator().next();
//    	} else {
//    		return null;
//    	}   	
//
//	}
//
//	public void startAsClient(BluetoothDevice pairedDevice) {
//		
//		local.setiAmTheServer(false);
//		remote.setName(pairedDevice.getName());
//		
//		
//		final Set<BTConnectionHandler<ShortMessage>> btHandler = new HashSet<BTConnectionHandler<ShortMessage>>();
//		btHandler.add(local);
//		btHandler.add(remote);
//		btHandler.add(maker);
//		
//		btConnector.connectToDevice(pairedDevice, btHandler);
//	}
//	
//}
