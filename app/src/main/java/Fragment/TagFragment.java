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

import com.example.haitr.planed_12062016.LoginActivity;
import com.example.haitr.planed_12062016.R;

import java.util.ArrayList;

import Controller.Tag_Control;

/**
 * A simple {@link Fragment} subclass.
 */
public class TagFragment extends Fragment {
    private ListView listView;
    private ArrayList<String> array;
    private ArrayAdapter arrayAdapter;
    private ImageButton btnAdd;
    private int Account_Id;
    private Tag_Control tag_control;

    public TagFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tag, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Account_Id = LoginActivity.Account_Id;
        listView = (ListView) getActivity().findViewById(R.id.listItem);
        tag_control = new Tag_Control(LoginActivity.db.getConnection());
        LoadList();
        btnAdd = (ImageButton) getActivity().findViewById(R.id.imageButtonTag);
        LoadList();
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, new CreateTagFragment()).commit();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        LoadList();

    }

    public void LoadList() {
        array = tag_control.LoadList(Account_Id);
        arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, array);
        listView.setAdapter(arrayAdapter);
    }
}
