package com.toumlilt.gameoftrones;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.transform.Source;

class DownloadSanitary extends AsyncTask<Void, String, ArrayList<Sanitary>> {

    private String URL_SANITARY = "http://opendata.paris.fr/explore/dataset/sanisettesparis2011/download/?format=json&timezone=Europe/Berlin";
    private int HTTP_ALL_OKAY = 200;

    protected ArrayList<Sanitary> doInBackground(Void... unused)
    {
        ArrayList<Sanitary> als = new ArrayList<>();
        try {
            URL url = new URL(this.URL_SANITARY);
            HttpURLConnection httpConn = null;
            httpConn = (HttpURLConnection) url.openConnection();

            if(httpConn.getResponseCode()!=this.HTTP_ALL_OKAY)
                throw new RuntimeException("Impossible to download sanitaries");

            InputStream in = httpConn.getInputStream();
            BufferedReader bis = new BufferedReader(new InputStreamReader(in));

            char[] buff = new char[2048];
            Pattern p = Pattern.compile("geom_x_y\\\"\\:\\ \\[([0-9.]*)\\,\\ ([0-9.]*)");
            Double lati;
            Double longi;

            while (bis.read(buff) != -1){
                String s =String.valueOf(buff);
                Matcher m = p.matcher(s);

                if (m.find()) {
                    try{
                        lati = Double.parseDouble(m.group(1));
                        longi = Double.parseDouble(m.group(2));
                        als.add(new Sanitary(lati, longi));
                    }catch (NumberFormatException e){}
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return als;
    }
}