package com.example.martin.try4;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;



public class CustomAdapter extends ArrayAdapter<ArrayList<String>> {

    ArrayList<String> myArrayList = null;

    CustomAdapter(Context context, ArrayList menuAdapter){
        super(context, R.layout.customrow , menuAdapter);
        this.myArrayList = menuAdapter;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater listInflater = LayoutInflater.from(getContext());
        View customView = listInflater.inflate(R.layout.customrow, parent, false);

        String singleItem = myArrayList.get(position);
        TextView mobileText = (TextView) customView.findViewById(R.id.listID);

        mobileText.setText(singleItem);
        return customView;
    }
}
