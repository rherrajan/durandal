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
import tk.icudi.durandal.logger.Log;

public class ToolbarFragment extends Fragment {

    private static final String TAG = "ToolbarFragment";

    private AppCompatActivity activity;
    private View toolbar;
    private Menu menu;

    BluetoothConnectionService mConnectionService;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (AppCompatActivity)activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mConnectionService = new BluetoothConnectionService(getActivity(), mHandler);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.toolbar = inflater.inflate(R.layout.fragment_toolbar, container, false);
        Toolbar toolbar = (Toolbar) this.toolbar.findViewById(R.id.main_toolbar);
        activity.setSupportActionBar(toolbar);

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
                // Launch the DeviceListActivity to see devices and do scan
                Intent serverIntent = new Intent(getActivity(), DeviceListActivity.class);
                startActivityForResult(serverIntent, DeviceListActivity.REQUEST_CONNECT_DEVICE_SECURE);
                return true;
            }

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
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
            case DeviceListActivity.REQUEST_ENABLE_BT:
                if (resultCode == Activity.RESULT_OK) {
                    Log.d(TAG, "Bluetooth is now enabled");
                }
        }
    }




    private void connectDevice(Intent data, boolean secure)  {

        // Get the device MAC address
        String mac_address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);

        Log.d(TAG, "mac_address: " + mac_address);

        setStatus(getString(R.string.title_paired_with, mac_address));

        BluetoothDevice device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(mac_address);
        mConnectionService.connect(device, secure);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        mConnectionService.write("Viele Gruesse ".getBytes());



    }

    /**
     * The Handler that gets information back from the BluetoothConnectionService
     */
    private final Handler mHandler = new Handler() {

        private String mConnectedDeviceName;

        /**
         * Updates the status on the action bar.
         *
         * @param resId a string resource ID
         */
        private void setStatus(int resId) {
            FragmentActivity activity = (FragmentActivity)getActivity();
            if (null == activity) {
                return;
            }
            final ActionBar actionBar = activity.getActionBar();
            if (null == actionBar) {
                return;
            }
            actionBar.setSubtitle(resId);
        }

        /**
         * Updates the status on the action bar.
         *
         * @param subTitle status
         */
        private void setStatus(CharSequence subTitle) {
            AppCompatActivity activity = (AppCompatActivity)getActivity();
            activity.getSupportActionBar().setSubtitle(subTitle);
        }

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothConnectionService.STATE_CONNECTED:
                            setStatus(getString(R.string.title_connected_to, mConnectedDeviceName));
                            break;
                        case BluetoothConnectionService.STATE_CONNECTING:
                            setStatus(R.string.title_connecting);
                            break;
                        case BluetoothConnectionService.STATE_LISTEN:
                        case BluetoothConnectionService.STATE_NONE:
                            setStatus(R.string.title_not_connected);
                            break;
                    }
                    break;
                case Constants.MESSAGE_WRITE:
//                    byte[] writeBuf = (byte[]) msg.obj;
                    break;
                case Constants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);

                    Log.d(TAG, mConnectedDeviceName + ":  " + readMessage);

                    break;
                case Constants.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
                    if (null != activity) {
                        Toast.makeText(activity, "Connected to "
                                + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Constants.MESSAGE_TOAST:
                    if (null != activity) {
                        Toast.makeText(activity, msg.getData().getString(Constants.TOAST),
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    private void setStatus(CharSequence subTitle) {

        AppCompatActivity activity = (AppCompatActivity)getActivity();

        if (null == activity) {
            return;
        }

        activity.getSupportActionBar().setSubtitle(subTitle);
    }

}
