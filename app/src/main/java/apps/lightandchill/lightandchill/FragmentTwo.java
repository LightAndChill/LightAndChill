package apps.lightandchill.lightandchill;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Switch;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentTwo.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentTwo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentTwo extends Fragment {

    public FragmentTwo() {
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
        View view = inflater.inflate(R.layout.fragment_two, container, false);

        final Switch swMusic = (Switch)view.findViewById(R.id.swMusic);
        final Switch swWeather = (Switch)view.findViewById(R.id.swWeather);
        final RadioGroup rgMusic = (RadioGroup)view.findViewById(R.id.rgMusic);

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        int defaultValue = getResources().getInteger(R.integer.activatedModeDefault);
        int state = sharedPref.getInt(getString(R.string.activatedMode), defaultValue);

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

        swMusic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putInt(getString(R.string.activatedMode), 1);
                    editor.commit();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.putExtra("Page", 1);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    getActivity().finish();
                    startActivity(intent);
                }else{
                    swMusic.setChecked(true);
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
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.putExtra("Page", 1);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    getActivity().finish();
                    startActivity(intent);
                }else{
                    swWeather.setChecked(true);
                }
            }
        });

        rgMusic.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt(getString(R.string.musicMode), checkedId);
                editor.commit();
            }
        });

        return view;
    }
}
