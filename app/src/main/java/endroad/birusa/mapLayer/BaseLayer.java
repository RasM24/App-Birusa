package endroad.birusa.mapLayer;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by dvoly on 07.12.2017.
 */

public abstract class BaseLayer {

    protected Context context;

    protected String getStringFromAssetFile(String name) {
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
