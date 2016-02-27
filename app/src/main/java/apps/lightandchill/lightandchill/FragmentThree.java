package apps.lightandchill.lightandchill;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentThree.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentThree#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentThree extends Fragment {
    public FragmentThree() {
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
        View view = inflater.inflate(R.layout.fragment_three, container, false);

        // TODO : Les profils utilisateurs, le choix de couleur de l'interface, la liste des IPs,...

        final EditText etIP = (EditText)view.findViewById(R.id.etIP);
        Button btPreferences = (Button)view.findViewById(R.id.btPreferences);
        Button btShow = (Button)view.findViewById(R.id.btShow);
        final TextView tvIP = (TextView)view.findViewById(R.id.tvResult);

        btPreferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.PrefIP), etIP.getText().toString());
                editor.commit();
                tvIP.setText(etIP.getText());
            }
        });

        btShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                String defaultValue = getResources().getString(R.string.IPDefault);
                String state = sharedPref.getString(getString(R.string.PrefIP), defaultValue);
                tvIP.setText(state);
            }
        });

        return view;
    }
}
