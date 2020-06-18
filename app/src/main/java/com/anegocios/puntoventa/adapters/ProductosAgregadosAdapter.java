package com.anegocios.puntoventa.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.anegocios.puntoventa.R;
import com.anegocios.puntoventa.dtosauxiliares.ProductosXYDTOAux;
import com.anegocios.puntoventa.utils.Utilerias;

import java.util.List;

public class ProductosAgregadosAdapter extends ArrayAdapter {

    List<ProductosXYDTOAux> mItems;
    private Context c;
    private String tip;


    public ProductosAgregadosAdapter(List<ProductosXYDTOAux> items, Context context,String tipo) {
        super(context, R.layout.tablaproductospv, items);
        c = context;
        tip=tipo;
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
        View rowView;
        if(tip.equals("G")) {
             rowView = inflater.inflate(R.layout.tablaproductospv, null);
        }
        else
        {
            rowView = inflater.inflate(R.layout.tablaproductoscarrito, null);
        }

        TextView txtNombrePV = (TextView) rowView.findViewById(R.id.txtNombrePV);
        TextView txtCantPV = (TextView) rowView.findViewById(R.id.txtCantPV);
        TextView txtPrecioPV = (TextView) rowView.findViewById(R.id.txtPrecioPV);
        TextView txtTotalPV = (TextView) rowView.findViewById(R.id.txtTotalPV);

        Utilerias ut = new Utilerias();

        txtNombrePV.setText(mItems.get(position).getDescripcionCorta());
        //txtNombrePV.setTextColor(Color.BLACK);
        txtPrecioPV.setText(ut.formatoDouble(mItems.get(position).getPrecioVenta()));
        //txtPrecioPV.setTextColor(Color.BLACK);
        txtCantPV.setText("" + mItems.get(position).getCantidad());
       // txtCantPV.setTextColor(Color.BLACK);
        txtTotalPV.setText(ut.formatoDouble(mItems.get(position).getCantidad()
                * mItems.get(position).getPrecioVenta()));
       // txtTotalPV.setTextColor(Color.BLACK);

        ListView gvOpcionesSeleccionadas = (ListView) rowView.findViewById(R.id.gvOpcionesSeleccionadas);
        if(mItems.get(position).getOpciones()==null || mItems.get(position).getOpciones().size()==0)
        {
            gvOpcionesSeleccionadas.setVisibility(View.INVISIBLE);
        }
        else
        {
            OpcionesPaqueteAdapter adapter = new OpcionesPaqueteAdapter(mItems.get(position).getOpciones(), c,tip);
            gvOpcionesSeleccionadas.setAdapter(adapter);
            ViewGroup.LayoutParams params = gvOpcionesSeleccionadas.getLayoutParams();
            params.height = 50*mItems.get(position).getOpciones().size();
            gvOpcionesSeleccionadas.setLayoutParams(params);
            gvOpcionesSeleccionadas.requestLayout();
            gvOpcionesSeleccionadas.setFocusable(false);
            gvOpcionesSeleccionadas.setClickable(false);
        }


        return rowView;
    }


}
