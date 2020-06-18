package com.anegocios.puntoventa.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.anegocios.puntoventa.R;
import com.anegocios.puntoventa.dtosauxiliares.PaqueteOpcionAux;
import com.anegocios.puntoventa.utils.Utilerias;

import java.util.List;

public class OpcionesPaqueteAdapter extends ArrayAdapter {

    List<PaqueteOpcionAux> mItems;
    private Context c;
    private String tip;


    public OpcionesPaqueteAdapter(List<PaqueteOpcionAux> items, Context context,String tipo) {
        super(context, R.layout.tablaopcionpaquete, items);
        c = context;
        mItems = items;
        tip=tipo;
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
        View rowView;
        if(tip.equals("G")) {
             rowView = inflater.inflate(R.layout.tablaopcionpaquete, null);
        }
        else
        {
             rowView = inflater.inflate(R.layout.tablaopcionpaquetecarrito, null);
        }
        TextView txtOpcionDesc = (TextView) rowView.findViewById(R.id.txtOpcionDesc);
        TextView txtxOpcionCant = (TextView) rowView.findViewById(R.id.txtxOpcionCant);


        Utilerias ut = new Utilerias();
        if(mItems.get(position).isMostrar()) {
            txtOpcionDesc.setText(mItems.get(position).getDescripion());
            //  txtOpcionDesc.setTextColor(Color.BLACK);
            txtxOpcionCant.setText(ut.formatoDouble(mItems.get(position).getCantidad()));
        }
        else
        {
            rowView.setVisibility(View.INVISIBLE);
        }
       // txtxOpcionCant.setTextColor(Color.BLACK);


        return rowView;
    }


}
