package com.example.qrconnect;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

/**
 * The AdminDeleteEventFragment class is the fragment displayed when trying to delete an event as a admin.
 */
public class AdminDeleteEventFragment extends DialogFragment {
    private Event event;
    private AdminDeleteEventFragment.AdminDeleteEventDialogListener listener;
    interface AdminDeleteEventDialogListener {
        void deleteEvent(Event event);
    }

    /**
     * Constructor to store the attribute of the class
     * @param event the event to be delete
     * @param listener a listener to pop up the fragment
     */
    public AdminDeleteEventFragment(Event event, AdminDeleteEventDialogListener listener) {
        this.event = event;
        this.listener = listener;
    }

    /**
     * Set up listener
     * @param context the context of the class
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if(context instanceof AdminDeleteEventFragment.AdminDeleteEventDialogListener){
            listener = (AdminDeleteEventFragment.AdminDeleteEventDialogListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement AdminDeleteBookDialogListener");
        }
    }

    /**
     * Create a dialog
     * @param savedInstanceState The last saved instance state of the Fragment,
     * or null if this is a freshly created Fragment.
     *
     * @return a dialog
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_delete_event,null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        return builder
                .setView(view)
                .setTitle("Delete an event")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Delete", (dialog, which) -> {

                    if (event != null){
                        listener.deleteEvent(event);
                    }
                })
                .create();
    }
}
