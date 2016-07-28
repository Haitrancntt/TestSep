package Fragment;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
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
    ArrayList<String> searchList;
    private ListView listView;
    private SearchView searchView;
    private Account_Control account_control;
    private int accountID;
    private ArrayList<String> arrayList;
    private ArrayAdapter adapter;
    private String email;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConnectivityManager connManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo m3g = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mWifi.isConnected() == false & m3g.isConnected() == false) {
            new AlertDialog.Builder(getActivity())
                    .setTitle("Error")
                    .setMessage("Please make sure that your device is already connected to the Internet!")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            getActivity().finishAffinity();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_account, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = (ListView) getActivity().findViewById(R.id.listViewAccount);
        account_control = new Account_Control(LoginActivity.db.getConnection());
        accountID = LoginActivity.Account_Id;
        searchView = (SearchView) getActivity().findViewById(R.id.searchView2);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
                    SearchList(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() == 0) {
                    LoadList();
                } else {
                    SearchList(newText);
                }
                return false;
            }
        });
    }

    public void LoadList() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new LoadAccountInBackGround().execute();

            }
        });
    }

    public void SearchList(final String text) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new SearchAccountInBackGround().execute(text);
            }
        });
    }

    //ON BACK PRESSED
    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    FragmentManager fm = getFragmentManager();
                    fm.beginTransaction().replace(R.id.content_frame, new AccountFragment()).commit();
                    return true;
                }
                return false;
            }
        });
    }

    private class LoadAccountInBackGround extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            arrayList = account_control.LoadList();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, arrayList);
            listView.setAdapter(adapter);
        }
    }

    private class SearchAccountInBackGround extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            searchList = account_control.Search(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, searchList);
            listView.setAdapter(adapter);
        }
    }

}
