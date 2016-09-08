package com.example.martin.try4;

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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class WorkingEdit extends AppCompatActivity {

    //final static int REQ_CODE = 1;
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

    int position;
    float pickedGradientPosition;
    ArrayList<Float> floatArrayGradient;
    float[] result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_working_edit);

        profileArray = new ArrayList<Profile>();
        floatArrayGradient = new ArrayList<Float>();

            FloatingActionButton myFab = (FloatingActionButton) findViewById(R.id.fabWE);
            myFab.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent i = new Intent(WorkingEdit.this, ColorPicker_try1.class);
                    i.putExtra("objectColor", "ffffff");
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
                        Intent i = new Intent(WorkingEdit.this, ColorPicker_try1.class);
                        i.putExtra("objectColor", arrayList.get(position));
                        startActivityForResult(i, REQ_CODE_CHANGE);
                        pickedPosition = position;
                    }
                }
        );

        list.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    public boolean onItemLongClick(AdapterView<?> arg0, View v, int index, long arg3) {

                        position=index;

                        AlertDialog diaBox = AskOption();
                        diaBox.show();

                        return true;
                    }
                });


            FloatingActionButton myFabComfirm = (FloatingActionButton) findViewById(R.id.fabWE2);
            myFabComfirm.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    EditText editText = (EditText) findViewById(R.id.editText2);
                    Log.i(TAG,"WE nazov :"+editText.getText().toString());

                    JSONArray JSColors = new JSONArray();
                    JSONArray JSGradients = new JSONArray();

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

                    for (int i = 0; i < arrayList.size(); i++){
                        JSGradients.put(floatArrayGradient.get(i).toString());
                    }

                    try {
                        //json object to be saved
                        JSONObject js1 = new JSONObject();
                        js1.put("name", editText.getText().toString());
                        js1.put("colors", JSColors);
                        js1.put("gradients", JSGradients);

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
        Log.i(TAG, "SP numColors: " + numColors);

        if (numColors>1){

            result = new float[numColors];
            for (int i = 0; i < numColors; i++) {
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

            layout = findViewById(R.id.gradientAddProfile);
            layout.setBackgroundDrawable((Drawable) paint);
        }
        else if (numColors==1){

            layout = findViewById(R.id.gradientAddProfile);
            layout.setBackgroundColor(Integer.parseInt(arrayList.get(0).toString(), 16)+0xFF000000);
        }
    }

    private AlertDialog AskOption()
    {
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(this)
                //set message, title, and icon
                .setTitle("Delete")
                .setMessage("Do you wish to Delete ?")
               // .setIcon(R.drawable.delete)

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menudelete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        AlertDialog diaBox = AskOptionCancel();
        diaBox.show();

    return true;
    }

    private AlertDialog AskOptionCancel()
    {
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(this)
                //set message, title, and icon
                .setTitle("Dismiss")
                .setMessage("Do you wish to Dismiss Profile ?")

                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        finish();
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
