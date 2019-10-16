package inetinfotech.myapps.fasttag;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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
    String email,password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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


    }

