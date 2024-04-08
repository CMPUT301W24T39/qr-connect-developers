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
 * The AdminDeleteProfileFragment class is the fragment displayed when trying to delete a profile as a admin.
 */
public class AdminDeleteProfileFragment extends DialogFragment {
    private String userId;
    private AdminDeleteProfileFragment.AdminDeleteProfileDialogListener listener;
    interface AdminDeleteProfileDialogListener {

        void deleteProfile(String profile);
    }

    /**
     * Constructor for the AdminDeleteProfileFragment.
     * @param userId the id of the user profile that was selected.
     * @param listener listener for the delete fragment.
     */
    public AdminDeleteProfileFragment(String userId, AdminDeleteProfileDialogListener listener) {
        this.userId = userId;
        this.listener = listener;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if(context instanceof AdminDeleteProfileFragment.AdminDeleteProfileDialogListener){
            listener = (AdminDeleteProfileFragment.AdminDeleteProfileDialogListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement AdminDeleteBookDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_delete_profile,null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        return builder
                .setView(view)
                .setTitle("Delete a profile")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Delete", (dialog, which) -> {

                    if (userId != null){
                        listener.deleteProfile(userId);
                    }
                })
                .create();
    }
}