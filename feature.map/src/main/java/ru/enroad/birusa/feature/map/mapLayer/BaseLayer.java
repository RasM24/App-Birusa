package ru.enroad.birusa.feature.map.mapLayer;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by dvoly on 07.12.2017.
 */

abstract class BaseLayer {

    protected Context context;

    String getStringFromAssetFile(String name) {
        byte[] buffer = null;
        InputStream is;

        try {
            is = context.getAssets().open(name);
            int size = is.available();
            buffer = new byte[size];
            is.read(buffer);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new String(buffer);
    }

}
