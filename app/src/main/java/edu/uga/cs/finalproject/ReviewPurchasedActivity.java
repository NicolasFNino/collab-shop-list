package edu.uga.cs.finalproject;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
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
 * ReviewPurchasedActivity class, to show the list of items that have added to the shopping list
 * not yet purchased.
 */
public class ReviewPurchasedActivity extends AppCompatActivity
        implements AddItemDialogFragment.AddItemDialogListener {

    public static final String DEBUG_TAG = "ReviewItemsActivity";

    private RecyclerView recyclerView; // the view of RecyclerView to show the list of items
    private RecyclerView.LayoutManager layoutManager; // manage layout of the RecyclerView
    private RecyclerView.Adapter recyclerAdapter; // adapter to handle the data for the RecyclerView

    private Button settleButton; // settle button to navigate to the SettleActivity

    private List<Item> list; // for holding items
    String user = "";

    /**
     * onCreate method, to initializes the activity, retrieves the list of items from the Google Firebase
     * and configures the RecyclerView and buttons
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent intent = getIntent(); // get user data
        this.user = intent.getStringExtra("user");

        Log.d(DEBUG_TAG, "ReviewItemsActivity.onCreate()");

        // set up the activity layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_items);

        // initialize RecyclerView and SettleButton
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        settleButton = (Button) findViewById(R.id.settleButton);
        // set click listener for the settle button
        settleButton.setOnClickListener(new ReviewPurchasedActivity.SettleButtonClickListener());

        // the floating button to show AddItemDialogFragment
        FloatingActionButton floatingButton = findViewById(R.id.floatingActionButton);
        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new AddItemDialogFragment();
                showDialogFragment(newFragment);
            }
        });
        // set up the layout manager of the RecyclerView
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // initialize Firebase database and reference
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("items");

        list = new ArrayList<Item>(); // initialize the list of items

        // get items from Firebase
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // iterate to get all items in the snapshot and add them to the list
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Item item = postSnapshot.getValue(Item.class);
                    list.add(item);
                    Log.d(DEBUG_TAG, "ReviewItemsActivity.onCreate(): added: " + item);
                }
                Log.d(DEBUG_TAG, "ReviewItemsActivity.onCreate(): setting recyclerAdapter");

                // filter the list the show only checked and not purchased items
                ArrayList<Item> checkedItemList = new ArrayList<Item>();
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getIsChecked() && !list.get(i).getPurchased()) {
                        checkedItemList.add(list.get(i));
                    }
                }
                // set up the recycler adapter and assign it to the RecyclerView
                recyclerAdapter = new ItemRecyclerAdapter(checkedItemList);
                recyclerView.setAdapter(recyclerAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });
    }

    /**
     * onFinishNewItemDialog method, to handle the result of the AddItemDialogFragment and adds the
     * new item to Firebase.
     *
     * @param item The new item that was added.
     */
    public void onFinishNewItemDialog(Item item) {
        // get the instance of the FirebaseDatabase, and a reference to the "items"
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("items");

        myRef.push().setValue(item)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        list.add(item); // add a new item
                        recyclerAdapter.notifyItemInserted(list.size() - 1);

                        Log.d(DEBUG_TAG, "Item saved: " + item);
                        Toast.makeText(getApplicationContext(), item.getItemName() + " created",
                                Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(getApplicationContext(), "Failed to create " + item.getItemName(),
                                Toast.LENGTH_SHORT).show();// Failure of adding a new item
                    }
                });
    }

    /**
     * SettleButtonClickListener class, to handle clicks on the settlement button and transit to
     * SettleActivity
     */
    public static class SettleButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), SettleActivity.class);
            view.getContext().startActivity(intent);
        }
    }

    /**
     * showDialogFragment method, to show a dialog fragment
     *
     * @param newFragment the DialogFragment to be showed.
     */
    void showDialogFragment(DialogFragment newFragment) {
        newFragment.show(getSupportFragmentManager(), null);
    }
}

