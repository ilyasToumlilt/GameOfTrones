package com.toumlilt.gameoftrones;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class WeaponsActivity extends Activity {

    private Weapon ws[];

    public WeaponsActivity(){
        this(new Weapon[]{});
    }

    public WeaponsActivity(Weapon[] ws){
        this.ws = ws;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weapons);

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
                            "Arme : " + ws[position].getName() + " " +
                            "PV : "+ws[position].getPv()  + " " +
                            "Scope : " + ws[position].getScope()
                    );
                    imageView.setImageResource(android.R.drawable.btn_star);
                }

                return rowView;
            }

        };
        ListView list=(ListView)findViewById(R.id.listview);
        list.setAdapter(aad);
    }

}
