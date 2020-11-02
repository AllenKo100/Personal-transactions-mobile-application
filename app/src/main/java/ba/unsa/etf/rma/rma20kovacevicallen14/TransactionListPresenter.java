package ba.unsa.etf.rma.rma20kovacevicallen14;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

public class TransactionListPresenter implements ITransactionListPresenter, WSTransactionListInteractor.OnTrasactionsRefreshDone, WSTransactionDeletePostInteractor.OnTransactionDelete, WSTransactionAddPostInteractor.OnTransactionAddDone, WSTransactionEditPostInteractor.OnTransactionAddDone {

    private boolean isConnected() {
        return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }

    private ITransactionListView view;
    private TransactionListInteractor interactor;
    private Context context;

    private int filterId;
    private int sortId;
    private Date date;

    public TransactionListPresenter(ITransactionListView view, Context context) {
        this.view = view;
        this.interactor = new TransactionListInteractor();
        this.context = context;
    }

    @Override
    public void refreshTransactions() {
        view.setTransactions(interactor.get());
        view.notifyTransactionListDataSetChanged();
    }

    public void refreshTransactions(int filterId, int sortId, Date date) {
        getData();
        this.filterId = filterId;
        this.sortId = sortId;
        this.date = date;
        view.setTransactions(interactor.get(filterId, sortId, date));
        view.notifyTransactionListDataSetChanged();
    }

    public void refreshTransactionsOffline(int filterId, int sortId, Date date) {
        interactor.getDataFromDb(context);
        this.filterId = filterId;
        this.sortId = sortId;
        this.date = date;
        view.setTransactions(interactor.get(filterId, sortId, date));
        view.notifyTransactionListDataSetChanged();
    }

    public void removeTransaction(Transaction transaction) {
        interactor.removeTransaction(transaction);
    }

    public void addTransaction(Transaction transaction) {
        interactor.addNewTransaction(transaction);
    }

    public void newTransactionRefresh(Transaction transaction, int filterId, int sortId, Date date) {
        interactor.addNewTransaction(transaction);
        view.setTransactions(interactor.get(filterId, sortId, date));
        view.notifyTransactionListDataSetChanged();
    }

    public ArrayList<Transaction> getTransactions() {
        return interactor.get();
    }

    public void getData() {
        WSTransactionListInteractor instanca = new WSTransactionListInteractor(this);
        instanca.execute("");
    }

    public void deleteTransaction(Transaction transaction) {
        if(ConnectionChecker.connectionStatus(context) == true) {
            WSTransactionDeletePostInteractor instanca = new WSTransactionDeletePostInteractor(this, transaction);
            instanca.execute("");
        } else {
            transaction.setMode("delete");
            interactor.deleteTransactionDatabase(transaction, context);
        }
    }

    public void addNewTransaction(Transaction transaction) {
        if(ConnectionChecker.connectionStatus(context) == true) {
            WSTransactionAddPostInteractor instanca = new WSTransactionAddPostInteractor(this, transaction);
            instanca.execute("");
        } else {
            interactor.addTransactionDatabase(transaction, context);
        }
    }

    public void editTransaction(Transaction transaction) {
        if(ConnectionChecker.connectionStatus(context) == true) {
            WSTransactionEditPostInteractor instanca = new WSTransactionEditPostInteractor(this, transaction);
            instanca.execute(transaction.getId().toString());
        } else {
            interactor.editTransactionDatabase(transaction, context);
        }
    }

    public void undoDeleteTransaction(Integer id) {
        Log.e("id u prezenteru", id.toString());
        interactor.undoDeleteTransaction(context, id);
    }

    @Override
    public void onDone(ArrayList<Transaction> results) {
        TransactionsModel.transactions.clear();
        TransactionsModel.transactions.addAll(results);
        view.setTransactions(interactor.get(filterId, sortId, date));
        view.notifyTransactionListDataSetChanged();
        view.setDetails();
    }

    @Override
    public void onDeleteDone() {

    }


    @Override
    public void onAddDone(Transaction transaction) {

    }

    @Override
    public void onEditDone(Transaction transaction) {

    }
}
