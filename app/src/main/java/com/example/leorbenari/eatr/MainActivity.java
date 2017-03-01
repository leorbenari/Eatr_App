package com.example.leorbenari.eatr;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Request;
import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.connection.YelpAPIFactory;
import com.yelp.clientlib.entities.Business;
import com.yelp.clientlib.entities.Category;
import com.yelp.clientlib.entities.SearchResponse;
import com.yelp.clientlib.entities.options.CoordinateOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    OkHttpClient mClient;
    List<Restaurant> mRestaurants;

    TextView RestaurantTitle, RateCategory;
    ImageView MainImage;
    YelpAPIFactory APIFactory;
    YelpAPI yelpAPI;
    Map<String, String> mParams;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mClient = new OkHttpClient();

        RestaurantTitle = (TextView) findViewById(R.id.titleLabel);
        RateCategory = (TextView) findViewById(R.id.rateCategoryLabel);

        MainImage = (ImageView) findViewById(R.id.mainImage);
        mRestaurants = new ArrayList<>();

        Picasso.with(this).load("http://i.imgur.com/DvpvklR.png").into(MainImage);

        MainImage.setOnTouchListener(new OnSwipeTouchListener(this){

            public void onSwipeTop() {
                Toast.makeText(MainActivity.this, "top", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeRight() {
                Toast.makeText(MainActivity.this, "right", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeLeft() {
                Toast.makeText(MainActivity.this, "left", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeBottom() {
                Toast.makeText(MainActivity.this, "bottom", Toast.LENGTH_SHORT).show();
            }

        });

        YelpAPIFactory APIFactory = new YelpAPIFactory(

                getString(R.string.consumerKey),
                getString(R.string.consumerSecret),
                getString(R.string.token),
                getString(R.string.tokenSecret));

                yelpAPI = APIFactory.createAPI();
                mParams = new HashMap<>();

        mParams.put("term", "food");



        new getPictures().execute();

    }

    class getPictures extends AsyncTask<String, Restaurant, String> {

        List<Restaurant> restaurants;

        @Override
        protected void onProgressUpdate(Restaurant...values){
            super.onProgressUpdate(values);
            mRestaurants.add(values[0]);
        }

        @Override
        protected String doInBackground(String... params) {

            CoordinateOptions coordinate = CoordinateOptions.builder()
                    .latitude(37.7421)
                    .longitude(-122.3436).build();
            Call<SearchResponse> call = yelpAPI.search(coordinate, mParams);
            Response<SearchResponse> response = null;
            try {
                response = call.execute();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (response != null) {
                Log.v("Businesses", response.body().businesses().toString());
                List<Business> businessList = response.body().businesses();
                restaurants = new ArrayList<>();
                Restaurant rest;

                int i = 0;

                for (Business bus: businessList) {
                    rest = new Restaurant(bus.name(), bus.url());
                    rest.setRatingCategory(bus.rating() + " " + catToString(bus.categories()));
                    getPics(rest, i);
                    i++;
                }


            }




            return null;
        }

        private String catToString(ArrayList<Category>  categories){

            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < categories.size(); i++){
                sb.append(categories.get(i).name());
                if (i != categories.size()-1) {
                    sb.append(", ");
                }
            }

            return sb.toString();
        }

        private void getPics(Restaurant r, final int index) {

            okhttp3.Request request = new okhttp3.Request.Builder()

                    .url(r.getPicURL())
                    .build();

                mClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(okhttp3.Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {

                        List<String> pictures = RestaurantPicScraper.getPics(response.body().string());

                        if (pictures.size() > 0) {
                            restaurants.get(index).setPictures(pictures);
                            onProgressUpdate(restaurants.get(index));
                        }
                    }
                })
                return response.body().string();




        }




    }

}
