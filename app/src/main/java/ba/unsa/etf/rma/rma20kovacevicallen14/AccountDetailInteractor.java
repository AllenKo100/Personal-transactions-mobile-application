package ba.unsa.etf.rma.rma20kovacevicallen14;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.Calendar;
import java.util.Date;

public class AccountDetailInteractor {

    public Account getAccount() {
        Account tmp = new Account();
        tmp.setBudget(getCurrentStats());
        tmp.setTotalLimit(AccountsModel.account.getTotalLimit());
        tmp.setMonthLimit(AccountsModel.account.getMonthLimit());
        return tmp;
    }

    public Double getTotalLimit() {
        return AccountsModel.account.getTotalLimit();
    }

    public Double getMonthLimit() {
        return AccountsModel.account.getMonthLimit();
    }

    public Double getBudget() {
        return AccountsModel.account.getBudget();
    }

    public void setTotalLimit(Double x) {
        AccountsModel.account.setTotalLimit(x);
    }

    public void setMonthLimit(Double x) {
        AccountsModel.account.setMonthLimit(x);
    }

    public void setBudget(Double x) {
        AccountsModel.account.setBudget(x);
    }

    public Double getCurrentStats() {
        Double tmp = 0.0;
        for(Transaction trans : TransactionsModel.transactions) {
            if(trans.getType() == PaymentType.INDIVIDUALINCOME) {
                tmp = tmp + trans.getAmount();
            } else if(trans.getType() == PaymentType.PURCHASE || trans.getType() == PaymentType.INDIVIDUALPAYMENT) {
                tmp = tmp - trans.getAmount();
            } else if(trans.getType() == PaymentType.REGULARINCOME) {
                Date pocetak = (Date) trans.getDate().clone();
                Date kraj = (Date) trans.getEndDate().clone();
                Calendar pocetakCal = Calendar.getInstance();
                Calendar krajCal = Calendar.getInstance();
                pocetakCal.setTime(pocetak);
                krajCal.setTime(kraj);
                while(krajCal.after(pocetakCal)) {
                    tmp = tmp + trans.getAmount();
                    pocetakCal.add(Calendar.DATE, trans.getTransactionInterval());
                }
            } else if(trans.getType() == PaymentType.REGULARPAYMENT) {
                Date pocetak = (Date) trans.getDate().clone();
                Date kraj = (Date) trans.getEndDate().clone();
                Calendar pocetakCal = Calendar.getInstance();
                Calendar krajCal = Calendar.getInstance();
                pocetakCal.setTime(pocetak);
                krajCal.setTime(kraj);
                while(krajCal.after(pocetakCal)) {
                    tmp = tmp - trans.getAmount();
                    pocetakCal.add(Calendar.DATE, trans.getTransactionInterval());
                }
            }
        }
        return tmp;
    }

    public Double getWithoutCurrentStats(Double amount) {
        return getCurrentStats() - amount;
    }

    public void getFromDB(Context context) {
        Cursor cur = getAccountCursor(context);
        if(cur.moveToFirst()) {
            Integer id = cur.getInt(cur.getColumnIndex(TransactionDBOpenHelper.ACCOUNT_ID));
            Double budget = Double.valueOf(cur.getString(cur.getColumnIndex(TransactionDBOpenHelper.ACCOUNT_BUDGET)));
            Double totalLimit = Double.valueOf(cur.getString(cur.getColumnIndex(TransactionDBOpenHelper.ACCOUNT_TOTAL_LIMIT)));
            Double monthLimit = Double.valueOf(cur.getString(cur.getColumnIndex(TransactionDBOpenHelper.ACCOUNT_MONTH_LIMIT)));
            AccountsModel.account.setBudget(getCurrentStats());
            AccountsModel.account.setTotalLimit(totalLimit);
            AccountsModel.account.setMonthLimit(monthLimit);
        }
        cur.close();
    }

    public void refreshAccountDB(Account account, Context context) {
        ContentResolver contentResolver = context.getApplicationContext().getContentResolver();
        Uri uri = Uri.parse("content://rma.provider.accounts/elements");
        ContentValues values = new ContentValues();
        values.put(TransactionDBOpenHelper.ACCOUNT_ID, 1);
        values.put(TransactionDBOpenHelper.ACCOUNT_BUDGET, account.getBudget());
        values.put(TransactionDBOpenHelper.ACCOUNT_TOTAL_LIMIT, account.getTotalLimit());
        values.put(TransactionDBOpenHelper.ACCOUNT_MONTH_LIMIT, account.getMonthLimit());
        contentResolver.update(uri, values, null, null);
    }

    public void putAccountDB(Context context) {
        ContentResolver contentResolver = context.getApplicationContext().getContentResolver();
        Uri uri = Uri.parse("content://rma.provider.accounts/elements");
        ContentValues values = new ContentValues();
        values.put(TransactionDBOpenHelper.ACCOUNT_BUDGET, AccountsModel.account.getBudget());
        values.put(TransactionDBOpenHelper.ACCOUNT_TOTAL_LIMIT, AccountsModel.account.getTotalLimit());
        values.put(TransactionDBOpenHelper.ACCOUNT_MONTH_LIMIT, AccountsModel.account.getMonthLimit());
        //ID 1 jer je samo jedan account na servisu
        values.put(TransactionDBOpenHelper.ACCOUNT_ID, 1);
        contentResolver.insert(uri, values);
    }

    public Cursor getAccountCursor(Context context) {
        ContentResolver cr = context.getApplicationContext().getContentResolver();
        String[] kolone = new String[]{
                TransactionDBOpenHelper.ACCOUNT_ID, TransactionDBOpenHelper.ACCOUNT_BUDGET, TransactionDBOpenHelper.ACCOUNT_TOTAL_LIMIT, TransactionDBOpenHelper.ACCOUNT_MONTH_LIMIT
        };
        Uri adresa = Uri.parse("content://rma.provider.accounts/elements");
        String where = null;
        String whereArgs[] = null;
        String order = null;
        Cursor cur = cr.query(adresa, kolone, where, whereArgs, order);
        return cur;
    }

    public void add(Context context, Account account) {
        ContentResolver contentResolver = context.getApplicationContext().getContentResolver();
        Uri uri = Uri.parse("content://rma.provider.accounts/elements");
        ContentValues values = new ContentValues();
        //Svakako samo jedan account imamo, pa stavljamo 1 kao ID
        values.put(TransactionDBOpenHelper.ACCOUNT_ID, 1);
        values.put(TransactionDBOpenHelper.ACCOUNT_BUDGET, account.getBudget());
        values.put(TransactionDBOpenHelper.ACCOUNT_TOTAL_LIMIT, account.getTotalLimit());
        values.put(TransactionDBOpenHelper.ACCOUNT_MONTH_LIMIT, account.getMonthLimit());
        contentResolver.insert(uri, values);
    }

    public void update(Context context, Account account) {
        ContentResolver contentResolver = context.getApplicationContext().getContentResolver();
        Uri uri = ContentUris.withAppendedId(Uri.parse("content://rma.provider.accounts/elements"), 1);
        ContentValues values = new ContentValues();
        //Svakako samo jedan account imamo, pa stavljamo 1 kao ID
        values.put(TransactionDBOpenHelper.ACCOUNT_ID, 1);
        values.put(TransactionDBOpenHelper.ACCOUNT_BUDGET, account.getBudget());
        values.put(TransactionDBOpenHelper.ACCOUNT_TOTAL_LIMIT, account.getTotalLimit());
        values.put(TransactionDBOpenHelper.ACCOUNT_MONTH_LIMIT, account.getMonthLimit());
        contentResolver.update(uri, values, null, null);
    }
}
