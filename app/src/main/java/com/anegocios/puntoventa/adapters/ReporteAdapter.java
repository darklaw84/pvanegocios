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
import com.anegocios.puntoventa.jsons.ReporteDetalleDTO;
import com.anegocios.puntoventa.utils.ImageSeek;
import com.anegocios.puntoventa.utils.Utilerias;

import java.text.DecimalFormat;
import java.util.List;

public class ReporteAdapter extends ArrayAdapter {

    List<ReporteDetalleDTO> mItems;
    private Context c;
    private String tipo;
    private String tam;


    public ReporteAdapter(List<ReporteDetalleDTO> items, Context context, String tipos,String tamanio) {
        super(context, R.layout.tablareporte, items);
        c = context;
        mItems = items;
        tipo = tipos;
        tam= tamanio;
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
        if(tam.equals("C")) {
            rowView = inflater.inflate(R.layout.tablareporte, null);
        }
        else
        {
            rowView = inflater.inflate(R.layout.tablareportegrande, null);
            TextView txtFolioApp = (TextView) rowView.findViewById(R.id.txtFolioApp);

            txtFolioApp.setText("" + mItems.get(position).getFolioApp());

        }
        TextView txtFolio = (TextView) rowView.findViewById(R.id.txtFolio);
        TextView txtReporteTotal = (TextView) rowView.findViewById(R.id.txtReporteTotal);
        TextView txtReporteAbonado = (TextView) rowView.findViewById(R.id.txtReporteAbonado);
        TextView txtCliente = (TextView) rowView.findViewById(R.id.txtCliente);
        TextView txtReporteSaldo = (TextView) rowView.findViewById(R.id.txtReporteSaldo);
        ImageView entregadoped = (ImageView) rowView.findViewById(R.id.entregadoped);


        txtFolio.setText("" + mItems.get(position).getFolio());
        // txtCodigo.setTextColor(Color.BLACK);
        txtReporteTotal.setText(formatear(mItems.get(position).getTotal()));
        // txtNombre.setTextColor(Color.BLACK);
        txtReporteAbonado.setText(formatear(mItems.get(position).getAbonado()));
        //  txtPrecio.setTextColor(Color.BLACK);
        txtCliente.setText("" + mItems.get(position).getCliente());
        txtReporteSaldo.setText(formatear(mItems.get(position).getSaldo()));
        // txtExistencia.setTextColor(Color.BLACK);
        if (tipo.equals("cotizaciones")) {
            txtReporteSaldo.setText("");
            txtReporteAbonado.setText("");
        }
        if (tipo.equals("pedidos")) {
            entregadoped.setVisibility(View.VISIBLE);
            if (mItems.get(position).isProdEntregado()) {
                entregadoped.setImageResource(R.drawable.entregadoped);
            } else {
                entregadoped.setImageResource(R.drawable.noentregadoped);
            }
        } else {
            entregadoped.setVisibility(View.GONE);
        }
        return rowView;
    }


    private String formatear(double dou) {
        DecimalFormat df = new DecimalFormat("#,##0.00");

        return df.format(dou);
    }

}
