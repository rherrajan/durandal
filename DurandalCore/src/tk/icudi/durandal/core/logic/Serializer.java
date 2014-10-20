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

	public static String serializableToString(Serializable source ) throws IOException {
		
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream( baos );
        oos.writeObject( source );
        oos.close();
        
        return new String(Base64.encode(baos.toByteArray(), Base64.URL_SAFE));
    }
	
    public static Serializable serializableFromString(String source) {
    	
		try{
	        byte [] data = Base64.decode(source, Base64.URL_SAFE);
	        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
	        Object o  = ois.readObject();
	        ois.close();
	        return (Serializable)o;
	        
		} catch(Exception e){
			Log.d(" --- ", "conversion error" , e);
			return null;
		}

    }

	public static String parcelableToString(Parcelable source) throws IOException {
		
		Parcel parcel = Parcel.obtain();
		source.writeToParcel(parcel, 0);
		
		parcel.setDataPosition(0);
		
		System.out.println(" --- dataSize: " + parcel.dataSize());
		
		String result = new String(Base64.encode(parcel.marshall(), Base64.URL_SAFE));
		
		parcel.recycle();
		
		return result;
	}
    
    public static <T extends Parcelable> T parcelableFromString(Class<T> clazz, String source) {

//    public static ShortMessage parcelableFromString(String source) {
    	
		try{
	        byte [] data = Base64.decode(source, Base64.URL_SAFE);
	        
	    	Parcel parcel = Parcel.obtain();
	    	parcel.unmarshall(data, 0, data.length);
	    	
	    	parcel.setDataPosition(0);
	    	
	    	Field f = clazz.getField("CREATOR");
	    	
	    	@SuppressWarnings("unchecked")
			Creator<T> creator = (Parcelable.Creator<T>)f.get(null);
	    	
	    	T result = creator.createFromParcel(parcel);

			parcel.recycle();
			
	    	return result;
	        
		} catch(Exception e){
			Log.d(" --- ", "conversion error" , e);
			return null;
		}

    }
    
}
