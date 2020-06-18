package com.anegocios.puntoventa;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anegocios.puntoventa.adapters.ViewPagerAdapter;
import com.anegocios.puntoventa.utils.Utilerias;

import static android.support.v4.view.ViewPager.*;

public class SliderActivity extends AppCompatActivity {


    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private LinearLayout dotsLayout;
    private Button btnSaltar;
    private int[] colorBackGround;
    private TextView[] dots;

    private String[] titulo = { " PRODUCTOS Y CLIENTES", "VERSIÓN DE ESCRITORIO", ""};
    private String[] contenido = {
            " Da de alta tus registros ! Son Ilimitados !",
             "Ve a www.anegocios.com y valídate con las mismas contraseñas que en la App \n\n ¿ Qué puedes hacer ?\n \n -Personalizar tu ticket                 - Importar Clientes         -Consultar Reportes", ""};
    private int[] image = { R.drawable.imagen2, R.drawable.imagen4, 0};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sliderlayout);
        viewPager = findViewById(R.id.ViewPager);
        btnSaltar = findViewById(R.id.btnSaltar);
        colorBackGround = getResources().getIntArray(R.array.array_backgorund);
        dotsLayout = findViewById(R.id.dotsLayout);
        addDots(0);
        loadViewPager();
    }

    private void addDots(int currentPage) {
        dots = new TextView[titulo.length];
        dotsLayout.removeAllViews();

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(".");
            dots[i].setTextSize(35);
            dots[i].setTextColor(Color.WHITE);

            if (i == currentPage) {
                dots[i].setTextSize(45);
                dots[i].setTypeface(dots[i].getTypeface(), Typeface.BOLD_ITALIC);

            }
            dotsLayout.addView(dots[i]);
        }
    }


    private void loadViewPager() {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        for (int i = 0; i < titulo.length; i++) {
            SliderFragment slider = new SliderFragment();
            adapter.addFragment(slider.newInstace(titulo[i], image[i], colorBackGround[i], contenido[i]));
        }
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(pagerListener);
    }

    OnPageChangeListener pagerListener = new OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int position) {
            addDots(position);
            if(position==4)
            {
              btnSaltar.setVisibility(INVISIBLE);
            }
            else
            {
                btnSaltar.setVisibility(VISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };

    public void btnListoClick(View view) {
        saltar();
    }

    public void btnSaltarClick(View view) {
        saltar();
    }

    public void btnWhatssClick(View view) {
        goToUrl("https://bit.ly/2JwSNWK");
    }

    private void saltar() {
        Utilerias ut = new Utilerias();
        ut.guardarValor("wizardVisto", "true", this);
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }

    private void goToUrl (String url) {
        Uri uriUrl = Uri.parse(url);
        Intent WebView = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(WebView);
    }
}
