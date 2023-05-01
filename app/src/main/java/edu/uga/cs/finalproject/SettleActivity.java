package edu.uga.cs.finalproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * SettleActivity class, to show a list of costs for each user and the total cost of all items
 * it reads the items data from the Google Firebase and computes the costs per user and total cost.
 */
public class SettleActivity extends AppCompatActivity {
    private static final String DEBUG_TAG = "SettleActivity";
    private RecyclerView recyclerView;
    private TextView averageCost;
    private TextView totalCost;
    private TextView title;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter recyclerAdapter;
    private List<String> usersList; // list of user emails
    private List<Double> costsList; //list of costs per user

    // the Google Firebase authentication and user objects
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    /**
     * onCreate method, is called when the activity is starting.
     * initializes the activity's user interface and sets up the RecyclerView,
     * TextViews and LayoutManager.
     * It retrieves the items from the database*
     * and calculates the costs per user for the shopping trip.
     * It sets the appropriate TextViews and Adapter for the RecyclerView.
     *
     * @param savedInstanceState the Bundle containing the activity's previously saved state, or null
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settle);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        averageCost = (TextView) findViewById(R.id.averageCost);
        totalCost = (TextView) findViewById(R.id.totalCost);
        title = (TextView) findViewById(R.id.usersTitle);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("items");

        usersList = new ArrayList<String>();
        costsList = new ArrayList<Double>();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DecimalFormat numberFormat = new DecimalFormat("#.00");
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Item item = postSnapshot.getValue(Item.class);
                    assert item != null;
                    String tempUser = item.getUser();
                    if (!usersList.contains(tempUser)) {
                        usersList.add(tempUser);
                        costsList.add(0.0);
                    }
                    postSnapshot.child("purchased").getRef().setValue(true);
                }
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Item item = postSnapshot.getValue(Item.class);
                    assert item != null;
                    String tempUser = item.getUser();
                    if (usersList.contains(tempUser)) {
                        int index = usersList.indexOf(tempUser);
                        try {
                            double temp = costsList.get(index) + Double.parseDouble(item.getPrice());
                            costsList.set(index, Double.parseDouble(numberFormat.format(temp)));
                        } catch (Exception e) {

                        }
                    }
                }
                double helper = 0.0;
                for (Double d : costsList) {
                    helper += d;
                }
                double helper2 = helper / usersList.size();


                System.out.println(Arrays.toString(usersList.toArray()));
                System.out.println(Arrays.toString(costsList.toArray()));

                totalCost.setText("Total cost: $" + numberFormat.format(helper));
                averageCost.setText("Average cost per user: $" + numberFormat.format(helper2));
                title.setText("Cost per user:");
                recyclerAdapter = new UserRecyclerAdapter(usersList, costsList);
                recyclerView.setAdapter(recyclerAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    } // onCreate

}
