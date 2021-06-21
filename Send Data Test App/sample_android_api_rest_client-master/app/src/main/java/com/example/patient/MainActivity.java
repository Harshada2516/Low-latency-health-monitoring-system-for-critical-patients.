package com.example.patient;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.AppCompatActivity;

import static java.lang.Integer.parseInt;

public class MainActivity extends AppCompatActivity {
    Context context;



    // Create the Handler
    private final Handler handler = new Handler();
    private final Handler handler1 = new Handler();

    private final Runnable runnable1 = new Runnable() {
        @Override
        public void run() {
            // Insert custom code here

            try {
                context=getApplicationContext();
                // Toast.makeText(context, "send data", Toast.LENGTH_LONG).show();
                reqGetRequest(context);

            } catch (Exception e) {
                // Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            // Repeat every 2 seconds
            handler.postDelayed(runnable, 2000);
        }
    };

    // Define the code block to be executed
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // Insert custom code here

            try {
                context=getApplicationContext();
               Toast.makeText(context, "send data", Toast.LENGTH_LONG).show();
                postRequest(context);

            } catch (Exception e) {
               // Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            // Repeat every 2 seconds
            handler.postDelayed(runnable, 200);
        }
    };








    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


      //  final Button clear = findViewById(R.id.rec);
        final Button button = findViewById(R.id.button_new_post);
//        final Button get1 = findViewById(R.id.g);
       final Button c = findViewById(R.id.p);
        final Button l = findViewById(R.id.q);
        c.setOnClickListener(v -> {
            Context context = getApplicationContext();
            try {
                Toast.makeText(context, "remove", Toast.LENGTH_LONG).show();
                handler.removeCallbacks(runnable);

            } catch (Exception e) {
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
            }
        });
//
        button.setOnClickListener(v -> {
            Context context = getApplicationContext();
            try {
                //Toast.makeText(context, "continuous data sending", Toast.LENGTH_LONG).show();
               //handler.post(runnable);
              postRequest(context);
            } catch (Exception e) {
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
            }
        });

        l.setOnClickListener(v -> {
            Context context = getApplicationContext();
            try {
                Toast.makeText(context, "button click", Toast.LENGTH_LONG).show();
                for(int i=0;i<11;i++) {
                    latency(context);
                }

            } catch (Exception e) {
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
            }
        });

    }


    public void postRequest(Context context) throws Exception {
        // replace with your host server URI
       String URL = "http://155.146.11.205:8585/patient/";

        ApiCaller apiCaller = new ApiCaller();

        try {


            String jsonLocation = AssetJSONFile("patient.json", MainActivity.this);
            JSONObject jsonobject = new JSONObject(jsonLocation);


            int i = 0, r = 0, h = 0, sp = 0;
            int co = jsonobject.getInt("co2");
            sp = jsonobject.getInt("spo2");
            h = jsonobject.getInt("heartRate");
            r = parseInt(jsonobject.getString("resp"));
            int bps = jsonobject.getInt("bpS");
            int bpd = jsonobject.getInt("bpD");
            int t = jsonobject.getInt("temp");
           //jsonobject.put("temp",992);
          //  apiCaller.Submit(URL, jsonobject.toString(), context);
            // send normal data
            int j;
            for( j=0;j<2;j++){
                apiCaller.Submit(URL, jsonobject.toString(), context);
              //  apiCaller.getRequest(context,url);
                TimeUnit.SECONDS.sleep(2);
            }
            TimeUnit.SECONDS.sleep(2);

            //spo2 --
            for( j=0;j<4;j++) {
                jsonobject.put("spo2", --sp);
                apiCaller.Submit(URL, jsonobject.toString(), context);

                TimeUnit.SECONDS.sleep(2);
            }
            TimeUnit.SECONDS.sleep(2);
            // normal data
            for( j=0;j<2;j++) {
                apiCaller.Submit(URL, jsonobject.toString(), context);

                TimeUnit.SECONDS.sleep(2);
            }
            //h--,spo2--
            for(j=0;j<2;j++) {
                jsonobject.put("heartRate", --h);
                jsonobject.put("spo2", --sp);
                apiCaller.Submit(URL, jsonobject.toString(), context);

                TimeUnit.SECONDS.sleep(2);
            }
            //normal data
            for( j=0;j<2;j++) {
                apiCaller.Submit(URL, jsonobject.toString(), context);

                TimeUnit.SECONDS.sleep(2);
            }


            // temp++ (above 991) ,spo2--,h--
            for(j=0;j<2;j++) {
                t += 32;
                jsonobject.put("heartRate", --h);
                jsonobject.put("spo2", --sp);
                jsonobject.put("temp", t);
                apiCaller.Submit(URL, jsonobject.toString(), context);

            }


        } catch (IOException ioException) {
            ioException.printStackTrace();
        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        } catch (NumberFormatException numberFormatException) {
            numberFormatException.printStackTrace();
        }


    }


    public static String AssetJSONFile(String filename, Context context) throws IOException {
        AssetManager manager = context.getAssets();
        InputStream file = manager.open(filename);
        byte[] formArray = new byte[file.available()];
        file.read(formArray);
        file.close();

        return new String(formArray);
    }


    // Calculate latency of application by analyzing get request
public void latency(Context context) throws Exception {

    String url = "http://155.146.11.205:8585/latency/";
    ApiCaller apiCaller = new ApiCaller();

        apiCaller.getRequest(context, url);

}

    public void reqGetRequest(Context context) throws Exception {
     //   Toast.makeText(context, "get", Toast.LENGTH_LONG).show();
        String url = "http://155.146.11.205:8585/doc/notif/";

        ApiCaller apiCaller = new ApiCaller();

            apiCaller.getRequest(context, url);
            TimeUnit.SECONDS.sleep(2);


    }
}

