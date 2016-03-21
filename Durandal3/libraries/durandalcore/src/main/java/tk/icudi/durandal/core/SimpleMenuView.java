package tk.icudi.durandal.core;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import tk.icudi.durandal.core.logic.StartOptions;
import tk.icudi.durandal.core.logic.player.PlayerAI;
import tk.icudi.durandal.core.logic.player.PlayerHuman;

public class SimpleMenuView extends LinearLayout {

	public SimpleMenuView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		
		Button button;
		button = (Button) findViewById(R.id.pve_1);
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
        	    StartOptions options = new StartOptions();
        	    options.addPlayer(new PlayerHuman());
        	    options.addPlayer(new PlayerAI(5));
            	startGame(options);
            }
        });
        
        button = (Button) findViewById(R.id.pve_2);
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
        	    StartOptions options = new StartOptions();
        	    options.addPlayer(new PlayerHuman());
        	    options.addPlayer(new PlayerAI(4));
            	startGame(options);
            }
        });
        
        button = (Button) findViewById(R.id.pve_3);
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
        	    StartOptions options = new StartOptions();
        	    options.addPlayer(new PlayerHuman());
        	    options.addPlayer(new PlayerAI(3));
            	startGame(options);
            }
        });
        
        button = (Button) findViewById(R.id.pve_4);
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
        	    StartOptions options = new StartOptions();
        	    options.addPlayer(new PlayerHuman());
        	    options.addPlayer(new PlayerAI(1));
        	    options.addPlayer(new PlayerAI(2));
        	    options.addPlayer(new PlayerAI(4));
        	    options.addPlayer(new PlayerAI(3));
            	startGame(options);
            }
        });
        
        button = (Button) findViewById(R.id.pvp_device);
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
        	    StartOptions options = new StartOptions();
        	    options.addPlayer(new PlayerHuman());
        	    options.addPlayer(new PlayerHuman());
            	startGame(options);
            }
        });
	}
	
	
	protected void startGame(StartOptions options) {
		Log.d(" --- ", " Game started: " + options);
	    Intent intent = new Intent(this.getContext(), DurandalCoreActivity.class);
	    intent.putExtra(StartOptions.START_OPTIONS_MESSAGE, options);
	    
	    startIntent(intent, 0);

	}
	
	protected void startIntent(Intent intent, int requestCode) {

	    if(this.getContext() instanceof Activity){
	    	Activity activity = (Activity)this.getContext();
	    	activity.startActivityForResult(intent, requestCode);
	    } else {
	    	Log.d(" --- ", "Context is not an activity: " + this.getContext());
	    	this.getContext().startActivity(intent);
	    }

	}

}
