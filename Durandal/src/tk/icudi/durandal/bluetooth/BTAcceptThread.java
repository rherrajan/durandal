//package tk.icudi.durandal.bluetooth;
//
//import java.io.IOException;
//import java.util.UUID;
//
//import android.bluetooth.BluetoothAdapter;
//import android.bluetooth.BluetoothServerSocket;
//import android.bluetooth.BluetoothSocket;
//import android.util.Log;
//
//class BTAcceptThread extends Thread {
//	
//	private static final String TAG = BTAcceptThread.class.getSimpleName();
//	
//	// TODO this is not always so!
//	public final static String NAME = "MIServiec";
//	
//    private final BluetoothServerSocket mmServerSocket;
//	private BluetoothSocket resultSocket;
//       	
//    public BTAcceptThread(BluetoothAdapter mBluetoothAdapter, UUID uuid) {
//    	
//        // Use a temporary object that is later assigned to mmServerSocket,
//        // because mmServerSocket is final
//        BluetoothServerSocket tmp = null;
//        try {
//            
//        	System.out.println(" --- MY_UUID: " + uuid);
//        	
//			// MY_UUID is the app's UUID string, also used by the client code
//            tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, uuid);
//            
//        } catch (IOException e) {
//        	Log.e(TAG, "Konnte Server-Socket nicht erstellen", e);
//        }
//        mmServerSocket = tmp;
//        
//        System.out.println(" --- mmServerSocket: " + mmServerSocket);
//    }
// 
//    @Override
//	public void run() {
//
//        // Keep listening until exception occurs or a socket is returned
//        do {
//    		try {
//    			System.out.println(" --- accept ... " );
//    			resultSocket = mmServerSocket.accept();
//    			onConnectionEstablished(resultSocket);
//    		} catch (IOException e) {
//    			Log.e(TAG, "Konnte Server nicht erstellen", e);
//    			onConnectionError(e);
//    			cancel();
//    		}
//        	
//        } while(resultSocket == null);
//    }
//
//	public void onConnectionEstablished(BluetoothSocket resultSocket){}
//	public void onConnectionError(IOException e){}
//	
//	public BluetoothSocket acceptClientAndClose() {
//		
//		BluetoothSocket socket;
//		
//		try {
//			System.out.println(" --- accept ... " );
//		    socket = mmServerSocket.accept();
//		} catch (IOException e) {
//			Log.e(TAG, "Konnte Server nicht erstellen", e);
//			return null;
//		}
//		
//		// If a connection was accepted
//		if (socket == null) {
//		    // Do work to manage the connection (in a separate thread)
//			cancel();
//		}
//		
//		return socket;
//	}
// 
//	/** Will cancel the listening socket, and cause the thread to finish */
//    public void cancel() {
//        try {
//            mmServerSocket.close();
//        } catch (IOException e) {
//        	Log.e(TAG, "Konnte Socket nicht schließen", e);
//        }
//    }
//    
//}
