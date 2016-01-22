package com.smarter.onejoke.utils;

import android.net.Uri;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by panl
 * Date at 16/1/22.
 */
public class FileUtil {
    private FileUtil() {
        throw new UnsupportedOperationException("can not be instanced");
    }

    public static boolean isSDCardEnable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static Uri saveFileToSDCard(String path, String title) throws Exception{
        File appDir = new File(Environment.getExternalStorageDirectory(), "OneJoke");
        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        return getImage(path,appDir,title);
    }

    public static Uri getImage(String path, File appDir,String saveName) throws Exception {
        File file = new File(appDir,saveName);
        URL url = new URL(path);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setConnectTimeout(1000 * 6);
        con.connect();
        if (con.getResponseCode() == 200) {
            InputStream inputStream = con.getInputStream();
            byte[] b = getByte(inputStream);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(b);
            fileOutputStream.close();

        }
        return Uri.fromFile(file);
    }

    private static byte[] getByte(InputStream inputStream) throws Exception {
        byte[] b = new byte[1024];
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int len;
        while ((len = inputStream.read(b)) != -1) {
            byteArrayOutputStream.write(b, 0, len);
        }
        byteArrayOutputStream.close();
        inputStream.close();
        return byteArrayOutputStream.toByteArray();
    }

}
