package com.toumlilt.gameoftrones;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class WeaponsActivity extends Activity {

    private DrawableWeapon ws[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weapons);

        ArrayList<DrawableWeapon> tmp;
        tmp =(ArrayList<DrawableWeapon>)getIntent().getExtras().getSerializable("weapons");
        this.ws = tmp.toArray(new DrawableWeapon[tmp.size()]);
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


        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                Context c = getApplicationContext();
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
                sp.edit().putInt("ind_curr_weapon", position).apply();
                finish();
            }
        });

        list.setAdapter(aad);
    }


}
