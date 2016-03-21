package tk.icudi.durandal.core;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

public class DurandalCoreActivity extends Activity {


    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		activateGameFragment();
	}

	private void activateGameFragment(){
		CoreFragment fragment = new CoreFragment();
		FragmentTransaction transaction = this.getFragmentManager().beginTransaction();
		transaction.add(fragment, "tagname");
		transaction.commit();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	    	Log.d(" --- ", "user stopped the game");
	    	this.setResult(Activity.RESULT_OK, getIntent());
	    	finish();
	    	return true;
	    }

	    return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
	    super.onRestoreInstanceState(savedInstanceState);
	    Log.d("---", "onRestoreInstanceState: " + savedInstanceState);
	}

}
