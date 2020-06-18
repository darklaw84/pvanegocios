package com.anegocios.puntoventa.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.anegocios.puntoventa.R;
import com.anegocios.puntoventa.dtosauxiliares.ClienteXYDTOAux;

import java.util.List;

public class ClientesAdapterGris extends ArrayAdapter {

    List<ClienteXYDTOAux> mItems;
    private Context c;


    public ClientesAdapterGris(List<ClienteXYDTOAux> items, Context context) {
        super(context, R.layout.tablaproductos, items);
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
        View rowView = inflater.inflate(R.layout.tablaclientesgris, null);

        TextView txtNombre = (TextView) rowView.findViewById(R.id.txtNombre);


       /* if (position % 2 == 0)
        {
            rowView.setBackgroundResource(R.color.listOscuro);
        }
        else
        {
            rowView.setBackgroundResource(R.color.listClaro);
        }*/

        String nombre = mItems.get(position).getNombre();
        String appPaterno = mItems.get(position).getApellidoP();
        String appMaterno = mItems.get(position).getApellidoM();

        if (nombre == null) {
            nombre = "";
        }
        if (appMaterno == null) {
            appMaterno = "";
        }
        if (appPaterno == null) {
            appPaterno = "";
        }
        txtNombre.setText(nombre + " "
                + appPaterno + " "
                + appMaterno);
        //  txtNombre.setTextColor(Color.BLACK);


        return rowView;
    }


}
