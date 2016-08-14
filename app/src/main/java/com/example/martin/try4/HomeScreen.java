package com.example.martin.try4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeScreen extends AppCompatActivity {

    private static final String TAG = "MyActivity";
    FloatingActionMenu materialDesignFAM;
    FloatingActionButton floatingActionButton1, floatingActionButton2, floatingActionButton3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        JSONObject JSONRootObject = new JSONObject();
        JSONArray jsonRootArray = new JSONArray();

        try {
            JSONArray JSColors = new JSONArray();
            JSColors.put("AAAAAA");
            JSColors.put("BBBBBB");

            JSONObject js1 = new JSONObject().put("profile1", JSColors);
            jsonRootArray.put(js1);


            JSONArray JSColors2 = new JSONArray();
            JSColors2.put("CCCCCC");
            JSColors2.put("DDDDDD");
            JSColors2.put("EEEEEE");

            JSONObject js2 = new JSONObject().put("profile2", JSColors2);
            jsonRootArray.put(js2);

            JSONRootObject.put("data",jsonRootArray);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i(TAG,"vytvoreny json "+JSONRootObject.toString());


        materialDesignFAM = (FloatingActionMenu) findViewById(R.id.material_design_android_floating_action_menu);
        floatingActionButton1 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item1);

        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(HomeScreen.this, WorkingEdit.class);
                startActivity(i);
                finish();
            }
        });
    }
}
