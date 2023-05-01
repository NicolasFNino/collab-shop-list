package edu.uga.cs.finalproject;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

/**
 * A dialog fragment for adding a new item to the shopping list
 */
public class AddItemDialogFragment extends DialogFragment {

    private EditText nameText;

    /**
     * Interface for communicating with the calling Activity
     */
    public interface AddItemDialogListener {
        /**
         * Called when a new item is added to the shopping list.
         *
         * @param item The new item that was added.
         */
        void onFinishNewItemDialog(Item item);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Inflate the layout for the dialog
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.add_item_dialog,
                (ViewGroup) getActivity().findViewById(R.id.root));

        // get a reference to the EditText view for the item name
        nameText = layout.findViewById(R.id.editText1);

        // create a new AlertDialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(layout); // set the custom layout fot the dialog

        builder.setTitle("New Item"); // set the title for the dialog
        // add a "Cancel" button to the dialog
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        // add an "OK" button to the dialog
        builder.setPositiveButton(android.R.string.ok, new ButtonClickListener());

        return builder.create(); // return the dialog
    }

    /**
     * Listener for the "OK" button in teh dialog
     */
    private class ButtonClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            // Get the text from the EditText view
            String name = nameText.getText().toString();
            // Generate a new ID for the item using Google Firebase
            String id = NewItemActivity.DatabaseInfo.getRef().push().getKey();
            Item item = new Item(name, id); // a new Item object

            // Get a reference to the calling Activity and cast it to the AddItemDialogListener interface
            AddItemDialogListener listener = (AddItemDialogListener) getActivity();
            // call the onFinishNewItemDialog method on the calling activity and pass in the new Item object
            listener.onFinishNewItemDialog(item);
            dismiss();
        }
    }
}
