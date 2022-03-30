package com.example.accelerometerandlight;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SecondPage  extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        TextView txt_current2nd , txt_prev2nd;

        txt_current2nd=findViewById(R.id.txt_current2nd);
        txt_current2nd.setMovementMethod(new ScrollingMovementMethod());
        txt_prev2nd=findViewById(R.id.txt_prev2nd);
        txt_prev2nd.setMovementMethod(new ScrollingMovementMethod());

        SharedPreferences sp= getApplicationContext().getSharedPreferences("Mydata", Context.MODE_PRIVATE);
        String currentStr=sp.getString("Current Pressure", " ");
        String prevStr=sp.getString("Previous Pressure", " ");

        txt_current2nd.setText(currentStr);
        txt_prev2nd.setText(prevStr);
}
}
