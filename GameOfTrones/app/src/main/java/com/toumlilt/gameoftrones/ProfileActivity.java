package com.toumlilt.gameoftrones;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ProfileActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    public static final String USER_PREFS = "USERPREFS";
    public static final String PREF_USERNAME = "USERNAMEkey";
    public static final String PREF_USERDESC = "USERDESCkey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        this.sharedPreferences = getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
        EditText usernameET = (EditText) findViewById(R.id.profileUsername);
        usernameET.setText(sharedPreferences.getString(PREF_USERNAME, ""));
        EditText userdescET = (EditText) findViewById(R.id.profileUserdesc);
        userdescET.setText(sharedPreferences.getString(PREF_USERDESC, ""));
    }

    public void onClickProfileSaveButton(View view) {
        /* fetching Textviews */
        EditText usernameET = (EditText) findViewById(R.id.profileUsername);
        EditText userdescET = (EditText) findViewById(R.id.profileUserdesc);

        /* saving shared preference */
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREF_USERNAME, usernameET.getText().toString());
        editor.putString(PREF_USERDESC, userdescET.getText().toString());
        editor.commit();

        /* releasing request with result */
        Intent intent = new Intent();
        intent.putExtra(GameActivity.EXTRA_USERNAME,
                usernameET.getText().toString());
        intent.putExtra(GameActivity.EXTRA_USERDESC,
                userdescET.getText().toString());
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

}
