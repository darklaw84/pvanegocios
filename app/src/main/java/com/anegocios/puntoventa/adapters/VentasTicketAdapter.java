package com.anegocios.puntoventa.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.anegocios.puntoventa.R;
import com.anegocios.puntoventa.dtosauxiliares.ProductosXYDTOAux;
import com.anegocios.puntoventa.jsons.VentasDetalleTicket;
import com.anegocios.puntoventa.jsons.VentasVentaTicketDTO;
import com.anegocios.puntoventa.utils.ImageSeek;
import com.anegocios.puntoventa.utils.Utilerias;

import java.util.List;

public class VentasTicketAdapter extends ArrayAdapter {

    List<VentasVentaTicketDTO> mItems;
    private Context c;
    String tip;


    public VentasTicketAdapter(List<VentasVentaTicketDTO> items, Context context, String tipo) {
        super(context, R.layout.tablaventasticket, items);
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
            rowView = inflater.inflate(R.layout.tablaventasticketgrande, null);
        } else {
            rowView = inflater.inflate(R.layout.tablaventasticket, null);
        }

        TextView txtCantVen = (TextView) rowView.findViewById(R.id.txtCantVen);
        TextView txtProdVen = (TextView) rowView.findViewById(R.id.txtProdVen);
        TextView txtPrecUniVen = (TextView) rowView.findViewById(R.id.txtPrecUniVen);
        TextView txtPrecVen = (TextView) rowView.findViewById(R.id.txtPrecVen);
        TextView txtIvaVen = (TextView) rowView.findViewById(R.id.txtIvaVen);

        txtCantVen.setText("" + mItems.get(position).getCantidad());
        // txtCodigo.setTextColor(Color.BLACK);
        txtProdVen.setText(mItems.get(position).getProducto());
        // txtNombre.setTextColor(Color.BLACK);
        txtPrecUniVen.setText("" + mItems.get(position).getPrecioUnit());
        //  txtPrecio.setTextColor(Color.BLACK);
        txtPrecVen.setText("" + mItems.get(position).getPrecio());
        // txtExistencia.setTextColor(Color.BLACK);
        txtIvaVen.setText("" + mItems.get(position).getIva());


        return rowView;
    }


}
