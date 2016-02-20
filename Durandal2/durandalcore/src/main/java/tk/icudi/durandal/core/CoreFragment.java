package tk.icudi.durandal.core;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tk.icudi.durandal.core.logic.GameLogic;
import tk.icudi.durandal.core.logic.StartOptions;
import tk.icudi.durandal.core.logic.player.PlayerHuman;
import tk.icudi.durandal.core.view.GameView;

public class CoreFragment extends Fragment {

    private StartOptions options;
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


    public void setOptions(StartOptions options){
        this.options = options;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(" --- ", "onCreate: ");

        super.onCreate(savedInstanceState);

        this.logic = new GameLogic(options);
        this.view = new GameView(getActivity());
        view.setLogic(logic);

        getActivity().setContentView(view);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(" --- ", "onCreateView: ");
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        handler.post(runner);
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runner);
    }

}
