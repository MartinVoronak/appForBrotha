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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    //float scale;
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
                    i.putExtra("decision",true);       //1 = nova farba, nemusim tahat z intentu gradient position
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
                        i.putExtra("decision",false);       //0 = musim tahat z intentu v CP gradient
                        i.putExtra("objectGradient", floatArrayGradient.get(position));
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
                        Log.i(TAG,"create JSON gradient: "+floatArrayGradient.get(i).toString());
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
            case 1:     //new color

                if (resultCode == RESULT_OK){
                    pickedColor = data.getStringExtra("picked");

                    pickedGradientPosition = (float) Float.parseFloat(data.getStringExtra("pickedGradientPostion")) / 100;
                    Log.i(TAG, "WE prenesena farba a pozicia: " + pickedColor +" "+pickedGradientPosition);

                    sortOrder();
                    list.setAdapter(cAdapter);

                }else if(resultCode == RESULT_CANCELED){
                    Log.i(TAG,"WE nepreniesla sa farba");
                }

                break;
            case 2:     //retake color
                if (resultCode == RESULT_OK) {
                    pickedColor = data.getStringExtra("picked");
                    pickedGradientPosition = (float) Float.parseFloat(data.getStringExtra("pickedGradientPostion"))/100;

                    sortOrderColorExists();
                    list.setAdapter(cAdapter);

                    Log.i(TAG, "zmena farby na poz: " + pickedPosition);
                }else if(resultCode == RESULT_CANCELED){
                    Log.i(TAG,"WE nepreniesla sa farba");
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


        Log.i(TAG, "WE numColors: " + numColors);

        if (numColors>1){
            if (floatArrayGradient.get(0) != 0 && floatArrayGradient.get(floatArrayGradient.size()-1)==1){
                Log.i(TAG, "WE in condition not 0 and 100: " + numColors);

                arrColors = new int[numColors+1];
                result = new float[numColors+1];

                result[0] =(float) 0;
                for (int i = 0; i < numColors; i++) {
                    result[i+1] =(float) floatArrayGradient.get(i).floatValue();
                }

                arrColors[0] = Integer.parseInt(arrayList.get(0).toString(), 16)+0xFF000000;
                for (int j=0;j<numColors;j++){
                    arrColors[j+1] = Integer.parseInt(arrayList.get(j).toString(), 16)+0xFF000000;
                }

                for (int j=0;j<numColors+1;j++) {
                    Log.i(TAG, "WE farby :" + arrColors[j]);
                    Log.i(TAG,"WE gradient position: "+result[j]);
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
            else if (floatArrayGradient.get(0) == 0 && floatArrayGradient.get(floatArrayGradient.size()-1)!=1){
                    Log.i(TAG, "WE in condition not 0 and 100: " + numColors);

                    arrColors = new int[numColors+1];
                    result = new float[numColors+1];

                    for (int i = 0; i < numColors; i++) {
                        result[i] =(float) floatArrayGradient.get(i).floatValue();
                    }
                    result[numColors] =(float) 1;

                    for (int j=0;j<numColors;j++){
                        arrColors[j] = Integer.parseInt(arrayList.get(j).toString(), 16)+0xFF000000;
                    }
                    arrColors[numColors] = Integer.parseInt(arrayList.get(numColors-1).toString(), 16)+0xFF000000;

                    for (int j=0;j<numColors+1;j++) {
                        Log.i(TAG, "WE farby :" + arrColors[j]);
                        Log.i(TAG,"WE gradient position: "+result[j]);
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
            else if (floatArrayGradient.get(0) != 0 && floatArrayGradient.get(numColors-1) != 1){

                Log.i(TAG, "WE in condition not 0 and 100: " + numColors);

                arrColors = new int[numColors+2];
                result = new float[numColors+2];

                    result[0] =(float) 0;
                    for (int i = 0; i < numColors; i++) {
                        result[i+1] =(float) floatArrayGradient.get(i).floatValue();
                    }
                    result[numColors+1] = 1;


                    arrColors[0] = Integer.parseInt(arrayList.get(0).toString(), 16)+0xFF000000;
                    for (int j=0;j<numColors;j++){
                        arrColors[j+1] = Integer.parseInt(arrayList.get(j).toString(), 16)+0xFF000000;
                    }
                    arrColors[numColors+1] = Integer.parseInt(arrayList.get(numColors-1).toString(), 16)+0xFF000000;

                for (int j=0;j<numColors+2;j++) {
                    Log.i(TAG, "WE farby :" + arrColors[j]);
                    Log.i(TAG,"WE gradient position: "+result[j]);
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
            else {
                arrColors = new int[numColors];
                result = new float[numColors];

                for (int i = 0; i < numColors; i++) {

                    result[i] =(float) floatArrayGradient.get(i).floatValue();
                    Log.i(TAG,"SP gradient position: "+result[i]);
                }

                //result[numColors-1] = 1;

                for (int j=0;j<numColors;j++){

                    arrColors[j] = Integer.parseInt(arrayList.get(j).toString(), 16)+0xFF000000;
                    Log.i(TAG,"SP farby :" +arrColors[j]);
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
            }
        else {
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

    void sortOrder(){
        Log.i(TAG,"WE arraylist size: "+arrayList.size());

        if (arrayList.isEmpty()){
            floatArrayGradient.add(pickedGradientPosition);
            arrayList.add(pickedColor);
        }
        else if (floatArrayGradient.contains(pickedGradientPosition)){
            //toast could not add a color, since it has same position as another
            Toast.makeText(getApplicationContext(), "Color at that position already exists !", Toast.LENGTH_LONG).show();
        }
        else {
            floatArrayGradient.add(pickedGradientPosition);
            arrayList.add(pickedColor);
            concurrentSort(floatArrayGradient,floatArrayGradient,arrayList);
        }
    }

    void sortOrderColorExists(){        //0_A 20_B 50_C 100_D
        arrayList.remove(pickedPosition);
        floatArrayGradient.remove(pickedPosition);
        floatArrayGradient.add(pickedGradientPosition);
        arrayList.add(pickedColor);
        concurrentSort(floatArrayGradient,floatArrayGradient,arrayList);
    }

    public static <T extends Comparable<T>> void concurrentSort(
            final List<T> key, List<?>... lists){
        // Create a List of indices
        List<Integer> indices = new ArrayList<Integer>();
        for(int i = 0; i < key.size(); i++)
            indices.add(i);

        // Sort the indices list based on the key
        Collections.sort(indices, new Comparator<Integer>(){
            @Override public int compare(Integer i, Integer j) {
                return key.get(i).compareTo(key.get(j));
            }
        });

        // Create a mapping that allows sorting of the List by N swaps.
        // Only swaps can be used since we do not know the type of the lists
        Map<Integer,Integer> swapMap = new HashMap<Integer, Integer>(indices.size());
        List<Integer> swapFrom = new ArrayList<Integer>(indices.size()),
                swapTo   = new ArrayList<Integer>(indices.size());
        for(int i = 0; i < key.size(); i++){
            int k = indices.get(i);
            while(i != k && swapMap.containsKey(k))
                k = swapMap.get(k);

            swapFrom.add(i);
            swapTo.add(k);
            swapMap.put(i, k);
        }

        // use the swap order to sort each list by swapping elements
        for(List<?> list : lists)
            for(int i = 0; i < list.size(); i++)
                Collections.swap(list, swapFrom.get(i), swapTo.get(i));
    }
}
