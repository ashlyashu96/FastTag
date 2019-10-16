package inetinfotech.myapps.fasttag;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.razorpay.Checkout;

import com.razorpay.PaymentResultListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity implements PaymentResultListener {
    Button btnrchrge;
    EditText txtamt, txtid;
    String tid,fastid;
    final int UPI_PAYMENT = 0;
    String a,b;
    Bitmap bitmap;
    ImageView imageView;
    ProgressBar pg;
    public final static int QRcodeWidth = 500 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnrchrge = findViewById(R.id.button);
        txtamt = findViewById(R.id.editText);
        txtid = findViewById(R.id.txt_uid);
        a= txtamt.getText().toString();
        imageView=findViewById(R.id.imageView3);
        pg=findViewById(R.id.progressBar2);
        btnrchrge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((txtamt.getText().toString().isEmpty()) || (txtid.getText().toString().isEmpty())) {
                    Toast.makeText(MainActivity.this, "Fill All feild", Toast.LENGTH_SHORT).show();
                }
                else
                {
                        CheckTagid();

                }
            }
        });
    }




    Bitmap TextToImageEncode(String Value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    QRcodeWidth, QRcodeWidth, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }


        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.Black) : getResources().getColor(R.color.White);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;

    }

    public void startPayment() {
        pg.setVisibility(View.VISIBLE);
        /**
         * You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;

        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Razorpay Corp");
            options.put("description", "Demoing Charges");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://rzp-mobile.s3.amazonaws.com/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount", Integer.parseInt(txtamt.getText().toString())*100);
            JSONObject preFill = new JSONObject();
            preFill.put("email", "sikander@gkmit.co");
            preFill.put("contact", "9680224241");

            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(final String razorpayPaymentID) {
        Toast.makeText(this, "Payment successfully done! " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://full-bottomed-cushi.000webhostapp.com/Fasttag_recharge.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//If we are getting success from server
                        if(response.contains("Success"))
                        {
                            pg.setVisibility(View.INVISIBLE);
                            Toast.makeText(MainActivity.this,"Recharge SuccessFully", Toast.LENGTH_SHORT).show();

                            b=txtid.getText().toString();
                            try {
                                bitmap = TextToImageEncode(encrypt(b,"ashlyg333"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            imageView.setImageBitmap(bitmap);
                            saveimage();
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this, "Payment Failed", Toast.LENGTH_SHORT).show();
                        }

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

                params.put("amount", txtamt.getText().toString());
                params.put("tagid",txtid.getText().toString());

                String hid= String.valueOf((txtid.getText().toString()).substring(0,1));
                String hid1= String.valueOf((txtid.getText().toString()).substring(4,6));
                params.put("hid",hid);
                params.put("hid1",hid1);

//returning parameter
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    @Override
    public void onPaymentError(int code, String response) {
        try {
            Toast.makeText(this, "Payment error please try again", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("OnPaymentError", "Exception in onPaymentError", e);
        }
    }

public void CheckTagid()
{
    pg.setVisibility(View.VISIBLE);
    StringRequest stringRequest = new StringRequest(Request.Method.POST,"https://full-bottomed-cushi.000webhostapp.com/Fasttag_checkTagid.php",
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
//If we are getting success from server

                    //  txt.setVisibility(View.VISIBLE);
                    if(response.contains("success"))
                    {
                        pg.setVisibility(View.INVISIBLE);
                        startPayment();
                    }
                    else

                    {
                        pg.setVisibility(View.INVISIBLE);
                        Toast.makeText(MainActivity.this,"Invalid Tagid",Toast.LENGTH_SHORT).show();
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
            params.put("tagid",txtid.getText().toString());

//returning parameter
            return params;
        }
    };

//Adding the string request to the queue
    RequestQueue requestQueue = Volley.newRequestQueue(this);
    requestQueue.add(stringRequest);
}
public void saveimage()
{
    BitmapDrawable draw = (BitmapDrawable) imageView.getDrawable();
    Bitmap bitmap = draw.getBitmap();

    FileOutputStream outStream = null;
    File sdCard = Environment.getExternalStorageDirectory();
    File dir = new File(sdCard.getAbsolutePath() + "/FasTagQR");
    dir.mkdirs();
    String fileName = String.format("%d.jpg", System.currentTimeMillis());
    File outFile = new File(dir, fileName);
    Toast.makeText(MainActivity.this,"Your FastTag Id is Stored In Device", Toast.LENGTH_SHORT).show();
    try {
        outStream = new FileOutputStream(outFile);
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    }
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
    try {
        outStream.flush();
    } catch (IOException e) {
        e.printStackTrace();
    }
    try {
        outStream.close();
    } catch (IOException e) {
        e.printStackTrace();
    }

}
    private String decrypt(String out, String anoop) throws Exception {
        SecretKeySpec k=gen(anoop);
        Cipher cipher=Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE,k);
        byte[] dv= Base64.decode(out,Base64.DEFAULT);
        byte[] deco= cipher.doFinal(dv);
        String n=new String(deco);
        return n;

    }

    private String encrypt(String user, String pass) throws Exception
    {
        SecretKeySpec k=gen(pass);
        Cipher cipher=Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE,k);
        byte[] en=cipher.doFinal(user.getBytes());
        String vall= Base64.encodeToString(en,Base64.DEFAULT);
        return vall;

    }

    private SecretKeySpec gen(String pass) throws Exception {

        final MessageDigest di= MessageDigest.getInstance("SHA-256");

        byte[] bytes=pass.getBytes("UTF-8");
        di.update(bytes,0,bytes.length);
        byte[] key=di.digest();
        SecretKeySpec secretKeySpec=new SecretKeySpec(key,"SHA");
        return secretKeySpec;

    }
}






