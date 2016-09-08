package com.example.martin.try4;

import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.io.IOException;
import java.util.UUID;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class BTrecieve extends AppCompatActivity {

    private static final String TAG = "MyActivity";

    //Button btnSend;
    BluetoothAdapter myBluetooth = null;
    private BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;

    //private ConnectedThread mConnectedThread;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // String for MAC address
    private static String address;
    //private static String address1 = "B0:E2:35:42:55:E8"; //xiaomi
    //private static String address2= "90:C1:15:A5:9E:21";  //xperia


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_btrecieve);

        Intent i = getIntent();
        address= i.getSerializableExtra("device_address").toString();

        FloatingActionButton btnSend = (FloatingActionButton) findViewById(R.id.fabBTcommunication);
        //ConnectBT connectBT = new ConnectBT();
        new ConnectBT().execute();

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBtnSend();      //method to turn on
            }
        });
    }


    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try
            {
                if (btSocket == null || !isBtConnected)
                {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available

                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                    ///static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
                    
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();//start connection
                }
            }
            catch (IOException e)
            {
                ConnectSuccess = false;//if the try failed, you can check the exception here
                logMessage("failed to connect");
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            if (!ConnectSuccess)
            {
                logMessage("Connection Failed. Is it a SPP Bluetooth? Try again.");
                finish();
            }
            else
            {
                logMessage("Connected.");
                isBtConnected = true;
            }
        }
    }

    private void logMessage(String message){
        Log.i(TAG, message);
    }

    private void msg2(String s)
    {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }

    private void Disconnect()
    {
        if (btSocket!=null) //If the btSocket is busy
        {
            try
            {
                btSocket.close(); //close connection
            }
            catch (IOException e)
            { msg2("Error");}
        }
        finish(); //return to the first layout
    }

    private void setBtnSend()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("AHOJ".toString().getBytes());
                msg2("message has been sent");
            }
            catch (IOException e)
            {
                msg2("Error");
            }
        }
        else {
            msg2("socket not functional");
        }
    }

}
