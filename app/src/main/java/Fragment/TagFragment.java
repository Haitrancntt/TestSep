package Fragment;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
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
    private int accountID, tagId;
    private ArrayAdapter arrayAdapter;
    private ImageButton btnAdd;
    private EditText txtName;
    private TextView txtError;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tag, container, false);
    }

    // ADD ALL TO ACTIVITY
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btnAdd = (ImageButton) getActivity().findViewById(R.id.imageButtonTask);
        listView = (ListView) getActivity().findViewById(R.id.listview_Tag);
        txtName = (EditText) getActivity().findViewById(R.id.edit_newtag);
        tag_control = new Tag_Control(LoginActivity.db.getConnection());
        accountID = LoginActivity.Account_Id;
        registerForContextMenu(listView);
        LoadList();
        btnAdd.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          String name = txtName.getText().toString();

                                          // VALIDATE NULL
                                          if (txtName.getText().toString().equals("")) {
                                              Toast.makeText(getContext(), R.string.null_space, Toast.LENGTH_LONG).show();
                                          } else {
                                              //VALIDATE NOT NULL
                                              boolean bCheck = tag_control.CheckTagExisted(name);
                                              if (bCheck) {
                                                  //CHECK EXISTED
                                                  Toast.makeText(getContext(), R.string.existed_tag, Toast.LENGTH_LONG).show();
                                              } else {
                                                  //ADD TAG
                                                  boolean b = tag_control.AddTag(name, accountID);
                                                  if (b) {
                                                      Toast.makeText(getContext(), R.string.add_tag_success, Toast.LENGTH_SHORT).show();
                                                      txtName.setText("");
                                                      LoadList();
                                                  } else {
                                                      Toast.makeText(getContext(), R.string.add_tag_failed, Toast.LENGTH_SHORT).show();

                                                  }
                                              }
                                          }
                                      }

                                  }

        );
    }

    // CREATE CONTEXT MENU FOR DELETE / EDIT
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == listView.getId()) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.setHeaderTitle(arrayList.get(info.position));
            String[] menuItems = getResources().getStringArray(R.array.menu);
            for (int i = 0; i < menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }

    // SHOW POP UP FOR EDIT TAG
    public void AlertEdit(String item, final String sEdit) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle(item);
        final EditText EditText = new EditText(getContext());
        EditText.setText(sEdit);
        alert.setView(EditText);
        tagId = tag_control.GetTagId(sEdit,accountID);
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tag_control.EditTag(EditText.getText().toString(), tagId);
                Toast.makeText(getContext(), R.string.edit_tag_success, Toast.LENGTH_LONG).show();
                LoadList();
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = alert.create();
        alertDialog.show();
    }

    // SHOW POP UP FOR DELETE
    public void AlertDelete(String title, String name) {
        final AlertDialog.Builder alertdelete = new AlertDialog.Builder(getContext());
        alertdelete.setTitle(title);
        final int Tag_id = tag_control.GetTagId(name,accountID);
        alertdelete.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tag_control.DeleteTag(Tag_id);
                LoadList();
                Toast.makeText(getContext(), R.string.delete_tag_success, Toast.LENGTH_SHORT).show();
            }
        });
        alertdelete.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = alertdelete.create();
        alertDialog.show();
    }

    // EVENT FOR CLICK ITEM CONTEXT
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int menuItemIndex = item.getItemId();
        String sEdit = arrayList.get(info.position);
        String[] menuItems = getResources().getStringArray(R.array.menu);

        switch (menuItemIndex) {
            case 0:
                AlertEdit(menuItems[0], sEdit);
                break;
            case 1:
                AlertDelete(menuItems[1], sEdit);
                break;
        }
        return true;
    }

    // LOAD LIST ALL TAG
    public void LoadList() {
        arrayList = tag_control.LoadList(accountID);
        arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(arrayAdapter);
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
                    fm.beginTransaction().replace(R.id.content_frame, new MainFragment()).commit();
                    return true;
                }
                return false;
            }
        });
    }
}