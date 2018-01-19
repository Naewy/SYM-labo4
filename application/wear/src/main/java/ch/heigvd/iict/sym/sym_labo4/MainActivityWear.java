package ch.heigvd.iict.sym.sym_labo4;

import android.os.Bundle;
import android.support.wear.widget.BoxInsetLayout;
import android.support.wearable.activity.WearableActivity;

import com.bozapro.circularsliderrange.CircularSliderRange;
import com.bozapro.circularsliderrange.ThumbEvent;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import ch.heigvd.iict.sym.sym_labo4.widgets.CircularSliderRangeFixed;
import ch.heigvd.iict.sym.wearcommon.Constants;


/*
 * Allow us to send colors to the mobile application by modified sliders
 * Modified by Lara Chauffoureaux, Tano Iannetta and Wojciech Myszkorowski
 */
public class MainActivityWear extends WearableActivity {

    private static final String TAG = MainActivityWear.class.getSimpleName();

    private BoxInsetLayout mContainerView           = null;

    private CircularSliderRangeFixed redSlider      = null;
    private CircularSliderRangeFixed greenSlider    = null;
    private CircularSliderRangeFixed blueSlider     = null;

    private double startAngleRed    =     0;
    private double startAngleGreen  =     0;
    private double startAngleBlue   =     0;
    private double endAngleRed      = 90+30;
    private double endAngleGreen    = 90+60;
    private double endAngleBlue     = 90+90;

    // for API Data Layer
    private DataClient dataClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_wear);
        setAmbientEnabled();

        //link to GUI
        this.mContainerView = findViewById(R.id.container);
        this.redSlider      = findViewById(R.id.circular_red);
        this.greenSlider    = findViewById(R.id.circular_green);
        this.blueSlider     = findViewById(R.id.circular_blue);

         dataClient = Wearable.getDataClient(MainActivityWear.this);

        //events
        this.redSlider.setOnSliderRangeMovedListener(new CircularSliderRange.OnSliderRangeMovedListener() {
            @Override public void onStartSliderMoved(double pos) { /* fixed */ }

            @Override public void onEndSliderMoved(double pos) {
                endAngleRed = 90 + pos;
            }

            @Override public void onStartSliderEvent(ThumbEvent event) { }

            @Override
            public void onEndSliderEvent(ThumbEvent event) {
                updateColor();
            }
        });

        this.greenSlider.setOnSliderRangeMovedListener(new CircularSliderRange.OnSliderRangeMovedListener() {
            @Override public void onStartSliderMoved(double pos) { /* fixed */ }

            @Override public void onEndSliderMoved(double pos) {
                endAngleGreen = 90 + pos;
            }

            @Override public void onStartSliderEvent(ThumbEvent event) { }

            @Override
            public void onEndSliderEvent(ThumbEvent event) {
                updateColor();
            }
        });

        this.blueSlider.setOnSliderRangeMovedListener(new CircularSliderRange.OnSliderRangeMovedListener() {
            @Override public void onStartSliderMoved(double pos) { /* fixed */ }

            @Override public void onEndSliderMoved(double pos) {
                endAngleBlue = 90 + pos;
            }

            @Override public void onStartSliderEvent(ThumbEvent event) { }

            @Override
            public void onEndSliderEvent(ThumbEvent event) {
                updateColor();
            }
        });

        updateColor();
    }
    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        super.onEnterAmbient(ambientDetails);
        updateDisplay();
    }

    @Override
    public void onUpdateAmbient() {
        super.onUpdateAmbient();
        updateDisplay();
    }

    @Override
    public void onExitAmbient() {
        updateDisplay();
        super.onExitAmbient();
    }

    private void updateDisplay() {
        if (isAmbient()) {
            mContainerView.setBackgroundColor(getResources().getColor(android.R.color.black));
        } else {
            mContainerView.setBackground(null);
        }
    }

    /**
     *  Method called when a slider stops moving
     *  Color RGB composants are computed
     *  You need to send them to the smartphone application using DataLayer API
     */
    private void updateColor() {
        int r = (int) Math.round(255 * ((endAngleRed   - startAngleRed)   % 360) / 360.0);
        int g = (int) Math.round(255 * ((endAngleGreen - startAngleGreen) % 360) / 360.0);
        int b = (int) Math.round(255 * ((endAngleBlue  - startAngleBlue)  % 360) / 360.0);



        // Send colors
        PutDataMapRequest putDataMapRequest = PutDataMapRequest.create(Constants.COLORS);
        putDataMapRequest.getDataMap().putInt(Constants.RED, r);
        putDataMapRequest.getDataMap().putInt(Constants.GREEN, g);
        putDataMapRequest.getDataMap().putInt(Constants.BLUE, b);

        PutDataRequest putDataRequest = putDataMapRequest.asPutDataRequest();
        Task<DataItem> putTaskData = dataClient.putDataItem(putDataRequest);
    }

}
