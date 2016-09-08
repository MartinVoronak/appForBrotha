package com.example.martin.try4;


import android.content.Context;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.PorterDuff;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;


//HOMESCREEN
public class CustomAdapterProfiles extends ArrayAdapter<Profile> {

    ArrayList<Profile> myArrayList = null;

    CustomAdapterProfiles(Context context, ArrayList<Profile> menuAdapter){
        super(context, R.layout.customrow , menuAdapter);
        this.myArrayList = menuAdapter;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater listInflater = LayoutInflater.from(getContext());
        View customView = listInflater.inflate(R.layout.customrow, parent, false);

        Profile i = myArrayList.get(position);
        String singleItem = i.getObjectName();
        TextView mobileText = (TextView) customView.findViewById(R.id.listID);

        mobileText.setText(singleItem);
        return customView;
    }
}
