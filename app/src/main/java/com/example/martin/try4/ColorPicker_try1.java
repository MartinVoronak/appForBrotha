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
import android.widget.Toast;

import java.text.NumberFormat;

public class ColorPicker_try1 extends AppCompatActivity {

    private static final String TAG = "MyActivity";
    EditText eText;
    int initialColor;
    Context skuska;
    EditText eTextPosition;
    float gradientPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_picker_try1);

        skuska = this;

        //color
        eText = (EditText) findViewById(R.id.editCP);
        //gradient
        eTextPosition =(EditText) findViewById(R.id.editPositionCP);

        Intent i = getIntent();

        String color = i.getSerializableExtra("objectColor").toString();
        eText.setText(color);

        boolean decision = Boolean.valueOf(i.getSerializableExtra("decision").toString());
        Log.i(TAG,"CP decision: "+decision);
        if (decision==true){    //nova farba
            gradientPosition = 0f;
        }
        else {  //prenesena farba
            gradientPosition =Float.parseFloat(i.getSerializableExtra("objectGradient").toString())*100;
        }

        //start color
        initialColor = Color.WHITE;

        //NumberFormat.getInstance().format(gradientPosition) == oreze 20.0 na 20
        eTextPosition.setText((NumberFormat.getInstance().format(gradientPosition)));

        Button button = (Button) findViewById(R.id.buttonCP);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (checkNumber(eTextPosition.getText().toString()) && checkColor(eText.getText().toString())){
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

            }
        });

    }

    boolean checkNumber(String text){
        Log.i(TAG,"CP "+text+" is what ?");
        try {
            int num = Integer.parseInt(text);
            Log.i(TAG,"CP "+num+" is a number");

            if (num >=0 && num <=100)
                return true;
            else {
                Toast.makeText(getApplicationContext(), text+": NUMBER not between 0 and 100", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(getApplicationContext(), text+": is not a NUMBER 0 - 100", Toast.LENGTH_SHORT).show();
            Log.i(TAG,"CP "+text+" is not a number");
            return false;
        }
    }

    boolean checkColor(String text){

        if ((text.matches("[a-fA-F0-9]+")) && text.length() == 6) {
            return true;
        }
        else{
            Toast.makeText(getApplicationContext(), text+": is not a COLOR", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
