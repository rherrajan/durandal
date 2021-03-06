//package tk.icudi.durandal.internet;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.atomic.AtomicInteger;
//
//import org.apache.http.HttpResponse;
//import org.apache.http.NameValuePair;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.message.BasicNameValuePair;
//
//import tk.icudi.durandal.core.logic.Message;
//import android.os.AsyncTask;
//import android.util.Log;
//
//public class UploadTask extends AsyncTask<Message, Void, Void>{
//	
//	private String baseURL = InternetMessageHandler.host + "durandal";
//	
//	private static AtomicInteger parallelTasks = new AtomicInteger(0);
//	
//	@Override
//	protected void onPreExecute() {
//		parallelTasks.incrementAndGet();
//
//		if(parallelTasks.intValue() > 5){
//			Log.d(" --- ", "Upload Task is slow: " + parallelTasks + " concurrent");
//		}
//	}
//	
//	@Override
//	protected void onPostExecute(Void result) {
//		parallelTasks.decrementAndGet();
//	}
//	
//	@Override
//	protected Void doInBackground(Message... params){
//
//		try {
//			sendMessage(params[0]);
//		} catch (IOException e) {
//			Log.d(" --- ", "connection exception for url '" + params[0] + " ' : ", e);
//		}
//		
//		return null;
//	}
//	
//	private void sendMessage(Message message) throws IOException {
//
//			// http://icudi00.appspot.com/durandal?command=add&user=tester&code=addCreep&value=1234567&tick=50
//			//String url = baseURL + "&tick=" + message.getTick() + "&user=" + message.getUser() + "&code=" + message.getCommand() + "&value=" + message.getValue();
//			
//	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//	        nameValuePairs.add(new BasicNameValuePair("command", "add"));
//	        nameValuePairs.add(new BasicNameValuePair("tick", String.valueOf(message.getTick())));
//	        nameValuePairs.add(new BasicNameValuePair("user", message.getUser()));
//	        nameValuePairs.add(new BasicNameValuePair("code", message.getCommand()));
//	        nameValuePairs.add(new BasicNameValuePair("value", message.getValue()));
//	        
//			openConnection(nameValuePairs);
//
//	}
//
//	private void openConnection(List<NameValuePair> nameValuePairs) throws IOException {
//		
//	    HttpClient httpclient = new DefaultHttpClient();
//	    HttpPost httppost = new HttpPost(baseURL);
//        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//        
//        HttpResponse response = httpclient.execute(httppost);
//        
//        int responseCode = response.getStatusLine().getStatusCode();
//        if(responseCode != 200){
//        	throw new IOException("The responseCode is: " + responseCode + " for this url: " + httppost.getURI());
//        }
//
//	}
//
//}
