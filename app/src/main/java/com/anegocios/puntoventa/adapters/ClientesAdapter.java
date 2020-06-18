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

public class ClientesAdapter extends ArrayAdapter {

    List<ClienteXYDTOAux> mItems;
    private Context c;
    private String tip;


    public ClientesAdapter(List<ClienteXYDTOAux> items, Context context,String tipo) {
        super(context, R.layout.tablaproductos, items);
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

        if(tip.equals("G"))
        {
            rowView = inflater.inflate(R.layout.tablaclientesgrande, null);

            TextView txtNombre = (TextView) rowView.findViewById(R.id.txtNombre);
            TextView txtColonia = (TextView) rowView.findViewById(R.id.txtColonia);
            TextView txtTelefono = (TextView) rowView.findViewById(R.id.txtTelefono);
            TextView txtExt = (TextView) rowView.findViewById(R.id.txtExt);
            TextView txtCalle = (TextView) rowView.findViewById(R.id.txtCalle);

            txtColonia.setText(mItems.get(position).getColonia());
            txtTelefono.setText(mItems.get(position).getTelefono());
            txtExt.setText(mItems.get(position).getNumeroExt());
            txtCalle.setText(mItems.get(position).getCalle());



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
        }
        else {
             rowView = inflater.inflate(R.layout.tablaclientes, null);

            TextView txtNombre = (TextView) rowView.findViewById(R.id.txtNombre);



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
        }

        return rowView;
    }


}
