package com.anegocios.puntoventa.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.anegocios.puntoventa.R;
import com.anegocios.puntoventa.dtosauxiliares.GruposVRXYAux;

import java.util.List;

public class BotonesGruposAdapter extends RecyclerView.Adapter<ViewHolder> {
    private List<GruposVRXYAux> list;
    private GridView gv;
    private Context context;

    public BotonesGruposAdapter(List<GruposVRXYAux> Data) {
        list = Data;
        this.gv = gv;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.botonpv, parent, false);

        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.botonPV.setText(list.get(position).getNombre());
        holder.botonPV.setBackgroundColor(Color.parseColor(list.get(position).getColor()));

    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        } else {
            return 0;
        }
    }
}
