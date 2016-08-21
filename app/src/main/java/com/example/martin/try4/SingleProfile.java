package com.example.martin.try4;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SingleProfile extends AppCompatActivity {

    //interface
    private static final String TAG = "MyActivity";
    Profile profile = null;
    ListView list;
    CustomAdapter cAdapter;
    TextView editTextSP;

    //objects
    int objectID;
    ArrayList<Profile> profiles;

    //colorpicker
    final static int REQ_CODE_NEW = 1;
    final static int REQ_CODE_CHANGE = 2;
    int pickedPosition;
    String pickedColor;
    ArrayList<String> arrayList;

    //gradient
    int[] arrColors;
    int numColors;
    float floatArray[];
    float scale;
    View layout;
    PaintDrawable paint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_profile);
        Log.i(TAG, "--------------SP----------------");

        Intent i = getIntent();
        this.objectID = (int) i.getSerializableExtra("objectID");
        this.profiles = (ArrayList<Profile>) i.getSerializableExtra("objectsList");
        this.profile = profiles.get(objectID);

        editTextSP = (TextView)findViewById(R.id.editTextSP);
        editTextSP.setText(profile.getObjectName());

        //color preview of one profile
        arrayList = profile.getArrayList();

        list = (ListView) findViewById(R.id.mylistViewSP);
        cAdapter = new CustomAdapter(this, arrayList);
        list.setAdapter(cAdapter);

        //-----------------------------------------------------GRADIENT-------------------------------------------------------//
        setGradient();
        //-----------------------------------------------------!GRADIENT-------------------------------------------//

        //RETAKE ITEM
        list.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                        Intent i = new Intent(SingleProfile.this, ColorPicker.class);
                        startActivityForResult(i, REQ_CODE_CHANGE);
                        pickedPosition = position;
                    }
                }
        );


        //DELETE ITEM
        list.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> arg0, View v, int index, long arg3) {

                Log.i(TAG,"SP to delete: "+index);
                arrayList.remove(index);
                list.setAdapter(cAdapter);
                return false;
            }
        });

        //ADD button
        FloatingActionButton myFab = (FloatingActionButton) findViewById(R.id.fabSP3);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent i = new Intent(SingleProfile.this, ColorPicker.class);
                startActivityForResult(i, REQ_CODE_NEW); //RCN == 1
            }
        });


        //OK button
        FloatingActionButton myFabOK = (FloatingActionButton) findViewById(R.id.fabSPC);
        myFabOK.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Profile newProfile = new Profile(editTextSP.getText().toString(),arrayList);
                profiles.set(objectID,newProfile);

                JSONArray jsonArrayDeserialization= new JSONArray();

                for (int j=0;j<profiles.size();j++)
                {
                    JSONArray JSColors = new JSONArray();
                    JSONObject JSONRootObject = new JSONObject();

                    for (int i = 0; i < profiles.get(j).getArrayList().size(); i++){
                        JSColors.put(profiles.get(j).getArrayList().get(i));
                    }

                    JSONObject js1 = new JSONObject();
                    try {
                        js1.put("name", profiles.get(j).getObjectName());
                        js1.put("colors", JSColors);

                        jsonArrayDeserialization.put(js1);

                        JSONRootObject.put("data",jsonArrayDeserialization);
                        Log.i(TAG, "vytvoreny json " + JSONRootObject.toString());

                        SharedPreferences.Editor editor = getSharedPreferences("MyPrefsFile", MODE_PRIVATE).edit();
                        editor.putString("savedData", JSONRootObject.toString());
                        editor.commit();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }


                Intent i = new Intent(SingleProfile.this, HomeScreen.class);
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch(requestCode){
            case 1:

                if (resultCode == RESULT_OK){
                    pickedColor=data.getStringExtra("picked");
                    Log.i(TAG, "prenesena farba: " + pickedColor);

                    arrayList.add(pickedColor);
                    //cAdapter = new CustomAdapter(this, arrayList);
                    list.setAdapter(cAdapter);

                }else if(resultCode == RESULT_CANCELED){
                    Log.i(TAG,"nepreniesla sa farba");
                }

                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    pickedColor = data.getStringExtra("picked");
                    arrayList.set(pickedPosition, pickedColor);
                    list.setAdapter(cAdapter);

                    Log.i(TAG, "zmena farby na poz: " + pickedPosition);
                }
                break;
        }

        //--------------------------------GRADIENT----------------------------------------------//
        setGradient();
        //-------------------------------!GRADIENT----------------------------------------------//

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menudelete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        profiles.remove(objectID);

        if (profiles.isEmpty()) {
            Log.i(TAG,"SP osamoteny vymazany");

            SharedPreferences settings = getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE);
            settings.edit().remove("savedData").commit();
        }
        else {
            JSONArray jsonArrayDeserialization = new JSONArray();

            for (int j = 0; j < profiles.size(); j++) {
                JSONArray JSColors = new JSONArray();
                JSONObject JSONRootObject = new JSONObject();

                for (int i = 0; i < profiles.get(j).getArrayList().size(); i++) {
                    JSColors.put(profiles.get(j).getArrayList().get(i));
                }

                JSONObject js1 = new JSONObject();
                try {
                    js1.put("name", profiles.get(j).getObjectName());
                    js1.put("colors", JSColors);

                    jsonArrayDeserialization.put(js1);

                    JSONRootObject.put("data", jsonArrayDeserialization);
                    Log.i(TAG, "vytvoreny json " + JSONRootObject.toString());

                    SharedPreferences.Editor editor = getSharedPreferences("MyPrefsFile", MODE_PRIVATE).edit();
                    editor.putString("savedData", JSONRootObject.toString());
                    editor.commit();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
        finish();
        return true;
    }

    public void setGradient(){
        numColors = arrayList.size();
        arrColors = new int[numColors];

        scale = (float) 1/(numColors-1);
        Log.i(TAG, "SP scale: " + scale);

        if (numColors>1){

            floatArray = new float[numColors];
            floatArray[0]=0;
            floatArray[numColors-1] = 1;

            float temp = scale;
            for (int j=1;j<numColors-1;j++){
                floatArray[j]= temp;
                temp = temp + scale;
                Log.i(TAG,"SP floatArray: "+floatArray[j]);
            }


            //profile.getArrayList().size()
            for (int j=0;j<numColors;j++){
                arrColors[j] = Integer.parseInt(profile.getArrayList().get(j).toString(), 16)+0xFF000000;
            }

            ShapeDrawable.ShaderFactory shaderFactory = new ShapeDrawable.ShaderFactory() {
                @Override
                public Shader resize(int width, int height) {
                    LinearGradient linearGradient = new LinearGradient(0, 0, width, height,
                            arrColors, //pouzity array farieb
                            floatArray,
                            Shader.TileMode.REPEAT);
                    return linearGradient;
                }
            };
            paint = new PaintDrawable();
            paint.setShape(new RectShape());
            paint.setShaderFactory(shaderFactory);

            layout = findViewById(R.id.gradient);
            layout.setBackgroundDrawable((Drawable) paint);
        }
        else if (numColors==1){

            layout = findViewById(R.id.gradient);
            layout.setBackgroundColor(Integer.parseInt(profile.getArrayList().get(0).toString(), 16)+0xFF000000);
        }
    }
}
