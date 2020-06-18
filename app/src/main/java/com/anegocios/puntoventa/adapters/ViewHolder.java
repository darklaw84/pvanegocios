package com.anegocios.puntoventa.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.anegocios.puntoventa.R;

public class ViewHolder extends RecyclerView.ViewHolder {

    public Button botonPV;

    public ViewHolder(View v) {
        super(v);
         botonPV = (Button) v.findViewById(R.id.botonPV);
    }

}
