package com.example.martin.try4;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class HomeScreen extends AppCompatActivity {

    private static final String TAG = "MyActivity";
    FloatingActionMenu materialDesignFAM;
    FloatingActionButton floatingActionButton1, floatingActionButton2, floatingActionButton3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Log.i(TAG, "---------------------------------------------------------------------------------------------------------");

        materialDesignFAM = (FloatingActionMenu) findViewById(R.id.material_design_android_floating_action_menu);
        floatingActionButton1 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item1);

        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(HomeScreen.this, WorkingEdit.class);
                startActivity(i);
            }
        });

        floatingActionButton2 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item2);

        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences("MyPrefsFile", MODE_PRIVATE).edit();
                editor.clear();
                editor.commit();

            }
        });

        SharedPreferences.Editor editor = getSharedPreferences("MyPrefsFile", MODE_PRIVATE).edit();
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        Log.i(TAG,"HS RESUMED");

        SharedPreferences prefs = getSharedPreferences("MyPrefsFile", MODE_PRIVATE);
        String restoredText = prefs.getString("savedData", null);
        if (restoredText != null) {
            Log.i(TAG,"HS Savenuty JSON: "+restoredText);

            try {
                JSONObject  jsonRootObjectDeserialization = new JSONObject(restoredText);
                JSONArray jsonArrayDeserialization = jsonRootObjectDeserialization.optJSONArray("data");
                Log.i(TAG,"HS deserializacia 0: "+jsonArrayDeserialization.length());
                Log.i(TAG, "HS deserializacia 1: " + jsonArrayDeserialization.toString());

                    for(int i=0; i < jsonArrayDeserialization.length(); i++) {
                        JSONObject jsonObject = jsonArrayDeserialization.getJSONObject(i);
                        Log.i(TAG,"HS deserializacia 2: "+jsonObject.toString());

                        String name = jsonObject.optString("name").toString();
                        Log.i(TAG,"HS deserializacia name 3: "+name);

                        JSONArray jsonArrayColors = jsonObject.getJSONArray("colors");
                        Log.i(TAG,"HS deserializacia colors 3: "+jsonArrayColors.toString());

                            for(int j=0; j < jsonArrayColors.length(); j++) {
                                //JSONObject jsonColor = jsonArrayColors.getJSONObject(j);
                                //Log.i(TAG,"HS deserializacia colors 3: "+jsonColor);
                                String color = jsonArrayColors.get(j).toString();
                                Log.i(TAG,"HS deserializacia color 4: "+color);
                            }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
        }



    }
}
