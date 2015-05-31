package tk.icudi.durandal.core;

import tk.icudi.durandal.core.logic.GameLogic;
import tk.icudi.durandal.core.logic.StartOptions;
import tk.icudi.durandal.core.view.GameView;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;

public class DurandalCoreActivity extends Activity {

	private GameView view;
	private GameLogic logic;
	
	private Handler handler = new Handler();
    private Runnable runner = new Runnable() {
        @Override
		public void run() {
        	// 50ms Pause nach einem Tick
        	boolean changed = logic.tick();
        	if(changed){
        		view.invalidate();
        	}
            handler.postDelayed(runner, 50);
        }
    };
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

//		setContentView(R.layout.activity_fullscreen);
		
	    Intent intent = getIntent();

	    final StartOptions options = (StartOptions)intent.getExtras().get(StartOptions.START_OPTIONS_MESSAGE);

		if(options != null){			
			if(options.getPlayers().size() == 0){
				throw new RuntimeException("no players specified");
			}
		} else {
			throw new RuntimeException("please specify start options");
		}

		this.logic = new GameLogic(options);
		this.view = new GameView(this);
		view.setLogic(logic);
		
		setContentView(view);
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
	
    @Override
    protected void onResume() {
        super.onResume();
        handler.post(runner);
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runner);
    }

}
