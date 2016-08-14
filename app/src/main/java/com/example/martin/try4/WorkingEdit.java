package com.example.martin.try4;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class WorkingEdit extends AppCompatActivity {

    final static int REQ_CODE = 1;
    private static final String TAG = "MyActivity";
    String pickedColor;
    ArrayAdapter<String> adapter;
    ArrayList<String> arrayList;
    ListView list;
    CustomAdapter cAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_working_edit);

        FloatingActionButton myFab = (FloatingActionButton) findViewById(R.id.fabWE);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(WorkingEdit.this, ColorPicker.class);
                startActivityForResult(i, REQ_CODE);
            }
        });

        list = (ListView) findViewById(R.id.mylistView);
        arrayList = new ArrayList<String>();

        cAdapter = new CustomAdapter(this, arrayList);
        list.setAdapter(cAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // TODO Auto-generated method stub

        if(requestCode == REQ_CODE){

            if (resultCode == RESULT_OK){
                pickedColor=data.getStringExtra("picked");
                Log.i(TAG, "prenesena farba: " + pickedColor);

                // this line adds the data of your EditText and puts in your array
                arrayList.add(pickedColor);
                // next thing you have to do is check if your adapter has changed
                //cAdapter.notifyDataSetChanged();
                list.setAdapter(cAdapter);


            }else if(resultCode == RESULT_CANCELED){
                Log.i(TAG,"nepreniesla sa farba");
            }
        }

    }
}
