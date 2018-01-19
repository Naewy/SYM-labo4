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

        this.buttonSimple = (Button) findViewById(R.id.button1);
        this.actionButton= (Button) findViewById(R.id.button2);
        this.buttonWearables = (Button) findViewById(R.id.button3);

        if(getIntent() != null)
            onNewIntent(getIntent());

        buttonSimple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int notificationId = 001; // The channel ID of the notification.
                String id = "my_channel_01"; // Build intent for notification content
                Intent viewIntent = new Intent(NotificationActivity.this, NotificationActivity.class);
                PendingIntent viewPendingIntent = PendingIntent.getActivity(NotificationActivity.this, 0, viewIntent, 0);

                NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(NotificationActivity.this, "001")
                        .setSmallIcon(R.drawable.ic_alert_white_18dp)
                        .setContentTitle("Toto")
                        .setContentText("Yverdon les bains de sang")
                        .setContentIntent(viewPendingIntent);

                // Get an instance of the NotificationManager service
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(NotificationActivity.this);

                // Issue the notification with notification manager.
                notificationManager.notify(notificationId, notificationBuilder.build());
            }
        });

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(Intent.ACTION_VIEW);
                String id = "my_channel_01";
                Uri geoUri = Uri.parse("geo:0,0?q=" + Uri.encode("Yverdon-les-Bains"));
                mapIntent.setData(geoUri);
                PendingIntent mapPendingIntent = PendingIntent.getActivity(NotificationActivity.this, 0, mapIntent, 0);

                NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(NotificationActivity.this, id)
                        .setSmallIcon(R.drawable.ic_lightbulb_on_white_18dp)
                        .setContentTitle("Toto2")
                        .setContentText("Yverdon-les-bains de sang")
                        .setContentIntent(mapPendingIntent)
                        .addAction(R.drawable.common_google_signin_btn_icon_light,
                            getString(R.string.app_name), mapPendingIntent);

                // Get an instance of the NotificationManager service
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(NotificationActivity.this);

                // Issue the notification with notification manager.
                notificationManager.notify(02, notificationBuilder.build());
            }
        });

        buttonWearables.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent mapIntent = new Intent(Intent.ACTION_VIEW);
                // The channel ID of the notification.
                Uri geoUri = Uri.parse("geo:0,0?q=" + Uri.encode("Yverdon-les-Bains"));
                mapIntent.setData(geoUri);
                PendingIntent mapPendingIntent =
                    PendingIntent.getActivity(NotificationActivity.this, 0, mapIntent, 0);

                String id = "my_channel_03";

                // Create a WearableExtender to add functionality for wearables
                NotificationCompat.WearableExtender wearableExtender =
                    new NotificationCompat.WearableExtender()
                        .addAction(new NotificationCompat.Action(R.drawable.common_google_signin_btn_icon_light, getString(R.string.app_name), mapPendingIntent))
                        .setHintHideIcon(true);

                // Create a NotificationCompat.Builder to build a standard notification
                // then extend it with the WearableExtender
                Notification notif = new NotificationCompat.Builder(NotificationActivity.this, id)
                        .setContentTitle("Toto3")
                        .setContentText("Yverdon-les-bains de sang")
                        .setSmallIcon(R.drawable.common_google_signin_btn_icon_light)
                        .extend(wearableExtender)
                        .build();

                // Get an instance of the NotificationManager service
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(NotificationActivity.this);

                // Issue the notification with notification manager.
                notificationManager.notify(02, notif);

            }
        });
    }

    /* A IMPLEMENTER */

    /*
     *  Code fourni pour les PendingIntent
     */

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
