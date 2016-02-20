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

        final Button btMusic = (Button)view.findViewById(R.id.btActivateMusic);
        final Button btWeather = (Button)view.findViewById(R.id.btActivateWeather);

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        int defaultValue = getResources().getInteger(R.integer.activatedModeDefault);
        int state = sharedPref.getInt(getString(R.string.activatedMode), defaultValue);

        switch (state) {
            case 0:
                btMusic.setEnabled(true);
                btWeather.setEnabled(true);
                btMusic.setText(R.string.activate);
                btWeather.setText(R.string.activate);
                break;
            case 1:
                btMusic.setEnabled(false);
                btWeather.setEnabled(true);
                btMusic.setText(R.string.activated);
                btWeather.setText(R.string.activate);
                break;
            case 2:
                btMusic.setEnabled(true);
                btWeather.setEnabled(false);
                btMusic.setText(R.string.activate);
                btWeather.setText(R.string.activated);
                break;
        }

        btMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt(getString(R.string.activatedMode), 1);
                editor.commit();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                getActivity().finish();
                startActivity(intent);
            }
        });

        btWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt(getString(R.string.activatedMode), 2);
                editor.commit();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                getActivity().finish();
                startActivity(intent);
            }
        });

        return view;
    }
}
