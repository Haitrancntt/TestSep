package com.example.haitr.planed_12062016;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import Controller.Account_Control;
import Database.DatabaseConnection;

/**
 * Created by haitr on 6/12/2016.
 */
public class LoginActivity extends AppCompatActivity {
    public static DatabaseConnection db = new DatabaseConnection();
    public static int Account_Id;
    EditText txtUS, txtPass;
    Account_Control account_control;
    TextView lblus, lblpass;
    private Button btnlogin;
    private RelativeLayout relativeLayout;
    private int iPermission;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);
        try {
            db.Connect();
            account_control = new Account_Control(db.getConnection());
        } catch (Exception e) {
            e.printStackTrace();
        }
        lblus = (TextView) findViewById(R.id.lblErrorUS);
        lblpass = (TextView) findViewById(R.id.lblErrorPass);
        btnlogin = (Button) findViewById(R.id.buttonlogin);
        txtPass = (EditText) findViewById(R.id.txtPass);
        txtUS = (EditText) findViewById(R.id.txtUsername);
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lblpass.setText("");
                lblus.setText("");
                String username = txtUS.getText().toString();
                String password = txtPass.getText().toString();
                username = "huyle32@vanlanguni.vn";
                password = "8320748";
                try {
                    boolean b2 = account_control.CheckEmail(username);
                    if (b2) {
                        boolean b1 = account_control.CheckUsername(username);
                        if (b1) {
                            boolean b = account_control.CheckLogin(username, password);
                            if (b) {
                                Account_Id = account_control.GetAccountID(username);

                                //   Toast.makeText(LoginActivity.this, Account_Id+"", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                lblpass.setText(R.string.error_pass_login);
                                txtPass.setText("");
                                txtPass.requestFocus();
                                InputMethodManager imm = (InputMethodManager) getSystemService(LoginActivity.this.INPUT_METHOD_SERVICE);
                                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                            }
                        } else {
                            lblus.setText(R.string.error_username_login);
                            txtUS.requestFocus();
                            InputMethodManager imm = (InputMethodManager) getSystemService(LoginActivity.this.INPUT_METHOD_SERVICE);
                            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                        }
                    } else {
                        lblus.setText(R.string.error_email_login);
                        txtUS.requestFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(LoginActivity.this.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                    }

                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        });
        relativeLayout = (RelativeLayout) findViewById(R.id.background);
        // relativeLayout.setBackgroundResource(R.drawable.bg);
        relativeLayout.setBackgroundResource(R.color.colorBackground);
    }


}
