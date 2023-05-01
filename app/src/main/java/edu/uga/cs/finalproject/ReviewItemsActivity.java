package edu.uga.cs.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * ReviewItemsActivity class, shows a list of review items to the user and
 * allows user to add new items to the list.
 */
public class ReviewItemsActivity extends AppCompatActivity
        implements AddItemDialogFragment.AddItemDialogListener {

    public static final String DEBUG_TAG = "ReviewItemsActivity";

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter recyclerAdapter;
    private List<Item> itemsList;
    String user = "";

    /**
     * onCreate method, called when the activity is starting
     * initialize the activity and set the layout
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // retrieve the intent that started this activity and extract the user data
        Intent intent = getIntent();
        this.user = intent.getStringExtra("user");

        Log.d(DEBUG_TAG, "ReviewItemsActivity.onCreate()");

        // set up the activity layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_items);

        // initialize the RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        // set up the FloatingActionButton and its click listener
        FloatingActionButton floatingButton = findViewById(R.id.floatingActionButton);
        floatingButton.setOnClickListener(new View.OnClickListener() {
            /**
             * onClick method, called when the FloatingActionButton has been clicked.
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                // Create a new AddItemDialogFragment and show it
                DialogFragment newFragment = new AddItemDialogFragment();
                showDialogFragment(newFragment);
            }
        });
        // set the layout manager for the RecyclerView
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // get a reference to the Firebase database and the "items" node
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("items");

        // initialize the list of items
        itemsList = new ArrayList<Item>();

        // aa a single value event listener to the database reference
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            /**
             * onDataChange method, called when the data at the specified location has changed.
             *
             * @param snapshot The current data at the location snapshot at the specified location
             */
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                // Iterate through the children of the snapshot and add each item to the list.
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Item item = postSnapshot.getValue(Item.class);
                    itemsList.add(item);
                    Log.d(DEBUG_TAG, "ReviewItemsActivity.onCreate(): added: " + item);
                }
                Log.d(DEBUG_TAG, "ReviewItemsActivity.onCreate(): setting recyclerAdapter");

                // filter out checked items and create a new list with only unchecked items
                ArrayList<Item> notCheckedItemList = new ArrayList<Item>();
                for (int i = 0; i < itemsList.size(); i++) {
                    if (!itemsList.get(i).getIsChecked()) {
                        notCheckedItemList.add(itemsList.get(i));
                    }
                }

                // set up the RecyclerView adapter and attach it to the RecyclerView
                recyclerAdapter = new ItemRecyclerAdapter(notCheckedItemList, user);
                recyclerView.setAdapter(recyclerAdapter);
            }

            /**
             * onCancelled method, called when a error occurs
             * while attempting to read data at the specified location
             *
             * @param databaseError A description of the error that occurred
             */
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Failed: " + databaseError.getMessage());
            }
        });
    }

    /**
     * onFinishNewItemDialog method, called when
     * the AddItemDialogFragment finishes and returns the new item
     *
     * @param item The new item that was added.
     */
    public void onFinishNewItemDialog(Item item) {
        // get an instance of the FirebaseDatabase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        // get a reference to the "items' node in the database
        DatabaseReference myRef = database.getReference("items");

        // add teh new item to the database
        myRef.push().setValue(item)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        itemsList.add(item);
                        // notify teh RecyclerView adapter that an item has been inserted
                        recyclerAdapter.notifyItemInserted(itemsList.size() - 1);

                        Log.d(DEBUG_TAG, "Item saved: " + item);
                        Toast.makeText(getApplicationContext(), item.getItemName() + " created",
                                Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(getApplicationContext(), "Failed to create " + item.getItemName(),
                                Toast.LENGTH_SHORT).show(); // if the item addition fails
                    }
                });
    }

    void showDialogFragment(DialogFragment newFragment) {
        newFragment.show(getSupportFragmentManager(), null);
    }
}

