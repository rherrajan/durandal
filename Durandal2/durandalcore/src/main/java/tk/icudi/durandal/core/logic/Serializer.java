package tk.icudi.durandal.core.logic;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Base64;
import android.util.Log;

public class Serializer {

	public static String objectToString(Object source) throws IOException {
		//return serializableToString((Serializable) source);
		return parcelableToString((Parcelable) source);

	}

	public static <T extends Parcelable, Serializable> T objectFromString(Class<T> clazz, String source) {
		//return serializableFromString(clazz, source);
		return parcelableFromString(clazz, source);
	}

	/*
	private static String serializableToString(Serializable source ) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream( baos );
		oos.writeObject(source);

		byte[] objectBytes = baos.toByteArray();
		oos.close();

		// String result = new String(Base64.encode(objectBytes, Base64.DEFAULT));
		String result = new String(objectBytes);

		System.out.println(" --- send: '" + result + "'");

		return result;
	}


	private static <T extends Serializable> T serializableFromString(Class<T> clazz, String source) {

		System.out.println(" --- recieved: '" + source + "'");

		try{
			// byte[] data = Base64.decode(source, Base64.DEFAULT);
			byte[] data = source.getBytes();
			ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
			Object o  = ois.readObject();
			ois.close();
			return (T)o;

		} catch(Exception e){
			Log.d(" --- ", "conversion error for: '"+source+"'"  , e);
			return null;
		}
	}
    */


	private static String parcelableToString(Parcelable source) throws IOException {
		
		Parcel parcel = Parcel.obtain();
		source.writeToParcel(parcel, 0);
		
		parcel.setDataPosition(0);
		
		System.out.println(" --- dataSize: " + parcel.dataSize());

		//String result = new String(Base64.encode(parcel.marshall(), Base64.URL_SAFE));
		String result = new String(parcel.marshall());
		
		parcel.recycle();
		
		return result;
	}
    
    private static <T extends Parcelable> T parcelableFromString(Class<T> clazz, String source) {

    	System.out.println(" --- source: '" + source + "'");

		Parcel parcel = null;

		try{
	        //byte [] data = Base64.decode(source, Base64.URL_SAFE);
			byte [] data = source.getBytes();

	    	parcel = Parcel.obtain();
	    	parcel.unmarshall(data, 0, data.length);
	    	
	    	parcel.setDataPosition(0);
	    	
	    	Field f = clazz.getField("CREATOR");
	    	
	    	@SuppressWarnings("unchecked")
			Creator<T> creator = (Creator<T>)f.get(null);
	    	
	    	T result = creator.createFromParcel(parcel);

	    	return result;

		} catch(Exception e){
			Log.d(" --- ", "conversion error for: '"+source+"'"  , e);
			return null;
		} finally {
			if(parcel != null){
				parcel.recycle();
			}
		}

    }


    
}
