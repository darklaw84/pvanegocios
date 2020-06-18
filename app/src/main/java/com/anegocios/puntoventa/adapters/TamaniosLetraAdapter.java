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
import com.anegocios.puntoventa.jsons.TamanioLetraDTO;

import java.util.List;

public class TamaniosLetraAdapter extends ArrayAdapter {

    List<TamanioLetraDTO> mItems;
    private Context c;


    public TamaniosLetraAdapter(List<TamanioLetraDTO> items, Context context) {
        super(context, R.layout.comboletra, items);
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
        View rowView = inflater.inflate(R.layout.comboletra, null);

        TextView txtTamanioLetraCombo = (TextView) rowView.findViewById(R.id.txtTamanioLetraCombo);



        txtTamanioLetraCombo.setText(mItems.get(position).getTexto());

        txtTamanioLetraCombo.setTextSize(mItems.get(position).getTamanio());



        return rowView;
    }


}
