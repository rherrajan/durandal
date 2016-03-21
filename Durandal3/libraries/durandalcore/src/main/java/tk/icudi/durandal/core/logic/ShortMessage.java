package tk.icudi.durandal.core.logic;

import java.io.IOException;
import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class ShortMessage implements Serializable, Parcelable, BTMessage {

	private static final long serialVersionUID = 1L;

	public enum Identifier {
		build_creep,
		build_tower,
		set_killer,
		set_dead_creeps,
		set_dead_towers,
		resetGame,
	}
	
	public Identifier identifier;
	public Object object;
	private boolean objectIsParcelable;
		
	public ShortMessage(Identifier identifier, Object object) {
		this.identifier = identifier;
		this.objectIsParcelable = (object instanceof Parcelable);
		this.object = object;
	}

	public ShortMessage(Parcel in) {
		String identifierString = in.readString();
		this.identifier = Identifier.valueOf(identifierString);
		this.objectIsParcelable = in.readByte() == 1;
		if(objectIsParcelable){
			this.object = in.readParcelable(ShortMessage.class.getClassLoader());
		} else {
			this.object = in.readValue(ShortMessage.class.getClassLoader());
		}
		
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		String identifierString = identifier.toString();
		
		System.out.println(" --- write identifierString: " + identifierString);
		
		dest.writeString(identifierString);
		dest.writeByte((byte) (objectIsParcelable ? 1 : 0));
		if(objectIsParcelable){
			dest.writeParcelable((Parcelable)object, 0);
		} else {
			dest.writeValue(object);
		}
		
	}
	
	public String toString(){
		if(identifier == Identifier.build_tower){
			return identifier.toString() + ": " + object;
		}
		return identifier.toString();
	}

	@Override
	public int describeContents() {
		return 0;
	}
	
    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((identifier == null) ? 0 : identifier.hashCode());
		result = prime * result + ((object == null) ? 0 : object.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ShortMessage other = (ShortMessage) obj;
		if (identifier != other.identifier)
			return false;
		if (object == null) {
			if (other.object != null)
				return false;
		} else if (!object.equals(other.object))
			return false;
		return true;
	}

	public static final Creator<ShortMessage> CREATOR = new Creator<ShortMessage>() {
		public ShortMessage createFromParcel(Parcel in) {
		    return new ShortMessage(in);
		}
		
		public ShortMessage[] newArray(int size) {
		    return new ShortMessage[size];
		}
	};

	@Override
	public byte[] getBytes() {
		try {
			String transportString = Serializer.objectToString(this);
			return transportString.getBytes();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
