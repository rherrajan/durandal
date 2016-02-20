package tk.icudi.durandal.view.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
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
import tk.icudi.durandal.logger.Log;

public class ToolbarFragment extends Fragment {

    private static final String TAG = "ToolbarFragment";

    private static final int REQUEST_ENABLE_BT = 1;

    private View toolbar;
    private Menu menu;

    public AppCompatActivity getAppCompatActivity() {
        return (AppCompatActivity) getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.toolbar = inflater.inflate(R.layout.fragment_toolbar, container, false);
        Toolbar toolbar = (Toolbar) this.toolbar.findViewById(R.id.main_toolbar);
        getAppCompatActivity().setSupportActionBar(toolbar);

        return this.toolbar;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        this.menu = menu;
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {

            case R.id.action_bluetooth: {


                // Get the local Bluetooth adapter


                // If the adapter is null, then Bluetooth is not supported
                BluetoothAdapter mBtAdapter = BluetoothAdapter.getDefaultAdapter();

                if (mBtAdapter == null) {
                    Toast.makeText(getActivity(), "Bluetooth is not available", Toast.LENGTH_LONG).show();
                    return true;
                }

                if (!mBtAdapter.isEnabled()) {
                    Log.d(TAG, "enable Bluetooth...");
                    Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
                } else {
                    showMultiplayerMenu();
                }

                return true;
            }

            default:
                return super.onOptionsItemSelected(item);
        }

    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
/*
            case DeviceListActivity.REQUEST_CONNECT_DEVICE_SECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, true);
                }
                break;
            case DeviceListActivity.REQUEST_CONNECT_DEVICE_INSECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, false);
                }
                break;

*/

            case REQUEST_ENABLE_BT:
                if (resultCode == Activity.RESULT_OK) {
                    Log.d(TAG, "Bluetooth is enabled");
                    showMultiplayerMenu();
                }

        }
    }

    private void showMultiplayerMenu(){

        Log.d(TAG, "showMultiplayerMenu");

        MultiplayerFragment newFragment = new MultiplayerFragment();
        FragmentTransaction transaction = getAppCompatActivity().getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.commit();
    }





}
