package com.example.anshuman.arduinocontroller;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.widget.Toast;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;

import android.content.ActivityNotFoundException;
import android.widget.Toast;
public class MainActivity extends AppCompatActivity {

    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    public ProgressDialog progress;
    public boolean isBtConnected = false;
    TextView VoiceToWordText,TextofLight1,TextofFan,TextofAC,TextofDoor,TextofLight2;
    Button Speak;

    private final int REQ_CODE_SPEECH_INPUT = 100;

    String address = null;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent newint = getIntent();
        address = newint.getStringExtra(BtDevicesActivity.EXTRA_ADDRESS); //receive the address of the bluetooth device
        Speak = (Button) findViewById(R.id.button);
        VoiceToWordText = (TextView) findViewById(R.id.SpokenText);
        TextofLight2= (TextView) findViewById(R.id.textView10);
        TextofLight1=(TextView) findViewById(R.id.textView11);
        TextofFan= (TextView) findViewById(R.id.textView12);
        TextofAC= (TextView) findViewById(R.id.textViewAC);
        TextofDoor= (TextView) findViewById(R.id.textViewDoor);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,Main2Activity.class));
            }
        });

        Speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptSpeechInput();

            }
        });

        myBluetooth = BluetoothAdapter.getDefaultAdapter();

        if (myBluetooth == null) {
            //Show a mensag. that the device has no bluetooth adapter
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
            progress = ProgressDialog.show(MainActivity.this, "Connecting...", "Please Wait!!!");  //show a progress dialog
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
                msg("Connection Failed.\n SerialPort Bluetooth not Available? \n Check Your Devices.");
                finish();
            } else {
                msg("Device Connected.");
                isBtConnected = true;
            }
            progress.dismiss();
        }
    }

    // fast way to call Toast
    private void msg(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }


    /**
     * Showing google speech input dialog
     */
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

    /**
     * Receiving speech input
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    VoiceToWordText.setText(result.get(0));

                    String input = VoiceToWordText.getText().toString();

                    if (input.equals("show command list")) {

                        startActivity(new Intent(MainActivity.this,Command.class));

                    }

                    if (input.equals("show device menu")) {

                        startActivity(new Intent(MainActivity.this,BtDevicesActivity.class));

                    }



                    if (input.equals("connect smart home")||(input.equals("smart"))) {
                        if(!isBtConnected ){
                            new ConnectBT().execute();}
                        else {

                            Toast.makeText(getApplicationContext(),"Please Pair devices",Toast.LENGTH_LONG).show();

                        }}

                    if(input.equals("initiate Smart home")){
                        if(btSocket!=null){
                            try {
                                btSocket.getOutputStream().write("a".getBytes());
                                btSocket.getOutputStream().write("c".getBytes());
                                btSocket.getOutputStream().write("e".getBytes());
                                btSocket.getOutputStream().write("i".getBytes());
                            }catch(IOException e) {
                                msg("Success!");
                            }
                        }
                    }
                    if (input.equals("Smart home shut down")){
                        if (btSocket!=null){
                            try {
                                btSocket.getOutputStream().write("b".getBytes());
                                btSocket.getOutputStream().write("d".getBytes());
                                btSocket.getOutputStream().write("f".getBytes());
                                btSocket.getOutputStream().write("j".getBytes());
                            }catch(IOException e){
                                msg("Success!");
                            }
                        }
                    }
                    if (input.equals("second light on")||input.equals("second light on karo")) {
                        if (btSocket != null) {
                            try {
                                btSocket.getOutputStream().write("a".getBytes());
                            } catch (IOException e) {
                                msg("Success!");
                            }
                        }
                        TextofLight2.setText("The light 2 is ON");
                    }
                    if (input.equals("second light off")||input.equals("second light off karo")) {
                        if (btSocket != null) {
                            try {
                                btSocket.getOutputStream().write("b".getBytes());
                            } catch (IOException e) {
                                msg("Success!");
                            }
                        }
                        TextofLight2.setText("The light 2 is OFF");
                    }

                    if (input.equals("turn on the fan")||input.equals("fan on karo")) {
                        if (btSocket != null) {
                            try {
                                btSocket.getOutputStream().write("c".getBytes());
                            } catch (IOException e) {
                                msg("Success!");
                            }
                        }
                        TextofFan.setText("Fan is ON");
                    }
                    if (input.equals("turn off the fan")||input.equals("fan band karo")) {
                        if (btSocket != null) {
                            try {
                                btSocket.getOutputStream().write("d".getBytes());
                            } catch (IOException e) {
                                msg("Success!");
                            }
                        }
                        TextofFan.setText("Fan is OFF");
                    }
                    if (input.equals("turn on AC")||input.equals("AC on karo")) {
                        if (btSocket != null) {
                            try {
                                btSocket.getOutputStream().write("e".getBytes());
                            } catch (IOException e) {
                                msg("Success!");
                            }
                        }
                        TextofAC.setText("AC is ON");
                    }
                    if (input.equals("turn off AC")||input.equals("AC band karo")) {
                        if (btSocket != null) {
                            try {
                                btSocket.getOutputStream().write("f".getBytes());
                            } catch (IOException e) {
                                msg("Success!");
                            }
                        }
                        TextofAC.setText("AC is OFF");
                    }

                    if (input.equals("lock the door")||input.equals("Darwaza band karo")) {
                        if (btSocket != null) {
                            try {
                                btSocket.getOutputStream().write("h".getBytes());
                            } catch (IOException e) {
                                msg("Success!");
                            }
                        }
                        TextofDoor.setText("door is LOCKED");
                    }

                    if (input.equals("unlock the door")||input.equals("Darwaza kholo")) {
                        if (btSocket != null) {
                            try {
                                btSocket.getOutputStream().write("g".getBytes());
                            } catch (IOException e) {
                                msg("Success");
                            }
                        }
                        TextofDoor.setText("door is UNLOCKED");
                    }
                    if (input.equals("first light on")||input.equals("first light on karo")) {
                        if (btSocket != null) {
                            try {
                                btSocket.getOutputStream().write("i".getBytes());
                            } catch (IOException e) {
                                msg("Success!");
                            }
                        }
                        TextofLight1.setText("The Light 1 is On");
                    }

                    if (input.equals("first light off")||input.equals("first light band karo")) {
                        if (btSocket != null) {
                            try {
                                btSocket.getOutputStream().write("j".getBytes());
                            } catch (IOException e) {
                                msg("Success");
                            }
                        }
                        TextofLight1.setText("The Light 1 is Off");
                    }

                }

                break;
            }

        }
    }

}




