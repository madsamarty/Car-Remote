package com.samehness.carremote;

import android.animation.ValueAnimator;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.sdsmdg.harjot.vectormaster.VectorMasterView;
import com.sdsmdg.harjot.vectormaster.models.PathModel;

import java.io.IOException;
import java.util.UUID;


public class ControlPanel extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener {

    public static String address = "null";
    CurvedBottomNavigationView bottomNavBar;
    VectorMasterView fab1,fab2,fab3;
    RelativeLayout lin_id;
    PathModel outline;


    //**********************Bluetooth Stuff*******************

    private static int REQUEST_ENABLE_BT = 0;
    BluetoothAdapter myBluetooth = null;
    boolean isBtConnected = false;
    //SSP UUID. Look for it
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    //*********************************************************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_panel);

        Intent intent = getIntent();
        String name = intent.getStringExtra("DeviceName");
        address = intent.getStringExtra("DeviceMacAddress");

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(name);
        setSupportActionBar(toolbar);

        ///////Load Home Fragment
        loadFragment(new FragmentHome());


        ////// Navigation Drawer /////////
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        ///////////////////////////////////////

        bottomNavBar = findViewById(R.id.bottom_nav);
        fab1 = findViewById(R.id.fab1);
        fab2 = findViewById(R.id.fab2);
        fab3 = findViewById(R.id.fab3);
        lin_id = findViewById(R.id.lin_id);
        bottomNavBar.inflateMenu(R.menu.nav_menu);
        bottomNavBar.setSelectedItemId(R.id.action2);
        bottomNavBar.setOnNavigationItemSelectedListener(this);

    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(doubleBackToExitPressedOnce){

                if (BluetoothConnection.btSocket == null ){
                    super.onBackPressed();

                }else{
                    try {
                        disconnect();
                        super.onBackPressed();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                super.onBackPressed();

                finish();
                return;

            }
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(getApplicationContext(), "Click BACK again to Disconnect and Exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.control_panel, menu);
        return true;
    }

    boolean key_flag = false;


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        myBluetooth = BluetoothAdapter.getDefaultAdapter();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_reconnect) {

            if(myBluetooth.isEnabled()){
                    new ConnectBT2().execute();
            }else if (!(myBluetooth.isEnabled())){

                Intent turnOnIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(turnOnIntent, REQUEST_ENABLE_BT);

            }
        }
        if (id == R.id.action_disconnect) {
            if(myBluetooth.isEnabled()){
                if (!(BluetoothConnection.btSocket == null)){
                    try {
                        snackBar(lin_id, "Disconnecting...", Snackbar.LENGTH_LONG);
                        BluetoothConnection.btSocket.close();
                        myBluetooth.disable();
                    } catch (IOException e) {
                        msg("No Connection Found",0);
                    }
                }else if (BluetoothConnection.btSocket == null){
                    msg("No Connection Found",0);
                }
            }
            else if (!(myBluetooth.isEnabled())){
                msg("Bluetooth Has Been Disabled", 0);
            }
        }
        if (id == R.id.action_lock_status) {

        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();



        if (id == R.id.nav_engine) {
            /*Intent i = new Intent(ControlPanel.this, Location.class);
            item.setChecked(false);
            startActivity(i);*/
            msg("Wait For Newer Version.",0);
        }  else if (id == R.id.nav_start_engine) {
            msg("Wait For Newer Version.",0);
        }else if(id == R.id.nav_location){
            /*Intent i = new Intent(getApplicationContext(), Location.class);
            startActivity(i);*/
            msg("Wait For Newer Version.",0);

        }else if (id == R.id.nav_settings){
            /*Intent i = new Intent(getApplicationContext(), Settings.class);
            startActivity(i);*/
            msg("Wait For Newer Version.",0);
        }

        Fragment fragment = null;
        switch (item.getItemId()){
            case R.id.action1:
                fragment = new FragmentOptions();

                draw(6);

                lin_id.setX(bottomNavBar.mFirstCurveControlPoint1.x);
                fab1.setVisibility(View.VISIBLE);
                fab2.setVisibility(View.GONE);
                fab3.setVisibility(View.GONE);

                drawAnimation(fab1);
                break;
            case R.id.action2:
                fragment = new FragmentHome();

                draw(2);

                lin_id.setX(bottomNavBar.mFirstCurveControlPoint1.x);
                fab1.setVisibility(View.GONE);
                fab2.setVisibility(View.VISIBLE);
                fab3.setVisibility(View.GONE);

                drawAnimation(fab2);

                break;
            case R.id.action3:
                fragment = new FragmentHydraulic();

                draw();

                lin_id.setX(bottomNavBar.mFirstCurveControlPoint1.x);
                fab1.setVisibility(View.GONE);
                fab2.setVisibility(View.GONE);
                fab3.setVisibility(View.VISIBLE);

                drawAnimation(fab3);

                break;

        }



        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return loadFragment(fragment);
    }


    public void sendSignal (String number ){
        if (BluetoothConnection.btSocket != null)
        {
            try{
                BluetoothConnection.btSocket.getOutputStream().write(number.toString().getBytes());
            } catch (IOException e) {
                msg("Error", 0);
            }
        }

    }

    private void disconnect() throws IOException {
        BluetoothConnection.btSocket.close();
        msg("Disconnected", 0);

    }

    class ConnectBT2 extends AsyncTask<Void, Void, Void> {

        //if it's here, it's almost connected
        private boolean ConnectSuccess = true;

        @Override
        protected void onPreExecute(){
            snackBar(lin_id, "Reconnecting...", Snackbar.LENGTH_LONG);
        }

        /////////////////////////////////////////////////////////////////////////////
        //while the progress dialog is shown, the connection is done in background//
        ///////////////////////////////////////////////////////////////////////////
        @Override
        protected Void doInBackground (Void... Devices){


            try {
                if (BluetoothConnection.btSocket==null || !isBtConnected) {

                    //get the mobile bluetooth device
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();

                    //connects to the device's address and checks if it's available
                    BluetoothDevice newDevice = myBluetooth.getRemoteDevice(address);


                    //create a RFCOMM (SPP) connection
                    BluetoothConnection.btSocket = newDevice.createInsecureRfcommSocketToServiceRecord(myUUID);

                    //Stops Discovery mode
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();

                    //start connection
                    BluetoothConnection.btSocket.connect();
                }
            } catch (IOException e) {

                //if the try failed, you can check the exception here
                ConnectSuccess = false;
            }


            return null;
        }

        ////////////////////////////////////////////////////////////////
        //after the doInBackground, it checks if everything went fine//
        //////////////////////////////////////////////////////////////
        protected void onPostExecute (Void result) {
            super.onPostExecute(result);

            if (!ConnectSuccess){
                msg("Something Went Wrong", 0);
            }else {
                snackBar(lin_id, "Connected", Snackbar.LENGTH_SHORT);
            }

            //progress.dismiss();
        }
    }

    private void draw() {

        bottomNavBar.mFirstCurveStartPoint.set((bottomNavBar.mNavigationBarWidth * 10/12)
                - (bottomNavBar.CURVE_CIRCLE_RADIUS * 2)
                - (bottomNavBar.CURVE_CIRCLE_RADIUS / 3), 0 );

        bottomNavBar.mFirstCurveEndPoint.set(bottomNavBar.mNavigationBarWidth * 10/12,bottomNavBar.CURVE_CIRCLE_RADIUS
                + (bottomNavBar.CURVE_CIRCLE_RADIUS/4));

        bottomNavBar.mSecondCurveStartPoint = bottomNavBar.mFirstCurveEndPoint;

        bottomNavBar.mSecondCurveEndPoint.set((bottomNavBar.mNavigationBarWidth * 10/12)
                + (bottomNavBar.CURVE_CIRCLE_RADIUS*2) + (bottomNavBar.CURVE_CIRCLE_RADIUS/3), 0);


        bottomNavBar.mFirstCurveControlPoint1.set(bottomNavBar.mFirstCurveStartPoint.x
                        + bottomNavBar.CURVE_CIRCLE_RADIUS + (bottomNavBar.CURVE_CIRCLE_RADIUS/4),
                bottomNavBar.mFirstCurveStartPoint.y);

        bottomNavBar.mFirstCurveControlPoint2.set(bottomNavBar.mFirstCurveEndPoint.x
                        - (bottomNavBar.CURVE_CIRCLE_RADIUS  * 2 ) + bottomNavBar.CURVE_CIRCLE_RADIUS,
                bottomNavBar.mFirstCurveEndPoint.y);


        bottomNavBar.mSecondCurveControlPoint1.set(bottomNavBar.mSecondCurveStartPoint.x
                        + (bottomNavBar.CURVE_CIRCLE_RADIUS  * 2 ) - bottomNavBar.CURVE_CIRCLE_RADIUS,
                bottomNavBar.mSecondCurveStartPoint.y);

        bottomNavBar.mSecondCurveControlPoint2.set(bottomNavBar.mSecondCurveEndPoint.x
                        - bottomNavBar.CURVE_CIRCLE_RADIUS + (bottomNavBar.CURVE_CIRCLE_RADIUS/4),
                bottomNavBar.mSecondCurveEndPoint.y);


    }

    private void drawAnimation(final VectorMasterView fab) {

        outline = fab.getPathModelByName("outline");
        outline.setStrokeColor(Color.WHITE);
        outline.setTrimPathEnd(0.0f);

        final ValueAnimator valueAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
        valueAnimator.setDuration(1000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                outline.setTrimPathEnd((float)valueAnimator.getAnimatedValue());
                fab.update();
            }
        });
        valueAnimator.start();
    }

    public void draw(int i) {
        bottomNavBar.mFirstCurveStartPoint.set((bottomNavBar.mNavigationBarWidth/i)
                - (bottomNavBar.CURVE_CIRCLE_RADIUS*2) - (bottomNavBar.CURVE_CIRCLE_RADIUS/3), 0);

        bottomNavBar.mFirstCurveEndPoint.set(bottomNavBar.mNavigationBarWidth/i,bottomNavBar.CURVE_CIRCLE_RADIUS
                + (bottomNavBar.CURVE_CIRCLE_RADIUS/4));


        bottomNavBar.mSecondCurveStartPoint = bottomNavBar.mFirstCurveEndPoint;
        bottomNavBar.mSecondCurveEndPoint.set((bottomNavBar.mNavigationBarWidth/i)
                + (bottomNavBar.CURVE_CIRCLE_RADIUS*2) + (bottomNavBar.CURVE_CIRCLE_RADIUS/3), 0);



        bottomNavBar.mFirstCurveControlPoint1.set(bottomNavBar.mFirstCurveStartPoint.x
                        + bottomNavBar.CURVE_CIRCLE_RADIUS + (bottomNavBar.CURVE_CIRCLE_RADIUS/4),
                bottomNavBar.mFirstCurveStartPoint.y);

        bottomNavBar.mFirstCurveControlPoint2.set(bottomNavBar.mFirstCurveEndPoint.x
                        - (bottomNavBar.CURVE_CIRCLE_RADIUS  * 2 ) + bottomNavBar.CURVE_CIRCLE_RADIUS,
                bottomNavBar.mFirstCurveEndPoint.y);


        bottomNavBar.mSecondCurveControlPoint1.set(bottomNavBar.mSecondCurveStartPoint.x
                        + (bottomNavBar.CURVE_CIRCLE_RADIUS  * 2 ) - bottomNavBar.CURVE_CIRCLE_RADIUS,
                bottomNavBar.mSecondCurveStartPoint.y);

        bottomNavBar.mSecondCurveControlPoint2.set(bottomNavBar.mSecondCurveEndPoint.x
                        - bottomNavBar.CURVE_CIRCLE_RADIUS + (bottomNavBar.CURVE_CIRCLE_RADIUS/4),
                bottomNavBar.mSecondCurveEndPoint.y);

    }


    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    public void snackBar (View view, String message, int duration){
        Snackbar.make(view,message,duration).show();
    }

    // Master class to make Toasts all over the activity
    private void msg (String s, int t){
        if(t == 0){
            int toastDurationInMilliSeconds = 500;
            final Toast toast = Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT);
            CountDownTimer toastCountDown;
            toastCountDown = new CountDownTimer(toastDurationInMilliSeconds, 500 /*Tick duration*/) {
                public void onTick(long millisUntilFinished) {
                    toast.show();
                }
                public void onFinish() {
                    toast.cancel();
                }
            };
            // Show the toast and starts the countdown
            toast.show();
            toastCountDown.start();

        } else if (t == 1 ){
            Toast toast = Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG);
            toast.show();
        }


    }



}
