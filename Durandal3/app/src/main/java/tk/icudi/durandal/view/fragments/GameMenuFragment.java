package tk.icudi.durandal.view.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import tk.icudi.durandal.R;
import tk.icudi.durandal.core.CoreFragment;
import tk.icudi.durandal.core.logic.StartOptions;
import tk.icudi.durandal.core.logic.player.PlayerAI;
import tk.icudi.durandal.core.logic.player.PlayerHuman;

public class GameMenuFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.menu_game, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Button button;
        button = (Button) findViewById(R.id.button_pve_1);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                StartOptions options = new StartOptions();
                options.addPlayer(new PlayerHuman());
                options.addPlayer(new PlayerAI(5));
                startGame(options);
            }
        });

        button = (Button) findViewById(R.id.button_pve_2);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                StartOptions options = new StartOptions();
                options.addPlayer(new PlayerHuman());
                options.addPlayer(new PlayerAI(4));
                startGame(options);
            }
        });

        button = (Button) findViewById(R.id.button_pve_3);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                StartOptions options = new StartOptions();
                options.addPlayer(new PlayerHuman());
                options.addPlayer(new PlayerAI(3));
                startGame(options);
            }
        });

        button = (Button) findViewById(R.id.button_pve_4);
        button.setOnClickListener(new View.OnClickListener() {
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

        button = (Button) findViewById(R.id.button_pvp_device);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                StartOptions options = new StartOptions();
                options.addPlayer(new PlayerHuman());
                options.addPlayer(new PlayerHuman());
                startGame(options);
            }
        });
    }

    private View findViewById(int id) {
        return getActivity().findViewById(id);
    }

    private void startGame(StartOptions options) {

        CoreFragment fragment = new CoreFragment();
        fragment.setOptions(options);

        FragmentTransaction transaction = getActivity().getFragmentManager().beginTransaction();
        transaction.add(fragment, "tagname");
        transaction.commit();
    }
}
