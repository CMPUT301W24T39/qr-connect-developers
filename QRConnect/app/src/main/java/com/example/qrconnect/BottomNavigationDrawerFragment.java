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

public class BottomNavigationDrawerFragment extends BottomSheetDialogFragment {

    private Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView= inflater.inflate(R.layout.bottom_nav_layout, container, false);

        NavigationView navigationView=rootView.findViewById(R.id.navigation_view);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                int itemId = menuItem.getItemId();
                if (itemId == R.id.navDownload) {

                    return true;
                } else if (itemId == R.id.navUpload) {

                    return true;
                } else if (itemId == R.id.navReuse) {
                    startActivity(new Intent(getActivity(), SelectEventPage.class));
                    return true;
                }
                return false;
            }
        });

        return rootView;
    }

}