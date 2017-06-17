package com.zhuravlev.sergey.auction;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public class ImageLoader extends AsyncTask<String, Void, Bitmap> {

    private ImageView imageView;
    private Context context;

    public ImageLoader(ImageView imageView, Context context) {
        this.context = context;
        this.imageView = imageView;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        try {
            InputStream in = null;
            Bitmap bitmap;
            URI url = new URI(strings[0]);
            switch (url.getScheme()) {
                case "http":
                    in = url.toURL().openStream();
                    break;
                case "assets":
                    in = context.getAssets().open(url.getPath().substring(1));
                    break;
            }
            bitmap = BitmapFactory.decodeStream(in);
            return bitmap;
        } catch (IOException e) {
            Log.e("Auction.ImageLoader", "Ошибка ввода-вывода: " + e.getMessage());
            cancel(true);
            return null;
        } catch (Exception e) {
            Log.e("Auction.ImageLoader", e.getMessage());
            cancel(true);
            return null;
        }
    }
}
