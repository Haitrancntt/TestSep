package Fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.haitr.planed_12062016.LoginActivity;
import com.example.haitr.planed_12062016.R;

import java.util.ArrayList;

import Controller.Account_Control;

/**
 * Created by Thanh Huy on 7/14/2016.
 */
public class ResetPassFragment extends android.support.v4.app.Fragment {
    private ListView listView;
    private SearchView searchView;
    private Account_Control account_control;
    private int accountID;
    private ArrayList<String> arrayList;
    private ArrayAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_account, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView = (ListView) getActivity().findViewById(R.id.listViewAccount);
        account_control = new Account_Control(LoginActivity.db.getConnection());
        accountID = LoginActivity.Account_Id;
        arrayList = account_control.LoadList();
        adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

}
