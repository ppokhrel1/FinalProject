package com.example.abhishek.final_project;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SearchActivity extends Activity
{
    // the Rotten Tomatoes API key of your application! get this from their website
    private static final String API_KEY = "2a22404edcda6b604ead94228eb4e006";

    // the number of movies you want to get in a single request to their web server
    private static final int MOVIE_PAGE_LIMIT = 10;
    //public MyMovie movie;
    private EditText searchBox;
    private Button searchButton;
    private ListView moviesList;
    //private List<MyMovie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchBox = (EditText) findViewById(R.id.text_search_box);
        searchButton = (Button) findViewById(R.id.button_search);
        searchButton.setOnClickListener(new OnClickListener()
        {
            // send an API request when the button is pressed
            @Override
            public void onClick(View arg0)
            {
                //new RequestTask().execute(");//+ "s=" + "Borat");
                //http://api.rottentomatoes.com/api/public/v1.0/movies.json?apikey=" + API_KEY + "&q=" + "borat" + "&page_limit=" + MOVIE_PAGE_LIMIT
                //searchBox.getText().toString().trim()
                try {
                    final List<MyMovie> movies;
                    movies = new RecipeFeed().execute().get();
                    final ArrayList<String> mymovies = new ArrayList<String>();
                    moviesList = (ListView) findViewById(R.id.list_movies);
                    for(MyMovie movie: movies){
                        mymovies.add(movie.title);
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(SearchActivity.this, android.R.layout.simple_list_item_1, mymovies);
                    moviesList.setAdapter(arrayAdapter);
                    moviesList.setOnItemClickListener(new AdapterView.OnItemClickListener()
                    {
                        // argument position gives the index of item which is clicked
                        public void onItemClick(AdapterView<?> arg0, View v,int position, long arg3)
                        {

                            String selectedAnimal=mymovies.get(position);
                            Toast.makeText(getApplicationContext(), "Movie selected : " + selectedAnimal, Toast.LENGTH_LONG).show();
                        }
                    });
                }catch(Exception e){
                    Log.d("ad", "exception");
                }
            }
        });


    }

    private void refreshMoviesList(String[] movieTitles)
    {
        moviesList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_2, movieTitles));
    }

    class RecipeFeed extends AsyncTask<String, Void, List<MyMovie>> {

        protected ArrayList<MyMovie> doInBackground(String... urls) {
            //Log.d("ppdasf", "auiydfui");
            return this.populate();
        }

        protected void onPostExecute(List<MyMovie> recipes){
            super.onPostExecute(recipes);
        }

        //returns a list of recipes after querying the site.
        //parameters are obtained after querying the url.
        private ArrayList<MyMovie> populate(){
            ArrayList<MyMovie> items = new ArrayList<MyMovie>();
            int pageNumber = 1;
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            //String ingredientsAPI = intent.getStringExtra("ingredients");
            //ingredientsAPI.replaceAll(" ", "+");
            //String myRecipe = intent.getStringExtra("recipe");
            //Log.d("tystdf", ingredientsAPI + myRecipe);
            int randomNumber = (int) new Random().nextInt(5000);
            Log.d("gore ", " " + randomNumber);
            String myurl = "http://api.themoviedb.org/3/movie/" + randomNumber +"?api_key=" + API_KEY + "&append_to_response=similar_movies";
            try {
                URL url = new URL
                        (myurl);
                //Log.d("kjhdfkjas", myurl);
                HttpURLConnection urlConnection =
                        (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                // gets the server json data
                BufferedReader bufferedReader =
                        new BufferedReader(new InputStreamReader(
                                urlConnection.getInputStream()));
                String next;


                while ((next = bufferedReader.readLine()) != null) {
                    //JSONArray ja = new JSONArray(next);
                    JSONObject jas = new JSONObject(next);
                    //JSONArray ja = new JSONArray("results");
                    Log.d("uweyuri", jas.toString());

                    JSONObject myJson = jas.getJSONObject("similar_movies");
                    Log.d("sdf", myJson.toString());
                    JSONArray ja = myJson.getJSONArray("results");
                    //ja = ja.getJSONArray("results");
                    //JSONObject js = (JSONObject) ja.getString("results");
                    Log.d("adfas", ja.toString());
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject jo = (JSONObject) ja.get(i);
                        Log.d("helo", "muji");
                        //JSONArray js = ja.getJSONArray(i);
                        //Log.d("sdfaf", js.toString());
                        Log.d("adf", jo.toString());
                        MyMovie movie = new MyMovie();
                        movie.title = jo.getString("original_title");
                        items.add(movie);
                        Log.d("adf", movie.title);
                        //get all the jsonobjects, create different recipes from them and add to items array.

                    }
                }
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return items;
            //return null;
        }
    }
}