package com.toumlilt.gameoftrones;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Activité gérant l'enregistrement d'un joueur dans l'application.
 * Cette Activité est également le point de l'application.
 * Si un joueur est déjà enregistré, on passera directement à la GameActivity
 */
public class SignUpActivity extends AppCompatActivity {

    /**
     * Champ de saisie du pseudo
     */
    private EditText signUpEditText;
    /**
     * Bouton de validation
     */
    private Button   signUpButton;

    /**
     * Pour la musique
     */
    private static MediaPlayer mediaPlayer;

    /**
     * Pour la persistance des données du joueur
     */
    private SharedPreferences sharedPreferences;
    /**
     * Identifiant de la shared pref
     */
    public static final String USER_PREFS = "USERPREFS";
    /**
     * Clé pour le pseudo ( sharedPreferences )
     */
    public static final String PREF_USERNAME = "USERNAMEkey";
    /**
     * Clé pour le statut ( sharedPreference )
     */
    public static final String PREF_USERDESC = "USERDESCkey";
    /**
     * Clé pour le message de l'intent
     */
    public final static String EXTRA_MESSAGE = "com.toumlilt.gameottrones.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // getting UI Views
        this.signUpButton = (Button) findViewById(R.id.signUpButton);
        this.signUpEditText = (EditText) findViewById(R.id.signUpEditText);

        // Setting up the music
        mediaPlayer= MediaPlayer.create(this, R.raw.background_sound);
        mediaPlayer.start();

        // Setting up the sharedPreferences for data persistence
        this.sharedPreferences = getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);

        /* testing if a user already exists */
        if(!sharedPreferences.getString(PREF_USERNAME, "").isEmpty()){
            // if so we go directly to the GameActivity
            this.startGameActivity();
        }

    }

    /**
     * Gestion de l'évent click sur le bouton de validation
     * @param view
     */
    public void onClickSignUpButton(View view) {

        /* saving the new username */
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREF_USERNAME, signUpEditText.getText().toString());
        editor.commit();

        /* starting game activity */
        this.startGameActivity();
    }

    /**
     * Creation d'un Intent contenant les données du joueur
     * Puis démarre la GameActivity
     */
    private void startGameActivity() {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(EXTRA_MESSAGE, sharedPreferences.getString(PREF_USERNAME, ""));
        intent.putExtra(GameActivity.EXTRA_USERDESC, sharedPreferences.getString(PREF_USERDESC, ""));

        startActivity(intent);
    }
}
