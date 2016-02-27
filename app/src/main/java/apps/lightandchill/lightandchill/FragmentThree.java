package apps.lightandchill.lightandchill;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


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
        final Button btPreferences = (Button)view.findViewById(R.id.btPreferences);
        final ListView lvIPs = (ListView)view.findViewById(R.id.lvIPs);

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        final Set<String> listIps = new HashSet<String>(sharedPref.getStringSet(getString(R.string.listIps), new HashSet<String>()));


        if(listIps.size() > 0){
            ArrayList<String> arListIps = new ArrayList<String>(listIps);
            ArrayAdapter<String> arListIPs = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, arListIps);
            lvIPs.setAdapter(arListIPs);
        }

        btPreferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listIps.add(etIP.getText().toString());
                ArrayList<String> arListIps = new ArrayList<String>(listIps);
                ArrayAdapter<String> arListIPs = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, arListIps);
                lvIPs.setAdapter(arListIPs);
                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putStringSet(getString(R.string.listIps), listIps);
                editor.commit();
            }
        });

        lvIPs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = ((TextView)view).getText().toString();

                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.PrefIP), item);
                editor.commit();
            }
        });

        return view;
    }
}
