package edu.uga.cs.finalproject;

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

/**
 * ItemRecyclerAdapter class, a custom adapter for showing a list of items using RecyclerView
 */
public class ItemRecyclerAdapter extends RecyclerView.Adapter<ItemRecyclerAdapter.ItemHolder> {

    public static final String DEBUG_TAG = "ItemRecyclerAdapter";

    private List<Item> list;
    String user = "";

    public ItemRecyclerAdapter(List<Item> list) {
        this.list = list;
    }

    public ItemRecyclerAdapter(List<Item> list, String u) {
        this.list = list;
        this.user = u;
    }

    /**
     * ViewHolder class, for the individual items.
     */
    class ItemHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView price;
        CheckBox checked;
        Button updateButton;

        public ItemHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.itemName);
            price = (TextView) view.findViewById(R.id.itemPrice);
            checked = view.findViewById(R.id.checkItem);
            updateButton = view.findViewById(R.id.button_update);
        }
    }

    /**
     * onCreateViewHolder method, called when RecycleView needs a new ViewHolder of the given type to represent an item
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return view
     */
    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new ItemHolder(view);
    }

    /**
     * onBindViewHolder method, called by RecyclerView to show the data at the specified position
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        Item item = list.get(position);
        NewItemActivity.DatabaseInfo myDatabase = new NewItemActivity.DatabaseInfo();
        Log.d(DEBUG_TAG, "onBindViewHolder: " + item);

        holder.name.setText(item.getItemName());
        System.out.println(item.getPrice());
        holder.price.setText(item.getPrice());
        if (holder.checked.isChecked() && !item.getIsChecked()) {
            holder.checked.toggle();
        } else if (!holder.checked.isChecked() && item.getIsChecked()) {
            holder.checked.toggle();
        }
        holder.updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item.setPrice(holder.price.getText().toString());
                myDatabase.getRef().child(item.getItemId()).child("price").setValue(holder.price.getText().toString());
                myDatabase.getRef().child(item.getItemId()).child("user").setValue(user);
                if (holder.checked.isChecked()) {
                    item.setIsChecked(true);
                    myDatabase.getRef().child(item.getItemId()).child("isChecked").setValue(true);
                } else {
                    item.setIsChecked(false);
                    myDatabase.getRef().child(item.getItemId()).child("isChecked").setValue(false);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}