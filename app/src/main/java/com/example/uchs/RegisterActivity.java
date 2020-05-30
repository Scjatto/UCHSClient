package com.example.uchs;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.NoCopySpan;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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

public class RegisterActivity extends AppCompatActivity {
    public static final String DEBUG_TAG = "Register";
    private ConstraintLayout layout;
    Spinner spinner_prof;
    ArrayAdapter<CharSequence> adapter_prof;
    ArrayAdapter<CharSequence> adapter_serv;
    Spinner spinner_cat;
    ArrayAdapter<CharSequence> adapter_cat;
    Button submit_button;
    EditText editName;
    EditText editPhone;
    EditText editUserName;
    EditText editPassword;
    EditText editAgeLoc;

    String userCat;
    String profServType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spinner_prof = (Spinner) findViewById(R.id.edit_Profession);
        adapter_prof = ArrayAdapter.createFromResource(this, R.array.professions, android.R.layout.simple_spinner_item);
        adapter_prof.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter_serv = ArrayAdapter.createFromResource(this, R.array.Services, android.R.layout.simple_spinner_item);
        adapter_serv.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner_cat = (Spinner) findViewById(R.id.edit_Category);
        adapter_cat = ArrayAdapter.createFromResource(this, R.array.categories, android.R.layout.simple_spinner_item);
        adapter_cat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_cat.setAdapter(adapter_cat);


        layout = (ConstraintLayout)findViewById(R.id.registerLayout);
        editName = (EditText)findViewById(R.id.edit_Name);
        editPhone = (EditText)findViewById(R.id.edit_Phoneno);
        editUserName = (EditText)findViewById(R.id.edit_Username);
        editPassword = (EditText)findViewById(R.id.edit_Password);
        editAgeLoc = (EditText)findViewById(R.id.edit_AgeLoc);
        submit_button = (Button)findViewById(R.id.btSubmitRegister);

        editName.addTextChangedListener(enableRegister);
        editPhone.addTextChangedListener(enableRegister);
        editAgeLoc.addTextChangedListener(enableRegister);
        editUserName.addTextChangedListener(enableRegister);
        editPassword.addTextChangedListener(enableRegister);

        submit_button.setOnClickListener(registerSubmitListener);


        spinner_cat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView prof = (TextView) findViewById(R.id.enter_Profession);
                TextView ageLoc = (TextView)findViewById(R.id.enterAgeLoc);
                userCat = parent.getItemAtPosition(position).toString();
                if (position==0) {
                    prof.setText("Profession");
                    ageLoc.setText("Age");
                    editAgeLoc.setText("");
                    editAgeLoc.setInputType(InputType.TYPE_CLASS_NUMBER);
                    editAgeLoc.setHint("Enter Age");
                    spinner_prof.setAdapter(adapter_prof);
                }
                else if(position==1){
                    prof.setText("Service Type");
                    ageLoc.setText("Location");
                    editAgeLoc.setInputType(InputType.TYPE_CLASS_TEXT);
                    editAgeLoc.setText("");
                    editAgeLoc.setHint("Enter Location");
                    spinner_prof.setAdapter(adapter_serv);
                }
                spinner_prof.setOnItemSelectedListener(getProfServCat);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @SuppressLint("RestrictedApi")
    public  boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.header_menu, menu);
        if(menu instanceof MenuBuilder){
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }
        MenuItem item = menu.findItem(R.id.configure_sop);
        item.setVisible(false);
        MenuItem item1 = menu.findItem(R.id.logout);
        item1.setVisible(false);

        return true;
    }

    private AdapterView.OnItemSelectedListener getProfServCat = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            profServType = parent.getItemAtPosition(position).toString();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private void userRegistration(User user, RequestQueue requestQueue) {
        String url = "https://tribal-marker-274610.el.r.appspot.com/registerUser?";
        url += "&uid=" + user.userID;
        if (user.userName.length > 1) {
            url += "&fname=" + user.userName[0] + "&lname=" + user.userName[1].replace(" ","-");
        } else {
            url += "&fname=" + user.userName[0] + "&lname=" + "";
        }

        url += "&phone=" + user.userPh;
        url += "&ccode=+91&age=" + user.userAge;
        url += "&specz=" + user.userProf;
        url += "&pass=" + user.userPass;
        Log.d(DEBUG_TAG, url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        enableStatus(true);
                        try {
                            int status = response.getInt("status");
                            if (status == 1) {
                                String msg = "User Registration Successful!!";
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                String msg = response.getString("errorMsg");
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String msg;
                enableStatus(true);
                if (error instanceof TimeoutError) {
                    msg = "Request Timed Out!! Please try again!!";
                } else if (error instanceof NoConnectionError) {
                    msg = "No internet detected!! Please check the internet connection";
                } else {
                    msg = "Server Not Responding!!";
                }
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
        enableStatus(false);
        int socketTimeOut = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeOut, 1, 1);
        jsonObjectRequest.setRetryPolicy(policy);
        requestQueue.add(jsonObjectRequest);
    }


    private void helplineRegistration(Helpline helpline, RequestQueue requestQueue) {
        String url = "https://tribal-marker-274610.el.r.appspot.com/registerHelpline?";
        url += "&hid=" + helpline.helplineID;
        url += "&hname=" + helpline.helplineName;
        url += "&phone=" + helpline.helplinePh;
        url += "&ccode=+91&specz=" + helpline.helplineType;
        url += "&pass=" + helpline.helplinePass;
        url += "&loc=" + helpline.helplineLocation;
        Log.d(DEBUG_TAG, url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        enableStatus(true);
                        try {
                            int status = response.getInt("status");
                            if (status == 1) {
                                String msg = "Helpline Registration Successful!!";
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                String msg = response.getString("errorMsg");
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String msg;
                enableStatus(true);
                if (error instanceof TimeoutError) {
                    msg = "Request Timed Out!! Please try again!!";
                } else if (error instanceof NoConnectionError) {
                    msg = "No internet detected!! Please check the internet connection";
                } else {
                    msg = "Server Not Responding!!";
                }
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });

        enableStatus(false);
        int socketTimeOut = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeOut, 1, 1);
        jsonObjectRequest.setRetryPolicy(policy);
        requestQueue.add(jsonObjectRequest);
    }





    private boolean fillCheck() {
        for(int i=0; i< layout.getChildCount(); i++) {
            View view = layout.getChildAt(i);

            if (view instanceof EditText) {
                if (((EditText) view).getText().toString().isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    };

    private void enableStatus(boolean status) {
        for (int i=0; i<layout.getChildCount(); i++) {
            View view = layout.getChildAt(i);
            view.setEnabled(status);
        }
    }

    private TextWatcher enableRegister = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(fillCheck() && editPhone.getText().toString().length() == 10) {
                submit_button.setEnabled(true);
            } else {
                submit_button.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    private View.OnClickListener registerSubmitListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String uCat = userCat;
            String servProf = profServType;
            String uName = editName.getText().toString();
            String uPh = editPhone.getText().toString();
            String uUName = editUserName.getText().toString();
            String uPass = editPassword.getText().toString();
            String ageLoc = editAgeLoc.getText().toString();

            RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity.this);

            if (uCat.toLowerCase().equals("user")) {
                User user = new User(uName, uPh, uUName, uPass, servProf, ageLoc);
                Log.d(DEBUG_TAG, user.genUserString());
                userRegistration(user, requestQueue);
            }
            else {
                Helpline helpline = new Helpline(uName, uPh, uUName, uPass, servProf, ageLoc);
                Log.d(DEBUG_TAG, helpline.genHelpString());
                helplineRegistration(helpline,requestQueue);
            }
        }
    };


}
