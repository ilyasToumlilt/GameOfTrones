package com.toumlilt.gameoftrones;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Activité gérant la sélection de l'arme d'attaque
 * les armes seront listées dans un ListView avec les informations les concernant
 * à savoir nom, portée d'attaque et force
 * Et pour le Bonus, une image les illustrant
 */
public class WeaponsActivity extends Activity {

    /**
     * Tableau des DrawableWeapons à afficher
     */
    private DrawableWeapon ws[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weapons);

        // Creation de la liste des DrawableWeapon
        ArrayList<DrawableWeapon> tmp;
        tmp =(ArrayList<DrawableWeapon>)getIntent().getExtras().getSerializable("weapons");
        this.ws = tmp.toArray(new DrawableWeapon[tmp.size()]);

        // getting the ListView
        ListView list=(ListView)findViewById(R.id.listview);

        ArrayAdapter<Weapon> aad = new ArrayAdapter<Weapon>(this, android.R.layout.activity_list_item, this.ws){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                Context c = getContext();
                LayoutInflater inf = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View rowView = convertView;

                if(convertView == null ) {
                    rowView = inf.inflate(android.R.layout.activity_list_item, parent, false);
                    TextView textView = (TextView) rowView.findViewById(android.R.id.text1);
                    ImageView imageView = (ImageView) rowView.findViewById(android.R.id.icon);
                    textView.setText(
                            "Name : " + ws[position].getName() + " " +
                            "Damage : "+ws[position].getPv()  + " " +
                            "Range : " + ws[position].getScope()
                    );
                    imageView.setImageResource(ws[position].getImage());
                }
                return rowView;
            }
        };

        // Gestion d'un event click sur un élement de la liste
        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                // on sauvegarde la sélection.
                Context c = getApplicationContext();
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
                sp.edit().putInt("ind_curr_weapon", position).apply();
                // et on revient vers la gameActivity.
                finish();
            }
        });

        list.setAdapter(aad);
    }


}
