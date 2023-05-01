package edu.uga.cs.finalproject;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * NewItemActivity class, an Activity that is responsible for adding to the Google Firebase database
 */
public class NewItemActivity extends AppCompatActivity {


    // declare UI components
    private EditText itemNameView;
    private Button saveButton;
    DatabaseReference myRef; // Firebase Database reference

    /**
     * onCreate method, initializes the activity, sets the content view,
     * and initializes UI components and their click listeners
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);

        // Initialize UI components
        itemNameView = (EditText) findViewById(R.id.editText1);
        saveButton = (Button) findViewById(R.id.button);

        // set onClickListener for the save button
        saveButton.setOnClickListener(new ButtonClickListener());
    }

    /**
     * ButtonClickListener class, implements the View.OnClickListener
     * interface to handle button click events
     */
    private class ButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            addItem();
        }
    }

    /**
     * addItem method, responsible for creating a new item and adding it to the Firebase
     * handles success and failure events as well.
     */
    private void addItem() {
        // get Firebase reference
        myRef = FirebaseDatabase.getInstance().getReference("items");
        String id = myRef.push().getKey(); // get the unique id for the item

        String itemName = itemNameView.getText().toString();
        final Item item = new Item(itemName, id); // create a new item object with the given id and name

        // add the new item to the database
        myRef.child(id).setValue(item)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Job lead created for " + item.getItemName(),
                                Toast.LENGTH_SHORT).show();

                        itemNameView.setText("");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(getApplicationContext(), "Failed to create a Job lead for " + item.getItemName(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }


    /**
     * DatabaseInfo class, a utility class that contains static methods for getting
     * DatabaseReference and FirebaseDatabase instances
     */
    public static class DatabaseInfo {
        private static DatabaseReference myRef;
        private static FirebaseDatabase database;

        /**
         * initializes FirebaseDatabase instance
         * and DatabaseReference
         */
        public DatabaseInfo() {
            database = FirebaseDatabase.getInstance();
            myRef = database.getReference("items");
        }

        public static DatabaseReference getRef() {
            return myRef; // return DatabaseReference instance
        }

        public static FirebaseDatabase getDatabase() {
            return database; // return FirebaseDatabase instance
        }
    }
}