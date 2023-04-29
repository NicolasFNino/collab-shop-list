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

public class AddItemDialogFragment extends DialogFragment {

    private EditText nameText;
    private EditText priceText;

    public interface AddItemDialogListener {
        void onFinishNewItemDialog(Item item);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.add_item_dialog,
                (ViewGroup) getActivity().findViewById(R.id.root));

        nameText = layout.findViewById( R.id.editText1 );

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(layout);

        builder.setTitle( "New Item" );
        builder.setNegativeButton( android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton( android.R.string.ok, new ButtonClickListener() );

        return builder.create();
    }

    private class ButtonClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            String name = nameText.getText().toString();
            String id = NewItemActivity.DatabaseInfo.getRef().push().getKey();
            //String price = priceText.getText().toString();
            Item item = new Item( name, id);

            AddItemDialogListener listener = (AddItemDialogListener) getActivity();
            listener.onFinishNewItemDialog( item );
            dismiss();
        }
    }
}
