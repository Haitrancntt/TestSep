package Fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

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
    private String email;


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
        searchView = (SearchView) getActivity().findViewById(R.id.searchView2);
        LoadList();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                email = listView.getItemAtPosition(position).toString();
                try {
                    accountID = account_control.GetAccountID(email);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                new AlertDialog.Builder(getActivity())
                        .setTitle("Reset Password")
                        .setMessage("Are you sure you want to reset password for " + email + "?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                account_control.ResetPassword(accountID);
                                Toast.makeText(getActivity(), "Reset Password Successfully!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length() == 0) {
                    LoadList();
                } else {
                    ArrayList<String> list = account_control.Search(query);
                    adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, list);
                    listView.setAdapter(adapter);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() == 0) {
                    LoadList();
                } else {
                    ArrayList<String> list = account_control.Search(newText);
                    adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, list);
                    listView.setAdapter(adapter);
                }
                return false;
            }
        });
    }

    public void LoadList() {
        arrayList = account_control.LoadList();
        adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(adapter);
    }

}
