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


public class NewItemActivity extends AppCompatActivity {


    private EditText itemNameView;
    private Button   saveButton;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);

        itemNameView = (EditText) findViewById( R.id.editText1 );
        saveButton = (Button) findViewById( R.id.button );

        saveButton.setOnClickListener( new ButtonClickListener()) ;
    }

    private class ButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            addItem();
        }
    }

    private void addItem() {
        myRef = FirebaseDatabase.getInstance().getReference("items");
        String id = myRef.push().getKey();

        String itemName = itemNameView.getText().toString();
        final Item item = new Item(itemName, id);

        myRef.child(id).setValue( item )
                .addOnSuccessListener( new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Job lead created for " + item.getItemName(),
                                Toast.LENGTH_SHORT).show();

                        itemNameView.setText("");
                    }
                })
                .addOnFailureListener( new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText( getApplicationContext(), "Failed to create a Job lead for " + item.getItemName(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }


    public static class DatabaseInfo {
        private static DatabaseReference myRef;
        private static FirebaseDatabase database;

        public DatabaseInfo() {
            database = FirebaseDatabase.getInstance();
            myRef = database.getReference("items");
        }

        public static DatabaseReference getRef() {
            return myRef;
        }
        public static FirebaseDatabase getDatabase() {
            return database;
        }
    }
}