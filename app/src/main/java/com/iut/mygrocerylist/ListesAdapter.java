package com.iut.mygrocerylist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.HashMap;

public class ListesAdapter extends SimpleAdapter {

    private LayoutInflater mInflater;
    private Context context;
    private ArrayList<HashMap<String, String>> arrayList;

    public ListesAdapter(@NonNull Context context, ArrayList<HashMap<String, String>> data, int resource,
                           String[] from, int[] to) {
        super (context, data, resource, from, to);
        this.arrayList = data;
        this.context = context;
        mInflater = LayoutInflater.from (context);
    }

    @Override
    public Object getItem (int position)
    {
        return super.getItem (position);
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent) {
        convertView = mInflater.inflate (R.layout.list_row_listes, null);
        ProgressBar pb = convertView.findViewById(R.id.listProgressBar);
        TextView tx = convertView.findViewById(R.id.listProgressValue);
        int progressValueBar = Integer.parseInt(arrayList.get(position).get("progressValueBar"));
        pb.setProgress(progressValueBar);
        if(progressValueBar == 100) tx.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
        else tx.setTextColor(context.getResources().getColor(android.R.color.secondary_text_light));
        return super.getView (position, convertView, parent);
    }
}
