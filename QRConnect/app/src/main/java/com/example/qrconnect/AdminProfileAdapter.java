package com.example.qrconnect;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * The AdminProfileAdapter class makes the objects in adminProfileDataList to be able to show in the view.
 * It extends the ArrayAdapter class.
 */
public class AdminProfileAdapter extends ArrayAdapter<UserProfile> {
    private ArrayList<UserProfile> profiles;
    private Context context;

    /**
     * Constructs a AdminProfileAdapter with the specified details.
     * @param context context for the AdminProfileAdapter.
     * @param profiles events for the AdminProfileAdapter.
     */
    public AdminProfileAdapter(Context context, ArrayList<UserProfile> profiles) {
        super(context, 0, profiles);
        this.context = context;
        this.profiles = profiles;
    }

    /**
     * This makes the profiles show in the view.
     * @param position The position of the item within the adapter's data set of the item whose view
     *        we want.
     * @param convertView The old view to reuse, if possible.
     * @param parent The parent that this view will eventually be attached to
     * @return Return the view.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.list_profile, parent,false);
        }

        UserProfile profile = profiles.get(position);
        TextView profileName = view.findViewById(R.id.profile_name_text);
        profileName.setText(profile.getFirstName() + " " + profile.getLastName());

        return view;
    }
}

