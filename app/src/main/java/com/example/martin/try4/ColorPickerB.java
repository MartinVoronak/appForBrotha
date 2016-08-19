package com.example.martin.try4;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class ColorPickerB extends AppCompatActivity {

    String color1 = "0066ff";
    String color2 = "ff4000";
    String color3 = "79ff4d";
    private static final String TAG = "MyActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_picker_b);

        Log.i(TAG, "CP B");

        Button button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent i = new Intent();
                i.putExtra("picked",color1);
                setResult(RESULT_OK, i);
                finish();
            }
        });

        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent i = new Intent();
                i.putExtra("picked", color2);
                setResult(RESULT_OK, i);
                finish();
            }
        });

        Button button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent i = new Intent();
                i.putExtra("picked", color3);
                setResult(RESULT_OK, i);
                finish();
            }
        });

    }
}
