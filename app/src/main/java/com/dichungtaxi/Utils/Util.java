package com.dichungtaxi.Utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.dichungtaxi.main.R;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by CHIP on 15/08/2015.
 */
public class Util {

    public static void sleep(long sleep) {
        try {
            Thread.sleep(sleep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static StringBuilder getContentFromUrl(String strUrl, Context context) {


        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {

            URL url = new URL(strUrl);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
            return jsonResults;

        } catch (MalformedURLException e) {
            Toast.makeText(context, R.string.internet_error, Toast.LENGTH_SHORT).show();
            e.printStackTrace();

        } catch (IOException e) {
            Toast.makeText(context, R.string.internet_error, Toast.LENGTH_SHORT).show();
            e.printStackTrace();

        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return null;
    }
}
