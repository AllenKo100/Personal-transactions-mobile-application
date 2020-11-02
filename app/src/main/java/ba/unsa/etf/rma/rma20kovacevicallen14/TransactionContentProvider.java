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
import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class TransactionContentProvider extends ContentProvider {

    private static final int ALLROWS = 1;
    private static final int ONEROW = 2;
    private static final UriMatcher uM;

    static {
        uM = new UriMatcher(UriMatcher.NO_MATCH);
        uM.addURI("rma.provider.transactions", "elements", ALLROWS);
        uM.addURI("rma.provider.transactions", "elements/#", ONEROW);
    }

    TransactionDBOpenHelper tHelper;

    @Override
    public boolean onCreate() {
        tHelper = new TransactionDBOpenHelper(getContext(), TransactionDBOpenHelper.DATABASE_NAME, null,
                TransactionDBOpenHelper.DATABASE_VERSION);
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase database;
        try {
            database = tHelper.getWritableDatabase();
        } catch (SQLException e) {
            database = tHelper.getReadableDatabase();
        }
        String groupby = null;
        String having = null;
        SQLiteQueryBuilder squery = new SQLiteQueryBuilder();

        switch (uM.match(uri)) {
            case ONEROW:
                String idRow = uri.getPathSegments().get(1);
                squery.appendWhere(TransactionDBOpenHelper.TRANSACTION_INTERNAL_ID+"="+idRow);
            default:break;
        }
        squery.setTables(TransactionDBOpenHelper.TRANSACTION_TABLE);
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
                return "vnd.android.cursor.item/vnd.rma.elemental";
            default:
                throw new IllegalArgumentException("Unsuported uri: " + uri.toString());
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase database;
        try {
            database = tHelper.getWritableDatabase();
        } catch (SQLiteException e) {
            database = tHelper.getReadableDatabase();
        }
        long id = database.insert(TransactionDBOpenHelper.TRANSACTION_TABLE, null, values);
        return uri.buildUpon().appendPath(String.valueOf(id)).build();
    }

    //@RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        /*int count = 0;
        SQLiteDatabase database;
        try {
            database = tHelper.getWritableDatabase();
        } catch (SQLiteException e) {
            database = tHelper.getReadableDatabase();
        }
        SQLiteQueryBuilder squery = new SQLiteQueryBuilder();
        switch (uM.match(uri)) {
            case ONEROW:
                String idRow = uri.getPathSegments().get(1);
                String where = TransactionDBOpenHelper.TRANSACTION_INTERNAL_ID + "=" + idRow;
                count = database.delete(TransactionDBOpenHelper.TRANSACTION_TABLE, where, selectionArgs);
                break;
            case ALLROWS:
                count = database.delete(TransactionDBOpenHelper.TRANSACTION_TABLE, selection, selectionArgs);
            default:break;
        }*/
        /*squery.setTables(TransactionDBOpenHelper.TRANSACTION_TABLE);
        database.delete(TransactionDBOpenHelper.TRANSACTION_TABLE, null, null);
        count = squery.delete(database, selection, selectionArgs);*/
        //return count;
        SQLiteDatabase database;
        try {
            database = tHelper.getWritableDatabase();
        } catch (SQLiteException e) {
            database = tHelper.getReadableDatabase();
        }
        database.delete(TransactionDBOpenHelper.TRANSACTION_TABLE, "id=" + selectionArgs[0], null);
        getContext().getContentResolver().notifyChange(uri, null);

        return 0;
    }

    //@RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        /*int count = 0;
        SQLiteDatabase database;
        try {
            database = tHelper.getWritableDatabase();
        } catch (SQLiteException e) {
            database = tHelper.getReadableDatabase();
        }
        switch (uM.match(uri)) {
            case ONEROW:
                String idRow = uri.getPathSegments().get(1);
                String where = TransactionDBOpenHelper.TRANSACTION_INTERNAL_ID + "=" + idRow;
                count = database.update(TransactionDBOpenHelper.TRANSACTION_TABLE, values, where, selectionArgs);
                break;
            default:break;
        }
        return count;*/
        int uriType = uM.match(uri);
        SQLiteDatabase db = tHelper.getWritableDatabase();
        int rowsUpdated = 0;

        switch (uriType) {
            case ALLROWS:
                rowsUpdated = db.update(tHelper.TRANSACTION_TABLE, values, selection, selectionArgs);
                break;
            case ONEROW:
                String id = uri.getLastPathSegment();
                if(TextUtils.isEmpty(selection)) {
                    rowsUpdated = db.update(tHelper.TRANSACTION_TABLE, values, tHelper.TRANSACTION_INTERNAL_ID+"="+id, null);
                } else {
                    rowsUpdated = db.update(tHelper.TRANSACTION_TABLE, values, tHelper.TRANSACTION_INTERNAL_ID+"="+id + " and " + selection, selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }
}
