package com.example.martin.try4;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class AddNewProfile extends AppCompatActivity {

    final static int REQ_CODE = 1;
    private static final String TAG = "MyActivity";
    String pickedColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_profile);

        EditText eText = (EditText) findViewById(R.id.editText);
        eText.clearFocus();
        FloatingActionButton myFab = (FloatingActionButton) findViewById(R.id.fab2);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(AddNewProfile.this, ColorPicker.class);
                startActivityForResult(i,REQ_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // TODO Auto-generated method stub

        if(requestCode == REQ_CODE){

            if (resultCode == RESULT_OK){
                pickedColor=data.getStringExtra("picked");
                Log.i(TAG,"prenesena farba: "+pickedColor);

                //TODO whatever zou want to do after comeback


            }else if(resultCode == RESULT_CANCELED){
                Log.i(TAG,"nepreniesla sa farba");
            }
        }

    }
}