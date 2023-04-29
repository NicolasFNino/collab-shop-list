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

        public UserHolder(View view ) {
            super(view);
            email = (TextView) view.findViewById( R.id.email );
            cost = (TextView) view.findViewById( R.id.cost );
        }
    }

    @Override
    public UserHolder onCreateViewHolder( ViewGroup parent, int viewType ) {
        View view = LayoutInflater.from( parent.getContext()).inflate( R.layout.user, parent, false );
        return new UserHolder( view );
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder( UserHolder holder, int position ) {
        String user = list.get( position );
        Double cost = costs.get( position );
        Log.d( DEBUG_TAG, "onBindViewHolder: " + user );

        holder.email.setText(user);
        holder.cost.setText(cost+"");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
