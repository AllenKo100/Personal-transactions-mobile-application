package ba.unsa.etf.rma.rma20kovacevicallen14;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.github.mikephil.charting.renderer.scatter.SquareShapeRenderer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class TransactionListInteractor implements ITransactionListInteractor {

    private ArrayList<Transaction> trenutneTransakcije = new ArrayList<>();

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public void getDataFromDb(Context context) {
        TransactionDBOpenHelper db = new TransactionDBOpenHelper(context, TransactionDBOpenHelper.DATABASE_NAME, null, TransactionDBOpenHelper.DATABASE_VERSION);
        Cursor cur = db.getUnsyncedTransactions();
        ArrayList<Transaction> baza = new ArrayList<>();
        if(cur.moveToFirst()) {
            do {
                try {
                    Transaction tmp = new Transaction();
                    tmp.setId(cur.getInt(cur.getColumnIndex(TransactionDBOpenHelper.TRANSACTION_ID)));
                    tmp.setTitle(cur.getString(cur.getColumnIndex(TransactionDBOpenHelper.TRANSACTION_TITLE)));
                    tmp.setDate(simpleDateFormat.parse(cur.getString(cur.getColumnIndex(TransactionDBOpenHelper.TRANSACTION_DATE))));
                    tmp.setAmount(cur.getDouble(cur.getColumnIndex(TransactionDBOpenHelper.TRANSACTION_AMOUNT)));
                    tmp.setItemDescription(cur.getString(cur.getColumnIndex(TransactionDBOpenHelper.TRANSACTION_ITEM_DESCRIPTION)));
                    tmp.setTransactionInterval(cur.getInt(cur.getColumnIndex(TransactionDBOpenHelper.TRANSACTION_INTERVAL)));
                    tmp.setType(dajTipTransakcije(cur.getString(cur.getColumnIndex(TransactionDBOpenHelper.TRANSACTION_TYPE))));
                    tmp.setEndDate(simpleDateFormat.parse(cur.getString(cur.getColumnIndex(TransactionDBOpenHelper.TRANSACTION_END_DATE))));
                    tmp.setMode(cur.getString(cur.getColumnIndex(TransactionDBOpenHelper.TRANSACTION_MODE)));
                    baza.add(tmp);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } while (cur.moveToNext());
        }
        db.close();
        cur.close();
        //dodajemo u model
        if(TransactionsModel.transactions.isEmpty()) {
            TransactionsModel.transactions.addAll(baza);
        }
    }

    //Baza metode
    public void getFromDB(Context context) {
        ArrayList<Transaction> tmp = new ArrayList<Transaction>();
        Cursor cur = getTransactionCursor(context);
        while(cur.moveToNext()) {
            /*Integer id = null;
            if(!cur.getString(cur.getColumnIndex(TransactionDBOpenHelper.TRANSACTION_MODE)).equals("add")) {
                id = Integer.valueOf(cur.getString(cur.getColumnIndex(TransactionDBOpenHelper.TRANSACTION_ID)));
            }*/
            Integer internalId = Integer.valueOf(cur.getString(cur.getColumnIndex(TransactionDBOpenHelper.TRANSACTION_INTERNAL_ID)));
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
            //if(id != null) newTransaction.setId(id);
            newTransaction.setTitle(title);
            newTransaction.setAmount(amount);
            newTransaction.setDate(date);
            newTransaction.setItemDescription(description);
            newTransaction.setEndDate(endDate);
            newTransaction.setTransactionInterval(interval);
            newTransaction.setType(type);

            tmp.add(newTransaction);
        }
        if(TransactionsModel.transactions.isEmpty()) {
            TransactionsModel.transactions.addAll(tmp);
        }
    }
    public void deleteTransactionDatabase(Transaction transaction, Context context) {
        ContentResolver contentResolver = context.getApplicationContext().getContentResolver();
        Uri uri = Uri.parse("content://rma.provider.transactions/elements");
        ContentValues values = new ContentValues();
        values.put(TransactionDBOpenHelper.TRANSACTION_ID, transaction.getId());
        values.put(TransactionDBOpenHelper.TRANSACTION_TITLE, transaction.getTitle());
        values.put(TransactionDBOpenHelper.TRANSACTION_AMOUNT, transaction.getAmount());
        values.put(TransactionDBOpenHelper.TRANSACTION_DATE, simpleDateFormat.format(transaction.getDate()));
        values.put(TransactionDBOpenHelper.TRANSACTION_ITEM_DESCRIPTION, transaction.getItemDescription());
        values.put(TransactionDBOpenHelper.TRANSACTION_TYPE, typeToString(transaction));
        if(transaction.getType() == PaymentType.REGULARINCOME || transaction.getType() == PaymentType.REGULARPAYMENT) {
            values.put(TransactionDBOpenHelper.TRANSACTION_END_DATE, simpleDateFormat.format(transaction.getEndDate()));
            values.put(TransactionDBOpenHelper.TRANSACTION_INTERVAL, transaction.getTransactionInterval());
        } else {
            values.put(TransactionDBOpenHelper.TRANSACTION_END_DATE, "null");
            values.put(TransactionDBOpenHelper.TRANSACTION_INTERVAL, "null");
        }
        values.put(TransactionDBOpenHelper.TRANSACTION_MODE, "delete");
        contentResolver.insert(uri, values);
        Log.e("DeleteTransaction", "u bazu");
    }

    public void addTransactionDatabase(Transaction transaction, Context context) {
        ContentResolver contentResolver = context.getApplicationContext().getContentResolver();
        Uri uri = Uri.parse("content://rma.provider.transactions/elements");
        ContentValues values = new ContentValues();
        transaction.setId(noviId());
        values.put(TransactionDBOpenHelper.TRANSACTION_ID, transaction.getId());
        values.put(TransactionDBOpenHelper.TRANSACTION_TITLE, transaction.getTitle());
        values.put(TransactionDBOpenHelper.TRANSACTION_AMOUNT, transaction.getAmount().toString());
        values.put(TransactionDBOpenHelper.TRANSACTION_DATE, simpleDateFormat.format(transaction.getDate()));
        values.put(TransactionDBOpenHelper.TRANSACTION_ITEM_DESCRIPTION, transaction.getItemDescription());
        values.put(TransactionDBOpenHelper.TRANSACTION_TYPE, typeToString(transaction));
        if(transaction.getType() == PaymentType.REGULARINCOME || transaction.getType() == PaymentType.REGULARPAYMENT) {
            values.put(TransactionDBOpenHelper.TRANSACTION_END_DATE, simpleDateFormat.format(transaction.getEndDate()));
            values.put(TransactionDBOpenHelper.TRANSACTION_INTERVAL, transaction.getTransactionInterval());
        } else {
            values.put(TransactionDBOpenHelper.TRANSACTION_END_DATE, "null");
            values.put(TransactionDBOpenHelper.TRANSACTION_INTERVAL, "null");
        }
        values.put(TransactionDBOpenHelper.TRANSACTION_MODE, "add");
        contentResolver.insert(uri, values);
        Log.e("AddTransaction", "u bazu");
    }

    public void editTransactionDatabase(Transaction transaction, Context context) {
        ContentResolver contentResolver = context.getApplicationContext().getContentResolver();
        Uri uri = Uri.parse("content://rma.provider.transactions/elements");
        ContentValues values = new ContentValues();
        /*String where = "internalId=?";
        String whereArgs[] = new String[]{String.valueOf(transaction.getInternalId())};*/
        values.put(TransactionDBOpenHelper.TRANSACTION_ID, transaction.getId().toString());
        values.put(TransactionDBOpenHelper.TRANSACTION_TITLE, transaction.getTitle());
        values.put(TransactionDBOpenHelper.TRANSACTION_AMOUNT, transaction.getAmount().toString());
        values.put(TransactionDBOpenHelper.TRANSACTION_DATE, simpleDateFormat.format(transaction.getDate()));
        values.put(TransactionDBOpenHelper.TRANSACTION_ITEM_DESCRIPTION, transaction.getItemDescription());
        values.put(TransactionDBOpenHelper.TRANSACTION_TYPE, typeToString(transaction));
        if(transaction.getType() == PaymentType.REGULARINCOME || transaction.getType() == PaymentType.REGULARPAYMENT) {
            values.put(TransactionDBOpenHelper.TRANSACTION_END_DATE, simpleDateFormat.format(transaction.getEndDate()));
            values.put(TransactionDBOpenHelper.TRANSACTION_INTERVAL, transaction.getTransactionInterval().toString());
        } else {
            values.put(TransactionDBOpenHelper.TRANSACTION_END_DATE, "null");
            values.put(TransactionDBOpenHelper.TRANSACTION_INTERVAL, "null");
        }
        values.put(TransactionDBOpenHelper.TRANSACTION_MODE, "edit");
        //contentResolver.update(uri, values, null, null);
        contentResolver.insert(uri, values);
        Log.e("Edit", "u bazi");
    }

    public void undoDeleteTransaction(Context context, Integer id) {
        Log.e("id u interactoru", id.toString());
        ContentResolver cr = context.getApplicationContext().getContentResolver();
        Uri adresa = Uri.parse("content://rma.provider.transactions/elements");
        //cr.delete(adresa, null, new String[] {String.valueOf(id)});
        TransactionDBOpenHelper tHelper = new TransactionDBOpenHelper(context, TransactionDBOpenHelper.DATABASE_NAME, null,
                TransactionDBOpenHelper.DATABASE_VERSION);
        tHelper.undoDelete(id);
    }
    //kraj metoda baze

    @Override
    public ArrayList<Transaction> get() {
        return TransactionsModel.transactions;
    }

    public void removeTransaction(Transaction transaction) {
        get().remove(transaction);
    }

    public void addNewTransaction(Transaction transaction) {
        get().add(transaction);
    }

    public ArrayList<Transaction> get(int filterId, int sortId, Date date) {
        ArrayList<Transaction> nova = new ArrayList<>();
        nova.addAll(TransactionsModel.transactions);
        Log.e("velicina", Integer.toString(nova.size()));
        mjesecnoFiltriranje(nova, date);
        filtriranje(nova, filterId);
        sortiranje(nova, sortId);

        return nova;
    }

    public void mjesecnoFiltriranje(ArrayList<Transaction> tmp, Date date) {
        tmp.clear();

        SimpleDateFormat monthSimpleDateFormater = new SimpleDateFormat("MMMM");
        SimpleDateFormat yearSimpleDateFormater = new SimpleDateFormat("yyyy");

        SimpleDateFormat monthCheck = new SimpleDateFormat("mm");

        ArrayList<Transaction> filtrirana = new ArrayList<Transaction>();

        for(Transaction transakcija : TransactionsModel.transactions) {
            if(transakcija.getType() != PaymentType.REGULARINCOME && transakcija.getType() != PaymentType.REGULARPAYMENT && monthSimpleDateFormater.format(date).equals(monthSimpleDateFormater.format(transakcija.getDate())) &&
                    yearSimpleDateFormater.format(date).equals(yearSimpleDateFormater.format(transakcija.getDate()))) {
                filtrirana.add(transakcija);
            } else if(transakcija.getType() == PaymentType.REGULARPAYMENT || transakcija.getType() == PaymentType.REGULARINCOME) {
                //morat cemo casting uradit zbog .clone() jer pravimo duboku kopiju
                Date pocetak = (Date) transakcija.getDate().clone();
                Date kraj = (Date) transakcija.getEndDate().clone();
                Calendar pocetakCal = Calendar.getInstance();
                Calendar krajCal = Calendar.getInstance();
                Calendar trenutniCal = Calendar.getInstance();
                pocetakCal.setTime(pocetak);
                krajCal.setTime(kraj);
                trenutniCal.setTime(date);
                while(krajCal.after(pocetakCal)) {
                    if(pocetakCal.equals(trenutniCal) || (pocetakCal.get(Calendar.DAY_OF_MONTH) <= trenutniCal.getActualMaximum(Calendar.DATE) && pocetakCal.get(Calendar.MONTH) == trenutniCal.get(Calendar.MONTH))) {
                        filtrirana.add(transakcija);
                    }
                    pocetakCal.add(Calendar.DATE, transakcija.getTransactionInterval());
                }

            }
        }

        tmp.addAll(filtrirana);
    }

    public void filtriranje(ArrayList<Transaction> tmp, int idFilter) {
        ArrayList<Transaction> pomocna = new ArrayList<>();
        if(idFilter == 0) {
            pomocna.addAll(tmp);
        } else if(idFilter == 1) {
            for(Transaction trans : tmp) {
                if(trans.getType() == PaymentType.INDIVIDUALPAYMENT) {
                    pomocna.add(trans);
                }
            }
        } else if(idFilter == 2) {
            for(Transaction trans : tmp) {
                if(trans.getType() == PaymentType.REGULARPAYMENT) {
                    pomocna.add(trans);
                }
            }
        } else if(idFilter == 3) {
            for(Transaction trans : tmp) {
                if(trans.getType() == PaymentType.PURCHASE) {
                    pomocna.add(trans);
                }
            }
        } else if(idFilter == 4) {
            for(Transaction trans : tmp) {
                if(trans.getType() == PaymentType.INDIVIDUALINCOME) {
                    pomocna.add(trans);
                }
            }
        } else if(idFilter == 5) {
            for(Transaction trans : tmp) {
                if(trans.getType() == PaymentType.REGULARINCOME) {
                    pomocna.add(trans);
                }
            }
        }

        tmp.clear();
        tmp.addAll(pomocna);
    }

    public void sortiranje(ArrayList<Transaction> tmp, int idSort) {
        if(idSort == 0) {
            for (int i = 0; i < tmp.size() - 1; i++) {
                for (int j = 0; j < tmp.size() - i - 1; j++) {
                    if (tmp.get(j).getAmount() > tmp.get(j + 1).getAmount()) {
                        Collections.swap(tmp, j, j + 1);
                    }
                }
            }
        }
        else if(idSort == 1) {
            for (int i = 0; i < tmp.size() - 1; i++) {
                for (int j = 0; j < tmp.size() - i - 1; j++) {
                    if (tmp.get(j).getAmount() < tmp.get(j + 1).getAmount()) {
                        Collections.swap(tmp, j, j + 1);
                    }
                }
            }
        }
        else if(idSort == 2) {
            for(int i = 0; i < tmp.size()-1; i++) {
                for(int j = 0; j < tmp.size()-i-1; j++) {
                    if(tmp.get(j).getTitle().compareTo(tmp.get(j+1).getTitle()) < 0) {
                        Collections.swap(tmp, j, j+1);
                    }
                }
            }
        }
        else if(idSort == 3) {
            for(int i = 0; i < tmp.size()-1; i++) {
                for(int j = 0; j < tmp.size()-i-1; j++) {
                    if(tmp.get(j).getTitle().compareTo(tmp.get(j+1).getTitle()) > 0) {
                        Collections.swap(tmp, j, j+1);
                    }
                }
            }
        }
        else if(idSort == 4) {
            for(int i = 0; i < tmp.size()-1; i++) {
                for(int j = 0; j < tmp.size()-i-1; j++) {
                    if(tmp.get(j).getDate().after(tmp.get(j+1).getDate())) {
                        Collections.swap(tmp, j, j+1);
                    }
                }
            }
        }
        else if(idSort == 5) {
            for(int i = 0; i < tmp.size()-1; i++) {
                for(int j = 0; j < tmp.size()-i-1; j++) {
                    if(tmp.get(j).getDate().before(tmp.get(j+1).getDate())) {
                        Collections.swap(tmp, j, j+1);
                    }
                }
            }
        }
    }

    private String typeToString(Transaction transaction) {
        if(transaction.getType() == PaymentType.INDIVIDUALINCOME) return "INDIVIDUALINCOME";
        else if(transaction.getType() == PaymentType.REGULARINCOME) return "REGULARINCOME";
        else if(transaction.getType() == PaymentType.PURCHASE) return "PURCHASE";
        else if(transaction.getType() == PaymentType.INDIVIDUALPAYMENT) return "INDIVIDUALPAYMENT";
        else if(transaction.getType() == PaymentType.REGULARPAYMENT) return "REGULARPAYMENT";
        return null;
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

    private PaymentType dajTipTransakcije(String type) {
        if(type.equals("REGULARINCOME")) return PaymentType.REGULARINCOME;
        if(type.equals("REGULARPAYMENT")) return PaymentType.REGULARPAYMENT;
        if(type.equals("PURCHASE")) return PaymentType.PURCHASE;
        if(type.equals("INDIVIDUALINCOME")) return PaymentType.INDIVIDUALINCOME;
        if(type.equals("INDIVIDUALPAYMENT")) return PaymentType.INDIVIDUALPAYMENT;
        return null;
    }

    public Integer noviId() {
        Integer id = 0;
        for(Transaction trans : TransactionsModel.transactions) {
            if(trans.getId() != null) {
                if (trans.getId() > id) {
                    id = trans.getId();
                }
            }
        }
        id = id + 1;
        return id;
    }
}
