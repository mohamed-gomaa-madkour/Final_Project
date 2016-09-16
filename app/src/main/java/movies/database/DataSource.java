package movies.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;


/**
 * Created by MOHAM on 12/09/2016.
 */

public class DataSource {

    Context context;
    String TAG = "DataSource";

    // Database fields
    private SQLiteDatabase database;
    private DBHelper dbHelper;


    private String[] allColumns = {MovieContract.COLUMN_ID,
            MovieContract.MOVIE_ID, MovieContract.MOVIE_JSONOBJECT};

    public DataSource(Context context) {
        dbHelper = new DBHelper(context);
        this.context = context;
    }



    public void deleteDB() {
        context.deleteDatabase(DBHelper.DATABASE_NAME);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long addMovie(ContentValues values) {
        long id = -1;
        try {

            id = database.insert(MovieContract.TABLE_Networks, null, values);
        } catch (Exception e) {
            Toast.makeText(context, "Something Error in Adding..", Toast.LENGTH_SHORT).show();

        }
        return id;
    }

    public int deleteMovie(int movie_id) {
        int id = -1;
        try {
            id = database.delete(MovieContract.TABLE_Networks, MovieContract.MOVIE_ID
                    + " = " + movie_id, null);
        } catch (Exception e) {
            Toast.makeText(context, "Something Error in deleting..", Toast.LENGTH_SHORT).show();
        }
        return id;
    }


    public  Cursor getMovies(){
        Cursor cursor = database.query(MovieContract.TABLE_Networks,
                allColumns, null, null, null, null, null);

        return cursor;




    }


    public Cursor searchID(int id){
        Cursor cursor = database.query(MovieContract.TABLE_Networks,
                allColumns, MovieContract.MOVIE_ID + "='" + id + "'",
                null, null, null, null);
        return cursor;



    }
}