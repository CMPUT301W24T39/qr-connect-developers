package com.example.qrconnect;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


/**
 * The BottomNavigationDrawerFragment class manages the drop down menu after long clicks on a QR code.
 * It extends BottomSheetDialogFragment.
 */
public class BottomNavigationDrawerFragment extends BottomSheetDialogFragment {

    /**
     * This maintains the function in the drop down menu.
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to. The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     * @return Return a view for the drop down menu.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView= inflater.inflate(R.layout.bottom_nav_layout, container, false);

        NavigationView navigationView=rootView.findViewById(R.id.navigation_view);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                int itemId = menuItem.getItemId();
                if (itemId == R.id.nav_download) {

                    return true;
                } else if (itemId == R.id.nav_upload) {

                    return true;
                } else if (itemId == R.id.nav_turn_right){
                    startActivity(new Intent(getActivity(), SelectEventPage.class));
                    return true;
                }
                return false;
            }
        });

        return rootView;
    }
}