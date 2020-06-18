package com.anegocios.puntoventa.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.anegocios.puntoventa.R;
import com.anegocios.puntoventa.jsons.GrupoVRXY;

import java.util.List;

public class SimpleAdapter extends ArrayAdapter {

    List<String> mItems;
    private Context c;


    public SimpleAdapter(List<String> items, Context context) {
        super(context, R.layout.simplespinnergrupo,R.id.textoMostrar, items);
        c = context;
        mItems = items;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View rowView = inflater.inflate(R.layout.simplespinnergrupo, null);
        TextView textoMostrar = (TextView) rowView.findViewById(R.id.textoMostrar);
        textoMostrar.setText("" + mItems.get(position));


        return rowView;
    }


}
