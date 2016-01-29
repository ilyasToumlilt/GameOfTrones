package com.toumlilt.gameoftrones;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SignUpActivity extends AppCompatActivity {

    private Button   signUpButton;
    private EditText signUpEditText;

    public final static String EXTRA_MESSAGE = "com.toumlilt.gameottrones.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        this.signUpButton = (Button) findViewById(R.id.signUpButton);
        this.signUpEditText = (EditText) findViewById(R.id.signUpEditText);

        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.background_sound);
        mediaPlayer.start();

    }

    public void onClickSignUpButton(View view) {
        // Do something in response to signUpButton
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(EXTRA_MESSAGE, signUpEditText.getText().toString());
        startActivity(intent);
    }
}
