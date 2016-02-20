package tk.icudi.durandal.view.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tk.icudi.durandal.R;

public class GameMenuFragment extends Fragment {

    public AppCompatActivity getAppCompatActivity() {
        return (AppCompatActivity) getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.menu_game, container, false);
    }


}
