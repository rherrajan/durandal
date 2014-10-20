//package tk.icudi.durandal.internet;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.ProtocolException;
//import java.net.URL;
//import java.util.List;
//
//import tk.icudi.durandal.core.logic.Message;
//import android.os.AsyncTask;
//import android.util.Log;
//
//public class ResetTask extends AsyncTask<Integer, Void, List<Message>>{
//	
//	private String stringUrl = InternetMessageHandler.host + "durandal?command=delete";
//	
//	@Override
//	protected List<Message> doInBackground(Integer... params){
//
//		try {
//			readUrl();
//			return null;
//		} catch (IOException e) {
//			Log.d(" --- ", "connection exception: ", e);
//			return null;
//		}
//		
//	}
//	
//	private void readUrl() throws IOException {
//		InputStream is = null;
//
//		try {
//			is = openConnection();
//
//		} finally {
//			if (is != null) {
//				is.close();
//			}
//		}
//	}
//
//	private InputStream openConnection() throws MalformedURLException, IOException, ProtocolException {
//		
//		URL url = new URL(stringUrl);
//		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//		conn.setReadTimeout(10000);
//		conn.setConnectTimeout(15000);
//		conn.setRequestMethod("GET");
//		conn.setDoInput(true);
//		
//		conn.connect();
//		int responseCode = conn.getResponseCode();
//
//		if(responseCode != 200){
//			Log.d(" --- ", "The responseCode is: " + responseCode + " for this url: " + stringUrl);
//			return null;
//		} else {
//			return conn.getInputStream();
//		}
//		
//	}
//
//}
