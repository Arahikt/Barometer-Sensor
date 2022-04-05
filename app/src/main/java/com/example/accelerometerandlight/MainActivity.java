package com.example.accelerometerandlight;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;


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


    private int pointsPlotted = 0;
    private int graphIntervalCounter = 0;

    Button button, button1;
    SharedPreferences sp;
    String pressureStr, prevPressureStr, changeInPressureStr;

    private Viewport viewport;

    private int stepDetector;
    private TextView txt_stepDetector;

    LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[]{
//            new DataPoint(new Date().getTime(), 900),
//            new DataPoint(new Date().getTime(), 800),
//            new DataPoint(2, 3),
//            new DataPoint(3, 2),
//            new DataPoint(4, 6)
    });
    String dataWithPressure;
    private String temporary;
SimpleDateFormat sdf = new SimpleDateFormat("mm:ss ");
private Handler mHandler = new Handler();

DatabaseHandler dbh;
SQLiteDatabase sqLiteDatabase;

LineGraphSeries<DataPoint> dataseries= new LineGraphSeries<>(new DataPoint[0]);


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
        sp = getSharedPreferences("Mydata", Context.MODE_PRIVATE);
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

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND_MULTIPLE);
                String strp1 = temporary;

                intent.putExtra(Intent.EXTRA_TEXT, strp1);


                intent.setType("Application/txt");
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

        graph.getGridLabelRenderer().setNumHorizontalLabels(5);
//
//        graph.getGridLabelRenderer().setLabelFormatter(new
//                DateAsXAxisLabelFormatter(graph.getContext(), sdf));


        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(){
    @Override
    public String formatLabel(double value, boolean isValueX){
        if (isValueX){
            return sdf.format(new Date((long)value));
        }else{
            return super.formatLabel(value,isValueX);

        }
    }
});
        series.setDataPointsRadius(10);
        viewport.setYAxisBoundsManual(true);
        viewport.setXAxisBoundsManual(true);
        viewport.isScrollable();
        viewport.setScrollable(true);
        viewport.setScrollableY(true);
        viewport.setScalable(true);
        viewport.setScalableY(true);

        graph.addSeries(series);

        dbh = new DatabaseHandler(this);
sqLiteDatabase = dbh.getWritableDatabase();

        graph.addSeries(dataseries);
        graph.getGridLabelRenderer().setNumHorizontalLabels(4);
insertData();

//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                int a=0;
//            }
//        }, 1000);

//        sensorManager.registerListener(this, mPressure, 50, 1000);
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

            int index=0;
            if (temporary != null) {

                temporary +=String.valueOf(pointsPlotted)+ " , " + String.valueOf(savedPressure) + " , " + String.valueOf(changeInPressure) + "\n";
            } else {
                temporary = String.valueOf(pointsPlotted)+ " , " + String.valueOf(savedPressure) + " , " + String.valueOf(changeInPressure) + "\n";
            }
            index++;
            prog_pressureMeter.setProgress((int) changeInPressure);
            pointsPlotted +=  5;
//            series.appendData(new DataPoint(pointsPlotted, pressureCurrentValue), false, pointsPlotted + 1);
            Date date1 = new Date();
            String newDate= new Date().toString();
            String date2=null;
            try {
                date1 = new SimpleDateFormat("mm:ss").parse(newDate);

            } catch (ParseException e) {
                e.printStackTrace();
            }

            Calendar calendar = Calendar.getInstance();
            Date d1 = calendar.getTime();
            calendar.add(Calendar.SECOND, 5);
            series.appendData(new DataPoint(d1, pressureCurrentValue), true, 20);

            dataWithPressure = pressureStr + changeInPressureStr;
//            viewport.setMaxX(pointsPlotted + 0.005);
//            viewport.setMinX(pointsPlotted - 800);
            Double min = savedPressure,max=0.0;
            if(savedPressure>max){
                max= savedPressure;
            }else if(savedPressure<min){
                min = savedPressure;
            }
            
            viewport.setMaxY(max + 0.40);
            viewport.setMinY(min - 0.40);

//            viewport.setMinX(pointsPlotted);
//            viewport.setMaxY(100);
//            sensorManager.registerListener (this, mPressure, 1000, 100000);


        } else if (sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {



            stepDetector = (int) (stepDetector + sensorEvent.values[0]);

            txt_stepDetector.setText("Steps: " + String.valueOf(stepDetector));
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
    protected void onResume() {
        super.onResume();
    }
public void insertData(){
       long xValue = new Date().getTime();
       double yValue = savedPressure;
       dbh.insertDate(xValue, yValue);
       dataseries.resetData(grabData());
}
private DataPoint[] grabData(){
        String [] column ={ "xValue","yValue"};
        Cursor cursor = sqLiteDatabase.query("Table1", column, null, null, null , null, null);
    DataPoint[] dataPoints = new DataPoint[cursor.getCount()];
    for (int i=0;i<cursor.getCount();i++){
        cursor.moveToNext();
        dataPoints[i]= new DataPoint(cursor.getLong(0), cursor.getDouble(1));
    }
    return dataPoints;
}

}