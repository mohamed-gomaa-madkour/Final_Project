package movies.fragments;


import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moviesapp.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;

import movies.database.DataSource;
import movies.database.MovieContract;
import movies.model.Movie;
import movies.model.Review;
import movies.model.Trailer;
import movies.BackgroundTasks.ReviewsTask;
import movies.BackgroundTasks.TrailersTask;
import movies.utils.Connectivity;


/**
 * Created by MOHAM on 12/09/2016.
 */

public class DetailFragment extends Fragment implements TrailersTask.TrailersCallback, ReviewsTask.ReviewCallback {

    public static final String MOVIE_DATA = "DETAIL_MOVIE";
    public static final String TAG = "Detail Fragment";
    List<Trailer> trailerList;
    private Movie movie;
    private ImageView mImageView;
    private TextView mTitleView, mOverviewView, releaseDateTV;
    private TextView voteAverageView;

    ListView trailersListV, reviewsListV;
    ImageButton favBtn;

    private boolean isFavorite(int movie_id) {
        // search in database to know if movie in favourite list or no
        DataSource dataSource = new DataSource(getActivity());
        dataSource.open();

        Cursor cursor=dataSource.searchID( movie_id);
        cursor.moveToFirst();

        if(cursor.getCount() > 0){
            return true;
        }else return false;


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        init(rootView);
        return rootView;
    }

    private void init(View rootView) {
        Bundle arguments = getArguments();
        if (arguments == null)
            return;

        movie = arguments.getParcelable(DetailFragment.MOVIE_DATA);

        trailersListV = (ListView) rootView.findViewById(R.id.trailersListV);
        reviewsListV = (ListView) rootView.findViewById(R.id.reviewsListV);

        mImageView = (ImageView) rootView.findViewById(R.id.moviePoster);
        mTitleView = (TextView) rootView.findViewById(R.id.detail_title);
        mOverviewView = (TextView) rootView.findViewById(R.id.overview);
        releaseDateTV = (TextView) rootView.findViewById(R.id.date);
        voteAverageView = (TextView) rootView.findViewById(R.id.vote_average);

        favBtn = (ImageButton) rootView.findViewById(R.id.favBtn);
        favBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFavorite(movie.getId()))
                {
                   // adding movie to favourite list
                    DataSource dataSource = new DataSource(getActivity());
                dataSource.open();
                ContentValues values = new ContentValues();
                values.put(MovieContract.MOVIE_ID, movie.getId());
                String movieData = movie.getJsonObject();
                values.put(MovieContract.MOVIE_JSONOBJECT, movieData);
                long id = dataSource.addMovie(values);
                if (id != -1) {
                    Toast.makeText(getActivity(), "Added to Favourites", Toast.LENGTH_SHORT).show();
                    favBtn.setImageResource(R.mipmap.ic_remove_favourites);
                }}
                else {
                   //delete movie from favourite list
                    DataSource dataSource = new DataSource(getActivity());
                    dataSource.open();
                    int id = dataSource.deleteMovie(movie.getId());
                    if (id != 0) {
                        Toast.makeText(getActivity(), "Removed from Favourites", Toast.LENGTH_SHORT).show();
                        favBtn.setImageResource(R.mipmap.ic_favourites);
                    }


                }
            }
        });
        trailersListV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              // make intent to youtube

                String url = "https://www.youtube.com/watch?v=" + trailerList.get(position).getKey();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });
        if (isFavorite(movie.getId())) {
            favBtn.setImageResource(R.mipmap.ic_remove_favourites);
        }

        if (Connectivity.isConnected(getActivity()))
            new TrailersTask(DetailFragment.this).execute(movie.getId() + "");
        new ReviewsTask(DetailFragment.this).execute(movie.getId() + "");
        try {
            String image_url = "http://image.tmdb.org/t/p/w342" + movie.getImage2();
            Picasso.with(getActivity()).load(image_url).into(mImageView);
            mTitleView.setText(movie.getTitle());
            mOverviewView.setText(movie.getOverview());
            String movie_date = movie.getDate();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String date = DateUtils.formatDateTime(getActivity(),
                    dateFormat.parse(movie_date).getTime(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR);
            releaseDateTV.setText(date);
            voteAverageView.setText(Integer.toString(movie.getRating()));
        } catch (Exception e) {
            Log.e(TAG, "Error " + e);
        }
    }




    @Override
    public void postExecuteReviews(List<Review> reviewList) {
        try {
            String[] reviews = new String[reviewList.size()];
            Log.d(TAG, "Reviews Count " + reviewList.size());
            for (int i = 0; i < reviewList.size(); i++) {
                reviews[i] = reviewList.get(i).getAuthor()+"--->> "+reviewList.get(i).getContent();
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, reviews);
            reviewsListV.setAdapter(arrayAdapter);
        } catch (Exception e) {
            Log.e(TAG, "Exception , " + e);
        }
    }

    @Override
    public void postExecuteTrailers(List<Trailer> trailerList) {
        try {
            DetailFragment.this.trailerList = trailerList;
            String[] trailers = new String[trailerList.size()];
            Log.d(TAG, "Trailers Count " + trailerList.size());
            for (int i = 0; i < trailerList.size(); i++) {
                trailers[i] = trailerList.get(i).getName();
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, trailers);
            trailersListV.setAdapter(arrayAdapter);
        } catch (Exception e) {
            Log.e(TAG, "Exception , " + e);
        }
    }

}