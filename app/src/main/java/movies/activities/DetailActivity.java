package movies.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.moviesapp.R;

import movies.fragments.DetailFragment;
/**
 * Created by MOHAM on 12/09/2016.
 */
public class DetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        // add fragment on this Activity and send movie data to it.



       Bundle arguments = new Bundle();
        arguments.putParcelable(DetailFragment.MOVIE_DATA,
                getIntent().getParcelableExtra(DetailFragment.MOVIE_DATA));
        DetailFragment detailFragment = new DetailFragment();
        detailFragment.setArguments(arguments);


        getSupportFragmentManager().beginTransaction().replace(R.id.movie_detail_container, detailFragment).commit();




        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(null);
        }

    }
}