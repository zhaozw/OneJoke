package com.smarter.onejoke.utils;

import android.os.AsyncTask;

/**
 * Created by panl on 15/12/5.
 */
public class GifDataLoader extends AsyncTask<String,Void,byte[]>{

    @Override
    protected byte[] doInBackground(String... params) {
        final String gifUrl = params[0];

        if (gifUrl == null)
            return null;

        try {
            return ByteArrayHttpClient.get(gifUrl);
        } catch (OutOfMemoryError e) {
            return null;
        }
    }
}
