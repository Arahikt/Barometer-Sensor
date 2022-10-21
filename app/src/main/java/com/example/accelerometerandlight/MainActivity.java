package com.example.accelerometerandlight;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
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

import java.text.BreakIterator;


public class MainActivity extends AppCompatActivity implements SensorEventListener {


    private static final String TAG = "MainActivity";
    private SensorManager sensorManager;
    private Sensor mPressure;
    double pressureCurrentValue = 0;
    double pressurePreviousValue = 0;
    double changeInPressure = 0;
    String data;

    private static final String FILE_Name = "Data.txt";
    TextView txt_currentPressure, txt_prevPressure, txt_pressureChange;

    double savedPressure;


    private int pointsPlotted = 0;
    private final int graphIntervalCounter = 0;

    Button button, button2;

    String pressureStr, prevPressureStr, changeInPressureStr;

    private Viewport viewport;

    LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[]{
//            new DataPoint(0, 1),
//            new DataPoint(1, 5),
//            new DataPoint(2, 3),
//            new DataPoint(3, 2),
//            new DataPoint(4, 6)
    });
    String dataWithPressure;
    private String pressureReadings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button2 = findViewById(R.id.button2);
        button2.setOnClickListener(view -> {
            sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            mPressure = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
            sensorManager.registerListener(MainActivity.this, mPressure, SensorManager.SENSOR_DELAY_NORMAL);
        });

        txt_currentPressure = findViewById(R.id.txt_currentPressure);
        txt_prevPressure = findViewById(R.id.txt_prevPressure);
        txt_pressureChange = findViewById(R.id.txt_pressureChange);

        button = findViewById(R.id.button);
        button.setOnClickListener(view -> {

            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            String outputData = pressureReadings;

            intent.putExtra(Intent.EXTRA_TEXT, outputData);


            intent.setType("text/plain");
            Intent shareIntent = Intent.createChooser(intent, null);
            startActivity(shareIntent);
        });

        GraphView graph = (GraphView) findViewById(R.id.graph);
        viewport = graph.getViewport();
        viewport.setScalable(true);

        graph.getGridLabelRenderer().setVerticalAxisTitle(" ");


        graph.addSeries(series);
        viewport.setYAxisBoundsManual(true);
        viewport.setXAxisBoundsManual(true);
        viewport.setScrollable(true);
        viewport.setScrollableY(true);
//        viewport.setScalable(true);
        viewport.setScalableY(true);

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor sensor = sensorEvent.sensor;
        if (sensor.getType() == Sensor.TYPE_PRESSURE) {
            pressureCurrentValue = sensorEvent.values[0];
            pressureStr += (pressureCurrentValue + ", ");
            prevPressureStr += (pressurePreviousValue + ", ");
            changeInPressure = Math.abs(pressureCurrentValue - pressurePreviousValue);

            if (changeInPressureStr != null) {
                changeInPressureStr += String.valueOf(changeInPressure + ", ");
            } else {
                changeInPressureStr = " ";
            }
            pressurePreviousValue = pressureCurrentValue;
            txt_currentPressure.setText(String.valueOf("Current Pressure: \n" + pressureCurrentValue));
            txt_prevPressure.setText(String.valueOf("Previous Pressure:    \n" + pressurePreviousValue));

            txt_pressureChange.setText(String.valueOf("Change:  \n" + changeInPressure));

            if (pressureReadings != null) {
                pressureReadings += String.valueOf(pointsPlotted) + " , " + String.valueOf(pressureCurrentValue) + " , " + String.valueOf(changeInPressure) + "\n";
            } else {

                pressureReadings = String.valueOf(pointsPlotted) + " , " + String.valueOf(pressureCurrentValue) + " , " + 0.0 + "\n";
            }

            pointsPlotted += 1;
            series.appendData(new DataPoint(pointsPlotted, pressureCurrentValue), false, pointsPlotted + 400);

            dataWithPressure = pressureStr + changeInPressureStr;
            viewport.setMaxX(pointsPlotted + 0.005);
//            viewport.setMinX(pointsPlotted - 400);
            viewport.setMaxY(pressureCurrentValue + 0.10);
            viewport.setMinY(pressureCurrentValue - 0.10);

//            viewport.setMinX(pointsPlotted);
//            viewport.setMaxY(100);

        }
    }
//        if (sensor.getType() == Sensor.TYPE_PRESSURE) {
//            pressurePreviousValue = sensorEvent.values[0];
//
//            pressureStr += pressurePreviousValue + ", ";
//            prevPressureStr += pressurePreviousValue + ", ";
//// --------------!!!!!!!!!!!
////            pressureStr += String.valueOf(String.format("%.6f", savedPressure) + ", ");
////            prevPressureStr += String.valueOf(String.format("%.6f", pressurePreviousValue) + ", ");
//
//            changeInPressure = Math.abs(pressureCurrentValue - pressurePreviousValue);
//            if (changeInPressureStr != null) {
//                changeInPressureStr += changeInPressure + ", ";
//            } else {
//                changeInPressureStr = " ";
//            }
//
//            pressurePreviousValue = pressureCurrentValue;
//            txt_currentPressure.setText((int) pressureCurrentValue);
//            txt_prevPressure.setText((int) pressurePreviousValue);
//
//            txt_pressureChange.setText((int) changeInPressure);
//
//
//            if (temporary != null) {
//
//                temporary += pointsPlotted + " , " + pressurePreviousValue + " , " + changeInPressure + "\n";
//            } else {
//                temporary = pointsPlotted + " , " + pressurePreviousValue + " , " + changeInPressure + "\n";
//            }
////             startTime = System.currentTimeMillis();
//
////            if (pointsPlotted % 10 ==0 ){
////                endTime = System.currentTimeMillis();
////                time = endTime - startTime;
////                txt_stepDetector.setText(String.valueOf(time/1000));
////            }
//            pointsPlotted += 1;
//            series.appendData(new DataPoint(pointsPlotted, pressureCurrentValue), false, pointsPlotted + 400);
//
//            dataWithPressure = pressureStr + changeInPressureStr;
//            viewport.setMaxX(pointsPlotted + 0.005);
////            viewport.setMinX(pointsPlotted - 400);
////            viewport.setMaxY(pressurePreviousValue + 0.10);
////            viewport.setMinY(pressurePreviousValue - 0.10);
//
////            viewport.setMinX(pointsPlotted);
////            viewport.setMaxY(100);
//
//        }


    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }


}