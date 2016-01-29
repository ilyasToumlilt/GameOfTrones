package com.toumlilt.gameoftrones;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
    }

    public void onClickProfileSaveButton(View view) {
        Intent intent = new Intent();
        EditText usernameET = (EditText) findViewById(R.id.profileUsername);
        intent.putExtra(GameActivity.EXTRA_USERNAME,
                usernameET.getText().toString());
        EditText userdescET = (EditText) findViewById(R.id.profileUserdesc);
        intent.putExtra(GameActivity.EXTRA_USERDESC,
                userdescET.getText().toString());
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

}
