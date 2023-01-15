package com.anegocios.puntoventa.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.anegocios.puntoventa.ClientesActivity;
import com.anegocios.puntoventa.ClientesGrandeActivity;
import com.anegocios.puntoventa.R;
import com.anegocios.puntoventa.dtosauxiliares.ClienteXYDTOAux;

import java.util.List;

public class ClientesAdapter extends ArrayAdapter {

    List<ClienteXYDTOAux> mItems;
    private Context c;
    private String tip;
    ClientesActivity a;
    ClientesGrandeActivity aG;


    public ClientesAdapter(List<ClienteXYDTOAux> items, Context context,
                           String tipo, ClientesActivity act, ClientesGrandeActivity actG) {
        super(context, R.layout.tablaproductos, items);
        c = context;
        a = act;
        aG = actG;
        mItems = items;
        tip = tipo;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View rowView;


        if (tip.equals("G")) {
            rowView = inflater.inflate(R.layout.tablaclientesgrande, null);
            ImageButton btnWaze = (ImageButton) rowView.findViewById(R.id.btnWaze);
            ImageButton btnMaps = (ImageButton) rowView.findViewById(R.id.btnMaps);

            TextView txtNombre = (TextView) rowView.findViewById(R.id.txtNombre);
            TextView txtColonia = (TextView) rowView.findViewById(R.id.txtColonia);
            TextView txtTelefono = (TextView) rowView.findViewById(R.id.txtTelefono);
            TextView txtExt = (TextView) rowView.findViewById(R.id.txtExt);
            TextView txtCalle = (TextView) rowView.findViewById(R.id.txtCalle);


            txtColonia.setText(mItems.get(position).getColonia());
            txtTelefono.setText(mItems.get(position).getTelefono());
            txtExt.setText(mItems.get(position).getNumeroExt());
            txtCalle.setText(mItems.get(position).getCalle());

            String uri = "";
            String uriMaps = "";
            btnMaps.setVisibility(View.GONE);
            btnWaze.setVisibility(View.GONE);
            if (mItems.get(position).getComentario()!= null  && mItems.get(position).getComentario().contains("[#")) {
                btnMaps.setVisibility(View.VISIBLE);
                btnWaze.setVisibility(View.VISIBLE);
                int inicia = mItems.get(position).getComentario().indexOf("[");
                int termina = mItems.get(position).getComentario().indexOf("]");
                String ubi = mItems.get(position).getComentario().substring(inicia + 2, termina - 1);


                String[] datos = ubi.split(",");
                if (datos.length == 2) {
                    uri = "waze://?ll=" + datos[0] + "," + datos[1] + "&z=10";
                    uriMaps = "http://maps.google.com/maps?daddr=" + datos[0] + "," + datos[1];

                    try {
                        if (Double.parseDouble(datos[0])!=0)
                        {
                            btnMaps.setVisibility(View.VISIBLE);
                            btnWaze.setVisibility(View.VISIBLE);
                        }
                    }
                    catch (Exception ex)
                    {

                    }
                }
            }


            String nombre = mItems.get(position).getNombre();
            String appPaterno = mItems.get(position).getApellidoP();
            String appMaterno = mItems.get(position).getApellidoM();

            final String finalUri = uri;
            btnWaze.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    aG.mostrarWaze(finalUri);

                }
            });

            final String finalUriMaps = uriMaps;
            btnMaps.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    aG.mostrarMaps(finalUriMaps);

                }
            });

            txtNombre.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    aG.mostrarClienteActual(position);
                }
            });

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
        } else {
            rowView = inflater.inflate(R.layout.tablaclientes, null);

            TextView txtNombre = (TextView) rowView.findViewById(R.id.txtNombre);

            ImageButton btnWaze = (ImageButton) rowView.findViewById(R.id.btnWaze);
            ImageButton btnMaps = (ImageButton) rowView.findViewById(R.id.btnMaps);
            TextView txtRuta = (TextView) rowView.findViewById(R.id.txtRuta);


            String uri = "";
            String uriMaps = "";
            btnMaps.setVisibility(View.GONE);
            btnWaze.setVisibility(View.GONE);

            if (mItems.get(position).getComentario()!= null && mItems.get(position).getComentario().contains("[#")) {

                int inicia = mItems.get(position).getComentario().indexOf("[");
                int termina = mItems.get(position).getComentario().indexOf("]");
                String ubi = mItems.get(position).getComentario().substring(inicia + 2, termina - 1);


                String[] datos = ubi.split(",");
                if (datos.length == 2) {
                    uri = "waze://?ll=" + datos[0] + "," + datos[1] + "&z=10";
                    uriMaps = "http://maps.google.com/maps?daddr=" + datos[0] + "," + datos[1];

                    try {
                        if (Double.parseDouble(datos[0])!=0)
                        {
                            btnMaps.setVisibility(View.VISIBLE);
                            btnWaze.setVisibility(View.VISIBLE);
                        }
                    }
                    catch (Exception ex)
                    {

                    }

                }
            }


            String nombre = mItems.get(position).getNombre();
            String appPaterno = mItems.get(position).getApellidoP();
            String appMaterno = mItems.get(position).getApellidoM();
            String ruta = mItems.get(position).getRuta();

            final String finalUri = uri;
            btnWaze.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    a.mostrarWaze(finalUri);

                }
            });

            final String finalUriMaps = uriMaps;
            btnMaps.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    a.mostrarMaps(finalUriMaps);

                }
            });

            txtNombre.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    a.mostrarClienteActual(position);
                }
            });

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
            txtRuta.setText(ruta);
            //  txtNombre.setTextColor(Color.BLACK);
        }

        return rowView;
    }


}
