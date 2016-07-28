package com.example.rupali.onlineshopping1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class AddCasual extends AppCompatActivity {

    ImageView ivCasual;
    EditText etNameCasual,etPriceCasual;
    Bitmap bitmap=null;
    String pic,name,price;

    String urlCasual="http://redesignedfashion.esy.es/casual.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_casual);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ivCasual = (ImageView) findViewById(R.id.ivCasual);
        etNameCasual = (EditText) findViewById(R.id.etNameCasual);
        etPriceCasual = (EditText) findViewById(R.id.etPriceCasual);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void btnCasual(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Choose image to upload"), 15);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 15:
                if (resultCode == RESULT_OK) {
                    if (requestCode == 15 && resultCode == RESULT_OK && data != null && data.getData() != null) {

                        Uri uri = data.getData();
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            ivCasual.setImageBitmap(bitmap);
                            pic=getStringImage(bitmap);
                            //uploadImage_newprod(bitmap, "hello");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }
        }
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        Log.d("image",encodedImage);
        return encodedImage;
    }

    public void btnAddCasual(View view) {

        name = etNameCasual.getText().toString();
        price = etPriceCasual.getText().toString();

        if(bitmap!=null && name!=null && price!=null){
            uploadImage_casual(bitmap, "hello");
        }
        else
        {
            Toast.makeText(AddCasual.this,"Fill all the entries",Toast.LENGTH_LONG).show();
        }
    }

    public boolean uploadImage_casual(Bitmap bit,final String code)
    {
        class UploadImage extends AsyncTask<Bitmap,Void,String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(AddCasual.this, "Uploading Image", "Please wait...", true, true);
            }


            @Override
            protected String doInBackground(Bitmap... params) {
                Bitmap bitmap = params[0];
                String uploadImage = pic;

                List<NameValuePair> data = new ArrayList<NameValuePair>();
                data.add(new BasicNameValuePair("image", pic));
                data.add(new BasicNameValuePair("name", name));
                data.add(new BasicNameValuePair("price", price));

                InputStream inputStream = null;

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(urlCasual);
                    Log.d("url", "working");
                    httpPost.setEntity(new UrlEncodedFormEntity(data));
                    Log.d("setEntity", "working");
                    HttpResponse response = httpClient.execute(httpPost);
                    Log.d("http", response.getEntity().toString());
                    HttpEntity entity1= response.getEntity();
                    inputStream = entity1.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader
                            (inputStream,"UTF-8"),8);
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    Log.d("sb",reader.toString());
                    while((line=reader.readLine())!=null){
                        sb.append(line+'\n');
                    }
                    Log.d("sb",sb.toString());

                    //String result
                    //    b=true;
                    return "Success";
                } catch (Exception ex) {
                }

                return "Unsuccessful";
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Toast.makeText(AddCasual.this,s,Toast.LENGTH_SHORT).show();
                loading.dismiss();
            }

        }
        new UploadImage().execute(bit);
        return true;
    }
}
