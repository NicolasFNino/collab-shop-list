package edu.uga.cs.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * RegisterActivity class, handles user registration using Firebase Authentication
 */
public class RegisterActivity extends AppCompatActivity {

    private static final String DEBUG_TAG = "RegisterActivity";

    private EditText emailText;
    private EditText passText;
    private Button submitButton;

    /**
     * onCreate method, called when the activity is starting
     * initializes the UI elements and sets up the click listener for the submit button.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailText = (EditText) findViewById(R.id.editText);
        passText = (EditText) findViewById(R.id.editText5);

        // set up the click listener for the submit button
        submitButton = (Button) findViewById(R.id.button3);
        submitButton.setOnClickListener(new RegisterButtonClickListener());
    }

    /**
     * RegisterButtonClickListener class, a click listener for the submit button
     * validates and processes user registration using Firebase Authentication.
     */
    private class RegisterButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            // get the input values
            final String email = emailText.getText().toString();
            final String password = passText.getText().toString();

            // get the instance of FirebaseAuth
            final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

            // register the new user using FirebaseAuth
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                Toast.makeText(getApplicationContext(),
                                        "Successful registration: " + email,
                                        Toast.LENGTH_SHORT).show(); // check if the registration is successful

                                Log.d(DEBUG_TAG, "createUserWithEmail: success");

                                FirebaseUser user = firebaseAuth.getCurrentUser(); // get the instance of FirebaseUser

                                // move to the next activity (ItemManagementActivity)
                                Intent intent = new Intent(RegisterActivity.this, ItemManagementActivity.class);
                                startActivity(intent);

                            } else {
                                Log.w(DEBUG_TAG, "There was an error registering the user", task.getException());
                                Toast.makeText(RegisterActivity.this, "Registration failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }

                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("exception", e.getMessage()); // called when the registration process encounters a failure
                        }
                    });
        }
    }
}
