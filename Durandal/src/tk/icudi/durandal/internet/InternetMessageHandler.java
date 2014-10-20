//package tk.icudi.durandal.internet;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import tk.icudi.durandal.core.logic.Message;
//import android.content.Context;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//import android.util.Log;
//
//
//public class InternetMessageHandler {
//
//	
////	public static String host = "http://10.0.2.2:8888/";
////	public static String host = "http://192.168.178.21:8888/";
//	public static String host = "http://icudi00.appspot.com/";
//	
//	private InternetChecker checker;
//	
//	Map<String, List<Message>> cachedMessages = new HashMap<String, List<Message>>();
//	List<String> newPlayers = new ArrayList<String>();
//	
//	private static InternetMessageHandler instance = new InternetMessageHandler();
//
//	public static InternetMessageHandler getInstance(){
//		return instance;
//	}
//	
//	private InternetMessageHandler() {
//		this.checker = new InternetChecker();
//		reset();
//		checker.start();
//	}
//
//	public void reset() {
//		new ResetTask().execute();
//		checker.reset();
//	}
//	
//	public void setIntervall(int intervall) {
//		System.out.println(" --- setIntervall: " + intervall);
//		checker.timeToSleep = intervall;
//	}
//	
//	public void startListening(int intervall) {
//		setIntervall(intervall);
//		checker.active = true;
//	}
//
//	protected void stopListening() {
//		checker.active = false;
//	}
//
//	public void uploadMessage(Message message) {
//		new UploadTask().execute(message);
//	}
//	
//	public static boolean isInternetAvailable(Context context) {
//		
//		ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
//		if (networkInfo != null && networkInfo.isConnected()) {
//			return true;
//		} else {
//			Log.d(" --- ", "No network connection available");
//			return false;
//		}
//	}
//
//	public boolean isListening() {
//		return checker.active;
//	}
//
//	public void registerPlayer(String playerName) {
//		cachedMessages.put(playerName, new ArrayList<Message>());
//	}
//	
//	public List<Message> fetch(String playerName) {
//					
//		updatePlayerMessages();
//		
//		List<Message> result = cachedMessages.get(playerName);
//		cachedMessages.put(playerName, new ArrayList<Message>());
//		
//		return result;
//	}
//
//	public String getUnattendedPlayer() {
//		
//		updatePlayerMessages();
//		
//		if(newPlayers.isEmpty()){
//			return null;
//		} else {
//			String result = newPlayers.get(0);
//			newPlayers.remove(0);
//			return result;
//		}
//	}
//	
//	private void updatePlayerMessages() {
//		
//		List<Message> newMessages = checker.fetch();
//		
//		if(newMessages.isEmpty()){
//			return;
//		}
//		
//		for(Message newMessage : newMessages){
//						
//			String playerName = newMessage.getUser();
//			
//			if(cachedMessages.containsKey(playerName) == false){
//				Log.d(" --- ", "new player detected: " + playerName);
//				newPlayers.add(playerName);
//				cachedMessages.put(playerName, new ArrayList<Message>());
//			} 
//			
//			List<Message> playerMessages = cachedMessages.get(playerName);
//			playerMessages.add(newMessage);
//		}
//	}
//
//
//}
