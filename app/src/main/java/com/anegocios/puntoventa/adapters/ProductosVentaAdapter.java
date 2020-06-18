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

public class ProductosVentaAdapter extends ArrayAdapter {

    List<ProductosXYDTOAux> mItems;
    private Context c;


    public ProductosVentaAdapter(List<ProductosXYDTOAux> items, Context context) {
        super(context, R.layout.tablaproductos, items);
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
        Utilerias ut = new Utilerias();
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View rowView = inflater.inflate(R.layout.tablaproductosventa, null);

        TextView txtNombre = (TextView) rowView.findViewById(R.id.txtVenNomProducto);
        TextView txtExistencia = (TextView) rowView.findViewById(R.id.txtVenExistencia);
        TextView txtPrecio = (TextView) rowView.findViewById(R.id.txtVenPrecio);
        ImageView imagenProductoConsulta = (ImageView) rowView.findViewById(R.id.imagenVenProducto);

       /* if (position % 2 == 0)
        {
            rowView.setBackgroundResource(R.color.listOscuro);
        }
        else
        {
            rowView.setBackgroundResource(R.color.listClaro);
        }*/

        // txtCodigo.setTextColor(Color.BLACK);
        txtNombre.setText(mItems.get(position).getDescripcionCorta());
        // txtNombre.setTextColor(Color.BLACK);
        txtPrecio.setText("" + mItems.get(position).getPrecioVenta());
        //  txtPrecio.setTextColor(Color.BLACK);
        txtExistencia.setText("" + mItems.get(position).getExistencias());
        // txtExistencia.setTextColor(Color.BLACK);



        Usuario permisos = ut.obtenerPermisosUsuario(c);

        if(permisos.getMostrarImagenes()) {

            if (mItems.get(position).getImagenGuardada() != null) {
                //quiere decir que la traemos de el server
                imagenProductoConsulta.setImageBitmap(
                        ut.StringToBitMapLocal(mItems.get(position).getImagenGuardada()));
            } else {
                //quiere decir que no la encontro, por lo tanto la buscamos en local
                if (mItems.get(position).getImgString() != null && mItems.get(position).getImgString().length() > 0
                ) {
                    imagenProductoConsulta.setImageBitmap(ut.StringToBitMap(mItems.get(position).getImgString()));
                    if (ut.getBitmap() != null) {

                        ut.setBitmap(null);
                    }
                } else {
                    imagenProductoConsulta.setImageResource(R.drawable.sinimagen);
                }
            }
        }
        else
        {
            imagenProductoConsulta.setImageResource(R.drawable.sinimagen);
        }
     /*   if (imagen != null) {
            imagen = null;
        }*/


        return rowView;
    }


}
