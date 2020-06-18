package com.anegocios.puntoventa.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageSeek extends AsyncTask<String, Void, Bitmap> {

    private Exception exception;

    protected Bitmap doInBackground(String... urls) {
        try {
            if(urls[0]!=null) {
                String urln=urls[0];
                //comentar la siguiente linea cuando ya estemos en prod
                //urln=urln.replace("104.154.160.93","http://104.154.160.93");
               // urln=urln.replace("http://anegocios.com","http://www.anegocios.com");
                URL url = new URL(urln);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;
                Bitmap myBitmap = BitmapFactory.decodeStream(input,null,options);
                return myBitmap;
            }
        } catch (Exception e) {
            this.exception = e;

            return null;
        } finally {

        }
        return null;
    }

    protected void onPostExecute(Bitmap feed) {
        // TODO: check this.exception
        // TODO: do something with the feed
    }
}