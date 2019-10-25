package inetinfotech.myapps.fasttag;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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

import java.util.HashMap;
import java.util.Map;

public class Registration extends AppCompatActivity {
    TextView msg;
    EditText name,email,password,conpassword,ph,vno;
    Button register,login;
    SharedPreferences sh;
    public boolean shlogin=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        msg=findViewById(R.id.textView);
        name=findViewById(R.id.txt_name);
        email=findViewById(R.id.txt_mail);
        password=findViewById(R.id.txt_pswd);
        conpassword=findViewById(R.id.txt_cpwd);
        ph=findViewById(R.id.txt_ph);
        vno=findViewById(R.id.txt_vno);
        register=findViewById(R.id.btn_reg);
        login=findViewById(R.id.btn_login);

        sh= getSharedPreferences("reg",MODE_PRIVATE);
        shlogin=sh.getBoolean("hi",false);

        if(shlogin)
        {
            Intent intent = new Intent(Registration.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((name.getText().toString().isEmpty())||(email.getText().toString().isEmpty())||(password.getText().toString().isEmpty())||(conpassword.getText().toString().isEmpty())||(ph.getText().toString().isEmpty())||(vno.getText().toString().isEmpty()))
                {
                    Toast.makeText(Registration.this,"Fill All feild", Toast.LENGTH_SHORT).show();
                }
                else if(!(password.getText().toString()).equals(conpassword.getText().toString()))
                {
                    Toast.makeText(Registration.this,"Password Miss Matched", Toast.LENGTH_SHORT).show();
                }
                Register();

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Registration.this,Login.class);
                startActivity(intent);
            }
        } );

    }
    public void Register() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://full-bottomed-cushi.000webhostapp.com/Fasttag_reg.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//If we are getting success from server
                        Toast.makeText(Registration.this, response, Toast.LENGTH_SHORT).show();



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//You can handle error here if you want
                    }

                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
//Adding parameters to request

                params.put("name", name.getText().toString());
                params.put("email", email.getText().toString());
                params.put("pswd",password.getText().toString());
                params.put("ph", ph.getText().toString());
                params.put("vchno", vno.getText().toString());


//returning parameter
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
