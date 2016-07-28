package com.example.rupali.onlineshopping1;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Entity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;

public class LoginPage extends AppCompatActivity {

    EditText etUser,etUser1,etPass1;
    TextView textView;
    String username,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void loginSuccess(View view) {
        etUser = (EditText) findViewById(R.id.etUsername);
        username = etUser.getText().toString();
        Toast.makeText(this,"You have successfully logged in "+username,Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void getData(){
        etUser1 = (EditText) findViewById(R.id.etUsername);
        username = etUser1.getText().toString();
        etPass1 = (EditText) findViewById(R.id.etPassword);
        password = etPass1.getText().toString();
        class task extends AsyncTask<String, Void, String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(LoginPage.this, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();

                if(Integer.parseInt(s)==1){
                    s = "Welcome "+username;
                    Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                    Intent inAdmin = new Intent(LoginPage.this,Admin.class);
                    startActivity(inAdmin);
                }
                else{
                    s = "You are not authenticated. Try again.";
                    Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(String... params) {
                List<NameValuePair> data = new ArrayList<NameValuePair>();
                data.add(new BasicNameValuePair("username",username));
                data.add(new BasicNameValuePair("password",password));
                InputStream inputStream = null;
                String result = null;

                try{
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost("http://redesignedfashion.esy.es/LoginAdmin.php");
                    Log.d("url","working");
                    httpPost.setEntity(new UrlEncodedFormEntity(data));
                    Log.d("setEntity","working");
                    HttpResponse httpResponse = httpClient.execute(httpPost);
                    Log.d("http",httpResponse.getEntity().toString());
                    HttpEntity entity = httpResponse.getEntity();
                    inputStream = entity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader
                            (inputStream,"UTF-8"),8);
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    Log.d("sb",reader.toString());
                    while((line=reader.readLine())!=null){
                        sb.append(line+'\n');
                    }
                    Log.d("sb",sb.toString());
                    result = sb.toString().trim();
                }
                catch (Exception e){
                    return "Unsuccessful";
                }
                return result;
            }

        }
        task t = new task();
        t.execute();
    }
    public void loginAdminSuccess(View view) {
        getData();
    }
}
