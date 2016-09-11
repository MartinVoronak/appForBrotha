package com.example.martin.try4;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.ParcelUuid;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;

public class PairDevice extends AppCompatActivity {

    private static final String TAG = "MyActivity";
    BluetoothAdapter bluetooth;
    ArrayList<BTDevice> arrayList;

    private OutputStream outputStream;
    private InputStream inStream;

    int devicePosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pair_device);

        bluetooth = BluetoothAdapter.getDefaultAdapter();
        setBluetooth(true);

        bluetooth.setName(bluetooth.getName());
        if (bluetooth.isEnabled()) {

            setBluetooth(true);

            String mydeviceaddress = bluetooth.getAddress();
            String mydevicename = bluetooth.getName();

            String status = mydevicename + " : " + mydeviceaddress;
            Log.i(TAG, "HS BT my info: " + status);
        } else {
            String status = "Bluetooth is not Enabled.";
        }

        ListView deviceList = (ListView) findViewById(R.id.listViewBTDevices);
        Set<BluetoothDevice> pairedDevices = bluetooth.getBondedDevices();
        arrayList = new ArrayList<BTDevice>();

        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                BTDevice BtDevice = new BTDevice();

                Log.i(TAG, "BT paired device: " + device.getName() + " = " + device.getAddress());
                BtDevice.name = device.getName();
                BtDevice.mac = device.getAddress();

                arrayList.add(BtDevice);
            }
        }

        CustomAdapterBT cAdapter = new CustomAdapterBT(this, arrayList);
        deviceList.setAdapter(cAdapter);

        deviceList.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                        Log.i(TAG, "PD picked device: " + arrayList.get(position).name);

                        Intent i = new Intent(PairDevice.this, BTrecieve.class);
                        i.putExtra("device_address", arrayList.get(position).getMac());
                        startActivity(i);

                    }
                }
        );
    }

    public static boolean setBluetooth(boolean enable) {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        boolean isEnabled = bluetoothAdapter.isEnabled();
        if (enable && !isEnabled) {
            return bluetoothAdapter.enable();
        }
        else if(!enable && isEnabled) {
            return bluetoothAdapter.disable();
        }
        return true;
    }

}
