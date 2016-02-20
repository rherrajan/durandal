package tk.icudi.durandal.view.fragments;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import tk.icudi.durandal.R;
import tk.icudi.durandal.bluetooth.BluetoothConnectionService;
import tk.icudi.durandal.bluetooth.Constants;
import tk.icudi.durandal.bluetooth.DeviceListActivity;
import tk.icudi.durandal.core.DurandalMenuActivity;
import tk.icudi.durandal.logger.Log;

public class MultiplayerFragment extends Fragment {

    private static final String TAG = "MultiplayerFragment";


    public AppCompatActivity getAppCompatActivity() {
        return (AppCompatActivity) getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.menu_multiplayer, container, false);
    }


}
