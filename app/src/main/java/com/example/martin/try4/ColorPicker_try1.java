package com.example.martin.try4;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
    Intent i;
    String pickedPosition;
    EditText eTextPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_picker_try1);

        skuska = this;

        //start color
        initialColor = Color.WHITE;
        eText = (EditText) findViewById(R.id.editCP);
        eTextPosition =(EditText) findViewById(R.id.editPositionCP);

        Button button = (Button) findViewById(R.id.buttonCP);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ColorPickerDialog colorPickerDialog = new ColorPickerDialog(skuska, Integer.parseInt(eText.getText().toString(), 16) + 0xFF000000, new ColorPickerDialog.OnColorSelectedListener() {

                    @Override
                    public void onColorSelected(int color) {
                        Log.i(TAG, "CP my vybrata farba: " + color);

                        //CONTENT
                        Intent i = new Intent();
                        i.putExtra("picked", Integer.toHexString(color).substring(2));
                        i.putExtra("pickedGradientPostion",eTextPosition.getText().toString());
                        setResult(RESULT_OK, i);

                        finish();
                    }

                });
                colorPickerDialog.show();
            }
        });

    }
}
