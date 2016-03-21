package tk.icudi.durandal.view.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
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
import tk.icudi.durandal.controller.BTConnection;
import tk.icudi.durandal.controller.PlayerBlueToothLocal;
import tk.icudi.durandal.controller.PlayerBlueToothRemote;
import tk.icudi.durandal.core.CoreFragment;
import tk.icudi.durandal.core.logic.Serializer;
import tk.icudi.durandal.core.logic.ShortMessage;
import tk.icudi.durandal.core.logic.StartOptions;
import tk.icudi.durandal.logger.Log;

public class MultiplayerFragment extends Fragment {

    private static final String TAG = "MultiplayerFragment";
    private static final String start_message = "Start_the_game_NOW";

    private static final int REQUEST_PAIR_DEVICE = 1;

    private BluetoothConnectionService mConnectionService;
    private BluetoothDevice device;

    private PlayerBlueToothLocal local;
    private PlayerBlueToothRemote remote;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mConnectionService = new BluetoothConnectionService(getActivity(), mHandler);
        mConnectionService.start();

        Log.i(TAG, "accepting connections on " + BluetoothAdapter.getDefaultAdapter().getAddress());

        this.local = new PlayerBlueToothLocal("You");
        this.remote = new PlayerBlueToothRemote("unknown");
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

                if (device == null) {
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
                    return;
                }
                mConnectionService.write("hi".getBytes());
            }
        });

        View button_start = getActivity().findViewById(R.id.button_start_game);
        button_start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(TAG, "start game ...");

                if (mConnectionService.getState() != BluetoothConnectionService.STATE_CONNECTED) {
                    Log.d(TAG, "not connected");
                    return;
                }


                mConnectionService.write(start_message.getBytes()); // inform the other player

                BTConnection<ShortMessage> connection = new ConnectionWrapper(mConnectionService);
                boolean youAreTheServer = true;
                local.onConnectionEstablished(connection, youAreTheServer);

                StartOptions options = new StartOptions();
                options.setReleaseCreepAtStart(false);
                options.addPlayer(local);
                options.addPlayer(remote);

                startGame(options);

            }

        });
    }

    private void startGame(StartOptions options) {
        Log.d(TAG, " Game started: " + options);

        CoreFragment fragment = new CoreFragment();
        fragment.setOptions(options);

        FragmentTransaction transaction = getActivity().getFragmentManager().beginTransaction();
        transaction.add(fragment, "tagname");
        transaction.commit();
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


//    private void connectDevice(String mac_address, boolean secure) {
//
//        //String mac_address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
//
//        Log.d(TAG, "mac_address: " + mac_address);
//
//        setStatus(getString(R.string.title_paired_with, mac_address));
//
//        BluetoothDevice device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(mac_address);
//        mConnectionService.connect(device, secure);
//
//        //mConnectionService.write("Viele Gruesse ".getBytes());
//
//    }

    private void setStatus(CharSequence subTitle) {

        getAppCompatActivity().getSupportActionBar().setSubtitle(subTitle);

        Log.i(TAG, subTitle.toString());
    }


    /**
     * The Handler that gets information back from the BluetoothConnectionService
     */
    private final Handler mHandler = new Handler() {


        String mConnectedDeviceName;

        private void setStatus(CharSequence subTitle) {
            MultiplayerFragment.this.setStatus(subTitle);
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

                    if(readMessage.equals(start_message)){
                        BTConnection<ShortMessage> connection = new ConnectionWrapper(mConnectionService);
                        boolean youAreTheServer = true;
                        local.onConnectionEstablished(connection, youAreTheServer);

                        StartOptions options = new StartOptions();
                        options.setReleaseCreepAtStart(false);
                        options.addPlayer(local);
                        options.addPlayer(remote);

                        startGame(options);
                    }


                    System.out.println(" --- readMessage.length: " + readMessage.length());

                    if(readMessage.length() > 100){
                        ShortMessage shortMsg = createMessageFromBytes(readBuf);

                        if(shortMsg != null){
                            Log.i(TAG, mConnectedDeviceName + " msg:  " + shortMsg);
                            remote.obtainedMessage(shortMsg);
                        }
                    } else {
                        Log.i(TAG, mConnectedDeviceName + " raw:  " + readMessage);
                    }

                    break;
                case Constants.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
                    Toast.makeText(getActivity(), "Connected to " + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    break;
                case Constants.MESSAGE_TOAST:
                    String toastMsg = msg.getData().getString(Constants.TOAST);
                    Toast.makeText(getActivity(), toastMsg, Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "connection manager: " + toastMsg);
                    break;
            }
        }

        private ShortMessage createMessageFromBytes(byte[] buffer) {

            String stringMessage = new String(buffer);

            ShortMessage msg = Serializer.objectFromString(ShortMessage.class, stringMessage);

            return msg;


        }
    };

}
