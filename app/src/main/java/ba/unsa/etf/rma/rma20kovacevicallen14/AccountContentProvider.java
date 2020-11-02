package ba.unsa.etf.rma.rma20kovacevicallen14;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AccountContentProvider extends ContentProvider {

    private static final int ALLROWS = 1;
    private static final int ONEROW = 2;
    private static final UriMatcher uM;

    static {
        uM = new UriMatcher(UriMatcher.NO_MATCH);
        uM.addURI("rma.provider.accounts", "elements", ALLROWS);
        uM.addURI("rma.provider.accounts", "elements/#", ONEROW);
    }

    TransactionDBOpenHelper aHelper;

    @Override
    public boolean onCreate() {
        aHelper = new TransactionDBOpenHelper(getContext(), TransactionDBOpenHelper.DATABASE_NAME, null,
                TransactionDBOpenHelper.DATABASE_VERSION);
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase database;
        try {
            database = aHelper.getWritableDatabase();
        } catch (SQLException e) {
            database = aHelper.getReadableDatabase();
        }
        String groupby = null;
        String having = null;
        SQLiteQueryBuilder squery = new SQLiteQueryBuilder();

        switch (uM.match(uri)) {
            case ONEROW:
                String idRow = uri.getPathSegments().get(1);
                squery.appendWhere(TransactionDBOpenHelper.ACCOUNT_ID + "=" + idRow);
            default:
                break;
        }
        squery.setTables(TransactionDBOpenHelper.ACCOUNT_TABLE);
        Cursor cursor = squery.query(database, projection, selection, selectionArgs, groupby, having, sortOrder);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uM.match(uri)) {
            case ALLROWS:
                return "vnd.android.cursor.dir/vnd.rma.elemental";
            case ONEROW:
                return "vnd.android.cursor.dir/vnd.rma.elemental";
            default:
                throw new IllegalArgumentException("Unsuported uri: " + uri.toString());
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase database;
        try {
            database = aHelper.getWritableDatabase();
        } catch (SQLiteException e) {
            database = aHelper.getReadableDatabase();
        }
        long id = database.insert(TransactionDBOpenHelper.ACCOUNT_TABLE, null, values);
        return uri.buildUpon().appendPath(String.valueOf(id)).build();
    }

    //@RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        //Nije potrebno implementirat za sad
        /*int count = 0;
        SQLiteDatabase database;
        try {
            database = tHelper.getWritableDatabase();
        } catch (SQLiteException e) {
            database = tHelper.getReadableDatabase();
        }
        SQLiteQueryBuilder squery = new SQLiteQueryBuilder();
        switch (uM.match(uri)) {
            case ACCID:
                String idRow = uri.getPathSegments().get(1);
                String where = TransactionDBOpenHelper.ACCOUNT_ID + "=" + idRow;
                count = database.delete(TransactionDBOpenHelper.ACCOUNT_TABLE, where, null);
                break;
            default:
                break;
        }
        return count;*/
        return 0;
    }

    //@RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int uriType = uM.match(uri);
        SQLiteDatabase database;
        try {
            database = aHelper.getWritableDatabase();
        } catch (SQLiteException e) {
            database = aHelper.getReadableDatabase();
        }

        int rowsUpdated = 0;

        switch (uriType) {
            case ALLROWS:
                rowsUpdated = database.update(TransactionDBOpenHelper.ACCOUNT_TABLE, values, selection, selectionArgs);
                break;
            case ONEROW:
                String id = uri.getLastPathSegment();
                if(TextUtils.isEmpty(selection)) {
                    rowsUpdated = database.update(TransactionDBOpenHelper.ACCOUNT_TABLE, values, TransactionDBOpenHelper.ACCOUNT_ID + "=" + id, null);
                } else {
                    rowsUpdated = database.update(TransactionDBOpenHelper.ACCOUNT_TABLE, values, TransactionDBOpenHelper.ACCOUNT_ID + "=" + " and " + selection, selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unsuported uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return rowsUpdated;
    }
}
