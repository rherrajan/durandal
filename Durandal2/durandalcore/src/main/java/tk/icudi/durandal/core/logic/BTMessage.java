package tk.icudi.durandal.core.logic;

import android.annotation.SuppressLint;
import android.os.Parcelable;

@SuppressLint("ParcelCreator")
public interface BTMessage extends Parcelable{

	byte[] getBytes();
}
