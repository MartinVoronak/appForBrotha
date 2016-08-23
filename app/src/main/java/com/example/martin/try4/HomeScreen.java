package com.example.martin.try4;


import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class HomeScreen extends AppCompatActivity {

    private static final String TAG = "MyActivity";

    ArrayList<Profile> profiles;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Log.i(TAG, "---------------------------------------------------------------------------------------------------------");

        FloatingActionButton floatingActionButton1 = (FloatingActionButton) findViewById(R.id.fabHS);
        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(HomeScreen.this, WorkingEdit.class);
                startActivity(i);
            }
        });

        SharedPreferences.Editor editor = getSharedPreferences("MyPrefsFile", MODE_PRIVATE).edit();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG,"HS RESUMED");
        String name = "";

        profiles = new ArrayList<Profile>();

        SharedPreferences prefs = getSharedPreferences("MyPrefsFile", MODE_PRIVATE);
        String restoredText = prefs.getString("savedData", null);
        if (restoredText != null) {
            Log.i(TAG, "HS Savenuty JSON: " + restoredText);

            try {
                JSONObject  jsonRootObjectDeserialization = new JSONObject(restoredText);
                JSONArray jsonArrayDeserialization = jsonRootObjectDeserialization.optJSONArray("data");

                    for(int i=0; i < jsonArrayDeserialization.length(); i++) {
                        JSONObject jsonObject = jsonArrayDeserialization.getJSONObject(i);
                        Log.i(TAG,"HS deserializacia 2: "+jsonObject.toString());


                        //name
                        name = jsonObject.optString("name").toString();
                        JSONArray jsonArrayColors = jsonObject.getJSONArray("colors");

                        //colors
                        ArrayList<String> profileListColors = new ArrayList<String>();

                        for(int j=0; j < jsonArrayColors.length(); j++) {

                            String color = jsonArrayColors.get(j).toString();
                            profileListColors.add(color);
                        }

                        Profile profile = new Profile(name,profileListColors);
                        profiles.add(profile);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
        }

        ArrayAdapter<Profile> allAdapter = new CustomAdapterProfiles(this, profiles);
        ListView menuListView = (ListView) findViewById(R.id.listViewHS);
        menuListView.setAdapter(allAdapter);

        menuListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long l) {

                        Intent i = new Intent(HomeScreen.this, SingleProfile.class);
                        i.putExtra("objectsList", profiles);
                        i.putExtra("objectID", position);
                        startActivity(i);
                    }
                }
        );


    }

}
