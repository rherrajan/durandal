package tk.icudi.durandal.core.logic;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class Point implements Serializable, Parcelable{

	private static final long serialVersionUID = 1L;
	
	public int x,y;
	
	public Point() {

	}
	
	public Point(Point source) {
		this.x = source.x;
		this.y = source.y;
	}

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Point(Parcel in) {
		this.x = in.readInt();
		this.y = in.readInt();
	}

	@Override
	public String toString() {
		return x + "/" + y;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(x);
		dest.writeInt(y);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
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
		Point other = (Point) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	@Override
	public int describeContents() {
		return 0;
	}
	
	public static final Creator<Point> CREATOR = new Creator<Point>() {
		public Point createFromParcel(Parcel in) {
		    return new Point(in);
		}
		
		public Point[] newArray(int size) {
		    return new Point[size];
		}
	};
	
	
}
