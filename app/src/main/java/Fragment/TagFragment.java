package Fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.haitr.planed_12062016.LoginActivity;
import com.example.haitr.planed_12062016.R;

import java.util.ArrayList;

import Controller.Tag_Control;

/**
 * A simple {@link Fragment} subclass.
 */
public class TagFragment extends Fragment {
    private ListView listView;
    private ArrayList<String> arrayList;
    private Tag_Control tag_control;
    private int accountID;
    private ArrayAdapter arrayAdapter;
    private ImageButton btnAdd;
    private EditText txtName;
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
        btnAdd = (ImageButton) getActivity().findViewById(R.id.imageButtonTask);
        listView = (ListView) getActivity().findViewById(R.id.listview_Tag);
        txtName = (EditText) getActivity().findViewById(R.id.edit_newtag);
        tag_control = new Tag_Control(LoginActivity.db.getConnection());
        accountID = LoginActivity.Account_Id;
        LoadList();
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = txtName.getText().toString();
                boolean b = tag_control.AddTag(name, accountID);
                if (b) {
                    Toast.makeText(getContext(), "Added new Tag successfully", Toast.LENGTH_SHORT).show();
                    txtName.setText("");
                    LoadList();
                } else {

                }
            }
        });
    }

    public void LoadList() {
        arrayList = tag_control.LoadList(accountID);
        arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(arrayAdapter);
    }
}
