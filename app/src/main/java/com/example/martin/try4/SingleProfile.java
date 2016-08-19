package com.example.martin.try4;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SingleProfile extends AppCompatActivity {

    private static final String TAG = "MyActivity";
    Profile profile = null;
    ListView list;
    CustomAdapter cAdapter;
    int objectID;
    ArrayList<Profile> profiles;

    final static int REQ_CODE_NEW = 1;
    String pickedColor;
    ArrayList<String> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_profile);
        Log.i(TAG,"--------------SP----------------");

        Intent i = getIntent();
        this.profile = (Profile) i.getSerializableExtra("objectKEY");
        this.objectID = (int) i.getSerializableExtra("objectID");
        this.profiles = (ArrayList<Profile>) i.getSerializableExtra("objectsList");

        TextView editTextSP = (TextView)findViewById(R.id.editTextSP);
        editTextSP.setText(profile.getObjectName());

        list = (ListView) findViewById(R.id.mylistViewSP);
        cAdapter = new CustomAdapter(this, profile.getArrayList());
        list.setAdapter(cAdapter);

        arrayList = profile.getArrayList();



        FloatingActionButton myFab = (FloatingActionButton) findViewById(R.id.fabSP3);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(SingleProfile.this, ColorPickerB.class);
                startActivityForResult(i, REQ_CODE_NEW);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // TODO Auto-generated method stub

        if(requestCode == REQ_CODE_NEW){

            if (resultCode == RESULT_OK){
                pickedColor=data.getStringExtra("picked");
                Log.i(TAG, "prenesena farba: " + pickedColor);

                arrayList.add(pickedColor);
                //list.setAdapter(cAdapter);  //used to be like this
                cAdapter = new CustomAdapter(this, arrayList);
                list.setAdapter(cAdapter);

            }else if(resultCode == RESULT_CANCELED){
                Log.i(TAG,"nepreniesla sa farba");
            }
        }

    }
}
