package com.example.martin.try4;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class HomeScreen extends AppCompatActivity {

    private static final String TAG = "MyActivity";
    ArrayList<Profile> profiles;

    CustomListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Log.i(TAG, "---------------------------------------------------------------------------------------------------------");

        //deleteData();

        FloatingActionButton floatingActionButton1 = (FloatingActionButton) findViewById(R.id.fabHS);
        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(HomeScreen.this, WorkingEdit.class);
                startActivity(i);
            }
        });

        FloatingActionButton floatingActionButton2 = (FloatingActionButton) findViewById(R.id.fabHSsend);
        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent i = new Intent(HomeScreen.this, PairDevice.class);
                startActivity(i);
            }
        });


        SharedPreferences.Editor editor = getSharedPreferences("MyPrefsFile", MODE_PRIVATE).edit();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "HS RESUMED");
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
                        JSONArray jsonArrayGradient = jsonObject.getJSONArray("gradients");

                        //colors and gradients
                        ArrayList<String> profileListColors = new ArrayList<String>();
                        ArrayList<Float> profileListGradients = new ArrayList<Float>();

                        for(int j=0; j < jsonArrayColors.length(); j++) {

                            String color = jsonArrayColors.get(j).toString();
                            profileListColors.add(color);

                            float gradient = Float.parseFloat(jsonArrayGradient.get(j).toString());
                            profileListGradients.add(gradient);
                        }

                        Profile profile = new Profile(name,profileListColors,profileListGradients);
                        profiles.add(profile);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
        }

        adapter = new CustomListAdapter(this, profiles);
        ListView menuListView  = (ListView) findViewById(R.id.listViewHS);
        menuListView.setAdapter(adapter);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menudelete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        AlertDialog diaBox = AskOption();
        diaBox.show();

        return true;
    }

    void deleteData(){

        SharedPreferences.Editor editor = getSharedPreferences("MyPrefsFile", MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();
    }

    private AlertDialog AskOption()
    {
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(this)
                .setTitle("Delete")
                .setMessage("Do you wish to Delete all ?")

                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {

                        deleteData();
                        profiles.clear();
                        adapter.updateResults(profiles);
                        dialog.dismiss();
                    }

                })



                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                })
                .create();
        return myQuittingDialogBox;
    }

}
