package movies.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.moviesapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import movies.activities.MoviesActivity;
import movies.adapter.MovieGridAdapter;
import movies.ActivityCalback.ActivityCallback;
import movies.database.DataSource;
import movies.model.Movie;
import movies.BackgroundTasks.MoviesTask;
import movies.utils.Connectivity;
import movies.utils.Settings;

/**
 * Created by MOHAM on 12/09/2016.
 */

public class MovieFragment extends Fragment implements MoviesTask.Callback {


    private final String FAVOURITES = "fav";
    private final String POPULARITY_DESC = "popularity.desc";
    private final String RATING_DESC = "vote_count.desc";
    private final String MOVIES_KEY = "movies";
    private GridView mGridView;
    private TextView progressTV;
    private ProgressBar progressBar;
    private LinearLayout progressLayout;
    private MovieGridAdapter mMovieGridAdapter;
    private String sortBy = POPULARITY_DESC;
    private ArrayList<Movie> moviesList = null;
    private Settings settings;
    MoviesTask moviesTask;

    String TAG = "MovieFragment";

    ActivityCallback activityCallback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        settings = new Settings(getActivity());
        Log.d(TAG, "Create");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "On Create View");
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        activityCallback = (ActivityCallback) getActivity();

        mGridView = (GridView) view.findViewById(R.id.gridview_movies);
        progressLayout = (LinearLayout) view.findViewById(R.id.progress);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressTV = (TextView) view.findViewById(R.id.progressTV);


        initAdapter();
        Log.d(TAG, "Update First Time..");
        sortBy = settings.getSortBy();
        if (!sortBy.equalsIgnoreCase(FAVOURITES))
            updateMovies(sortBy);
        else {
            loadFavourites();
        }

        return view;
    }

    private void initAdapter() {
        mMovieGridAdapter = new MovieGridAdapter(getActivity());
        mGridView.setAdapter(mMovieGridAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = mMovieGridAdapter.getItem(position);
                if (activityCallback != null)
                    activityCallback.actionCallback(movie);
            }
        });
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (moviesList != null) {
            outState.putParcelableArrayList(MOVIES_KEY, moviesList);
        }
        Log.d(TAG, "On Save Instance..");
        super.onSaveInstanceState(outState);
    }

    private void updateMovies(String sort_by) {
        Log.d(TAG, "Loading Movies in order to " + sort_by);
        if (Connectivity.isConnected(getActivity())) {
            moviesTask = new MoviesTask(MovieFragment.this);
            moviesTask.execute(sort_by);
        } else {
            progressBar.setVisibility(View.GONE);
            progressTV.setText("Check Internet Connection");
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_main, menu);
        MenuItem action_sort_by_popularity = menu.findItem(R.id.action_sort_by_popularity);
        MenuItem action_sort_by_rating = menu.findItem(R.id.action_sort_by_rating);
        MenuItem action_show_favourites = menu.findItem(R.id.action_show_favourites);
        sortBy = settings.getSortBy();
        if (sortBy.equalsIgnoreCase(RATING_DESC))
            action_sort_by_rating.setChecked(true);
        else if (sortBy.equalsIgnoreCase(POPULARITY_DESC))
            action_sort_by_popularity.setChecked(true);
        else
            action_show_favourites.setChecked(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (moviesTask != null)
            moviesTask.cancel(true);
        if (item.isChecked())
            return true;
        item.setChecked(true);
        switch (id) {
            case R.id.action_sort_by_popularity:
                sortBy = POPULARITY_DESC;
                updateMovies(sortBy);
                settings.setSortBy(sortBy);
                return true;
            case R.id.action_sort_by_rating:
                sortBy = RATING_DESC;
                settings.setSortBy(sortBy);
                updateMovies(sortBy);
                return true;
            case R.id.action_show_favourites:
                sortBy = FAVOURITES;
                settings.setSortBy(sortBy);
                loadFavourites();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadFavourites() {
        DataSource dataSource = new DataSource(getActivity());
        dataSource.open();
        List<Movie> movieList = new ArrayList<>();

        Cursor cursor=dataSource.getMovies();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String movieObject = cursor.getString(2);
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(movieObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Movie movie = null;
            try {
                movie = new Movie(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            movieList.add(movie);
            cursor.moveToNext();
        }
        cursor.close();
        moviesList=(ArrayList<Movie>) movieList;
        Log.d(TAG, "Favourites Size " + moviesList.size());
        if (moviesList == null || moviesList.isEmpty()) {
            progressLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            progressTV.setText("No Favourites..");
            mGridView.setVisibility(View.GONE);
        } else {
            progressLayout.setVisibility(View.GONE);
            mMovieGridAdapter.clear();
            for (Movie movie : moviesList)
                mMovieGridAdapter.add(movie);
            mMovieGridAdapter.notifyDataSetChanged();
        }
        dataSource.close();
    }

    @Override
    public void preExecute() {
        mGridView.setVisibility(View.GONE);
        progressLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        progressTV.setVisibility(View.VISIBLE);
        progressTV.setText("Loading Movies \n wait a moment...");

    }

    @Override
    public void postExecute(List<Movie> movieList) {
        if (mMovieGridAdapter != null) {
            mMovieGridAdapter.clear();
            if (movieList != null)
                for (Movie movie : movieList) {
                    mMovieGridAdapter.add(movie);
                }
        }
        mGridView.setVisibility(View.VISIBLE);
        progressLayout.setVisibility(View.GONE);
        moviesList = new ArrayList<>();
        if (movieList != null)
            moviesList.addAll(movieList);
        if (MoviesActivity.frameLayout != null)
            if (activityCallback != null && !moviesList.isEmpty())
                activityCallback.actionCallback(moviesList.get(0));
    }
}