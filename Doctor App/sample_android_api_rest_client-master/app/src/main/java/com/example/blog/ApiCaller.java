package com.example.blog;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.StringRequest;
import java.io.UnsupportedEncodingException;
import java.text.BreakIterator;

import com.android.volley.AuthFailureError;
import com.android.volley.toolbox.Volley;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response;
import com.android.volley.Request;
import android.content.Context;
import org.json.JSONException;

import android.net.ConnectivityManager;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONObject;

public class ApiCaller {
    private Toast mToastToShow;
    public void showToast(Context c,String p) {
        // Set the toast and duration
        int toastDurationInMilliSeconds = 200;
        mToastToShow = Toast.makeText(c, p, Toast.LENGTH_LONG);

        // Set the countdown to display the toast
        CountDownTimer toastCountDown;
        toastCountDown = new CountDownTimer(toastDurationInMilliSeconds, 200 /*Tick duration*/) {
            public void onTick(long millisUntilFinished) {
                mToastToShow.show();
            }
            public void onFinish() {
                mToastToShow.cancel();
            }
        };

        // Show the toast and starts the countdown
        mToastToShow.show();
        toastCountDown.start();
    }
    public void Submit(String URL, String data, Context context) {
        final String savedata= data;

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //Reason is some un-wanted characters was added when you compose the String. Try to remove hidden characters on source String.
                    JSONObject objres=new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));
                //  Toast.makeText(context,objres.toString(),Toast.LENGTH_LONG).show();
                   // showToast(context,objres.toString());
                    Log.i("VOLLEY", response);

                } catch (JSONException e) {
                   // Toast.makeText(context,"Server Error"+e.toString(),Toast.LENGTH_LONG).show();
                    showToast(context,"server error"+e.toString());
                }
                //Log.i("VOLLEY", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                error.printStackTrace();
                //Log.v("VOLLEY", error.toString());
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return savedata == null ? null : savedata.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    //Log.v("Unsupported Encoding while trying to get the bytes", data);
                    return null;
                }
            }

        };
        requestQueue.add(stringRequest);
    }


    public void getRequest(Context context,String url){

        RequestQueue queue = Volley.newRequestQueue(context);


        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    TextView t;
                    @Override

                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                      //  texrtView.setText("Response is: "+ response.substring(0,500));
                        try {
                            JSONObject o=new JSONObject(response);
                            int i= o.getInt("flag");
                            if(i==1)
                            {
                                Toast.makeText(context,response,Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }                        Log.d("VOLLEY", response);
//}
                        // showToast(context,response.substring(0,500));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

               // mTextView.setText("That didn't work!");
              //  Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(context,
                            "time out",
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof AuthFailureError) {
                    //TODO
                    Toast.makeText(context, "auth error", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    //TODO
                    Toast.makeText(context, "server error", Toast.LENGTH_SHORT).show();

                } else if (error instanceof NetworkError) {
                    //TODO
                    Toast.makeText(context, "nw error", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    //TODO
                    Toast.makeText(context, "parse error", Toast.LENGTH_SHORT).show();
                }
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
