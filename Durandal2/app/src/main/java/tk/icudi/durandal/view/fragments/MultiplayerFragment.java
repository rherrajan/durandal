package tk.icudi.durandal.view.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import tk.icudi.durandal.R;
import tk.icudi.durandal.bluetooth.BluetoothConnectionService;
import tk.icudi.durandal.bluetooth.Constants;
import tk.icudi.durandal.bluetooth.DeviceListActivity;
import tk.icudi.durandal.logger.Log;

public class MultiplayerFragment extends Fragment {

    private static final String TAG = "MultiplayerFragment";

    private static final int REQUEST_PAIR_DEVICE = 1;

    private BluetoothConnectionService mConnectionService;
    private BluetoothDevice device;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mConnectionService = new BluetoothConnectionService(getActivity(), mHandler);

        Log.i(TAG, "accepting connections on " + BluetoothAdapter.getDefaultAdapter().getAddress());

    }

    public AppCompatActivity getAppCompatActivity() {
        return (AppCompatActivity) getActivity();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View button_pair = getActivity().findViewById(R.id.button_pair);
        button_pair.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(TAG, "pairing...");

                Intent enableIntent = new Intent(getActivity(), DeviceListActivity.class);
                startActivityForResult(enableIntent, REQUEST_PAIR_DEVICE);
            }
        });

        View button_join = getActivity().findViewById(R.id.button_join);
        button_join.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(device == null){
                    Log.e(TAG, "not paired");
                    return;
                }

                Log.d(TAG, "connecting...");

                mConnectionService.connect(device, true);
            }
        });

        View button_test = getActivity().findViewById(R.id.button_test_connection);
        button_test.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(TAG, "testing connection ...");

                if (mConnectionService.getState() != BluetoothConnectionService.STATE_CONNECTED) {
                    Log.d(TAG, "not connected");
                }
                mConnectionService.write("hi".getBytes());
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.menu_multiplayer, container, false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case REQUEST_PAIR_DEVICE:
                if (resultCode == Activity.RESULT_OK) {
                    String macAdress = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);

                    this.device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(macAdress);

                    setStatus(getString(R.string.title_paired_with, device.getName()));
                }
                break;
        }
    }


    private void connectDevice(String mac_address, boolean secure) {
        
        //String mac_address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);

        Log.d(TAG, "mac_address: " + mac_address);

        setStatus(getString(R.string.title_paired_with, mac_address));

        BluetoothDevice device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(mac_address);
        mConnectionService.connect(device, secure);

        //mConnectionService.write("Viele Gruesse ".getBytes());

    }

    private void setStatus(CharSequence subTitle) {

        getAppCompatActivity().getSupportActionBar().setSubtitle(subTitle);

        Log.i(TAG, subTitle.toString());
    }


    /**
     * The Handler that gets information back from the BluetoothConnectionService
     */
    private final Handler mHandler = new Handler() {

        private void setStatus(CharSequence subTitle) {
            MultiplayerFragment.this.setStatus(subTitle);
        }

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothConnectionService.STATE_CONNECTED:

                            setStatus(getString(R.string.title_connected_to, device.getName()));
                            break;
                        case BluetoothConnectionService.STATE_CONNECTING:
                            setStatus(getString(R.string.title_connecting));
                            break;
                        case BluetoothConnectionService.STATE_LISTEN:
                        case BluetoothConnectionService.STATE_NONE:
                            setStatus(getString(R.string.title_not_connected));
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

                    Log.d(TAG, device.getName() + ":  " + readMessage);

                    break;
                case Constants.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    break;
                case Constants.MESSAGE_TOAST:
                    String toastMsg = msg.getData().getString(Constants.TOAST);
                    Toast.makeText(getActivity(), toastMsg, Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "connection manager: " + toastMsg);
                    break;
            }
        }
    };

/*
    private final Handler mHandler = new Handler() {

        private String mConnectedDeviceName;


        private void setStatus(int resId) {
            FragmentActivity activity = (FragmentActivity) getActivity();
            if (null == activity) {
                return;
            }
            final ActionBar actionBar = activity.getActionBar();
            if (null == actionBar) {
                return;
            }
            actionBar.setSubtitle(resId);
        }


        private void setStatus(CharSequence subTitle) {
            AppCompatActivity activity = (AppCompatActivity) getActivity();
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
                    Toast.makeText(getActivity(), "Connected to "
                            + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    break;
                case Constants.MESSAGE_TOAST:
                    Toast.makeText(getActivity(), msg.getData().getString(Constants.TOAST),
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    */
}
