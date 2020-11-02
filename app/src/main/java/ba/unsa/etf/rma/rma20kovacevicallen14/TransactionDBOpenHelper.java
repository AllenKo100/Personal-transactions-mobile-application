package ba.unsa.etf.rma.rma20kovacevicallen14;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TransactionDBOpenHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "RMADataBase.db";
    public static final int DATABASE_VERSION = 1;

    public TransactionDBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public TransactionDBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static final String TRANSACTION_TABLE = "transactions";
    public static final String TRANSACTION_ID = "id";
    public static final String TRANSACTION_INTERNAL_ID = "internalId";
    public static final String TRANSACTION_DATE = "date";
    public static final String TRANSACTION_AMOUNT = "amount";
    public static final String TRANSACTION_TITLE = "title";
    public static final String TRANSACTION_TYPE = "transactionType";
    public static final String TRANSACTION_ITEM_DESCRIPTION = "itemDescription";
    public static final String TRANSACTION_INTERVAL = "transactionInterval";
    public static final String TRANSACTION_END_DATE = "endDate";
    public static final String TRANSACTION_MODE = "mode";
    private static final String TRANSACTION_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + TRANSACTION_TABLE + " (" + TRANSACTION_INTERNAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + TRANSACTION_ID + " INTEGER UNIQUE, "
                    + TRANSACTION_DATE + " TEXT NOT NULL, "
                    + TRANSACTION_AMOUNT + " TEXT NOT NULL, "
                    + TRANSACTION_TITLE + " TEXT NOT NULL, "
                    + TRANSACTION_TYPE + " TEXT NOT NULL, "
                    + TRANSACTION_ITEM_DESCRIPTION + " TEXT, "
                    + TRANSACTION_INTERVAL + " INTEGER, "
                    + TRANSACTION_MODE + " TEXT, "
                    + TRANSACTION_END_DATE + " TEXT);";

    private static final String TRANSACTION_TABLE_DROP = "DROP TABLE IF EXISTS " + TRANSACTION_TABLE;

    public static final String ACCOUNT_TABLE = "accounts";
    public static final String ACCOUNT_ID = "id";
    public static final String ACCOUNT_BUDGET = "budget";
    public static final String ACCOUNT_TOTAL_LIMIT = "totalLimit";
    public static final String ACCOUNT_MONTH_LIMIT = "monthLimit";
    private static final String ACCOUNT_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + ACCOUNT_TABLE + " (" + ACCOUNT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + ACCOUNT_BUDGET + " REAL NOT NULL, "
                    + ACCOUNT_TOTAL_LIMIT + " REAL NOT NULL, "
                    + ACCOUNT_MONTH_LIMIT + " REAL NOT NULL);";

    private static final String ACCOUNT_TABLE_DROP = "DROP TABLE IF EXISTS " + ACCOUNT_TABLE;

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TRANSACTION_TABLE_CREATE);
        db.execSQL(ACCOUNT_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(ACCOUNT_TABLE_DROP);
        db.execSQL(TRANSACTION_TABLE_DROP);
        onCreate(db);
    }

    public Cursor getUnsyncedTransactions(){
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TRANSACTION_TABLE;
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    public void undoDelete(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+ TRANSACTION_TABLE + " WHERE " + TRANSACTION_ID + "=" + id);
        Log.e("DBOpen", "delete");
    }

    public void deleteAll()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TRANSACTION_TABLE);
        db.close();
    }
}
