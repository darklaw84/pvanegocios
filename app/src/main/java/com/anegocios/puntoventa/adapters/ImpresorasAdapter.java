package com.anegocios.puntoventa.adapters;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.anegocios.puntoventa.R;
import com.anegocios.puntoventa.utils.Utilerias;

import java.util.List;

public class ImpresorasAdapter extends ArrayAdapter {

    List<BluetoothDevice> mItems;
    private Context c;


    public ImpresorasAdapter(List<BluetoothDevice> items, Context context) {
        super(context, R.layout.tablaimpresoras, items);
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
        View rowView = inflater.inflate(R.layout.tablaimpresoras, null);

        TextView txtDispositivo = (TextView) rowView.findViewById(R.id.txtDispositivo);


        Utilerias ut = new Utilerias();
             txtDispositivo.setText(mItems.get(position).getName());

        txtDispositivo.setTextColor(Color.BLACK);


        return rowView;
    }


}
