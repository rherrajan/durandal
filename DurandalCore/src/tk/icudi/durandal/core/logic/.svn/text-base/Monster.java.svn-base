package tk.icudi.durandal.core.logic;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class Monster implements Serializable, Parcelable {
		
	private static final long serialVersionUID = 1L;

	private static int currentMaxID = 0; 
	
	int id;
	int orgLifepoints;
	int lifepoints;
	private int level;
	private Point place;
	
	public Monster() {
		this(randomPlace());
	}
	
	private Monster(Point place) {
		id = getNextID();
		this.place = place;
	}
	
	public Monster(Parcel in) {
		id = in.readInt();
		orgLifepoints = in.readInt();
		lifepoints = in.readInt();
		level = in.readInt();
		place = in.readParcelable(Point.class.getClassLoader());
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeInt(orgLifepoints);
		dest.writeInt(lifepoints);
		dest.writeInt(level);
		dest.writeParcelable(place, 0);
	}
	
	private synchronized static int getNextID(){
		currentMaxID++;
		return currentMaxID;
	}
	
	private static Point randomPlace(){
		Point place = new Point();
		place.x = (int)((0.05 * BoardModel.height) + (0.9 * BoardModel.height * GameLogic.rand.nextFloat()));
//		Log.d(" --- ", "Monster on: " + this.place.x);
		place.y = 0;
		
		return place;
	}

	public void setLifepoints(int lifepoints) {
		this.lifepoints = lifepoints;
		this.orgLifepoints = lifepoints;
		this.level = calculateLVL(orgLifepoints);
	}

	public int getLifepoints(){
		return lifepoints;
	}
	public static int calculateLVL(int orgLifepoints) {
		double level_double = Math.log10(orgLifepoints);
		return (int)Math.floor(level_double);
	}
	
	public Monster replikate() {
		Monster copy = new Monster();
		copy.place = new Point(place);
		copy.lifepoints = this.lifepoints;
		copy.orgLifepoints = this.orgLifepoints;
		copy.level = this.level;

		return copy;
	}

	public double getSize() {
		return this.lifepoints / Math.pow(10, this.level);
	}
	
	@Override
	public String toString() {
		return "Monster [id=" + id + ", lifepoints=" + lifepoints + ", level=" + level +"]";
	}

	public int getID() {
		return id;
	}
	
	public Point getPlace() {
		return place;
	}

	public int getLevel() {
		return level;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + level;
		result = prime * result + lifepoints;
		result = prime * result + orgLifepoints;
		result = prime * result + ((place == null) ? 0 : place.hashCode());
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
		Monster other = (Monster) obj;
		if (id != other.id)
			return false;
		if (level != other.level)
			return false;
		if (lifepoints != other.lifepoints)
			return false;
		if (orgLifepoints != other.orgLifepoints)
			return false;
		if (place == null) {
			if (other.place != null)
				return false;
		} else if (!place.equals(other.place))
			return false;
		return true;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	
	public static final Parcelable.Creator<Monster> CREATOR = new Parcelable.Creator<Monster>() {
		public Monster createFromParcel(Parcel in) {
		    return new Monster(in);
		}
		
		public Monster[] newArray(int size) {
		    return new Monster[size];
		}
	};

}
