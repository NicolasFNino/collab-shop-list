package edu.uga.cs.finalproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.List;
import java.util.function.DoubleUnaryOperator;


/**
 * RecyclerView class, an adapter for displaying the list of users and their respective costs.
 */
public class UserRecyclerAdapter extends RecyclerView.Adapter<UserRecyclerAdapter.UserHolder> {

    public static final String DEBUG_TAG = "UserRecyclerAdapter";

    private List<String> list;
    private List<Double> costs;
    String user = "";

    public UserRecyclerAdapter(List<String> list, List<Double> costs) {
        this.list = list;
        this.costs = costs;
    }

    class UserHolder extends RecyclerView.ViewHolder {

        TextView email;
        TextView cost;

        public UserHolder(View view) {
            super(view);
            email = (TextView) view.findViewById(R.id.email);
            cost = (TextView) view.findViewById(R.id.cost);
        }
    }

    /**
     * onCreateViewHolder method, Called when RecyclerView needs a new RecyclerView.
     * ViewHolder of the given type to represent an item.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return a UserHolder type view
     */
    @Override
    public UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user, parent, false);
        return new UserHolder(view);
    }

    /**
     * onBindViewHolder method, Called by RecyclerView to display the data at the specified position.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(UserHolder holder, int position) {
        String user = list.get(position);
        Double cost = costs.get(position);
        Log.d(DEBUG_TAG, "onBindViewHolder: " + user);

        holder.email.setText(user);
        holder.cost.setText(cost + "");
    }

    /**
     * getItemCount method, Returns the total number of items in the data set held by the adapter.
     *
     * @return the size of list
     */
    @Override
    public int getItemCount() {
        return list.size();
    }
}
