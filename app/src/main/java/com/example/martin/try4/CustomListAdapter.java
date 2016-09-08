package com.example.martin.try4;

import android.content.Context;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomListAdapter extends BaseAdapter {
    private Context context; //context
    private ArrayList<Profile> items; //data source of the list adapter

    //public constructor
    public CustomListAdapter(Context context, ArrayList<Profile> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size(); //returns total of items in the list
    }

    @Override
    public Object getItem(int position) {
        return items.get(position); //returns list item at the specified position
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // inflate the layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.customrow, parent, false);
        }

        // get current item to be displayed
        Profile currentItem = (Profile) getItem(position);

        // get the TextView for item name and item description
        TextView textViewItemName = (TextView) convertView.findViewById(R.id.listID);

        textViewItemName.setText(currentItem.getObjectName());

        int numColors = currentItem.getArrayList().size();


        if (numColors > 1) {

            int[] arrColors = new int[numColors];

            //positions of colors defined by user
            final float[] result = new float[numColors];
            for (int a = 0; a < numColors; a++) {
                result[a] = (float) currentItem.getGradients().get(a);
            }

            //make sure user didnt write error values (not fixed yet)
            result[0] = 0;
            result[numColors - 1] = 1;

            //colors
            for (int j = 0; j < numColors; j++) {
                arrColors[j] = Integer.parseInt(currentItem.getArrayList().get(j).toString(), 16) + 0xFF000000;
            }

            final int[] finalArrColors = arrColors;

            ShapeDrawable.ShaderFactory shaderFactory = new ShapeDrawable.ShaderFactory() {
                @Override
                public Shader resize(int width, int height) {
                    LinearGradient linearGradient = new LinearGradient(0, 0, width, height,
                            finalArrColors, //pouzity array farieb
                            result,
                            Shader.TileMode.REPEAT);
                    return linearGradient;
                }
            };
            PaintDrawable paint = new PaintDrawable();
            paint.setShape(new RectShape());
            paint.setShaderFactory(shaderFactory);

            textViewItemName.setBackgroundDrawable(paint);
        }
        else if (numColors == 1) {

            int[] arrColors = new int[2];
            arrColors[0] = Integer.parseInt(currentItem.getArrayList().get(0).toString(), 16) + 0xFF000000;
            arrColors[1] = Integer.parseInt(currentItem.getArrayList().get(0).toString(), 16) + 0xFF000000;


            float[] result = new float[numColors];

            result = new float[2];
            result[0]=0;
            result[1]=1;

            final float[] finalResult = result;
            final int[] finalArrColors1 = arrColors;

            ShapeDrawable.ShaderFactory shaderFactory = new ShapeDrawable.ShaderFactory() {
                @Override
                public Shader resize(int width, int height) {
                    LinearGradient linearGradient = new LinearGradient(0, 0, width, height,
                            finalArrColors1, //pouzity array farieb
                            finalResult,
                            Shader.TileMode.REPEAT);
                    return linearGradient;
                }
            };
            PaintDrawable paint = new PaintDrawable();
            paint.setShape(new RectShape());
            paint.setShaderFactory(shaderFactory);

            textViewItemName.setBackgroundDrawable(paint);
        }
        else {
            textViewItemName.setText("empty object");
        }

        return convertView;
    }
}
