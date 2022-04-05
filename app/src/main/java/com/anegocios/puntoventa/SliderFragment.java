package com.anegocios.puntoventa;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SliderFragment extends Fragment {


    View view;
    ImageView imageView;
    TextView txtTitulo;
    TextView txtContenido;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        if(getArguments()!=null)
        {
            view=inflater.inflate(R.layout.fragment_slider,container,false);
            imageView=view.findViewById(R.id.imageView);
            txtTitulo=view.findViewById(R.id.txtTitulo);
            txtContenido=view.findViewById(R.id.txtContenido);
            RelativeLayout relativeLay=view.findViewById(R.id.relativeLay);
            relativeLay.setBackgroundColor(getArguments().getInt("color"));
            txtTitulo.setText(getArguments().getString("titulo"));
            imageView.setImageResource(getArguments().getInt("image"));
            txtContenido.setText(getArguments().getString("contenido"));
        }
        else
        {
            view=inflater.inflate(R.layout.fragmentlisto,container,false);
        }


        return view;
    }

    public  SliderFragment newInstace(String titulo, int image,int color,String contenido)
    {
        SliderFragment slider= new SliderFragment();
        Bundle bundle= new Bundle();
        bundle.putString("titulo",titulo);
        bundle.putInt("image",image);
        bundle.putInt("color",color);
        bundle.putString("contenido",contenido);
        if(titulo.equals(""))
        {
            slider.setArguments(null);
        }
        else {
            slider.setArguments(bundle);
        }

        return slider;

    }
}
