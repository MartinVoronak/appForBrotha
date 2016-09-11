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

        Log.i(TAG,"SP gradient positions: "+floatArrayGradient.toString());

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
                        i.putExtra("objectColor", arrayList.get(position));
                        i.putExtra("decision",false);
                        i.putExtra("objectGradient", floatArrayGradient.get(position));
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
                Log.i(TAG,"SP longclick position: "+position);

                AlertDialog diaBox = AskOptionRemoveColor();
                diaBox.show();

                return true;
            }
        });

        //ADD button
        FloatingActionButton myFab = (FloatingActionButton) findViewById(R.id.fabSP3);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent i = new Intent(SingleProfile.this, ColorPicker_try1.class);
                i.putExtra("objectColor", "ffffff");
                i.putExtra("decision",true);
                startActivityForResult(i, REQ_CODE_NEW); //RCN == 1
            }
        });


        //OK button
        FloatingActionButton myFabOK = (FloatingActionButton) findViewById(R.id.fabSPC);        //TODO check after removing one gradient
        myFabOK.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Profile newProfile = new Profile(editTextSP.getText().toString(),arrayList,floatArrayGradient);

                Log.i(TAG,"SP before retake of color: "+" colors  "+profiles.get(objectID).getArrayList().toString() +"  gradients  "+ profiles.get(objectID).getGradients().toString());
                profiles.set(objectID, newProfile);
                Log.i(TAG, "SP after retake of color: " + " colors  " + profiles.get(objectID).getArrayList().toString() + "  gradients  " + profiles.get(objectID).getGradients().toString());

                Log.i(TAG,"SP object colors: " + newProfile.getArrayList().toString());
                Log.i(TAG,"SP object gradient positions: "+newProfile.getGradients().toString());

                JSONArray jsonArrayDeserialization= new JSONArray();

                for (int j=0;j<profiles.size();j++)
                {
                    JSONArray JSColors = new JSONArray();
                    JSONObject JSONRootObject = new JSONObject();
                    JSONArray JSGradients = new JSONArray();

                    for (int i = 0; i < profiles.get(j).getArrayList().size(); i++){
                        JSColors.put(profiles.get(j).getArrayList().get(i));
                        JSGradients.put(profiles.get(j).getGradients().get(i));
                    }

                    JSONObject js1 = new JSONObject();
                    try {
                        js1.put("name", profiles.get(j).getObjectName());
                        js1.put("colors", JSColors);
                        js1.put("gradients", JSGradients);

                        jsonArrayDeserialization.put(js1);

                        JSONRootObject.put("data",jsonArrayDeserialization);
                        Log.i(TAG, "SP vytvoreny json " + JSONRootObject.toString());

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {     //TODO sorting added color in wrong order
        Log.i(TAG, "-------------------------------------- SP ONRESULT --------------------------------------");

        switch(requestCode){
            case 1:

                if (resultCode == RESULT_OK){
                    pickedColor = data.getStringExtra("picked");

                    if (arrayList.size()!=0) {
                        pickedGradientPosition = (float) Float.parseFloat(data.getStringExtra("pickedGradientPostion")) / 100;
                        Log.i(TAG, "SP prenesena farba a pozicia: " + pickedColor +" "+pickedGradientPosition);
                    }
                    else {
                        pickedGradientPosition = 0;
                    }

                    /*
                    arrayList.add(pickedColor);
                    floatArrayGradient.add(pickedGradientPosition);
                    */

                    //call sort1
                    sortOrder();

                    list.setAdapter(cAdapter);

                }else if(resultCode == RESULT_CANCELED){
                    Log.i(TAG,"SP nepreniesla sa farba");
                }

                break;
            case 2:
                if (resultCode == RESULT_OK) {

                    pickedColor = data.getStringExtra("picked");
                    pickedGradientPosition = (float) Float.parseFloat(data.getStringExtra("pickedGradientPostion"))/100;

                    /*
                    arrayList.set(pickedPosition, pickedColor);
                    floatArrayGradient.set(pickedPosition,pickedGradientPosition);
                    */
                    sortOrderColorExists();

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

        AlertDialog diaBox = AskOptionDelteProfile();
        diaBox.show();

        return true;
    }

    public void setGradient(){
        numColors = arrayList.size();
        arrColors = new int[numColors];

        Log.i(TAG, "SP numColors: " + numColors);

        if (numColors>1){

            result = new float[numColors];
            for (int i = 0; i < numColors; i++) {

                result[i] =(float) floatArrayGradient.get(i).floatValue();
                Log.i(TAG,"SP gradient position: "+result[i]);
            }

            result[numColors-1] = 1;

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

            layout = findViewById(R.id.gradient);
            layout.setBackgroundDrawable((Drawable) paint);
        }
        else if (numColors==1){

            layout = findViewById(R.id.gradient);
            layout.setBackgroundColor(Integer.parseInt(arrayList.get(0).toString(), 16)+0xFF000000);
        }
    }

    private AlertDialog AskOptionRemoveColor()
    {
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(this)
                //set message, title, and icon
                .setTitle("Delete")
                .setMessage("Do you wish to Delete ?")

                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {

                        Log.i(TAG, "SP to delete: " + position);
                        arrayList.remove(position);

                        //was missing deletion of float gradient
                        floatArrayGradient.remove(position);
                        Log.i(TAG,"SP float gradients after remove: "+floatArrayGradient.toString());

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

    private AlertDialog AskOptionDelteProfile()
    {
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(this)
                //set message, title, and icon
                .setTitle("Delete")
                .setMessage("Do you wish to Delete Profile ?")

                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        deleteCurrentProfile();
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

    void deleteCurrentProfile(){
        profiles.remove(objectID);

        if (profiles.isEmpty()) {
            Log.i(TAG,"SP osamoteny, vymazany");

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
        Collections.sort(indices, new Comparator<Integer>() {
            @Override
            public int compare(Integer i, Integer j) {
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
