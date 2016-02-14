package tk.icudi.durandal.core.logic;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class Tower implements Serializable, Parcelable {

	private static final long serialVersionUID = 1L;

	private int range = 75;
	int damage = 1;
	private int lvl;
	public Point place;
	Tower eater;
	
	private BresenhamLine path;

	public Tower() {

	}
	
	public Tower(Parcel in) {
		this.range = in.readInt();
		this.damage = in.readInt();
		this.lvl = in.readInt();
		this.place = in.readParcelable(Point.class.getClassLoader());
		this.eater = in.readParcelable(Tower.class.getClassLoader());
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.range);
		dest.writeInt(this.damage);
		dest.writeInt(this.lvl);
		dest.writeParcelable(this.place, 0);
		dest.writeParcelable(this.eater, 0);
	}
	
	void setEater(Tower eater) {
		this.eater = eater;
		this.path = new BresenhamLine(this.place, eater.place);
	}

	public void move() {
		this.place = this.path.next();
	}

	public boolean isEaten() {
		return this.eater != null && this.path.isDone();
	}

	public void eat(Tower tower) {
		range++;
		int newDamage = this.damage + tower.damage/2;
//		Log.d("---", "Tower Damage " + damage + " => " + newDamage);
		this.damage = newDamage;
		this.lvl = newDamage;
	}

	public int getLvl() {
		return lvl;
	}

	public void setLvl(int lvl) {
		this.lvl = lvl;
	}

	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}

	public void setUsedMoney(int usedMoney) {
		final int lvl = calculateLVL(usedMoney);
		this.damage = lvl;
		this.lvl = lvl;
	}

	public static int calculateLVL(int usedMoney) {
		final int damage;
		if(usedMoney == 0){
			// insufficient funds
			damage = 1;
		} else {
			damage = (int)(Math.log10(5 * usedMoney));
		}
		return damage;
	}

	@Override
	public int describeContents() {
		return 0;
	}
	
	public static final Creator<Tower> CREATOR = new Creator<Tower>() {
		public Tower createFromParcel(Parcel in) {
		    return new Tower(in);
		}
		
		public Tower[] newArray(int size) {
		    return new Tower[size];
		}
	};


}
