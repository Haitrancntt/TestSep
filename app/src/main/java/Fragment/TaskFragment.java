package Fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
public class TaskFragment extends Fragment {
    private ImageButton btnadd;
    private ArrayList<String> arrayList;
    private String[] myList = new String[]{"Task 1", "Task 2", "Task 3"};
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;

    public TaskFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task, container, false);
        listView = (ListView) view.findViewById(R.id.listView);
        arrayList = new ArrayList<>(Arrays.asList(myList));
        arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item, myList);
        listView.setAdapter(arrayAdapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btnadd = (ImageButton) getActivity().findViewById(R.id.imageButtonTag);
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, new CreateTaskFragment()).commit();
                Toast toast = Toast.makeText(getContext(), "hello", Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }

}
