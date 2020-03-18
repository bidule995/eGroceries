package com.iut.mygrocerylist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.CheckBox;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;

public class ArticlesAdapter extends SimpleAdapter {

    private LayoutInflater mInflater;
    ArrayList<HashMap<String, String>> arrayList;


    public ArticlesAdapter(@NonNull Context context, ArrayList<HashMap<String, String>> data, int resource,
                           String[] from, int[] to) {
        super (context, data, resource, from, to);
        this.arrayList = data;
        mInflater = LayoutInflater.from (context);
    }

    @Override
    public Object getItem (int position)
    {
        return super.getItem (position);
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent) {
        if (convertView==null) {
            convertView = mInflater.inflate (R.layout.list_row_articles, null);
            CheckBox cb = convertView.findViewById(R.id.articleRecupereCheckBox);
            String recupere = arrayList.get(position).get("recupere");
            if(recupere == null) recupere = "0";
            if(recupere.equals("1")){
                cb.setChecked(true);
            } else {
                cb.setChecked(false);
            }
            cb.setTag(position);
        }
        return super.getView (position, convertView, parent);
    }
}
