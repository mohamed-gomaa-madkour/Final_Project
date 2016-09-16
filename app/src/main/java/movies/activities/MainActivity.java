package movies.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.FrameLayout;

import com.example.moviesapp.R;

import movies.calback.ActivityCallback;
import movies.model.Movie;
import movies.fragments.DetailFragment;

/**
 * Created by MOHAM on 12/09/2016.
 */
public class MainActivity extends AppCompatActivity implements ActivityCallback {

    static final String TAG = "MainActivityLog";
    public static FrameLayout frameLayout; // static to be used in MovieFragment to check the device type.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        frameLayout = (FrameLayout) findViewById(R.id.frame_container);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(null);
        }
    }

    @Override
    public void actionCallback(Movie movie) {
        Log.d(TAG, "Callback with " + movie.getJsonObject());

        if (frameLayout == null) { // start Activity when app runs on a phone
            Intent intent = new Intent(MainActivity.this, DetailActivity.class)
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