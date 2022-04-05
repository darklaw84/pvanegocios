package com.anegocios.puntoventa.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.anegocios.puntoventa.R;
import com.anegocios.puntoventa.jsons.AbonosDTO;
import com.anegocios.puntoventa.jsons.VentasVentaTicketDTO;
import com.anegocios.puntoventa.utils.Utilerias;

import java.util.List;

public class AbonosTicketAdapter extends ArrayAdapter {

    List<AbonosDTO> mItems;
    private Context c;
    String tip;


    public AbonosTicketAdapter(List<AbonosDTO> items, Context context, String tipo) {
        super(context, R.layout.tablaabonosticket, items);
        c = context;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        Utilerias ut = new Utilerias();
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View rowView;
        if (tip.equals("G")) {
            rowView = inflater.inflate(R.layout.tablaabonosticketgrande, null);
        } else {
            rowView = inflater.inflate(R.layout.tablaabonosticket, null);
        }

        TextView txtFechaAbono = (TextView) rowView.findViewById(R.id.txtFechaAbono);
        TextView txtCantidadAbono = (TextView) rowView.findViewById(R.id.txtCantidadAbono);
        TextView txtUsuarioAbono = (TextView) rowView.findViewById(R.id.txtUsuarioAbono);


        txtFechaAbono.setText("" + mItems.get(position).getFecha());
        txtCantidadAbono.setText("" + mItems.get(position).getCantidad());

        if (mItems.get(position).getUsuario() == null) {
            txtUsuarioAbono.setText("");
        } else {
            txtUsuarioAbono.setText("" + mItems.get(position).getUsuario());
        }


        return rowView;
    }


}
