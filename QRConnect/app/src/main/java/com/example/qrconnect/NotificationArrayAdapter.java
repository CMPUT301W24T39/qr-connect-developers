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
 * The NotificationArrayAdapter class makes the objects in notificationDataList to be able to show in the view.
 * It extends the ArrayAdapter class.
 */
public class NotificationArrayAdapter extends ArrayAdapter<Notification> {
    private ArrayList<Notification> notifications;
    private Context context;

    /**
     * Constructs a NotificationArrayAdapter with the specified details.
     * @param context context for the NotificationArrayAdapter.
     * @param notifications notifications for the NotificationArrayAdapter.
     */
    public NotificationArrayAdapter(Context context, ArrayList<Notification> notifications){
        super(context,0, notifications);
        this.notifications = notifications;
        this.context = context;
    }

    /**
     * This makes the notifications show in the view.
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
            view = LayoutInflater.from(context).inflate(R.layout.notification, parent,false);
        }

        Notification notification = notifications.get(position);

        // Get event, title, and description for the notification view
        TextView notificationEvent = view.findViewById(R.id.notification_event);
        TextView notificationDate = view.findViewById(R.id.notification_date);
        TextView notificationTitle = view.findViewById(R.id.notification_title);
        TextView notificationDescription = view.findViewById(R.id.notification_description);

        // Set event, title, and description for the notification view
        notificationEvent.setText("From: " + notification.getNotificationEvent());
        notificationDate.setText("Date: " + notification.getNotificationDate());
        notificationTitle.setText(notification.getNotificationTitle());
        notificationDescription.setText(notification.getNotificationDescription());

        return view;
    }
}
