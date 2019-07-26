package com.example.anshuman.arduinocontroller;

import android.content.Intent;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.content.Intent;
import android.speech.RecognizerIntent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;


import android.content.ActivityNotFoundException;
import android.widget.Toast;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ToggleButton;

public class Main2Activity extends AppCompatActivity

        implements NavigationView.OnNavigationItemSelectedListener
{
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    public ProgressDialog progress;
    public boolean isBtConnected = false;
    TextView Fan,Light1,Light2,AC,Door,All;
    ImageView Bt1,Sh1,Bt2,brow;
    ToggleButton t1,t2,t3,t4,t5,t6;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    String address = null;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        t1 = (ToggleButton) findViewById(R.id.toggleButton);
        t2 = (ToggleButton)findViewById(R.id.toggleButton2);
        t3 = (ToggleButton)findViewById(R.id.toggleButton3);
        t4 = (ToggleButton)findViewById(R.id.toggleButton4);
        t5 = (ToggleButton)findViewById(R.id.toggleButton5);
        t6 = (ToggleButton)findViewById(R.id.toggleButton6);
        brow=(ImageView)findViewById(R.id.imageView9);
        Bt1  = (ImageView) findViewById(R.id.imageView7);
        Bt2  = (ImageView) findViewById(R.id.imageView9);
        Sh1 = (ImageView) findViewById(R.id.imageView8);
        Light1= (TextView) findViewById(R.id.textView7);
        Light2= (TextView) findViewById(R.id.textView6);
        Fan= (TextView) findViewById(R.id.textView5);
        All =(TextView) findViewById(R.id.textView9);
        Door= (TextView) findViewById(R.id.textView4);
        AC= (TextView) findViewById(R.id.textView3);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Intent newint = getIntent();
        address = newint.getStringExtra(BtDevicesActivity.EXTRA_ADDRESS); //receive the address of the bluetooth device
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Main2Activity.this, MainActivity.class));
            }
        });
        brow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent browserIntent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://io.adafruit.com/anxhuman/dashboards/home-automation"));
                startActivity(browserIntent);
            }
        });

        Bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Main2Activity.this, BtDevicesActivity1.class));

            }
        });
        Sh1.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(!isBtConnected ){
                    new ConnectBT().execute();}
                t5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            if (btSocket != null) {
                                try {
                                    btSocket.getOutputStream().write("a".getBytes());
                                } catch (IOException e) {
                                    msg("Success!");
                                }
                            }
                            Light2.setText("The Light1 is ON");
                        } else {
                            if (btSocket != null) {
                                try {
                                    btSocket.getOutputStream().write("b".getBytes());
                                } catch (IOException e) {
                                    msg("Success!");
                                }
                            }
                            Light2.setText("The Light1 is OFF");
                        }
                    }
                });
                t6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            if (btSocket != null) {
                                try {
                                    btSocket.getOutputStream().write("a".getBytes());
                                    btSocket.getOutputStream().write("c".getBytes());
                                    btSocket.getOutputStream().write("e".getBytes());
                                    btSocket.getOutputStream().write("i".getBytes());
                                } catch (IOException e) {
                                    msg("Success!");
                                }
                            }
                            t1.setChecked(true);
                            t2.setChecked(true);
                            t3.setChecked(true);
                            t5.setChecked(true);
                            Light2.setText("The Light is ON");
                            AC.setText("AC is ON");
                            Light1.setText("Light1 is ON");
                            Fan.setText("Fan is ON");
                            All.setText("All Devices are ON");
                        } else {
                            if (btSocket != null) {
                                try {
                                    btSocket.getOutputStream().write("b".getBytes());
                                    btSocket.getOutputStream().write("d".getBytes());
                                    btSocket.getOutputStream().write("f".getBytes());
                                    btSocket.getOutputStream().write("j".getBytes());
                                } catch (IOException e) {
                                    msg("Success!");
                                }
                            }
                            t1.setChecked(false);
                            t2.setChecked(false);
                            t3.setChecked(false);
                            t5.setChecked(false);
                            All.setText("All Devices are OFF");
                            Light2.setText("The Light is OFF");
                            AC.setText("AC is OFF");
                            Light1.setText("Light1 is OFF");
                            Fan.setText("Fan is OFF");
                        }
                    }
                });
                t4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {

                            if (btSocket != null) {
                                try {
                                    btSocket.getOutputStream().write("g".getBytes());
                                } catch (IOException e) {
                                    msg("Success!");
                                }
                            }
                            Door.setText("door is UNLOCKED");

                        } else {
                            if (btSocket != null) {
                                try {
                                    btSocket.getOutputStream().write("h".getBytes());
                                } catch (IOException e) {
                                    msg("Success");
                                }
                            }
                            Door.setText("door is LOCKED");
                        }
                    }
                });

                t3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            if (btSocket != null) {
                                try {
                                    btSocket.getOutputStream().write("c".getBytes());
                                } catch (IOException e) {
                                    msg("Success!");
                                }
                            }
                            Fan.setText("Fan is ON");
                        } else {
                            if (btSocket != null) {
                                try {
                                    btSocket.getOutputStream().write("d".getBytes());
                                } catch (IOException e) {
                                    msg("Success!");
                                }
                            }
                            Fan.setText("Fan is OFF");
                        }
                    }
                });
                t1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            if (btSocket != null) {
                                try {
                                    btSocket.getOutputStream().write("e".getBytes());
                                } catch (IOException e) {
                                    msg("Success!");
                                }
                            }
                            AC.setText("AC is ON");
                        } else {
                            if (btSocket != null) {
                                try {
                                    btSocket.getOutputStream().write("f".getBytes());
                                } catch (IOException e) {
                                    msg("Success!");
                                }
                            }
                            AC.setText("AC is OFF");
                        }
                    }
                });
                t2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked)
                        {
                            if (btSocket != null) {
                                try {
                                    btSocket.getOutputStream().write("i".getBytes());
                                } catch (IOException e) {
                                    msg("Success!");
                                }
                            }
                            Light1.setText("Light1 is ON");
                        }
                        else
                        {
                            if (btSocket != null) {
                                try {
                                    btSocket.getOutputStream().write("j".getBytes());
                                } catch (IOException e) {
                                    msg("Success!");
                                }
                            }
                            Light1.setText("Light1 is OFF");
                        }
                    }
                });
            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        myBluetooth = BluetoothAdapter.getDefaultAdapter();

        if (myBluetooth == null) {
            //Show a message. that the device has no bluetooth adapter
            Toast.makeText(getApplicationContext(), "Bluetooth Device Not Available", Toast.LENGTH_LONG).show();

            //finish apk
            finish();
        }
        else if (!myBluetooth.isEnabled()) {
            //Ask to the user turn the bluetooth on
            Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnBTon, 1);
        }}


    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(Main2Activity.this, "Connecting...", "Please Wait!!!");  //show a progress dialog
        }

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try {
                if (btSocket == null || !isBtConnected) {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();//start connection
                }
            } catch (IOException e) {
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            if (!ConnectSuccess) {
                msg("/Connection Failed.\n SerialPort Bluetooth not Available? \n Check Your Devices.");
                finish();
            } else {
                msg("Device Connected.");
                isBtConnected = true;
            }
            progress.dismiss();
        }
    }

    private void msg(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()){

            case R.id.action_settings:
                Intent intent = new Intent(this,Main23Activity.class);
                startActivity(intent);
                break;
            case R.id.help:
                Intent intent1 = new Intent(this,Main24Activity.class);
                startActivity(intent1);
            case R.id.exit:
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
                break;
        }
      return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
