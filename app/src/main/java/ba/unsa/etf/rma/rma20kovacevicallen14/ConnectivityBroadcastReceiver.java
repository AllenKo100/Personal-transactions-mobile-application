package ba.unsa.etf.rma.rma20kovacevicallen14;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConnectivityBroadcastReceiver extends BroadcastReceiver implements WSTransactionAddPostInteractor.OnTransactionAddDone, WSTransactionEditPostInteractor.OnTransactionAddDone, WSTransactionDeletePostInteractor.OnTransactionDelete, WSAccountPostInteractor.OnAccountEditDone {

    //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

    private OnEditModeText caller;

    public ConnectivityBroadcastReceiver() {

    }

    public ConnectivityBroadcastReceiver(OnEditModeText p) {
        this.caller = p;
    }

    public interface OnEditModeText {
        public void onEditModeText();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() == null) {
            Toast toast = Toast.makeText(context, "Disconnected", Toast.LENGTH_SHORT);
            toast.show();
        }
        else {
            Toast toast = Toast.makeText(context, "Connected", Toast.LENGTH_SHORT);
            toast.show();
            //Također je sada potrebno uraditi spremanje na server, jer je došla konekcija
            Cursor cur = getTransactionCursor(context);
            while(cur.moveToNext()) {
                Integer id = null;
                if(!cur.getString(cur.getColumnIndex(TransactionDBOpenHelper.TRANSACTION_MODE)).equals("add")) {
                    id = Integer.valueOf(cur.getString(cur.getColumnIndex(TransactionDBOpenHelper.TRANSACTION_ID)));
                }
                String title = cur.getString(cur.getColumnIndexOrThrow(TransactionDBOpenHelper.TRANSACTION_TITLE));
                Double amount = Double.valueOf(cur.getString(cur.getColumnIndex(TransactionDBOpenHelper.TRANSACTION_AMOUNT)));
                Date date = null;
                Date endDate = null;
                PaymentType type = dajTipTransakcije(cur.getString(cur.getColumnIndex(TransactionDBOpenHelper.TRANSACTION_TYPE)));
                try {
                    date = simpleDateFormat.parse(cur.getString(cur.getColumnIndex(TransactionDBOpenHelper.TRANSACTION_DATE)));
                    if(type == PaymentType.REGULARINCOME || type == PaymentType.REGULARPAYMENT) {
                        endDate = simpleDateFormat.parse(cur.getString(cur.getColumnIndex(TransactionDBOpenHelper.TRANSACTION_END_DATE)));
                    } else {
                        endDate = null;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String description = cur.getString(cur.getColumnIndex(TransactionDBOpenHelper.TRANSACTION_ITEM_DESCRIPTION));
                Integer interval = null;
                if(type == PaymentType.REGULARINCOME || type == PaymentType.REGULARPAYMENT) {
                    interval = Integer.valueOf(cur.getString(cur.getColumnIndex(TransactionDBOpenHelper.TRANSACTION_INTERVAL)));
                }
                Transaction newTransaction = new Transaction();
                if(id != null) newTransaction.setId(id);
                newTransaction.setTitle(title);
                newTransaction.setAmount(amount);
                newTransaction.setDate(date);
                newTransaction.setItemDescription(description);
                newTransaction.setEndDate(endDate);
                newTransaction.setTransactionInterval(interval);
                newTransaction.setType(type);

                //provjerimo sada šta treba sa transakcijom
                if(cur.getString(cur.getColumnIndex(TransactionDBOpenHelper.TRANSACTION_MODE)).equals("add")) {
                    WSTransactionAddPostInteractor instanca = new WSTransactionAddPostInteractor(this, newTransaction);
                    instanca.execute("");
                } else if(cur.getString(cur.getColumnIndex(TransactionDBOpenHelper.TRANSACTION_MODE)).equals("edit")) {
                    WSTransactionEditPostInteractor instanca = new WSTransactionEditPostInteractor(this, newTransaction);
                    instanca.execute(newTransaction.getId().toString());
                } else if(cur.getString(cur.getColumnIndex(TransactionDBOpenHelper.TRANSACTION_MODE)).equals("delete")) {
                    WSTransactionDeletePostInteractor instanca = new WSTransactionDeletePostInteractor(this, newTransaction);
                    instanca.execute("");
                }
            }
            if(cur.moveToFirst()) {
                TransactionDBOpenHelper tHelper = new TransactionDBOpenHelper(context,
                        TransactionDBOpenHelper.DATABASE_NAME,null,
                        TransactionDBOpenHelper.DATABASE_VERSION);
                tHelper.deleteAll();
            }

            //Slanje promjena accounta na servis
            Cursor cursorAccount = getAccountCursor(context);
            Account account = new Account();
            if(cursorAccount.moveToFirst()) {
                Double budget = cursorAccount.getDouble(cursorAccount.getColumnIndex(TransactionDBOpenHelper.ACCOUNT_BUDGET));
                Double totalLimit = cursorAccount.getDouble(cursorAccount.getColumnIndex(TransactionDBOpenHelper.ACCOUNT_TOTAL_LIMIT));
                Double monthLimit = cursorAccount.getDouble(cursorAccount.getColumnIndex(TransactionDBOpenHelper.ACCOUNT_MONTH_LIMIT));
                account.setBudget(budget);
                account.setTotalLimit(totalLimit);
                account.setMonthLimit(monthLimit);
                WSAccountPostInteractor instanca = new WSAccountPostInteractor(this, account);
                instanca.execute("");
            }
            if(caller != null) {
                caller.onEditModeText();
            }
        }
    }

    public Cursor getTransactionCursor(Context context) {
        ContentResolver cr = context.getApplicationContext().getContentResolver();
        String[] kolone = new String[]{
                TransactionDBOpenHelper.TRANSACTION_INTERNAL_ID, TransactionDBOpenHelper.TRANSACTION_ID,
                TransactionDBOpenHelper.TRANSACTION_TITLE, TransactionDBOpenHelper.TRANSACTION_AMOUNT,
                TransactionDBOpenHelper.TRANSACTION_DATE, TransactionDBOpenHelper.TRANSACTION_ITEM_DESCRIPTION,
                TransactionDBOpenHelper.TRANSACTION_INTERVAL, TransactionDBOpenHelper.TRANSACTION_END_DATE,
                TransactionDBOpenHelper.TRANSACTION_TYPE, TransactionDBOpenHelper.TRANSACTION_MODE
        };
        Uri adresa = Uri.parse("content://rma.provider.transactions/elements");
        String where = null;
        String whereArgs[] = null;
        String order = null;
        Cursor cur = cr.query(adresa, kolone, where, whereArgs, order);
        return cur;
    }

    public Cursor getAccountCursor(Context context) {
        ContentResolver cr = context.getApplicationContext().getContentResolver();
        String[] kolone = new String[]{
                TransactionDBOpenHelper.ACCOUNT_ID, TransactionDBOpenHelper.ACCOUNT_BUDGET,
                TransactionDBOpenHelper.ACCOUNT_TOTAL_LIMIT, TransactionDBOpenHelper.ACCOUNT_MONTH_LIMIT
        };
        Uri adresa = Uri.parse("content://rma.provider.accounts/elements");
        String where = null;
        String whereArgs[] = null;
        String order = null;
        Cursor cur = cr.query(adresa, kolone, where, whereArgs, order);
        return cur;
    }

    private PaymentType dajTipTransakcije(String type) {
        if(type.equals("REGULARINCOME")) return PaymentType.REGULARINCOME;
        if(type.equals("REGULARPAYMENT")) return PaymentType.REGULARPAYMENT;
        if(type.equals("PURCHASE")) return PaymentType.PURCHASE;
        if(type.equals("INDIVIDUALINCOME")) return PaymentType.INDIVIDUALINCOME;
        if(type.equals("INDIVIDUALPAYMENT")) return PaymentType.INDIVIDUALPAYMENT;
        return null;
    }

    @Override
    public void onAddDone(Transaction transaction) {

    }

    @Override
    public void onEditDone(Transaction transaction) {

    }

    @Override
    public void onDeleteDone() {

    }

    @Override
    public void onEditDone(Account account) {

    }
}
