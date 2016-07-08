package Fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.haitr.planed_12062016.R;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 */
public class TagFragment extends Fragment {
    private ListView listView;
    private ArrayList<String> arrayList;
    private String[] strings = new String[]{"Travel", "Work", "Study", "Trip", "Exercise"};
    private ArrayAdapter<String> arrayAdapter;
    private ImageButton btnAdd;

    public TagFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tag, container, false);
        listView = (ListView) view.findViewById(R.id.listItem);
        arrayList = new ArrayList<>(Arrays.asList(strings));
        arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item, strings);
        listView.setAdapter(arrayAdapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btnAdd = (ImageButton) getActivity().findViewById(R.id.imageButtonTag);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Add new tag", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
