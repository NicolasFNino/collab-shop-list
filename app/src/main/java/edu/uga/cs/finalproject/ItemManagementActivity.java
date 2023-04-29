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

public class ItemManagementActivity extends AppCompatActivity {

    private static final String DEBUG_TAG = "ItemManagement";

    private Button newButton;
    private Button reviewItemsButton;
    private Button reviewPurchaseButton;
    private TextView signedInTextView;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_management);

        newButton = findViewById( R.id.button1 );
        reviewItemsButton = findViewById( R.id.button2 );
        reviewPurchaseButton = findViewById( R.id.button4 );
        signedInTextView = findViewById( R.id.textView3 );

        newButton.setOnClickListener( new NewItemButtonClickListener() );
        reviewItemsButton.setOnClickListener( new ReviewItemsButtonClickListener() );
        reviewPurchaseButton.setOnClickListener( new ReviewPurchasedButtonClickListener() );

        FirebaseAuth.getInstance().addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                if( currentUser != null ) {
                    Log.d(DEBUG_TAG, "user signed in:" + currentUser.getUid());
                    String userEmail = currentUser.getEmail();
                    signedInTextView.setText( "Welcome, " + userEmail+"! " );
                } else {
                    Log.d( DEBUG_TAG, "user signed out" );
                    signedInTextView.setText( "No user signed in :(" );
                }
            }
        });
    }

    private class NewItemButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), NewItemActivity.class);
            view.getContext().startActivity( intent );
        }
    }

    private class ReviewItemsButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), ReviewItemsActivity.class);
            view.getContext().startActivity(intent);
        }
    }

    private class ReviewPurchasedButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), ReviewPurchasedActivity.class);
            view.getContext().startActivity(intent);
        }
    }
}
