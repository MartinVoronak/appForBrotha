package com.example.martin.try4;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ColorPicker_try1 extends AppCompatActivity {

    private static final String TAG = "MyActivity";
    EditText eText;
    int initialColor;
    Context skuska;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_picker_try1);

        skuska = this;

        //start color
        initialColor = Color.WHITE;
        eText = (EditText) findViewById(R.id.editCP);

        ColorPickerDialog colorPickerDialog = new ColorPickerDialog(this, initialColor, new ColorPickerDialog.OnColorSelectedListener() {

            @Override
            public void onColorSelected(int color) {
                Log.i(TAG, " vybrata farba: " + color);
                //eText.setText(Integer.toHexString(color));
            }

        });
        colorPickerDialog.show();


        Button button = (Button) findViewById(R.id.buttonCP);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ColorPickerDialog colorPickerDialog = new ColorPickerDialog(skuska, Integer.parseInt(eText.getText().toString(), 16)+0xFF000000, new ColorPickerDialog.OnColorSelectedListener() {

                    @Override
                    public void onColorSelected(int color) {
                        Log.i(TAG, " vybrata farba: " + color);
                    }

                });
                colorPickerDialog.show();
            }
        });


    }
}
