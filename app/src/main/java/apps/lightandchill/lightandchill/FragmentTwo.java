package apps.lightandchill.lightandchill;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;

import java.net.HttpURLConnection;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentTwo.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentTwo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentTwo extends Fragment {

    protected View view = null;

    public FragmentTwo() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if (visible) {
            final Switch swMusic = (Switch)view.findViewById(R.id.swMusic);
            final Switch swWeather = (Switch)view.findViewById(R.id.swWeather);
            final RadioButton rdChill = (RadioButton)view.findViewById(R.id.rbMusicChill);
            final RadioButton rdParty = (RadioButton)view.findViewById(R.id.rbMusicParty);

            SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
            int defaultValue = getResources().getInteger(R.integer.activatedModeDefault);
            int state = sharedPref.getInt(getString(R.string.activatedMode), defaultValue);
            int musicMode = sharedPref.getInt(getString(R.string.musicMode), defaultValue);

            if(musicMode == 0){
                rdChill.setChecked(true);
            }else{
                rdParty.setChecked(true);
            }

            /*
            *   On vérifie dans quel mode de fonctionnement on se trouve
            **/
            switch (state) {
                case 0:
                    swMusic.setChecked(false);
                    swWeather.setChecked(false);
                    break;
                case 1:
                    swMusic.setChecked(true);
                    swWeather.setChecked(false);
                    break;
                case 2:
                    swMusic.setChecked(false);
                    swWeather.setChecked(true);
                    break;
            } //END SWITCH
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_two, container, false);

        final Switch swMusic = (Switch)view.findViewById(R.id.swMusic);
        final Switch swWeather = (Switch)view.findViewById(R.id.swWeather);
        final RadioGroup rgMusic = (RadioGroup)view.findViewById(R.id.rgMusic);
        final RadioButton rdChill = (RadioButton)view.findViewById(R.id.rbMusicChill);
        final RadioButton rdParty = (RadioButton)view.findViewById(R.id.rbMusicParty);

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        int defaultValue = getResources().getInteger(R.integer.activatedModeDefault);
        int state = sharedPref.getInt(getString(R.string.activatedMode), defaultValue);
        int musicMode = sharedPref.getInt(getString(R.string.musicMode), defaultValue);

        if(musicMode == 0){
            rdChill.setChecked(true);
        }else{
            rdParty.setChecked(true);
        }


        /*
        *   On vérifie dans quel mode de fonctionnement on se trouve
        **/
        switch (state) {
            case 0:
                swMusic.setChecked(false);
                swWeather.setChecked(false);
                break;
            case 1:
                swMusic.setChecked(true);
                swWeather.setChecked(false);
                break;
            case 2:
                swMusic.setChecked(false);
                swWeather.setChecked(true);
                break;
        }

        // TODO : Envoyer à l'arduino la commande lorsque l'on switche sur ces modes

        swMusic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putInt(getString(R.string.activatedMode), 1);
                    editor.commit();
                    swWeather.setChecked(false);

                    try
                    {
                        //On récupère l'adresse IP entrée dans le troisème onglet
                        String defaultValue = null;
                        String strIP = sharedPref.getString(getString(R.string.PrefIP), defaultValue);

                        //Si l'IP n'est pas nulle, on tente d'envoyer les infos à l'arduino
                        if(strIP != null){
                            int musicMode = sharedPref.getInt(getString(R.string.musicMode), 0);
                            URL url = new URL("http://" + strIP +"/music/" + musicMode);
                            HttpURLConnection net = (HttpURLConnection)url.openConnection();
                            net.setReadTimeout(10000);
                            net.setConnectTimeout(15000);
                            net.setRequestMethod("GET");
                            net.setDoInput(true);
                            net.connect();
                            Snackbar.make(view, "Musique activée !", Snackbar.LENGTH_SHORT).show();
                        }else{
                            Snackbar.make(view, "Echec d'activation du mode musique", Snackbar.LENGTH_LONG).show();
                        }
                    }
                    catch (Exception e)
                    {
                        Snackbar.make(view, "Fail " + e.toString(), Snackbar.LENGTH_LONG).show();
                    }
                }

            }
        });

        swWeather.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putInt(getString(R.string.activatedMode), 2);
                    editor.commit();
                    swMusic.setChecked(false);

                    try
                    {
                        //On récupère l'adresse IP entrée dans le troisème onglet
                        String defaultValue = null;
                        String strIP = sharedPref.getString(getString(R.string.PrefIP), defaultValue);

                        //Si l'IP n'est pas nulle, on tente d'envoyer les infos à l'arduino
                        if(strIP != null){
                            URL url = new URL("http://" + strIP +"/weather/");
                            HttpURLConnection net = (HttpURLConnection)url.openConnection();
                            net.setReadTimeout(10000);
                            net.setConnectTimeout(15000);
                            net.setRequestMethod("GET");
                            net.setDoInput(true);
                            net.connect();
                            Snackbar.make(view, "Météo activée !", Snackbar.LENGTH_SHORT).show();
                        }else{
                            Snackbar.make(view, "Echec d'activation du mode météo", Snackbar.LENGTH_LONG).show();
                        }
                    }
                    catch (Exception e)
                    {
                        Snackbar.make(view, "Fail " + e.toString(), Snackbar.LENGTH_LONG).show();
                    }
                }
            }
        });

        rgMusic.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();

                int radioButtonID = rgMusic.getCheckedRadioButtonId();
                View radioButton = rgMusic.findViewById(radioButtonID);
                int indexSelected = rgMusic.indexOfChild(radioButton);

                editor.putInt(getString(R.string.musicMode), indexSelected);
                editor.commit();
            }
        });

        return view;
    }
}
