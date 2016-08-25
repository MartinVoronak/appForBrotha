package com.example.martin.try4;

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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

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

    int position;
    float pickedGradientPosition;
    ArrayList<Float> floatArrayGradient;
    float[] result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "-------------------------------------- SP ONCREATE --------------------------------------");

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
        floatArrayGradient = profile.getGradients();
        //floatArrayGradient = new ArrayList<Float>();

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
                        Intent i = new Intent(SingleProfile.this, ColorPicker_try1.class);
                        startActivityForResult(i, REQ_CODE_CHANGE);
                        pickedPosition = position;
                    }
                }
        );


        //DELETE ITEM
        list.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> arg0, View v, int index, long arg3) {

                position=index;

                AlertDialog diaBox = AskOption();
                diaBox.show();

                return true;
            }
        });

        //ADD button
        FloatingActionButton myFab = (FloatingActionButton) findViewById(R.id.fabSP3);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent i = new Intent(SingleProfile.this, ColorPicker_try1.class);
                startActivityForResult(i, REQ_CODE_NEW); //RCN == 1
            }
        });


        //OK button
        FloatingActionButton myFabOK = (FloatingActionButton) findViewById(R.id.fabSPC);
        myFabOK.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Profile newProfile = new Profile(editTextSP.getText().toString(),arrayList,floatArrayGradient);
                profiles.set(objectID,newProfile);

                JSONArray jsonArrayDeserialization= new JSONArray();

                for (int j=0;j<profiles.size();j++)
                {
                    JSONArray JSColors = new JSONArray();
                    JSONObject JSONRootObject = new JSONObject();
                    JSONArray JSGradients = new JSONArray();

                    for (int i = 0; i < profiles.get(j).getArrayList().size(); i++){
                        JSColors.put(profiles.get(j).getArrayList().get(i));
                    }

                    for (int i = 0; i < arrayList.size(); i++){
                        JSGradients.put(floatArrayGradient.get(i).toString());
                    }

                    JSONObject js1 = new JSONObject();
                    try {
                        js1.put("name", profiles.get(j).getObjectName());
                        js1.put("colors", JSColors);
                        js1.put("gradients", JSGradients);

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
        Log.i(TAG, "-------------------------------------- SP ONRESULT --------------------------------------");

        switch(requestCode){
            case 1:

                if (resultCode == RESULT_OK){
                    pickedColor = data.getStringExtra("picked");

                    if (arrayList.size()!=0) {
                        pickedGradientPosition = (float) Float.parseFloat(data.getStringExtra("pickedGradientPostion")) / 100;
                        Log.i(TAG, "WE prenesena farba a pozicia: " + pickedColor +" "+pickedGradientPosition);
                    }
                    else {
                        pickedGradientPosition = 0;
                    }

                    arrayList.add(pickedColor);
                    floatArrayGradient.add(pickedGradientPosition);
                    list.setAdapter(cAdapter);

                }else if(resultCode == RESULT_CANCELED){
                    Log.i(TAG,"WE nepreniesla sa farba");
                }

                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    pickedColor = data.getStringExtra("picked");
                    pickedGradientPosition = (float) Float.parseFloat(data.getStringExtra("pickedGradientPostion"))/100;
                    arrayList.set(pickedPosition, pickedColor);
                    floatArrayGradient.set(pickedPosition,pickedGradientPosition);
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
                JSONArray JSGradients = new JSONArray();
                JSONObject JSONRootObject = new JSONObject();

                for (int i = 0; i < profiles.get(j).getArrayList().size(); i++) {
                    JSColors.put(profiles.get(j).getArrayList().get(i));
                    JSGradients.put(profiles.get(j).getGradients().get(i));
                }

                JSONObject js1 = new JSONObject();
                try {
                    js1.put("name", profiles.get(j).getObjectName());
                    js1.put("colors", JSColors);
                    js1.put("gradients", JSGradients);

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
        Log.i(TAG, "SP numColors: " + numColors);

        if (numColors>1){

            result = new float[numColors];
            for (int i = 0; i < numColors; i++) {
                //TODO set floatArrayGradient
                result[i] =(float) floatArrayGradient.get(i).floatValue();
                Log.i(TAG,"WE gradient position: "+result[i]);
            }

            result[numColors-1] = 1;

            for (int j=0;j<numColors;j++){

                arrColors[j] = Integer.parseInt(arrayList.get(j).toString(), 16)+0xFF000000;
                Log.i(TAG,"WE farby :" +arrColors[j]);
            }

            ShapeDrawable.ShaderFactory shaderFactory = new ShapeDrawable.ShaderFactory() {
                @Override
                public Shader resize(int width, int height) {
                    LinearGradient linearGradient = new LinearGradient(0, 0, width, height,
                            arrColors, //pouzity array farieb
                            result,
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
            layout.setBackgroundColor(Integer.parseInt(arrayList.get(0).toString(), 16)+0xFF000000);
        }
    }

    private AlertDialog AskOption()
    {
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(this)
                //set message, title, and icon
                .setTitle("Delete")
                .setMessage("Do you wish to Delete ?")

                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {

                        Log.i(TAG,"SP to delete: "+position);
                        arrayList.remove(position);
                        list.setAdapter(cAdapter);

                        setGradient();

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
