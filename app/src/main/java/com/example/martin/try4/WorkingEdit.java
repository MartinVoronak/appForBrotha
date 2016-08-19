package com.example.martin.try4;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class WorkingEdit extends AppCompatActivity {

    final static int REQ_CODE = 1;
    private static final String TAG = "MyActivity";
    String pickedColor;
    ArrayAdapter<String> adapter;
    ArrayList<String> arrayList;
    ListView list;
    CustomAdapter cAdapter;
    ArrayList <Profile> profileArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_working_edit);

        profileArray = new ArrayList<Profile>();

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

        //---------------------------------------------------

        //----------------------------------------------------


            FloatingActionButton myFabComfirm = (FloatingActionButton) findViewById(R.id.fabWE2);
            myFabComfirm.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    EditText editText = (EditText) findViewById(R.id.editText2);
                    Log.i(TAG,"WE nazov :"+editText.getText().toString());

                    JSONArray JSColors = new JSONArray();
                    JSONObject JSONRootObject = new JSONObject();

                    JSONArray jsonArrayDeserialization= new JSONArray();


                            try {
                                SharedPreferences prefs = getSharedPreferences("MyPrefsFile", MODE_PRIVATE);
                                String restoredText = prefs.getString("savedData", null);

                                if (restoredText!=null){
                                    JSONObject  jsonRootObjectDeserialization = new JSONObject(restoredText);
                                    jsonArrayDeserialization = jsonRootObjectDeserialization.optJSONArray("data");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                    for (int i = 0; i < arrayList.size(); i++){
                        JSColors.put(arrayList.get(i));
                    }

                    try {
                        //json object to be saved
                        JSONObject js1 = new JSONObject();
                        js1.put("name", editText.getText().toString());
                        js1.put("colors", JSColors);
                        //jsonroot array to be modified
                        jsonArrayDeserialization.put(js1);

                        JSONRootObject.put("data",jsonArrayDeserialization);
                        Log.i(TAG, "vytvoreny json " + JSONRootObject.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    SharedPreferences.Editor editor = getSharedPreferences("MyPrefsFile", MODE_PRIVATE).edit();
                    editor.putString("savedData", JSONRootObject.toString());
                    editor.commit();


                    Intent i = new Intent(WorkingEdit.this, HomeScreen.class);
                    startActivity(i);
                    finish();
                }
            });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // TODO Auto-generated method stub

        if(requestCode == REQ_CODE){

            if (resultCode == RESULT_OK){
                pickedColor=data.getStringExtra("picked");
                Log.i(TAG, "prenesena farba: " + pickedColor);

                arrayList.add(pickedColor);
                list.setAdapter(cAdapter);



            }else if(resultCode == RESULT_CANCELED){
                Log.i(TAG,"nepreniesla sa farba");
            }
        }

    }

    @Override
    public void onBackPressed()
    {
        Intent i = new Intent(this, HomeScreen.class);
        startActivity(i);
        finish();
    }
}
