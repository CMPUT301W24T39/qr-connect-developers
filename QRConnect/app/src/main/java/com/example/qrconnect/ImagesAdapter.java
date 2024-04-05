package com.example.qrconnect;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.qrconnect.ImageInfo;

import java.util.List;

public class ImagesAdapter extends ArrayAdapter<ImageInfo> {
    private Context context;
    private List<ImageInfo> images;

    public ImagesAdapter(Context context, List<ImageInfo> images) {
        super(context, 0, images);
        this.context = context;
        this.images = images;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.admin_list_image, parent, false);
        }

        ImageInfo imageInfo = getItem(position);

        ImageView imageView = convertView.findViewById(R.id.image_view);

        Glide.with(context).load(imageInfo.getUrl()).into(imageView);

        return convertView;
    }
}
