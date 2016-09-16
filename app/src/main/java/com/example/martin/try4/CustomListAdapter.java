package com.example.martin.try4;

import android.content.Context;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;


//HOMESCREEN
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

    public void updateResults(ArrayList<Profile> results) {
        items = results;
        //Triggers the list update
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.customrow, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // get current item to be displayed
        Profile currentItem = (Profile) getItem(position);
        viewHolder.itemName.setText(currentItem.getObjectName());


        int numColors = currentItem.getArrayList().size();
        final int[] arrColors;
        final float [] result;

        if (numColors > 1) {
            if (currentItem.getGradients().get(0) != 0 && currentItem.getGradients().get(currentItem.getGradients().size()-1)==1){

                arrColors = new int[numColors+1];
                result = new float[numColors+1];

                result[0] =(float) 0;
                for (int i = 0; i < numColors; i++) {
                    result[i+1] =(float) currentItem.getGradients().get(i).floatValue();
                }

                arrColors[0] = Integer.parseInt(currentItem.getArrayList().get(0).toString(), 16)+0xFF000000;
                for (int j=0;j<numColors;j++){
                    arrColors[j+1] = Integer.parseInt(currentItem.getArrayList().get(j).toString(), 16)+0xFF000000;
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
                PaintDrawable paint = new PaintDrawable();
                paint.setShape(new RectShape());
                paint.setShaderFactory(shaderFactory);

                paint.setCornerRadius(100);

                viewHolder.itemName.setBackgroundDrawable(paint);
            }
            else if (currentItem.getGradients().get(0) == 0 && currentItem.getGradients().get(currentItem.getGradients().size()-1)!=1){

                arrColors = new int[numColors+1];
                result = new float[numColors+1];

                for (int i = 0; i < numColors; i++) {
                    result[i] =(float) currentItem.getGradients().get(i).floatValue();
                }
                result[numColors] =(float) 1;

                for (int j=0;j<numColors;j++){
                    arrColors[j] = Integer.parseInt(currentItem.getArrayList().get(j).toString(), 16)+0xFF000000;
                }
                arrColors[numColors] = Integer.parseInt(currentItem.getArrayList().get(numColors-1).toString(), 16)+0xFF000000;

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
                PaintDrawable paint = new PaintDrawable();
                paint.setShape(new RectShape());
                paint.setShaderFactory(shaderFactory);

                paint.setCornerRadius(100);

                viewHolder.itemName.setBackgroundDrawable(paint);
            }
            else if (currentItem.getGradients().get(0) != 0 && currentItem.getGradients().get(numColors-1) != 1){

                arrColors = new int[numColors+2];
                result = new float[numColors+2];

                result[0] =(float) 0;
                for (int i = 0; i < numColors; i++) {
                    result[i+1] =(float) currentItem.getGradients().get(i).floatValue();
                }
                result[numColors+1] = 1;


                arrColors[0] = Integer.parseInt(currentItem.getArrayList().get(0).toString(), 16)+0xFF000000;
                for (int j=0;j<numColors;j++){
                    arrColors[j+1] = Integer.parseInt(currentItem.getArrayList().get(j).toString(), 16)+0xFF000000;
                }
                arrColors[numColors+1] = Integer.parseInt(currentItem.getArrayList().get(numColors-1).toString(), 16)+0xFF000000;

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
                PaintDrawable paint = new PaintDrawable();
                paint.setShape(new RectShape());
                paint.setShaderFactory(shaderFactory);

                paint.setCornerRadius(100);

                viewHolder.itemName.setBackgroundDrawable(paint);
            }
            else {
                arrColors = new int[numColors];
                result = new float[numColors];

                for (int i = 0; i < numColors; i++) {

                    result[i] =(float) currentItem.getGradients().get(i).floatValue();
                }

                //result[numColors-1] = 1;

                for (int j=0;j<numColors;j++){

                    arrColors[j] = Integer.parseInt(currentItem.getArrayList().get(j).toString(), 16)+0xFF000000;
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

                PaintDrawable paint = new PaintDrawable();
                paint.setShape(new RectShape());
                paint.setShaderFactory(shaderFactory);

                paint.setCornerRadius(100);

                viewHolder.itemName.setBackgroundDrawable(paint);
            }
        }
        else if (numColors == 1) {

            arrColors = new int[2];
            arrColors[0] = Integer.parseInt(currentItem.getArrayList().get(0).toString(), 16) + 0xFF000000;
            arrColors[1] = Integer.parseInt(currentItem.getArrayList().get(0).toString(), 16) + 0xFF000000;

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

            paint.setCornerRadius(100);

            viewHolder.itemName.setBackgroundDrawable(paint);
        }
        else {
            viewHolder.itemName.setText("empty object");
        }

        return convertView;
    }

    private class ViewHolder {
        TextView itemName;

        public ViewHolder(View view) {
            itemName = (TextView) view.findViewById(R.id.listID);
        }
    }
}
