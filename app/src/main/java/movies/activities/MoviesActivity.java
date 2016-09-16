package movies.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.FrameLayout;

import com.example.moviesapp.R;

import movies.ActivityCalback.ActivityCallback;
import movies.fragments.DetailFragment;
import movies.model.Movie;

/**
 * Created by MOHAM on 12/09/2016.
 */
public class MoviesActivity extends AppCompatActivity implements ActivityCallback {

    static final String TAG = "MainActivityLog";
    public static FrameLayout frameLayout; // static to be used in MovieFragment to check the device type.
    Movie mv=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        frameLayout = (FrameLayout) findViewById(R.id.frame_container);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {

            getSupportActionBar().setTitle(null);
        }
    }


    @Override
    public void actionCallback(Movie movie) {
        Log.d(TAG, "Callback with " + movie.getJsonObject());
        mv=movie;

        if (frameLayout == null) { // start Activity when app runs on a phone
            Intent intent = new Intent(MoviesActivity.this, DetailActivity.class)
                    .putExtra(DetailFragment.MOVIE_DATA, movie);
           startActivity(intent);
        } else { // start Activity when app runs on a tablet
            Bundle arguments = new Bundle();

            arguments.putParcelable(DetailFragment.MOVIE_DATA,
                    movie);

            DetailFragment detailFragment = new DetailFragment();
            detailFragment.setArguments(arguments);



            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, detailFragment).commit();
        }
    }
}