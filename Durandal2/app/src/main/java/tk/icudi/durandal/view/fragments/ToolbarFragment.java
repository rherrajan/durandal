package tk.icudi.durandal.view.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import tk.icudi.durandal.R;


public class ToolbarFragment extends Fragment {

    private View toolbar;
    private AppCompatActivity activity;
    private Menu menu;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (AppCompatActivity)activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.toolbar = inflater.inflate(R.layout.fragment_toolbar, container, false);
//        Toolbar toolbar = (Toolbar) this.toolbar.findViewById(R.id.nearby_toolbar);
//        activity.setSupportActionBar(toolbar);

        return this.toolbar;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        this.menu = menu;
        // inflater.inflate(R.menu.main_activity_actions, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
