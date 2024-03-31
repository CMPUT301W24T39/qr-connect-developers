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

    public AdminDeleteEventFragment(Event event, AdminDeleteEventDialogListener listener) {
        this.event = event;
        this.listener = listener;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if(context instanceof AdminDeleteEventFragment.AdminDeleteEventDialogListener){
            listener = (AdminDeleteEventFragment.AdminDeleteEventDialogListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement AdminDeleteBookDialogListener");
        }
    }

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
