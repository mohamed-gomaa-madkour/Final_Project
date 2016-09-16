package movies.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by MOHAM on 12/09/2016.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Movies.db";
    public static final int DATABASE_VERSION = 1;
    Context context;
    String TAG = "Database Helper";


    private static final String DATABASE_CREATE = "create table "
            + MovieContract.TABLE_Networks + "(" +MovieContract. COLUMN_ID
            + " integer primary key autoincrement, " +MovieContract. MOVIE_ID
            + " integer unique, " +MovieContract. MOVIE_JSONOBJECT + " text not null" + ");";



    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        Log.d(TAG, "Creating Database");
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "Upgrading Database");
        db.execSQL("DROP TABLE IF EXISTS " +MovieContract.TABLE_Networks);
        onCreate(db);
    }
}