package com.example.jayanthrajakumar.arduinoglassassistant;

import android.content.BroadcastReceiver;
import android.location.LocationManager ;
import android.content.Intent;
import android.location.Location;
import android.os.Handler;
import android.os.Message;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.content.IntentFilter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;
import android.support.v4.app.ActivityCompat;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.location.LocationRequest;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {
    int req = 1;
    static boolean ist1running,ist2running,isnotifmode;
    int req2 = 1;
    String prevstr="Default";
    static String notif4dash="Default";
    static String notif5dash="Default";
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private double currentLatitude;
    private double currentLongitude;
    Location src = new Location("");
    private TextView txtView;
    private static final String TAG = "BluetoothActivity";
    Location dloc = new Location("");
    // MAC address of remote Bluetooth device
    // Replace this with the address of your own module
    private final String address = "20:16:07:04:81:81";
    Timer timernav = new Timer();
    MyTask tasknav = new MyTask();

    Timer timernot = new Timer();
    MyTask2 tasknot = new MyTask2();

    // The thread that does all the work
    BluetoothThread btt;

    // Handler for writing messages to the Bluetooth connection
    Handler writeHandler;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
     //   Drive.DriveApi.newDriveContents(getGoogleApiClient()).setResultCallback(driveContentsCallback);
        ist1running=false;
        ist2running=false;
        timernot.schedule(tasknot, 0, 5000);
        timernav.schedule(tasknav, 0, 5000);
    }


    public void popup(View view) {
        // Toast.makeText(getApplicationContext(), "Hello World", Toast.LENGTH_SHORT).show();

        Button get_place = (Button) findViewById(R.id.button);
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        Intent intent;
        try {
            intent = builder.build(MainActivity.this);
            startActivityForResult(intent, req);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == req) {
            if (resultCode == RESULT_OK) {
            }
            Place place = PlacePicker.getPlace(data, this);
            //String Address=String.format("Place: %s",);
            LatLng loc = place.getLatLng();

            Location dest = new Location("");
            dest.setLatitude(loc.latitude);
            dest.setLongitude(loc.longitude);
            dloc=dest;

            // try {

            LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);


            src = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);


            TextView br_txt = (TextView) findViewById(R.id.textView2);
            br_txt.setText(String.format("%.2f", src.bearingTo(dest)));

            TextView dst_txt = (TextView) findViewById(R.id.textView);
            dst_txt.setText(String.format("%.2f", src.distanceTo(dest) / 1000));


            // Toast.makeText(getApplicationContext(), Double.toString(src.bearingTo(dest)), Toast.LENGTH_SHORT).show();
            // Toast.makeText(getApplicationContext(), Double.toString(src.distanceTo(dest)), Toast.LENGTH_SHORT).show();
            // } catch (SecurityException e) {

            //}

        }
    }


    public void refresh_fn(View view) {
        Log.v(TAG, "Connect button pressed.");

        // Only one thread at a time
        if (btt != null) {
            Log.w(TAG, "Already connected!");
            return;
        }

        // Initialize the Bluetooth thread, passing in a MAC address
        // and a Handler that will receive incoming messages
        btt = new BluetoothThread(address, new Handler() {

            @Override
            public void handleMessage(Message message) {

                String s = (String) message.obj;

                // Do something with the message
                if (s.equals("CONNECTED")) {
                    Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_SHORT).show();
                } else if (s.equals("DISCONNECTED")) {
                    Toast.makeText(getApplicationContext(),"Disconnected", Toast.LENGTH_SHORT).show();
                } else if (s.equals("CONNECTION FAILED")) {
                    Toast.makeText(getApplicationContext(),"Connection Failed", Toast.LENGTH_SHORT).show();
                    btt = null;
                } else {
                    Toast.makeText(getApplicationContext(),s, Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Get the handler that is used to send messages
        writeHandler = btt.getWriteHandler();

        // Run the thread
        btt.start();

        Toast.makeText(getApplicationContext(),"Connecting", Toast.LENGTH_SHORT).show();
    }

    public void notifs_fn(View view) {
        Intent myIntent = new Intent(this, NotifsActivity.class);
        startActivity(myIntent);
    }

    public void notif_toast(View view) {
         Toast.makeText(getApplicationContext(),  notif5dash, Toast.LENGTH_LONG).show();
       //Toast.makeText(getApplicationContext(),  ((MyApplication) this.getApplication()).notif2, Toast.LENGTH_SHORT).show();
    }

    public void send_fn(View view) {
        Log.v(TAG, "Write button pressed.");


        if(!ist1running) {



            ist1running=true;

        }
        else
        {

            ist1running=false;
        }





    }


    public void snd_fn(View view) {

        Log.v(TAG, "Write button pressed.");



        if(!ist2running) {



            ist2running=true;

        }
        else
        {
            ist2running=false;
            //timernot.cancel();
            //timernot.purge();
        }









    }

    public void txt_fn(View view) {

        EditText et2 =(EditText) findViewById(R.id.editText2);
        String sx=et2.getText().toString();


        char[] bigs=new  char[159];
        int kmax=sx.length(),k=0;
        for(int i=0;i<159;i++)
        {
            if(i==15||i==31||i==47||i==63||i==79||i==95||i==111||i==127||i==143){
                bigs[i]=',';
            }
            else
            {
                if(k<kmax)
                {
                    bigs[i]=sx.charAt(k);
                    k++;
                }
                else
                {
                    bigs[i]=' ';
                }

            }

        }

        String txt0=new String(bigs);
        String txt1 = "1," + txt0;
       // Toast.makeText(getApplicationContext(),txt1, Toast.LENGTH_LONG).show();



        Message msg = Message.obtain();
        msg.obj = txt1;
        writeHandler.sendMessage(msg);



        /*
        Timer timer = new Timer();
        MyTask task = new MyTask();
        if(ist1running=false) {


            timer.schedule(task, 0, 1000);
            ist1running=true;
        }

        if(ist1running=true) {
            timer.cancel();
            ist1running=false;
        }

        */
    }


    class MyTask extends TimerTask {
        @Override
        public void run() {

            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if(ist1running==true)
                    {
                        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
                        src = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);


                        TextView br_txt = (TextView) findViewById(R.id.textView2);
                        br_txt.setText(String.format("%06.2f", src.bearingTo(dloc)));

                        TextView dst_txt = (TextView) findViewById(R.id.textView);
                        dst_txt.setText(String.format("%06.2f", src.distanceTo(dloc) / 1000));



                        String data = "3," + br_txt.getText().toString() + "," + dst_txt.getText().toString();

                        Message msg = Message.obtain();
                        msg.obj = data;
                        writeHandler.sendMessage(msg);
                        Toast.makeText(getApplicationContext(),  data, Toast.LENGTH_SHORT).show();
                    }

                  // Toast.makeText(getApplicationContext(),  "Valar Morghulis", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }



    class MyTask2 extends TimerTask {
        @Override
        public void run() {

            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    char[] nnd=notif4dash.toCharArray();

                    if(notif4dash!="Default" &&  ist2running==true )
                    {
                        String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());

                        int k=98;
                        int kmax=currentDateTimeString.length();
                        for(int i=0;i<15;i++)
                        {
                            if(i<kmax)
                            {
                                nnd[k]=currentDateTimeString.charAt(i);
                                k++;
                            }
                            else
                            {
                                nnd[k]=' ';
                                k++;
                            }


                        }
                        String notif5dash=new String(nnd);
                       Log.i("packagename",notif5dash);
                        Message msg = Message.obtain();
                        msg.obj = notif5dash;
                        writeHandler.sendMessage(msg);


                       // prevstr=notif4dash;
                    }

                }
            });
        }

    }




}