package es.iessaladillo.pedrojoya.pr251.data.local;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import androidx.annotation.NonNull;

import es.iessaladillo.pedrojoya.pr251.utils.DatabaseUtils;

public class DbHelper extends SQLiteOpenHelper {

    private static DbHelper instance;

    private final AssetManager assetManager;

    private DbHelper(@NonNull Context context) {
        super(context, DbContract.DB_NAME, null, DbContract.DB_VERSION);
        assetManager = context.getAssets();
    }

    public static DbHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (DbHelper.class) {
                if (instance == null) {
                    // Use application context to avoid memory leaks.
                    instance = new DbHelper(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    // Called by the framewrok on API >= 16 to configure database, after creating database file
    // and opening database, and before calling onCreate.
    @Override
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        // Enable database log.
        setWriteAheadLoggingEnabled(true);
        // Enable foreign keys in SQLite.
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        DatabaseUtils.executeSqlFromAssetsFile(db, DbContract.DB_VERSION, assetManager);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop tables (NOT IN PRODUCTION!!!).
        db.execSQL(DbContract.Student.DROP_TABLE_QUERY);
        DatabaseUtils.executeSqlFromAssetsFile(db, DbContract.DB_VERSION, assetManager);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop tables (NOT IN PRODUCTION!!!).
        db.execSQL(DbContract.Student.DROP_TABLE_QUERY);
        DatabaseUtils.executeSqlFromAssetsFile(db, DbContract.DB_VERSION, assetManager);
    }

}
