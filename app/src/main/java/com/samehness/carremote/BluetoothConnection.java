package com.samehness.carremote;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;


public class BluetoothConnection extends AppCompatActivity {

    public static String EXTRA_ADDRESS = "device_address";
    public static String name = "device_address";
    public static String deviceName = "null";
    public static String deviceMac = "device_mac";
    public TextView status;
    CoordinatorLayout relative;
    ListView deviceList;

    //**********************Bluetooth Stuff*******************

    private static final int REQUEST_ENABLE_BT = 0;
    static BluetoothAdapter myBluetooth = null;
    public static BluetoothSocket btSocket = null;
    public static boolean isBtConnected = false;
    //SSP UUID. Look for it
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    //*********************************************************************




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_connection);
        setTitle("Connect to Device");

        relative = findViewById(R.id.relative);
        status   = findViewById(R.id.statusBT);

        // Turning ON Bluetooth automatically
        myBluetooth = BluetoothAdapter.getDefaultAdapter();
        deviceList = findViewById(R.id.deviceList);
        if (myBluetooth == null) {
            msg("Device Not Supported", 0);
            finish();
        }else if(myBluetooth.isEnabled()){
            pairedDeviceList();

        } else if((!myBluetooth.isEnabled())){
            Intent turnOnIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOnIntent, REQUEST_ENABLE_BT);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.connect_menu_action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            // action with ID action_refresh was selected
            case R.id.action_refresh:
                if(myBluetooth.isEnabled()){
                    Snackbar snack = Snackbar.make(relative,"Refreshing...", Snackbar.LENGTH_SHORT);
                    snack.show();
                    pairedDeviceList();
                    deviceList.setVisibility(View.VISIBLE);

                }else {
                    if(!(myBluetooth.isEnabled())){
                        Intent turnOnIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(turnOnIntent, REQUEST_ENABLE_BT);
                    }
                    status.setVisibility(View.VISIBLE);
                    deviceList.setVisibility(View.GONE);
                }
                break;
            default:
                break;
        }

        return true;
    }
    ////////////////////////////////////////////////////////////////////////
    private void pairedDeviceList(){
        Set<BluetoothDevice> pairedDevices = myBluetooth.getBondedDevices();
        final String[] DevicesName = new String[pairedDevices.size()];
        final String[] DevicesMac = new String[pairedDevices.size()];
        int Counter = 0;
        ArrayList<Device> list = new ArrayList<>();

        if(pairedDevices.size()>0)
        {
            status.setVisibility(View.GONE);
            for(BluetoothDevice bt : pairedDevices)
            {
                // put the name of bluetooth adapter and mac address of each device in array
                deviceName = bt.getName();
                deviceMac  = bt.getAddress();
                list.add (new Device(deviceName));
                DevicesMac[Counter] = deviceMac;
                DevicesName[Counter] = deviceName;
                Counter++;
            }
        }else{
            msg("No Paired Bluetooth Devices Found", 0);
        }

        // show the array in listView
        deviceTemp adapter = new deviceTemp(this, list);
        deviceList.setAdapter(adapter);

        //CLASS 2
        // When the user clicks on item in the list
        deviceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EXTRA_ADDRESS = DevicesMac[position];
                name = DevicesName[position];

                //Start Connection
                new ConnectBT().execute();
            }
        });

        deviceList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                PopupMenu popup = new PopupMenu(BluetoothConnection.this,view);
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        EXTRA_ADDRESS = DevicesMac[position];
                        name = DevicesName[position];
                        Intent i = new Intent(BluetoothConnection.this, ControlPanel.class);
                        i.putExtra("DeviceName", name);
                        i.putExtra("DeviceMacAddress", EXTRA_ADDRESS);
                        startActivity(i);
                        return true;
                    }
                });

                popup.show();
                return true;
            }
        });
    }

    ////////////////////////////////////////////////////////////////////////////
    private class Device {
        private String DeviceName;

        private Device(String dname){
            this.DeviceName = dname;

        }
    }

    /////////////////////////////////////////////////////////////////////////////
    public class deviceTemp extends ArrayAdapter<Device> {

        private deviceTemp (Context context, ArrayList<Device> devices) {
            super(context,0, devices);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            // Get the data item for this position
            Device device = getItem(position);

            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.bluetooth_device_template, parent, false);
            }

            // Lookup view for data population
            TextView dname = (TextView) convertView.findViewById(R.id.dname);

            // Populate the data into the template view using the data object
            dname.setText(device.DeviceName);

            // Return the completed view to render on screen
            return convertView;
        }

    }

    //////////////////////////////////////////////////////////////////////////
    //UI thread
    class ConnectBT extends AsyncTask<Void, Void, Void> {

        //if it's here, it's almost connected
        private boolean ConnectSuccess = true;

        @Override
        protected void onPreExecute(){
            Snackbar snack = Snackbar.make(relative,"Connecting to "+name+"...", Snackbar.LENGTH_LONG);
            snack.show();
        }

        /////////////////////////////////////////////////////////////////////////////
        //while the progress dialog is shown, the connection is done in background//
        ///////////////////////////////////////////////////////////////////////////
        @Override
        protected Void doInBackground (Void... Devices){


            try {
                if (btSocket==null || !isBtConnected) {

                    //get the mobile bluetooth device
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();

                    //connects to the device's address and checks if it's available
                    BluetoothDevice newDevice = myBluetooth.getRemoteDevice(EXTRA_ADDRESS);


                    //create a RFCOMM (SPP) connection
                    btSocket = newDevice.createInsecureRfcommSocketToServiceRecord(myUUID);

                    //Stops Discovery mode
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();

                    //start connection
                    btSocket.connect();
                }
            } catch (IOException e) {

                //if the try failed, you can check the exception here
                ConnectSuccess = false;
                btSocket = null;
            }


            return null;
        }

        ////////////////////////////////////////////////////////////////
        //after the doInBackground, it checks if everything went fine//
        //////////////////////////////////////////////////////////////
        protected void onPostExecute (Void result) {
            super.onPostExecute(result);

            if (!ConnectSuccess){
                msg("Connection Failed. Is it a SSP Bluetooth? Try again.", 0);
            }else {
                msg("Connected",0);
                Intent i = new Intent(BluetoothConnection.this, ControlPanel.class);
                i.putExtra("DeviceName", name);
                i.putExtra("DeviceMacAddress", EXTRA_ADDRESS);
                startActivity(i);
            }
        }
    }



    /////////////////////////////////////////////////////////////////////
    // MAKE TOAST
    private void msg (String s, int t){
        if(t == 0){
            Toast toast = Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT);
            toast.show();
        }else if (t == 1 ){
            Toast toast = Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG);
            toast.show();
        }

    }

}