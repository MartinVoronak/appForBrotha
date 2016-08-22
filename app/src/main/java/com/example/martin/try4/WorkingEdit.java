package com.example.martin.try4;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
    int pickedPosition;
    final static int REQ_CODE_NEW = 1;
    final static int REQ_CODE_CHANGE = 2;

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
        setContentView(R.layout.activity_working_edit);

        profileArray = new ArrayList<Profile>();

            FloatingActionButton myFab = (FloatingActionButton) findViewById(R.id.fabWE);
            myFab.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent i = new Intent(WorkingEdit.this, ColorPicker.class);
                    startActivityForResult(i, REQ_CODE_NEW);
                }
            });

        list = (ListView) findViewById(R.id.mylistView);
        arrayList = new ArrayList<String>();
        cAdapter = new CustomAdapter(this, arrayList);
        list.setAdapter(cAdapter);

        list.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                        Intent i = new Intent(WorkingEdit.this, ColorPicker.class);
                        startActivityForResult(i, REQ_CODE_CHANGE);
                        pickedPosition = position;
                    }
                }
        );

        list.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    public boolean onItemLongClick(AdapterView<?> arg0, View v, int index, long arg3) {

                        Log.i(TAG,"SP to delete: "+index);
                        arrayList.remove(index);
                        list.setAdapter(cAdapter);

                        setGradient();
                        return false;
                    }
                });


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
    }

    @Override
    public void onBackPressed()
    {
        Intent i = new Intent(this, HomeScreen.class);
        startActivity(i);
        finish();
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
                arrColors[j] = Integer.parseInt(arrayList.get(j).toString(), 16)+0xFF000000;
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

            layout = findViewById(R.id.gradientAddProfile);
            layout.setBackgroundDrawable((Drawable) paint);
        }
        else if (numColors==1){

            layout = findViewById(R.id.gradientAddProfile);
            layout.setBackgroundColor(Integer.parseInt(arrayList.get(0).toString(), 16)+0xFF000000);
        }
    }
}
