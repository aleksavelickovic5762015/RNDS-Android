package com.example.gioia.rnds1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;



import com.example.gioia.http.MyHttpRequest;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Timer;

public class MainActivity extends AppCompatActivity {

    //main app declaration
    TextView UserName;
    TextView UserStatus;
    Spinner ChangeStatus;
    Button goToSettings;
    Button noiseControl;
    Button noiseControlOther;
    Button disturbing_button;
    final long DURATA_TIMER=1000;//10mila millisecondi, 10sec.
    private Handler m_handler = new Handler();
    //microphone declaration
    //TextView mStatusView;
    TextView mStatusView_1sec;
    MediaRecorder mRecorder;
    Thread runner;
    private static double mEMA = 0.0;
    static final private double EMA_FILTER = 0.6;
    final Runnable updater = new Runnable(){

        public void run(){
            updateTv();
        };
    };
    final Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Sele: Get the references to the layout widgets
        UserName = findViewById(R.id.ShowUser);
        UserStatus = findViewById(R.id.ShowStatus);
        goToSettings = findViewById(R.id.Button_Settings);
        ChangeStatus = findViewById(R.id.spinner_availablestatuses);
        noiseControl = findViewById(R.id.Noise_control);
        noiseControlOther = findViewById(R.id.Noise_control_other);
        disturbing_button = findViewById(R.id.disturbing_button);

        new AsyncTask<Void, Void, UserStatus>() {

            @Override
            protected UserStatus doInBackground(Void... voids) {
                try {
                    if (MyHttpRequest.getUserStatus().get(0) != null)
                        return MyHttpRequest.getUserStatus().get(0);
                    else
                        return null;
                } catch (Throwable t) {
                    t.printStackTrace();
                    return null;

                }
            }

            @Override
            protected void onPostExecute(UserStatus userStatus) {
                super.onPostExecute(userStatus);
                if (userStatus != null) {
                    UserName.setText(userStatus.getUserName());
                    UserStatus.setText(userStatus.getCurrentStatus());
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "HTTP PROBLEM WITH USERDATA", Toast.LENGTH_LONG);
                    toast.show();
                }
            }

        }.execute();

        new AsyncTask<Void, Void, List<Statuses>>() {

            @Override
            protected List<Statuses> doInBackground(Void... voids) {
                try {
                    if (MyHttpRequest.getAvailableStatuses() != null)
                        return MyHttpRequest.getAvailableStatuses();
                    else
                        return null;
                } catch (Throwable t) {
                    t.printStackTrace();
                    return null;

                }
            }

            @Override
            protected void onPostExecute(List<Statuses> statuses) {
                super.onPostExecute(statuses);
                if (statuses != null) {
                    ArrayAdapter<Statuses> dataAdapter = new ArrayAdapter<Statuses>(getApplicationContext(), android.R.layout.simple_spinner_item, statuses);
                    ChangeStatus.setAdapter(dataAdapter);

                    ChangeStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            final Statuses selected = (Statuses) parent.getItemAtPosition(position);
                            ////
                            new AsyncTask<Void, Void, Statuses>() {

                                @Override
                                protected Statuses doInBackground(Void... voids) {
                                    try {
                                        MyHttpRequest.putUserStatus(selected);
                                        return null;
                                    } catch (Throwable t) {
                                        t.printStackTrace();
                                        return null;

                                    }
                                }

                                @Override
                                protected void onPostExecute(Statuses selected2) {
                                    //super.onPostExecute(selected);
                                    if (selected != null) {
                                        UserStatus.setText(selected.toString());
                                    } else {
                                        Toast toast = Toast.makeText(getApplicationContext(), "USERSTATUS", Toast.LENGTH_LONG);
                                        toast.show();
                                    }
                                }
                            }.execute();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "HTTP PROBLEM ON SPINNER STATUSES", Toast.LENGTH_LONG);
                    toast.show();
                }
            }

        }.execute();

        m_handler = new Handler();
        m_handler.postDelayed(time_over, DURATA_TIMER);


        goToSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), StatusSettings.class);
                startActivity(intent);

            }
        });
        noiseControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        ///////////////////////////////////////////////////////////////////////////
        ////microphone part
        ///////////////////////////////////////////////////////////////////////////
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {    //this,

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},                               //activity()
                    200); //BuildDev.RECORD_AUDIO is public static final int RECORD_AUDIO = 0;

        } else {
            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            //setContentView(R.layout.activity_main);                 //Sele:not needed - as embedded in main app


            //mStatusView = (TextView) findViewById(R.id.status);     //I suppose status is INT type    (TextView)
            mStatusView_1sec = (TextView) findViewById(R.id.status_1sec);
            if (runner == null) {
                runner = new Thread() {
                    public void run() {
                        while (runner != null) {
                            try {
                                Thread.sleep(50);  //1000
                                Log.i("Noise", "Tock");
                            } catch (InterruptedException e) {
                            }
                            ;
                            mHandler.post(updater);
                        }
                    }
                };
                runner.start();
                Log.d("Noise", "start runner()");
            }
        }
    }

    private Runnable time_over = new Runnable() {
        @Override
        public void run() {
            //Toast t=new Toast(MainActivity.this);
            //t.makeText(MainActivity.this, "AGGIORNO LO STATO", Toast.LENGTH_LONG).show();
            new AsyncTask<Void, Void, List<NoiseLevel>>(){

                @Override
                protected List<NoiseLevel> doInBackground(Void... voids) {
                    try{
                        return MyHttpRequest.getNoiseLevel();
                        }
                    catch(Throwable t){
                        t.printStackTrace();
                        return null;
                    }
                }

                @Override
                protected void onPostExecute(List<NoiseLevel> noiseLevel){
                    super.onPostExecute(noiseLevel);
                    if(noiseLevel != null){
                        //setto il valore dell'utente che sta usando l'app nel tasto grande
                        Log.i("test", "ID: = " + noiseLevel.get(0).getUserID() + " LEVEL = " + noiseLevel.get(0).getNoiseLevel());
                        String a = noiseLevel.get(0).getNoiseLevel();
                        if (a.equals("4")){
                            noiseControl.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
                            noiseControl.setText("your noise preference: ok with any noise");
                        }
                        else if (a.equals("3")){
                            noiseControl.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_light));
                            noiseControl.setText("your noise preference: ok with hight noise");
                        }
                        else if (a.equals("2")){
                            noiseControl.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_dark));
                            noiseControl.setText("your noise preference: ok with medium noise");
                        }
                        else if (a.equals("1")){
                            noiseControl.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                            noiseControl.setText("your noise preference: no noise, please");
                        }
                        else
                            noiseControl.setText("SOME PROBLEM WITH DATA: got " + a);
                        //setto il valore dell'utente che sta usando l'app nel tasto grande
                        String b = noiseLevel.get(1).getNoiseLevel();
                        if (b.equals("4")){
                            noiseControlOther.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
                            noiseControlOther.setText("flatmate preference: ok with any noise");
                        }
                        else if (b.equals("3")){
                            noiseControlOther.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_light));
                            noiseControlOther.setText("flatmate preference: ok with hight noise");
                        }
                        else if (b.equals("2")){
                            noiseControlOther.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_dark));
                            noiseControlOther.setText("flatmate preference: ok with medium noise");
                        }
                        else if (b.equals("1")){
                            noiseControlOther.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                            noiseControlOther.setText("flatmate preference: no noise, please");
                        }
                        else
                            noiseControlOther.setText("SOME PROBLEM WITH DATA: got " + b);
                        //faccio sapere all'utente se sta disturbando
                        if(noiseLevel.get(0).getIsDisturbing()==0){
                            disturbing_button.setText("OK! YOU ARE NOT DISTURBING");
                            disturbing_button.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
                        }
                        else if (noiseLevel.get(0).getIsDisturbing()==1){
                            disturbing_button.setText("PLEASE, LOWER YOUR NOISE");
                            disturbing_button.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                        }
                        else {
                            disturbing_button.setText("DATA PROBLEM");
                            disturbing_button.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                        }
                            ;
                    }else{
                        Toast toast = Toast.makeText(getApplicationContext(), "HTTP PROBLEM WITH NOISE", Toast.LENGTH_LONG);
                        toast.show();
                    }

                    m_handler.postDelayed(time_over, DURATA_TIMER);
                }

            }.execute();
        }
    };
    //////////////////////////////////////////////////////////////////////
    //Microphone functions
    /////////////////////////////////////////////////////////////////
    public void onResume()
    {
        super.onResume();
        startRecorder();
    }

    public void onPause()
    {
        super.onPause();
        stopRecorder();
    }

    public void startRecorder(){
        if (mRecorder == null)
        {
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.setOutputFile("/dev/null");
            try
            {
                mRecorder.prepare();
            }catch (java.io.IOException ioe) {
                android.util.Log.e("[Monkey]", "IOException: " +
                        android.util.Log.getStackTraceString(ioe));

            }catch (java.lang.SecurityException e) {
                android.util.Log.e("[Monkey]", "SecurityException: " +
                        android.util.Log.getStackTraceString(e));
            }
            try
            {
                mRecorder.start();
            }catch (java.lang.SecurityException e) {
                android.util.Log.e("[Monkey]", "SecurityException: " +
                        android.util.Log.getStackTraceString(e));
            }

            //mEMA = 0.0;
        }

    }
    public void stopRecorder() {
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        }
    }

    double SUM_VALUE_dB = 0;
    double dB_VALUE = 0;
    int i = 0;
    public void updateTv(){
        //mStatusView.setText(Double.toString((getAmplitudeEMA())) + " dB");
        double VALUE_dB = soundDb();

        //mStatusView.setText(Double.toString(VALUE_dB) + " dB");    //getAmplitude(); soundDb()
        if(VALUE_dB < 0)
            VALUE_dB=0;
        SUM_VALUE_dB += VALUE_dB;

        if(i > 99){
            dB_VALUE=Math.round(SUM_VALUE_dB/100);
            mStatusView_1sec.setText(Double.toString(dB_VALUE) + " dB");  //10    //print average dB after five measurments
            new AsyncTask<Void, Void, UserNoiseCheck>() {

                @Override
                protected UserNoiseCheck doInBackground(Void... voids) {
                    try {
                        MyHttpRequest.putUserNoiseCheck(dB_VALUE);
                        return null;
                    } catch (Throwable t) {
                        t.printStackTrace();
                        return null;
                    }
                }
            }.execute();
            SUM_VALUE_dB = 0;
            i = 0;
        }
        else{
            i++;
        }

    }
    public double soundDb(){    //double ampl
        //return  20 * Math.log10(getAmplitudeEMA() / amp);
        return  20 * Math.log10((getAmplitude() / 51805.5336) / 0.0002);
    }
    public double getAmplitude() {
        if (mRecorder != null)
            return  (mRecorder.getMaxAmplitude());
        else
            return 0;

    }

}