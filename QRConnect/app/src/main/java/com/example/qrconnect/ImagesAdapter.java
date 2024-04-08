package com.example.qrconnect;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * The adapter of the image list
 */
public class ImagesAdapter extends ArrayAdapter<ImageInfo> {
    private Context context;

    /**
     * The constructor of this class
     * @param context the context of this class
     * @param images the list of images
     */
    public ImagesAdapter(Context context, List<ImageInfo> images) {
        super(context, 0, images);
        this.context = context;
    }

    /**
     * Display the list of images
     * @param position The position of the item within the adapter's data set of the item whose view
     *        we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *        is non-null and of an appropriate type before using. If it is not possible to convert
     *        this view to display the correct data, this method can create a new view.
     *        Heterogeneous lists can specify their number of view types, so that this View is
     *        always of the right type (see {@link #getViewTypeCount()} and
     *        {@link #getItemViewType(int)}).
     * @param parent The parent that this view will eventually be attached to
     * @return the view
     */
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
