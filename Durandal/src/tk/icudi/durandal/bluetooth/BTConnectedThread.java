//package tk.icudi.durandal.bluetooth;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.util.Set;
//
//import tk.icudi.durandal.bluetooth2.BTConnection;
//import tk.icudi.durandal.bluetooth2.BTConnectionHandler;
//import tk.icudi.durandal.core.logic.BTMessage;
//import tk.icudi.durandal.core.logic.Serializer;
//import android.bluetooth.BluetoothSocket;
//import android.util.Log;
//
//abstract class BTConnectedThread<T extends BTMessage> extends Thread implements BTConnection<T> {
//	
//	private static final String TAG = BTConnectedThread.class.getSimpleName();
//	
//    private final BluetoothSocket mmSocket;
//    private final InputStream mmInStream;
//    private final OutputStream mmOutStream;
//    
//	private Set<BTConnectionHandler<T>> btHandler;
// 
//    public BTConnectedThread(BluetoothSocket socket, Set<BTConnectionHandler<T>> btHandler) {
//    	
//    	this.btHandler = btHandler;
//    	
//        mmSocket = socket;
//        
//        InputStream tmpIn = null;
//        OutputStream tmpOut = null;
// 
//        // Get the input and output streams, using temp objects because
//        // member streams are final
//        try {
//            tmpIn = socket.getInputStream();
//            tmpOut = socket.getOutputStream();
//        } catch (IOException e) { }
// 
//        mmInStream = tmpIn;
//        mmOutStream = tmpOut;
//    }
// 
//    @Override
//    public String getDeviceName() {
//    	return mmSocket.getRemoteDevice().getName();
//	}
//    
//    @Override
//	public void run() {
//        byte[] buffer = new byte[1024];  // buffer store for the stream
//        //int bytes; // bytes returned from read()
// 
//        // Keep listening to the InputStream until an exception occurs
//        while (true) {
//            try {
//                // Read from the InputStream
//            	/* int bytes = */ 
//            	mmInStream.read(buffer, 0, buffer.length);
//                // Send the obtained bytes to the UI activity
//                
//				for(BTConnectionHandler<T> listener : btHandler){
//					
//					T message = createMessageFromBytes(buffer);
//					
////					Message msg = new Message(buffer);
//					listener.obtainedMessage(message);
//				}
//
//                // mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer).sendToTarget();
//                
//            } catch (IOException e) {
//            	Log.e(TAG, "Socket read error", e);
//                break;
//            }
//        }
//    }
//
//	protected T createMessageFromBytes(byte[] buffer) {
//		
//		String stringMessage = new String(buffer);
//		T msg = Serializer.parcelableFromString(getMessageClass(), stringMessage);
//
//		return msg;
//	}
//
//	/* Call this from the main activity to send data to the remote device */
//    public void write(byte[] bytes) {
//        try {
//            mmOutStream.write(bytes);
//        } catch (IOException e) {
//        	Log.e(TAG, "socket write error", e);
//        }
//    }
// 
//	@Override
//	public void write(T msg) {
//		write(msg.getBytes());
//	}
//	
//    /* Call this from the main activity to shutdown the connection */
//    @Override
//	public void cancel() {
//        try {
//            mmSocket.close();
//        } catch (IOException e) {
//        	Log.e(TAG, "socket connect error", e);
//        }
//    }
//
//	protected abstract Class<T> getMessageClass();
//    
//}
