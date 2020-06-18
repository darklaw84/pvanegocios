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
import com.anegocios.puntoventa.jsons.Usuario;
import com.anegocios.puntoventa.utils.ImageSeek;
import com.anegocios.puntoventa.utils.Utilerias;

import java.util.List;

public class ProductosPVAdapter  extends ArrayAdapter {

    List<ProductosXYDTOAux> mItems;
    private Context c;
    private String colo;


    public ProductosPVAdapter(List<ProductosXYDTOAux> items, Context context, String color)
    {
        super(context, R.layout.productoimagen, items);
        c = context;
        mItems = items;
        colo = color;
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
        View rowView = inflater.inflate(R.layout.productoimagen,null);
        ImageView imagenProductoVenta = (ImageView)rowView.findViewById(R.id.imagenProductoVenta);

        TextView txtNomProducto = (TextView)rowView.findViewById(R.id.txtNomProducto);
        if(colo.equals("#FFFFFF") || colo.equals("#ffffff")) {
            colo="#CCCCCC";
        }

        txtNomProducto.setBackgroundColor(Color.parseColor(colo));
        txtNomProducto.setText(mItems.get(position).getDescripcionCorta());

        Usuario permisos = ut.obtenerPermisosUsuario(c);

        if(permisos.getMostrarImagenes()) {
            if (mItems.get(position).getImagenGuardada() != null) {
                //quiere decir que la traemos de el server
                imagenProductoVenta.setImageBitmap(ut.StringToBitMapLocal(mItems.get(position).getImagenGuardada()));
            } else {
                //quiere decir que no la encontro, por lo tanto la buscamos en local
                if (mItems.get(position).getImgString() != null && mItems.get(position).getImgString().length() > 0
                ) {
                    imagenProductoVenta.setImageBitmap(ut.StringToBitMap(mItems.get(position).getImgString()));
                    if (ut.getBitmap() != null) {

                        ut.setBitmap(null);
                    }
                } else {
                    imagenProductoVenta.setImageResource(R.drawable.sinimagen);
                }
            }
        }
        else {
            imagenProductoVenta.setVisibility(View.GONE);
            txtNomProducto.setHeight(100);
        }

      /*  if (imagen != null) {
            imagen = null;
        }*/

        return rowView;
    }








}
