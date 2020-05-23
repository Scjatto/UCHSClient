package com.example.uchs;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    Spinner spinner_login_cat;
    ArrayAdapter<CharSequence> adapter_login_cat;

    private EditText idphone;
    private EditText password;
    private Button finLogin;

    private String userType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setTitle("UCHSLogin");

        idphone = (EditText)findViewById(R.id.editIdPhone);
        password = (EditText)findViewById(R.id.editPassword);
        finLogin = (Button)findViewById(R.id.btFinLogin);

        idphone.addTextChangedListener(contentEnterWatcher);
        password.addTextChangedListener(contentEnterWatcher);
        finLogin.setOnClickListener(finLoginListener);

        spinner_login_cat = (Spinner) findViewById(R.id.login_edit_category);
        adapter_login_cat = ArrayAdapter.createFromResource(this, R.array.categories, android.R.layout.simple_spinner_item);
        adapter_login_cat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_login_cat.setAdapter(adapter_login_cat);
        spinner_login_cat.setOnItemSelectedListener(userCategorySelector);

    }


    private AdapterView.OnItemSelectedListener userCategorySelector = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            userType = parent.getItemAtPosition(position).toString();
//            Toast.makeText(getApplicationContext(),userType,Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private TextWatcher contentEnterWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String idPhoneInput = idphone.getText().toString();
            String passwdInput = password.getText().toString();

            finLogin.setEnabled(!idPhoneInput.isEmpty() && !passwdInput.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public void verifyLogin(final String uid, final String uPass, final String userType, RequestQueue requestQueue) {
//        String url = "https://jsonplaceholder.typicode.com/todos/1";
        String url = "https://tribal-marker-274610.el.r.appspot.com/login?";
        url += "type=" + userType;
        url += "&id=" + uid;
        url += "&pass=" + uPass;
        url = String.format(url);
        System.out.println("Login:: " + url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String data = response.toString();
//                                String dataString = data.toString();
                        System.out.println("Response>>>>>>>: " + data);
                            idphone.setEnabled(true);
                            password.setEnabled(true);
                            finLogin.setEnabled(true);
                            spinner_login_cat.setEnabled(true);
                        try {
                            int status = response.getInt("status");
                            System.out.println("Status:: " + status);
                            if (status == 1) {
                                int check = response.getInt("check");
                                if (check == 1) {
                                    // Do intent
                                    Intent finLoginIntent = new Intent(LoginActivity.this,ConfigureSopActivity.class);
                                    Bundle dataExtra = new Bundle();
                                    dataExtra.putString("CONFIGURE_SOP_TITLE",uid);
                                    dataExtra.putString("USER_TYPE",userType);
                                    saveCredentials(uid, uPass, userType);
                                    finLoginIntent.putExtras(dataExtra);
                                    startActivity(finLoginIntent);
                                    finish();
                                } else {
                                    String msg = "Invalid Credentials!! Please Check Again";
                                    Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(),"Server Data Error",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                idphone.setEnabled(true);
                password.setEnabled(true);
                finLogin.setEnabled(true);
                spinner_login_cat.setEnabled(true);
                String msg;
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    msg = "Request Timed Out!! Check your internet connection and try again";
                } else {
                    long timeEpoch = System.currentTimeMillis();
                    String time = DateFormat.format("dd/MM/yyyy HH:mm:ss" , timeEpoch).toString();
                    msg = "Server Not Responding!!";
                    System.out.println(time + " Server Not Responding:: " + error.getMessage());
                }
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });

        idphone.setEnabled(false);
        password.setEnabled(false);
        finLogin.setEnabled(false);
        spinner_login_cat.setEnabled(false);
        int socketTimeOut = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeOut, 1, 1);
        jsonObjectRequest.setRetryPolicy(policy);
        requestQueue.add(jsonObjectRequest);
    }

    private void saveCredentials(String accID, String accPass, String accType) {
        SharedPreferences sharedPreferences = getSharedPreferences("LoginCredentials", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("ACC_ID", accID);
        editor.putString("ACC_PASS", accPass);
        editor.putString("ACC_TYPE", accType);

        editor.apply();
    }


    private View.OnClickListener finLoginListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            // TODO Check ID existing in the server

            String id = idphone.getText().toString(); // TODO Changed to only UID when server entry is matched
            String pass = password.getText().toString();
            String uType = "";
            if (userType.toLowerCase().equals("user")) {
                uType = "user";
            } else {
                uType = "help";
            }

            RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
            verifyLogin(id, pass, uType, requestQueue);

        }
    };

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
