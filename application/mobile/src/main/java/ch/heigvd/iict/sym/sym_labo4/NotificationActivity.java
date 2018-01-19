package ch.heigvd.iict.sym.sym_labo4;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.RemoteInput;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import ch.heigvd.iict.sym.wearcommon.Constants;

public class NotificationActivity extends AppCompatActivity {

    private Button buttonSimple;
    private Button actionButton;
    private Button buttonWearables;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        // Link to GUI
        this.buttonSimple = (Button) findViewById(R.id.button1);
        this.actionButton= (Button) findViewById(R.id.button2);
        this.buttonWearables = (Button) findViewById(R.id.button3);

        if(getIntent() != null) {
            onNewIntent(getIntent());
        }

        // Listener associated with the "simple" notification button
        buttonSimple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int notificationId = 1;
                String id = getString(R.string.channel_1); // The channel ID of the notification

                // Building of the intent for the notification content
                Intent viewIntent = new Intent(NotificationActivity.this, NotificationActivity.class);
                PendingIntent viewPendingIntent = PendingIntent.getActivity(NotificationActivity.this, 0, viewIntent, 0);

                // Builder of notification layout
                NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(NotificationActivity.this, id)
                        .setSmallIcon(R.drawable.ic_lightbulb_on_white_18dp)
                        .setContentTitle(getString(R.string.notif_title_1))
                        .setContentText(getString(R.string.notif_content_1))
                        .setContentIntent(viewPendingIntent);

                // Get an instance of the NotificationManager service
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(NotificationActivity.this);

                // Issue the notification with notification manager.
                notificationManager.notify(notificationId, notificationBuilder.build());
            }
        });

        // Listener associated with the "action" notification button
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int notificationId = 2;
                String id = getString(R.string.channel_2); // The channel ID of the notification

                // Preparation on an intent to view the map
                Intent mapIntent = new Intent(Intent.ACTION_VIEW);
                Uri geoUri = Uri.parse("geo:0,0?q=" + Uri.encode("Yverdon-les-Bains"));
                mapIntent.setData(geoUri);
                PendingIntent mapPendingIntent = PendingIntent.getActivity(NotificationActivity.this, 0, mapIntent, 0);

                // Builder of notification layout
                NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(NotificationActivity.this, id)
                        .setSmallIcon(R.drawable.ic_lightbulb_on_white_18dp)
                        .setContentTitle(getString(R.string.notif_title_2))
                        .setContentText(getString(R.string.notif_content_2))
                        .setContentIntent(mapPendingIntent) // Add the action in the notification
                        .addAction(R.drawable.common_full_open_on_phone,
                            getString(R.string.app_name), mapPendingIntent);

                // Get an instance of the NotificationManager service
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(NotificationActivity.this);

                // Issue the notification with notification manager.
                notificationManager.notify(notificationId, notificationBuilder.build());
            }
        });

        // Listener associated with the "wearable-only" notification button
        buttonWearables.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int notificationId = 3;
                String id = getString(R.string.channel_3); // The channel ID of the notification

                // Preparation on an intent to view the map
                Intent mapIntent = new Intent(Intent.ACTION_VIEW);
                Uri geoUri = Uri.parse("geo:0,0?q=" + Uri.encode("Yverdon-les-Bains"));
                mapIntent.setData(geoUri);
                PendingIntent mapPendingIntent = PendingIntent.getActivity(NotificationActivity.this, 0, mapIntent, 0);

                // Create a WearableExtender to add functionality for only for wearables
                NotificationCompat.WearableExtender wearableExtender =
                    new NotificationCompat.WearableExtender()
                        .addAction(new NotificationCompat.Action(R.drawable.common_full_open_on_phone, getString(R.string.app_name), mapPendingIntent))
                        .setHintHideIcon(true);

                // Builder of notification layout
                Notification notification = new NotificationCompat.Builder(NotificationActivity.this, id)
                        .setContentTitle(getString(R.string.notif_title_3))
                        .setContentText(getString(R.string.notif_content_3))
                        .setSmallIcon(R.drawable.ic_lightbulb_on_white_18dp)
                        .extend(wearableExtender)
                        .build();

                // Get an instance of the NotificationManager service
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(NotificationActivity.this);

                // Issue the notification with notification manager.
                notificationManager.notify(notificationId, notification);
            }
        });
    }

    /*
     *  Method called by system when a new Intent is received
     *  Display a toast with a message if the Intent is generated by
     *  createPendingIntent method.
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(intent == null) return;
        if(Constants.MY_PENDING_INTENT_ACTION.equals(intent.getAction()))
            Toast.makeText(this, "" + intent.getStringExtra("msg"), Toast.LENGTH_SHORT).show();
    }

    /**
     * Method used to create a PendingIntent with the specified message
     * The intent will start a new activity Instance or bring to front an existing one.
     * See parentActivityName and launchMode options in Manifest
     * See https://developer.android.com/training/notify-user/navigation.html for TaskStackBuilder
     * @param requestCode The request code
     * @param message The message
     * @return The pending Intent
     */
    private PendingIntent createPendingIntent(int requestCode, String message) {
        Intent myIntent = new Intent(NotificationActivity.this, NotificationActivity.class);
        myIntent.setAction(Constants.MY_PENDING_INTENT_ACTION);
        myIntent.putExtra("msg", message);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(NotificationActivity.class);
        stackBuilder.addNextIntent(myIntent);

        return stackBuilder.getPendingIntent(requestCode, PendingIntent.FLAG_UPDATE_CURRENT);
    }

}
