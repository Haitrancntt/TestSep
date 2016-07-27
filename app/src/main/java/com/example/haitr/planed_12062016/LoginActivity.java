package com.example.haitr.planed_12062016;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import Class.Account;
import Controller.Account_Control;
import utilities.DatabaseConnection;

/**
 * Created by haitr on 6/12/2016.
 */
public class LoginActivity extends AppCompatActivity {

    public static int Account_Id;
    public static DatabaseConnection db;
    public static EditText txtUS, txtPass;
    public static CheckBox checkBox;
    public static SharedPreferences loginRemember;
    public static SharedPreferences.Editor loginRememberEdit;
    public static int iPermission;
    TextView lblus, lblpass;
    private Button btnlogin;
    private RelativeLayout relativeLayout;
    private Account account;
    private Account_Control account_control;
    private Encryption encryption;
    private String sPassEncrypt, sPassDecrypt;
    private ProgressDialog progressDialog;
    private boolean isLoginSave;
    private boolean isLogout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo m3g = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mWifi.isConnected() == false & m3g.isConnected() == false) {
            new AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("Please make sure that your device is already connected to the Internet!")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finishAffinity();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } else {
            encryption = new Encryption();
            db = SplashActivity.db;
            account_control = new Account_Control(db.getConnection());
            checkBox = (CheckBox) findViewById(R.id.checkBox);
            lblus = (TextView) findViewById(R.id.lblErrorUS);
            lblpass = (TextView) findViewById(R.id.lblErrorPass);
            btnlogin = (Button) findViewById(R.id.buttonlogin);
            txtPass = (EditText) findViewById(R.id.txtPass);
            txtUS = (EditText) findViewById(R.id.txtUsername);

            //SET REMEMBER ACCOUNT

            loginRemember = getSharedPreferences("Login", MODE_PRIVATE);
            loginRememberEdit = loginRemember.edit();
            isLoginSave = loginRemember.getBoolean("save", false);
            isLogout = loginRemember.getBoolean("logout", false);
            if (isLoginSave == true & isLogout == false) {

                try {
                    String s = loginRemember.getString("username", "");
                    Account_Id = account_control.GetAccountID(s);
                    iPermission = account_control.GetPermission(Account_Id);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                txtUS.setText(loginRemember.getString("username", ""));
                txtPass.setText(loginRemember.getString("pass", ""));
                checkBox.setChecked(true);
            }
            // BUTTON LISTENER
            btnlogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lblpass.setText("");
                    lblus.setText("");
                    String username = txtUS.getText().toString();
                    String password = txtPass.getText().toString();
                    if (txtUS.getText().length() == 0) {
                        lblus.setText("This field cannot be empty");
                    } else if (txtPass.getText().length() == 0) {
                        lblpass.setText("This field cannot be empty");
                    } else {
                        try {

                            sPassEncrypt = Encryption.bytesToHex(encryption.encrypt(password));
                            boolean b2 = account_control.CheckEmail(username);
                            if (b2) {
                                boolean b1 = account_control.CheckUsername(username);
                                if (b1) {
                                    boolean b = account_control.CheckLogin(username, sPassEncrypt);
                                    if (b) {
                                        Account_Id = account_control.GetAccountID(username);
                                        iPermission = account_control.GetPermission(Account_Id);
                                        progressDialog = new ProgressDialog(LoginActivity.this, R.style.AppTheme_Dark_Dialog);
                                        progressDialog.setIndeterminate(true);
                                        progressDialog.setMessage("Authentication...");
                                        progressDialog.show();
                                        if (checkBox.isChecked()) {
                                            // SET REMEMBER ACCOUNT
                                            loginRememberEdit.putBoolean("save", true);
                                            loginRememberEdit.putString("username", username);
                                            loginRememberEdit.putString("pass", password);
                                            loginRememberEdit.putBoolean("logout", false);
                                            loginRememberEdit.commit();
                                        } else {
                                            loginRememberEdit.clear();
                                            loginRememberEdit.commit();
                                        }
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
                }
            });
        }

    }
}
