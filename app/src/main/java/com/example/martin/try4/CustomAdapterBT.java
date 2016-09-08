package com.example.martin.try4;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapterBT extends ArrayAdapter<BTDevice> {

    ArrayList<BTDevice> myArrayList = null;
    BTDevice i;

    CustomAdapterBT(Context context, ArrayList<BTDevice> menuAdapter){
        super(context, R.layout.customrow3,menuAdapter);
        this.myArrayList = menuAdapter;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater listInflater = LayoutInflater.from(getContext());
        View customView = listInflater.inflate(R.layout.customrow3, parent, false);

        i = myArrayList.get(position);

        String itemText = i.name;
        TextView singeItem = (TextView) customView.findViewById(R.id.BTname);

        String itemText2 = i.mac;
        TextView singeItem2 = (TextView) customView.findViewById(R.id.BTmac);

        singeItem.setText(itemText);
        singeItem2.setText(itemText2);

        return customView;
    }
}
