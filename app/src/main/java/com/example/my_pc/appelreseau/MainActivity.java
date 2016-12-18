package com.example.my_pc.appelreseau;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.my_pc.appelreseau.bo.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity
{
    private final AsyncTask masyncTask = new AsyncTask()
    {
        @Override
        protected Object doInBackground(Object[] params)
        {

            try
            {
                Uri.Builder builder = new Uri.Builder();
                builder.scheme("http")
                        .authority("www.smartnsoft.com")
                        .appendPath("shared")
                        .appendPath("weather")
                        .appendPath("index.php")
                        .appendQueryParameter("city", "paris")
                        .appendQueryParameter("forecasts", "5");

                HttpURLConnection urlConnection = (HttpURLConnection) new URL(builder.build().toString()).openConnection();


                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                String answer = readIt(inputStream);
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("ANSWER",answer);
                message.setData(bundle);
                handler.sendMessage(message);

//                // Prepare the request.
//                Request request = new Request.Builder().url("http://smartnsoft.com/shared/weather/index.php?city=paris&forecasts=5").build();
//                // Execute the request.
//                Response response = client.newCall(request).execute();
//                // Get the result.
//                String answer = response.body().string();
//                // Create Message to the IHM thread
//                Message message = new Message();
//                // Get the answer
//                Bundle bundle = new Bundle();
//                bundle.putString("string", answer);
//                // Fill the message
//                message.setData(bundle);
//
//                handler.sendMessage(message);
//                return answer;

            } catch (IOException e)
            {
                e.printStackTrace();
            }
            return null;
        }
    };

    TextView text;
    String result;
    List<Weather> weathers = new ArrayList<>();
    Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
//            weathers = parse(msg.getData().getString("string"));
            result = msg.getData().getString("ANSWER");
            Log.d("thomasecalle","on handle messaage");
            weathers = parse(result);
//            if (text != null)
//            {
//                text.setText(result);
//            }

        }
    };
    private final OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RelativeLayout main = (RelativeLayout) findViewById(R.id.activity_main);
        text = (TextView) findViewById(R.id.text);

        masyncTask.execute();





//        if (weathers != null)
//        {
//            for (Weather weather : weathers)
//            {
//                Log.d("thomasecalle","MIN = "+weather.getTemperatureMin()+ " ||  MAX = "+weather.getTemperatureMax());
////                LinearLayout linear = new LinearLayout(this);
////                linear.setOrientation(LinearLayout.VERTICAL);
////                TextView min = new TextView(this);
////                TextView max = new TextView(this);
////
////                min.setText(weather.getTemperatureMin());
////                max.setText(weather.getTemperatureMax());
////
////                linear.addView(min);
////                linear.addView(max);
////
////                main.addView(linear);
//            }
//        }

    }


    private String readIt(InputStream is) throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(is));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) {
            response.append(line).append('\n');
        }
        return response.toString();
    }


    private List<Weather> parse(final String json) {
        try {
            final List<Weather> weathers = new ArrayList<>();
            final JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray  = jsonObject.getJSONArray("forecasts");
            Log.d("thomasecalle","before loop");
            for (int i = 0; i < jsonArray.length(); i++) {
                Log.d("thomasecalle","inside loop "+ i);
                JSONObject object = jsonArray.getJSONObject(i);
                weathers.add(new Weather(object));
            }
            return weathers;
        } catch (JSONException e) {
            Log.v("thomasecalle", "[JSONException] e : " + e.getMessage());
        }
        return null;
    }
}
