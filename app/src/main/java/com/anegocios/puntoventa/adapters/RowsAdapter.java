package com.anegocios.puntoventa.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.anegocios.puntoventa.R;
import com.anegocios.puntoventa.bdlocal.RowTicketLocal;
import com.anegocios.puntoventa.dtosauxiliares.ClienteXYDTOAux;

import java.util.List;

public class RowsAdapter extends ArrayAdapter {

    List<RowTicketLocal> mItems;
    private Context c;


    public RowsAdapter(List<RowTicketLocal> items, Context context) {
        super(context, R.layout.tablarows, items);
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
        View rowView = inflater.inflate(R.layout.tablarows, null);

        TextView txtTamanioLetra = (TextView) rowView.findViewById(R.id.txtTamanioLetra);
        TextView txtTextoRow = (TextView) rowView.findViewById(R.id.txtTextoRow);

        String tamanio = "";
        if(mItems.get(position).getTamanioLetra()==1)
        {
            tamanio="Chica";
        }
        else if (mItems.get(position).getTamanioLetra()==2)
        {
            tamanio="Mediana";
        }
        else if (mItems.get(position).getTamanioLetra()==3)
        {
            tamanio="Grande";
        }

        txtTamanioLetra.setText(tamanio);
        txtTextoRow.setText(mItems.get(position).getTexto());



        return rowView;
    }


}
