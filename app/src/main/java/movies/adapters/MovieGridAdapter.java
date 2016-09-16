package movies.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.moviesapp.R;
import com.squareup.picasso.Picasso;

import movies.model.Movie;

/**
 * Created by MOHAM on 12/09/2016.
 */
public class MovieGridAdapter extends ArrayAdapter<Movie> {

    public MovieGridAdapter(Context context) {
        super(context, 0);
    }


    public static class listElement {
        public final ImageView movieImgV;
        public final TextView movieTitleTv;

        public listElement(View view) {
            movieImgV = (ImageView) view.findViewById(R.id.movieImgV);
            movieTitleTv = (TextView) view.findViewById(R.id.movieTitleTv);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        listElement element;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.movie_item, parent, false);
            element = new listElement(convertView);
            convertView.setTag(element);
        }

        final Movie movie = getItem(position);
        String base_url="http://image.tmdb.org/t/p/w185";
        String image_url = base_url + movie.getImage(); // setup image url

        element = (listElement) convertView.getTag();
        String movieTitle=movie.getTitle();


        Picasso.with(getContext())
                .load(image_url)
                .into(element.movieImgV); // loading image from url into ImageView
        element.movieTitleTv.setText(movieTitle); // set movie title to TextView
        return convertView;
    }


}