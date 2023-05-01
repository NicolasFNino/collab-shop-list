package edu.uga.cs.finalproject;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

/**
 * MainActivity class, the starting activity of the application
 * the user options to signin and register
 */
public class MainActivity extends AppCompatActivity {

    private static final String DEBUG_TAG = "MainActivity";

    /**
     * initializes the activity set the default theme, and configures the signin and register buttons
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // set the night mode as the default theme for the activity
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(DEBUG_TAG, "Item: MainActivity.onCreate()");

        // initialize the signin and the buttons
        Button signInButton = findViewById(R.id.button1);
        Button registerButton = findViewById(R.id.button2);

        // set click listener for the buttons
        signInButton.setOnClickListener(new SignInButtonClickListener());
        registerButton.setOnClickListener(new RegisterButtonClickListener());
    }

    /**
     * a custom click listener for the signin button that launches the signin process using FirebaseUI
     */
    private class SignInButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            List<AuthUI.IdpConfig> providers = Arrays.asList(new AuthUI.IdpConfig.EmailBuilder().build());

            Log.d(DEBUG_TAG, "MainActivity.SignInButtonClickListener: Signing in started");

            Intent signInIntent = AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers).setTheme(R.style.LoginTheme).build();
            signInLauncher.launch(signInIntent);
        }
    }

    private ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(new FirebaseAuthUIActivityResultContract(), new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
        @Override
        public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
            onSignInResult(result);
        }
    });

    /**
     * Handles the result of the signin process and either starts the ItemManagementActivity or
     * shows an error message
     * @param result the FirebaseAuthUIAuthenticationResult containing the result of the signin process
     */
    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            if (response != null) {
                Log.d(DEBUG_TAG, "MainActivity.onSignInResult: response.getEmail(): " + response.getEmail());
            }
            Intent intent = new Intent(this, ItemManagementActivity.class);
            startActivity(intent);
        } else {
            Log.d(DEBUG_TAG, "MainActivity.onSignInResult: Failed to sign in");
            Toast.makeText(getApplicationContext(), "Sign in failed", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * A custom click listener for the register button that launches the RegisterActivity
     */
    private class RegisterButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), RegisterActivity.class);
            view.getContext().startActivity(intent);
        }
    }
}
