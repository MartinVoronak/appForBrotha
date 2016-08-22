package com.example.martin.try4;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class ColorPicker_try1 extends AppCompatActivity {

    private static final String TAG = "MyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_picker_try1);

        int initialColor = Color.WHITE;

        ColorPickerDialog colorPickerDialog = new ColorPickerDialog(this, initialColor, new ColorPickerDialog.OnColorSelectedListener() {

            @Override
            public void onColorSelected(int color) {
                Log.i(TAG, " vybrata farba: " + color);
            }

        });
        colorPickerDialog.show();
    }
}
