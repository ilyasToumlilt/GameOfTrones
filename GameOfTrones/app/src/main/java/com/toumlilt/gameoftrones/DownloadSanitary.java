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

/**
 * Tâche asynchrone pour télécharger et parser le JSON de sanisettes.
 * Le parseur est assez simple car il repose sur une expression régulière.
 * Seul le champ geom_x_y nous est utile, on isole latitude et latitude avec des 'group'.
 * Le JSON est parsé bloc-par-bloc, il n'est jamais chargé entièrement en mémoire.
 * */
class DownloadSanitary extends AsyncTask<Void, String, ArrayList<Sanitary>> {

    /**
     * Url donnant le JSON complet de toutes les sanisettes.
     * */
    private String URL_SANITARY = "http://opendata.paris.fr/explore/dataset/sanisettesparis2011/download/?format=json&timezone=Europe/Berlin";

    /**
     * Taille arbitraire des blocs pour le buffering.
     * */
    private int BLOC_SIZE = 2048;

    protected ArrayList<Sanitary> doInBackground(Void... unused)
    {
        ArrayList<Sanitary> als = new ArrayList<>();
        try {
            //Connexion
            URL url = new URL(this.URL_SANITARY);
            HttpURLConnection httpConn;
            httpConn = (HttpURLConnection) url.openConnection();

            if(httpConn.getResponseCode()!=HttpURLConnection.HTTP_OK)
                throw new RuntimeException("Impossible to download sanitaries");

            InputStream in = httpConn.getInputStream();
            BufferedReader bis = new BufferedReader(new InputStreamReader(in));

            //seul {"geom_x_y" : [ (latitude), (longitude) ]} nous intéresse.
            Pattern p = Pattern.compile("geom_x_y\\\"\\:\\ \\[([0-9.]*)\\,\\ ([0-9.]*)");
            char[] buff = new char[this.BLOC_SIZE];//parsing par blocs
            Double lati;
            Double longi;

            //get un bloc
            while (bis.read(buff) != -1)
            {
                //regex
                String s =String.valueOf(buff);
                Matcher m = p.matcher(s);

                if (m.find()) {
                    try{
                        //ajout à als
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