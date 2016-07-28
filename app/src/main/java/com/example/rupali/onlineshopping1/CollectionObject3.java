package com.example.rupali.onlineshopping1;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class CollectionObject3 extends Fragment {

    ListView list;
    Button btn;
    public static final String GET_IMAGE_URL="http://redesignedfashion.esy.es/getcasual.php";

    public GetAllImages getAlImages;

    public static final String BITMAP_ID = "image";

    public CollectionObject3() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_collection_object3, container, false);
        list=(ListView)v.findViewById(R.id.lvCasual);
        //getUrls();
        return v;
    }
    public void btnClickCasualDisp(){
        getUrls();
    }

    private void getImages(){
        class GetImages extends AsyncTask<Void,Void,Void> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(),"Downloading images."," Please wait...",false,false);
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                loading.dismiss();
                CustomList customList = new CustomList(getActivity(),GetAllImages.imageURLs,GetAllImages.bitmaps);
                list.setAdapter(customList);
            }

            @Override
            protected Void doInBackground(Void... voids) {
                try{
                    getAlImages.getAllImages();
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
                return null;
            }
        }
        GetImages getImages = new GetImages();
        getImages.execute();
    }

    private void getUrls(){
        class GetUrls extends AsyncTask<String,Void,String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(),"Loading...","Please wait..",true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                getAlImages = new GetAllImages(s);
                getImages();
            }

            @Override
            protected String doInBackground(String... params) {
                BufferedReader bufferedReader = null;
                try{
                    URL url = new URL(params[0]);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();
                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;
                    while((json=bufferedReader.readLine())!=null){
                        sb.append(json+"\n");
                    }
                    return sb.toString().trim();
                }catch (Exception e){
                    return null;
                }
            }
        }
        GetUrls gu = new GetUrls();
        gu.execute(GET_IMAGE_URL);
    }
}
