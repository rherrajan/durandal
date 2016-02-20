package tk.icudi.durandal.core;

import tk.icudi.durandal.core.logic.GameLogic;
import tk.icudi.durandal.core.logic.StartOptions;
import tk.icudi.durandal.core.logic.player.PlayerHuman;
import tk.icudi.durandal.core.view.GameView;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;

public class DurandalCoreActivity extends Activity {


    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		aktivateGameFragment();
	}

	private void aktivateGameFragment(){

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
