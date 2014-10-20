//package tk.icudi.durandal.internet;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.UnsupportedEncodingException;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.ProtocolException;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.List;
//
//import tk.icudi.durandal.core.logic.Message;
//import android.os.AsyncTask;
//import android.util.Log;
//
//public class DownloadTask extends AsyncTask<Integer, Void, List<Message>>{
//	
//	private String stringUrl = InternetMessageHandler.host + "durandal?command=fetch";
//	
//	@Override
//	protected List<Message> doInBackground(Integer... params){
//
//		try {
//			return readUrl();
//		} catch (IOException e) {
//			Log.d(" --- ", "connection exception: ", e);
//			return null;
//		}
//		
//	}
//	
//	private List<Message> readUrl() throws IOException {
//		InputStream is = null;
//
//		try {
//			is = openConnection();
//
//			if (is == null) {
//				return new ArrayList<Message>();
//			}
//			
//			return readMessages(is);
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
////		if (Build.VERSION.SDK_INT > 13) {
////			conn.setRequestProperty("Connection", "close");
////		}
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
//	private List<Message> readMessages(InputStream stream) throws IOException, UnsupportedEncodingException{
//		BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"), 8192);
//
//		List<Message> msgs = new ArrayList<Message>();
//		
//		String line;
//		while((line = reader.readLine()) != null){
//			Message message = new Message(line);
////			System.out.println(" --- message: " + message);
//			msgs.add(message);
//		}
//
//		return msgs;
//	}
//
//}
