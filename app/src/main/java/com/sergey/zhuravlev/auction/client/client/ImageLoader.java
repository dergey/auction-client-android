package com.sergey.zhuravlev.auction.client.client;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public class ImageLoader extends AsyncTask<String, Void, Bitmap> {

    private Context context;
    private ImageView imageView;

    public ImageLoader(ImageView imageView, Context context) {
        this.context = context;
        this.imageView = imageView;
    }

    @Override
    protected Bitmap doInBackground(String... urls) {
        try {
            URI path = new URI(urls[0]);
            String protocol = path.getScheme();
            switch (protocol) {
                case "http":
                case "https":
                    InputStream netStream = path.toURL().openStream();
                    return BitmapFactory.decodeStream(netStream);
                case "assets":
                    InputStream assetsStream = this.context.getAssets().open(path.getPath().substring(1));
                    return BitmapFactory.decodeStream(assetsStream);
                default:
                    cancel(true);
                    return null;
            }
        } catch (IOException e) {
            Log.e("Auction.ImageLoader", "Ошибка ввода-вывода: " + e.getMessage());
            cancel(true);
            return null;
        } catch (Exception paramVarArgs) {
            Log.e("Auction.ImageLoader", paramVarArgs.getMessage());
            cancel(true);
            return null;
        }
    }

    protected void onPostExecute(Bitmap paramBitmap) {
        this.imageView.setImageBitmap(paramBitmap);
    }

}
