package edu.uga.cs.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * ItemManagementActivity class, an activity that allows users to manage shopping items
 * Users can add new items, review existing items, or review purchase items.
 */
public class ItemManagementActivity extends AppCompatActivity {

    private static final String DEBUG_TAG = "ItemManagement";

    private Button newButton;
    private Button reviewItemsButton;
    private Button reviewPurchaseButton;
    private TextView signedInTextView;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    /**
     * onCreate method, initializes the layout and buttons for the ItemManagementActivity
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_management);

        // initialize Buttons and TextView
        newButton = findViewById(R.id.button1);
        reviewItemsButton = findViewById(R.id.button2);
        reviewPurchaseButton = findViewById(R.id.button4);
        signedInTextView = findViewById(R.id.textView3);

        // set onClick listeners for buttons
        newButton.setOnClickListener(new NewItemButtonClickListener());
        reviewItemsButton.setOnClickListener(new ReviewItemsButtonClickListener());
        reviewPurchaseButton.setOnClickListener(new ReviewPurchasedButtonClickListener());

        // initialize FirebaseAuth instance and set AuthStateListener
        FirebaseAuth.getInstance().addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser = firebaseAuth.getCurrentUser();
                if (currentUser != null) {
                    Log.d(DEBUG_TAG, "user signed in:" + currentUser.getUid());
                    String userEmail = currentUser.getEmail();
                    signedInTextView.setText("Welcome, " + userEmail + "! ");
                } else {
                    Log.d(DEBUG_TAG, "user signed out");
                    signedInTextView.setText("No user signed in :(");
                }
            }
        });
    }

    /**
     * NewItemButtonClickListener handles clicks on the "New Item" button.
     */
    private class NewItemButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), NewItemActivity.class);
            view.getContext().startActivity(intent);
        }
    }

    /**
     * ReviewItemButtonClickListener handles clicks on the "Review Items" button
     */
    private class ReviewItemsButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            // Start ReviewItemActivity and pass the current user's email
            Intent intent = new Intent(view.getContext(), ReviewItemsActivity.class);
            intent.putExtra("user", currentUser.getEmail());
            view.getContext().startActivity(intent);
        }
    }

    /**
     * ReviewPurchasedButtonClickListener handles clicks on the "Review Purchased" button.
     */
    private class ReviewPurchasedButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), ReviewPurchasedActivity.class);
            intent.putExtra("user", currentUser.getEmail());
            view.getContext().startActivity(intent);
        }
    }
}
