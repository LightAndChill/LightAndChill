package apps.lightandchill.lightandchill;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.OpacityBar;
import com.larswerkman.holocolorpicker.SVBar;
import com.larswerkman.holocolorpicker.SaturationBar;
import com.larswerkman.holocolorpicker.ValueBar;

import java.util.Timer;


public class FragmentOne extends Fragment{
    private boolean waitColorState = false;


    public FragmentOne() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_one, container, false);

        final ColorPicker picker = (ColorPicker) view.findViewById(R.id.pickerColor);
        final SaturationBar saturationBar = (SaturationBar)view.findViewById(R.id.saturationbar);


        /*
            In this listener we need to block the actions raised by the event because the actions
            would be called to many times. In order to accomplish that, we use a bolean that
            locks the actions and which unlocks itself 1 second later. We call the actions once again
            at this moment in order to always have the latest data from the user
         */
        saturationBar.setOnSaturationChangedListener(new SaturationBar.OnSaturationChangedListener() {
            @Override
            public void onSaturationChanged(final int saturation) {
                if (!waitColorState) {
                    waitColorState = true;
                    getColors(saturation);
                    saturationBar.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            getColors(saturation);
                            waitColorState = false;
                        }
                    }, 1000);

                }
            }
        });

        picker.addSaturationBar(saturationBar);

        //To get the color
        //picker.getColor();

        picker.setOnColorSelectedListener(new ColorPicker.OnColorSelectedListener() {
            @Override
            public void onColorSelected(int color) {
                getColors(color);
            }
        });

        return view;
    }

    private void getColors(int intColor){
        int red = Color.red(intColor);
        int green = Color.green(intColor);
        int blue = Color.blue(intColor);
        String textToDisplay = Integer.toString(red) + "/" + Integer.toString(green)
                                + "/" + Integer.toString(blue);
        Toast.makeText(getActivity(), textToDisplay, Toast.LENGTH_SHORT).show();
    }
}
