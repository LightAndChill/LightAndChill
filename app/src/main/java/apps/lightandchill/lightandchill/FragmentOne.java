package apps.lightandchill.lightandchill;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.os.StrictMode;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.SaturationBar;



public class FragmentOne extends Fragment{

    private boolean waitColorState = false;
    protected View view = null;
    protected int state = 0;

    public FragmentOne() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if (visible && view != null) {
            Button btActivate = (Button)view.findViewById(R.id.btActivateManual);

            SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
            int defaultValue = getResources().getInteger(R.integer.activatedModeDefault);
            state = sharedPref.getInt(getString(R.string.activatedMode), defaultValue);

            switch (state) {
                case 0:
                    btActivate.setEnabled(false);
                    btActivate.setText(R.string.activated);
                    break;
                default:
                    btActivate.setEnabled(true);
                    btActivate.setText(R.string.activate);
                    break;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_one, container, false);

        // Allow HTTP request on Main Thread (for test purposes)
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        final ColorPicker picker = (ColorPicker) view.findViewById(R.id.pickerColor);
        final SaturationBar saturationBar = (SaturationBar)view.findViewById(R.id.saturationbar);
        final CheckBox cbRandom = (CheckBox) view.findViewById(R.id.cbRandom);
        final ImageView imRainbow = (ImageView)view.findViewById(R.id.imRainbow);
        final Button btActivate = (Button)view.findViewById(R.id.btActivateManual);
        final Animation slideUp = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.slide_up);
        final Animation slideDown = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.slide_down);

        final Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setStartOffset(1000);
        fadeIn.setDuration(1000);

        final Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setDuration(1000);

        imRainbow.setVisibility(View.GONE);

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        int defaultValue = getResources().getInteger(R.integer.activatedModeDefault);
        state = sharedPref.getInt(getString(R.string.activatedMode), defaultValue);
        int defaultColor = getResources().getInteger(R.integer.colorSelectedDefault);
        int colorSaved = sharedPref.getInt(getString(R.string.colorSelected), defaultColor);
        int defaultRandomChecked = getResources().getInteger(R.integer.cbRandomCheckedDefault);
        int randomChecked = sharedPref.getInt(getString(R.string.cbRandomChecked), defaultRandomChecked);
        if(randomChecked == 0){
            cbRandom.setChecked(false);
            picker.setVisibility(View.VISIBLE);
            saturationBar.setVisibility(View.VISIBLE);
        }else{
            picker.setVisibility(View.GONE);
            saturationBar.setVisibility(View.GONE);
            cbRandom.setChecked(true);
        }


        /*

        *   On vérifie dans quel mode de fonctionnement on se trouve
        **/
        switch (state) {
            case 0:
                btActivate.setEnabled(false);
                btActivate.setText(R.string.activated);
                break;
             default:
                btActivate.setEnabled(true);
                btActivate.setText(R.string.activate);
                break;
        }

        /*
            In this listener we need to block the actions raised by the event because the actions
            would be called to many times. In order to accomplish that, we use a bolean that
            locks the actions and which unlocks itself 1 second later. We call the actions once again
            at this moment in order to always have the latest data from the user
         */
        saturationBar.setOnSaturationChangedListener(new SaturationBar.OnSaturationChangedListener() {
            @Override
            public void onSaturationChanged(final int saturation) {
                if (!waitColorState && state == 0) {
                    waitColorState = true;
                    getColors(saturation);
                    saturationBar.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            getColors(saturation);
                            waitColorState = false;
                        }
                    }, 1000);
                }else if(!waitColorState && state != 0){
                    Snackbar.make(view, "Le mode manuel n'est pas actif !", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        picker.addSaturationBar(saturationBar);

        picker.setColor(colorSaved);

        picker.setOnColorSelectedListener(new ColorPicker.OnColorSelectedListener() {
            @Override
            public void onColorSelected(int color) {
                if(state == 0){
                    getColors(color);
                }else{
                    Snackbar.make(view, "Le mode manuel n'est pas actif !", Snackbar.LENGTH_SHORT).show();
                }

            }
        });

        /*
        *   On appelle l'animation pour cacher la roue et on cache le contrôle
        *   TODO : Refaire l'animation pour qu'elle fonctionne sur toutes les tailles d'écran
        **/
        cbRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(state == 0) {
                    if (cbRandom.isChecked()) {
                        picker.startAnimation(slideUp);
                        saturationBar.startAnimation(slideUp);
                        cbRandom.startAnimation(slideUp);
                        imRainbow.startAnimation(fadeIn);
                        imRainbow.setVisibility(View.VISIBLE);
                        cbRandom.postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                picker.setVisibility(View.GONE);
                                saturationBar.setVisibility(View.GONE);
                            }
                        }, 1500);
                    } else {
                        picker.setVisibility(View.VISIBLE);
                        saturationBar.setVisibility(View.VISIBLE);
                        picker.startAnimation(slideDown);
                        saturationBar.startAnimation(slideDown);
                        cbRandom.startAnimation(slideDown);
                        imRainbow.startAnimation(fadeOut);
                        imRainbow.setVisibility(View.GONE);
                    }
                    SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putInt(getString(R.string.cbRandomChecked), cbRandom.isChecked() ? 1 : 0);
                    editor.commit();
                    try {
                        //On récupère l'adresse IP entrée dans le troisème onglet
                        String defaultValue = null;
                        String strIP = sharedPref.getString(getString(R.string.PrefIP), defaultValue);

                        //Si l'IP n'est pas nulle, on tente d'envoyer les infos à l'arduino
                        if (strIP != null) {
                            int isRandom = cbRandom.isChecked() ? 1 : 0;
                            URL url = new URL("http://" + strIP + "/auto/" + isRandom);
                            HttpURLConnection net = (HttpURLConnection) url.openConnection();
                            net.setReadTimeout(10000);
                            net.setConnectTimeout(15000);
                            net.setRequestMethod("GET");
                            net.setDoInput(true);
                            net.connect();
                            Snackbar.make(view, "Random activé !", Snackbar.LENGTH_SHORT).show();
                        } else {
                            Snackbar.make(view, "Echec d'activation du mode random", Snackbar.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        Snackbar.make(view, "Fail " + e.toString(), Snackbar.LENGTH_LONG).show();
                    }
                }else{
                    cbRandom.setChecked(false);
                    Snackbar.make(view, "Le mode manuel n'est pas actif !", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        btActivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt(getString(R.string.activatedMode), 0);
                editor.commit();
                state = 0;
                btActivate.setEnabled(false);
                btActivate.setText(R.string.activated);
            }
        });

        return view;
    }

    /*
    *   Fonction permettant de récupérer la couleur venant d'être selectionnée sur
    *   le contrôle prévu à cet effet.
    **/
    private void getColors(int intColor){
        int red = Color.red(intColor);
        int green = Color.green(intColor);
        int blue = Color.blue(intColor);
        String textToDisplay = Integer.toString(red) + "/" + Integer.toString(green)
                                + "/" + Integer.toString(blue);
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.colorSelected), intColor);
        editor.commit();


        try
        {
            //On récupère l'adresse IP entrée dans le troisème onglet
            String defaultValue = null;
            String strIP = sharedPref.getString(getString(R.string.PrefIP), defaultValue);

            //Si l'IP n'est pas nulle, on tente d'envoyer les infos à l'arduino
            if(strIP != null){
                URL url = new URL("http://" + strIP +"/manual/" + textToDisplay);
                HttpURLConnection net = (HttpURLConnection)url.openConnection();
                net.setReadTimeout(10000);
                net.setConnectTimeout(15000);
                net.setRequestMethod("GET");
                net.setDoInput(true);
                net.connect();
                InputStream is = net.getInputStream();
                //String result = is.toString();
                Snackbar.make(this.getView(), textToDisplay, Snackbar.LENGTH_SHORT).show();
            }else{
                Snackbar.make(this.getView(), getString(R.string.noIP), Snackbar.LENGTH_LONG).show();
            }
        }
        catch (Exception e)
        {
            Snackbar.make(this.getView(), "Fail " + e.toString(), Snackbar.LENGTH_LONG).show();
        }
    }

}
