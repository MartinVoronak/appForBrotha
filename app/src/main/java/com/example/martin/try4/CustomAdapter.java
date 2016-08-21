package com.example.martin.try4;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;


//SINGLE PROFILE COLORS
public class CustomAdapter extends ArrayAdapter<ArrayList<String>> {

    ArrayList<String> myArrayList = null;

    CustomAdapter(Context context, ArrayList menuAdapter){
        super(context, R.layout.customrow , menuAdapter);
        this.myArrayList = menuAdapter;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater listInflater = LayoutInflater.from(getContext());
        View customView = listInflater.inflate(R.layout.customrow2, parent, false);

        String singleItem = myArrayList.get(position);
        TextView profileText = (TextView) customView.findViewById(R.id.profileColorName);
        TextView  profileColor = (TextView) customView.findViewById(R.id.profileColorPreview);

        profileText.setText(singleItem);
        //set background of drawable
        profileColor.getBackground().setColorFilter(Color.parseColor("#"+myArrayList.get(position).toString()), PorterDuff.Mode.SRC_ATOP);

        return customView;
    }
}
