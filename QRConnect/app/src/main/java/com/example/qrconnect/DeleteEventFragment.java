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

public class DeleteEventFragment extends DialogFragment {
    private Event event;
    interface DeleteEventDialogListener {
        void deleteEvent(Event event);
    }

    public DeleteEventFragment(Event event) {

        this.event = event;
    }



    public DeleteEventFragment() {}
    public DeleteEventFragment(int contentLayoutId, Event event, DeleteEventDialogListener listener) {
        super(contentLayoutId);
        this.event = event;
        this.listener = listener;
    }

    private DeleteEventDialogListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if(context instanceof DeleteEventFragment.DeleteEventDialogListener){
            listener = (DeleteEventFragment.DeleteEventDialogListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement DeleteBookDialogListener");
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
