package com.example.qrconnect;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AdminImageAdapter extends ArrayAdapter<UserProfile> {
    private ArrayList<UserProfile> images;
    private Context context;

    public AdminImageAdapter(Context context, ArrayList<UserProfile> images) {
        super(context, 0, images);
        this.context = context;
        this.images = images;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_image, parent, false);

        }

        UserProfile user = images.get(position);
        TextView userName = view.findViewById(R.id.image_name_text);
        ImageView image = view.findViewById(R.id.admin_view_image);
        userName.setText(user.getFirstName() + " " + user.getLastName());
        if (user.getProfilePictureUploaded()) {
            Glide.with(this.getContext()).load(user.getProfilePictureURL()).into(image);
        } else {
            image.setImageBitmap(AvatarGenerator.generateAvatar(user));
        }

        return view;
    }
}
