package com.example.accelerometerandlight;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private static final String TAG = "MainActivity";
    private SensorManager sensorManager;
    private Sensor linearAccelerometer, accelerometer, mGyro, mLight, mPressure, mStepDetector;

    double pressureCurrentValue = 0;
    double pressurePreviousValue = 0;
    double changeInPressure = 0;
    String data;

    private static final String FILE_Name = "Data.txt";
    ProgressBar prog_pressureMeter;
    TextView txt_currentPressure, txt_prevPressure, txt_pressureChange;
    TextView xValue, yValue, zValue;
    TextView linearXValue, linearYValue, linearZValue;
    TextView xGyroValue, yGyroValue, zGyroValue;

    TextView light;
    TextView actualPressure;
    double savedPressure;

    long startTime=0;
    long endTime=0;
    long time=0;

    private int pointsPlotted = 0;
    private int graphIntervalCounter = 0;

    Button button, button1;
    SharedPreferences sp;
    String pressureStr, prevPressureStr, changeInPressureStr;

    private Viewport viewport;

    private int stepDetector;
    private TextView txt_stepDetector;

    LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[]{
//            new DataPoint(0, 1),
//            new DataPoint(1, 5),
//            new DataPoint(2, 3),
//            new DataPoint(3, 2),
//            new DataPoint(4, 6)
    });
    String dataWithPressure;
    private String temporary;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        xValue = (TextView) findViewById(R.id.xValue);
        yValue = (TextView) findViewById(R.id.yValue);
        zValue = (TextView) findViewById(R.id.zValue);

        linearXValue = (TextView) findViewById(R.id.linearXValue);
        linearYValue = (TextView) findViewById(R.id.linearYValue);
        linearZValue = (TextView) findViewById(R.id.linearZValue);

        xGyroValue = (TextView) findViewById(R.id.xGyroValue);
        yGyroValue = (TextView) findViewById(R.id.yGyroValue);
        zGyroValue = (TextView) findViewById(R.id.zGyroValue);
        txt_stepDetector = findViewById(R.id.txt_stepDetector);

//        light = (TextView) findViewById(R.id.light);
        actualPressure = (TextView) findViewById(R.id.actualPressure);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(MainActivity.this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        linearAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        sensorManager.registerListener(MainActivity.this, linearAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        mGyro = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensorManager.registerListener(MainActivity.this, mGyro, SensorManager.SENSOR_DELAY_NORMAL);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

//        mLight = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
//        sensorManager.registerListener(MainActivity.this, mLight, SensorManager.SENSOR_DELAY_NORMAL);

        mStepDetector = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        sensorManager.registerListener(MainActivity.this, mStepDetector, SensorManager.SENSOR_DELAY_NORMAL);

        txt_stepDetector.setText("Steps: " + String.valueOf(stepDetector));

        mPressure = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        sensorManager.registerListener(MainActivity.this, mPressure, SensorManager.SENSOR_DELAY_NORMAL);

        txt_currentPressure = findViewById(R.id.txt_currentPressure);
        txt_prevPressure = findViewById(R.id.txt_prevPressure);
        txt_pressureChange = findViewById(R.id.txt_pressureChange);
        prog_pressureMeter = findViewById(R.id.prog_pressureMeter);

        button = findViewById(R.id.button);
        button1 = findViewById(R.id.button1);
//        sp = getSharedPreferences("Mydata", Context.MODE_PRIVATE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                SharedPreferences.Editor editor= sp.edit();
//                editor.putString("Current Pressure", pressureStr);
//                editor.putString("Previous Pressure", prevPressureStr);
////                editor.commit();
//                String temp_str = pressureStr;
//                FileOutputStream outputStream = null;
//
//                String temp_str = "";
//                temp_str += pressureStr  + pressureChangeStr;
////                temp_str += pressureChangeStr;
//

//                            Bundle extras = new Bundle();
////                intent.setAction(Intent.ACTION_SEND);
//                String strp1="text1";
//                String strp2="text2";
//                            extras.putString("PRESSURE",strp1);
//                            extras.putString("CHANGE", strp2);
//            Intent intent = new Intent();
//                            intent.putExtras(extras);
//
////                                Intent shareIntent = Intent.createChooser(intent, null);
//                                startActivity(intent);
///

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                String strp1 = temporary;

                intent.putExtra(Intent.EXTRA_TEXT, strp1);


                intent.setType("text/plain");
                Intent shareIntent = Intent.createChooser(intent, null);
                startActivity(shareIntent);
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SecondPage.class);
                startActivity(intent);
            }
        });

        //Shared preferences
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                SharedPreferences.Editor editor= sp.edit();
//                editor.putString("Current Pressure", pressureStr);
//                editor.putString("Previous Pressure", prevPressureStr);
//                editor.commit();
//                Toast.makeText(MainActivity.this, "Pressure Saved.", Toast.LENGTH_LONG).show();
//
//            }
//        });
//        button1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent= new Intent(MainActivity.this, SecondPage.class);
//                startActivity(intent);
//
//            }
//        });


        GraphView graph = (GraphView) findViewById(R.id.graph);
        viewport = graph.getViewport();
        viewport.setScalable(true);

        graph.getGridLabelRenderer().setVerticalAxisTitle(" ");


        graph.addSeries(series);
        viewport.setYAxisBoundsManual(true);
        viewport.setXAxisBoundsManual(true);
        viewport.isScrollable();
        viewport.setScrollable(true);
        viewport.setScrollableY(true);
        viewport.setScalable(true);
        viewport.setScalableY(true);
        startTime = System.currentTimeMillis();

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor sensor = sensorEvent.sensor;
        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            xValue.setText("X value: " + sensorEvent.values[0]);
            yValue.setText("Y value: " + sensorEvent.values[1]);
            zValue.setText("Z value: " + sensorEvent.values[2]);
        } else if (sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            linearXValue.setText("Linear X value: " + sensorEvent.values[0]);
            linearYValue.setText("Linear Y value: " + sensorEvent.values[1]);
            linearZValue.setText("Linear Z value: " + sensorEvent.values[2]);
        } else if (sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            xGyroValue.setText("X Gyro value: " + sensorEvent.values[0]);
            yGyroValue.setText("Y Gyro value: " + sensorEvent.values[1]);
            zGyroValue.setText("Z Gyro value: " + sensorEvent.values[2]);
        }
//        else if (sensor.getType() == Sensor.TYPE_LIGHT) {
//            light.setText("Light: " + sensorEvent.values[0]);
//        }
        else if (sensor.getType() == Sensor.TYPE_PRESSURE) {
            savedPressure = sensorEvent.values[0];
            actualPressure.setText("Pressure: " + sensorEvent.values[0]);

            pressureCurrentValue = (savedPressure);

            pressureStr += String.valueOf(savedPressure + ", ");
            prevPressureStr += String.valueOf(pressurePreviousValue + ", ");
// --------------!!!!!!!!!!!
//            pressureStr += String.valueOf(String.format("%.6f", savedPressure) + ", ");
//            prevPressureStr += String.valueOf(String.format("%.6f", pressurePreviousValue) + ", ");

            changeInPressure = Math.abs(pressureCurrentValue - pressurePreviousValue);
            if (changeInPressureStr != null) {
                changeInPressureStr += String.valueOf(changeInPressure + ", ");
            } else {
                changeInPressureStr = " ";
            }

            pressurePreviousValue = pressureCurrentValue;
            txt_currentPressure.setText("Current: \n" + pressureCurrentValue);
            txt_prevPressure.setText("prev:    \n" + pressurePreviousValue);

            txt_pressureChange.setText("Change:  \n" + changeInPressure);


            if (temporary != null) {

                temporary +=String.valueOf(pointsPlotted)+ " , " + String.valueOf(savedPressure) + " , " + String.valueOf(changeInPressure) + "\n";
            } else {
                temporary = String.valueOf(pointsPlotted)+ " , " + String.valueOf(savedPressure) + " , " + String.valueOf(changeInPressure) + "\n";
            }
            prog_pressureMeter.setProgress((int) changeInPressure);
//             startTime = System.currentTimeMillis();

            if (pointsPlotted== 100 ||pointsPlotted==200){
                endTime = System.currentTimeMillis();
                time = endTime - startTime;
                txt_stepDetector.setText(String.valueOf(time));
            }
            pointsPlotted +=  1;
            series.appendData(new DataPoint(pointsPlotted, pressureCurrentValue), false, pointsPlotted + 400);

            dataWithPressure = pressureStr + changeInPressureStr;
            viewport.setMaxX(pointsPlotted + 0.005);
//            viewport.setMinX(pointsPlotted - 400);
            viewport.setMaxY(savedPressure + 0.10);
            viewport.setMinY(savedPressure - 0.10);

//            viewport.setMinX(pointsPlotted);
//            viewport.setMaxY(100);

        } else if (sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {

            stepDetector = (int) (stepDetector + sensorEvent.values[0]);

            txt_stepDetector.setText("Steps: " + String.valueOf(stepDetector));
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }


}