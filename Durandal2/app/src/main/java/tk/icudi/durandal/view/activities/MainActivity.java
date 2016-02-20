package tk.icudi.durandal.view.activities;

import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;

import tk.icudi.durandal.R;
import tk.icudi.durandal.logger.Log;
import tk.icudi.durandal.logger.LogFragment;
import tk.icudi.durandal.logger.LogWrapper;
import tk.icudi.durandal.logger.MessageOnlyLogFilter;
import tk.icudi.durandal.view.fragments.GameMenuFragment;
import tk.icudi.durandal.view.fragments.MultiplayerFragment;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(tk.icudi.durandal.R.layout.activity_main);
        showGameMenu();
    }

    @Override
    protected  void onStart() {
        super.onStart();
        initializeLogging();
    }

    private void showGameMenu(){
        FragmentTransaction transaction = this.getFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, new GameMenuFragment());
        transaction.commit();
    }

    /** Create a chain of targets that will receive log data */
    public void initializeLogging() {
        // Wraps Android's native log framework.
        LogWrapper logWrapper = new LogWrapper();
        // Using Log, front-end to the logging chain, emulates android.util.log method signatures.
        Log.setLogNode(logWrapper);

        // Filter strips out everything except the message text.
        MessageOnlyLogFilter msgFilter = new MessageOnlyLogFilter();
        logWrapper.setNext(msgFilter);

        // On screen logging via a fragment with a TextView.
        LogFragment logFragment = (LogFragment) getFragmentManager().findFragmentById(R.id.log_fragment);
        msgFilter.setNext(logFragment.getLogView());

        Log.i(TAG, "Ready");
    }

}
