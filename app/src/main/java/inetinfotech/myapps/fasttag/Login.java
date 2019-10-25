package inetinfotech.myapps.fasttag;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    Button llogin;
    EditText lpswd,lmail;
    TextView msglogin;
    public static final int RequestPermissionCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        EnableRuntimePermission();
        msglogin = findViewById(R.id.textView2);
        lmail = findViewById(R.id.txt_lname);
        lpswd = findViewById(R.id.txt_lpswd);
        llogin=findViewById(R.id.btn_llogin);

        llogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((lmail.getText().toString().isEmpty())||(lpswd.getText().toString().isEmpty()))
                {
                    Toast.makeText(Login.this,"Fill All feild", Toast.LENGTH_SHORT).show();
                }
                else {
                    login();

                }

            }
        });
    }
    public void login()
    {
                   StringRequest stringRequest = new StringRequest(Request.Method.POST,"https://full-bottomed-cushi.000webhostapp.com/Fasttag_login.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
//If we are getting success from server

                            //  txt.setVisibility(View.VISIBLE);
                            if(response.contains("success"))
                            {

                                Intent intent = new Intent(Login.this, MainActivity.class);
                                startActivity(intent);
                            }
                            else

                            {
                                Toast.makeText(Login.this,"Invalid Login",Toast.LENGTH_SHORT).show();
                            }

                            try {
                                JSONArray jsonArray=new JSONArray(response);
                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject json_obj = jsonArray.getJSONObject(i);
       }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
//You can handle error here if you want
                        }

                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<>();
//Adding parameters t o request

                    params.put("email", lmail.getText().toString());
                    params.put("password",lpswd.getText().toString());
//returning parameter
                    return params;
                }
            };

//Adding the string request to the queue
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }

    public void EnableRuntimePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(Login.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

// Toast.makeText(Cpature_image.this,"CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(Login.this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, RequestPermissionCode);


        }
    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        switch (RC) {

            case RequestPermissionCode:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

// Toast.makeText(Cpature_image.this,"Permission Granted, Now your application can access CAMERA.", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(this, "Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }
    }

