package com.ttr.homejsonasync;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class MainActivity extends Activity {

    static String students = "http://www.csita.cz/sklad/studenti.json";

    static int    studentId;
    static String studentName = "";
    static String studentSurname = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new MyAsyncTask().execute();

    }

    private class MyAsyncTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            DefaultHttpClient httpClient = new DefaultHttpClient(new BasicHttpParams());

            HttpPost httppost = new HttpPost(students);

            httppost.setHeader("Content-Type", "application/json");

            InputStream inputStream = null;

            String result = null;

            try {

                HttpResponse response = httpClient.execute(httppost);
                HttpEntity entity = response.getEntity();
                inputStream = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                StringBuilder theStringBuilder = new StringBuilder();

                String line = null;

                while((line = reader.readLine()) != null) {
                    theStringBuilder.append(line + "\n");
                }
                result = theStringBuilder.toString();
             }

            catch(Exception e){
                e.printStackTrace();
            }
            finally {
                try{
                    if(inputStream != null) inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            JSONObject root;

            try {
                root = new JSONObject(result);

                JSONArray studenti = root.getJSONArray("studenti");

                //Pole seznamStudentu by melo obsahovat 5 objektu
                ArrayList seznamStudentu = new ArrayList<String>();

                for(int i = 0; i < studenti.length(); i++) {
                    JSONObject zaznam = studenti.getJSONObject(i);

                    studentId = zaznam.getInt("id");
                    studentName = zaznam.getString("jmeno");
                    studentSurname = zaznam.getString("prijmeni");

                    //Každý záznam (=student) přidám do ArrayListu, ze kterého budu studenty načítat do ListView !!
                    seznamStudentu.add(zaznam);
                }
            }

            catch (JSONException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            TextView line1 = (TextView) findViewById(R.id.line1);
            TextView line2 = (TextView) findViewById(R.id.line2);
            TextView line3 = (TextView) findViewById(R.id.line3);

            line1.setText(getString(R.string.studentId) + studentId);
            line2.setText(getString(R.string.name) + studentName);
            line3.setText(getString(R.string.surname) + studentSurname);

        }
    }
}
