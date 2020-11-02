package ba.unsa.etf.rma.rma20kovacevicallen14;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;

public class AccountDetailPresenter implements WSAccountInteractor.OnAccountRefreshDone, WSAccountPostInteractor.OnAccountEditDone {

    private boolean isConnected() {
        return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }

    private AccountDetailInteractor interactor;
    private IAccountView view;
    private Context context;

    public AccountDetailPresenter() {
        this.interactor = new AccountDetailInteractor();
    }

    public void refresh() {
        view.setInfo(interactor.getAccount());
    }

    public AccountDetailPresenter(IAccountView view) {
        this.interactor = new AccountDetailInteractor();
        this.view = view;
    }

    public AccountDetailPresenter(IAccountView view, Context context) {
        this.interactor = new AccountDetailInteractor();
        this.view = view;
        this.context = context;
    }

    public AccountDetailPresenter(Context context) {
        this.interactor = new AccountDetailInteractor();
        //this.view = view;
        this.context = context;
    }

    public Double getTotalLimit() {
        return interactor.getTotalLimit();
    }

    public void setTotalLimit(Double x) {
        interactor.setTotalLimit(x);
    }

    public Double getMonthLimit() {
        return interactor.getMonthLimit();
    }

    public void setMonthLimit(Double x) {
        interactor.setMonthLimit(x);
    }

    public Double getBudget() {
        return interactor.getBudget();
    }

    public void setBudget(Double x) {
        interactor.setBudget(x);
    }

    public Double getCurrentStats() {
        return interactor.getCurrentStats();
    }

    public Double getWithoutCurrentStats(Double amount) {
        return interactor.getWithoutCurrentStats(amount);
    }

    public void refreshStats() {
        if(isConnected()) {
            WSAccountInteractor instanca = new WSAccountInteractor(this);
            instanca.execute("");
        } else {
            interactor.getFromDB(context);
            view.setInfo(interactor.getAccount());
        }
    }

    public void refreshEdit(Account account) {
        WSAccountPostInteractor instanca = new WSAccountPostInteractor(this, account);
        instanca.execute("");
    }

    public void refreshEditOffline(Account account) {
        if(!accInDb(context)) {
            interactor.add(context, account);
            AccountsModel.account = account;
        } else {
            interactor.update(context, account);
            AccountsModel.account = account;
        }
    }

    public void refreshBudget() {
        if(isConnected()) {
            Account acc = new Account(getCurrentStats(), getTotalLimit(), getMonthLimit());
            WSAccountPostInteractor instanca = new WSAccountPostInteractor(this, acc);
            instanca.execute("refreshBudget");
        }
    }

    @Override
    public void onDone(Account account) {
        AccountsModel.account.setBudget(account.getBudget());
        AccountsModel.account.setMonthLimit(account.getMonthLimit());
        AccountsModel.account.setTotalLimit(account.getTotalLimit());
        //view.setDetails(interactor.getBudget().toString(), interactor.getMonthLimit().toString());
        //view.setInfo();
        view.setInfo(account);
        //novo
        /*if(!accInDb(context)) {
            interactor.add(context, account);
            AccountsModel.account = account;
        } else {
            interactor.update(context, account);
            AccountsModel.account = account;
        }*/
    }

    @Override
    public void onEditDone(Account account) {
        AccountsModel.account.setBudget(/*account.*/getBudget());
        AccountsModel.account.setMonthLimit(/*account.*/getMonthLimit());
        AccountsModel.account.setTotalLimit(/*account.*/getTotalLimit());
        //view.setDetails(getBudget().toString(), getMonthLimit().toString());
        //view.setInfo();
        /*if(!accInDb(context)) {
            interactor.add(context, account);
            AccountsModel.account = account;
        } else {
            interactor.update(context, account);
            AccountsModel.account = account;
        }*/
    }

    public boolean accInDb(Context context) {
        TransactionDBOpenHelper tHelper = new TransactionDBOpenHelper(context,
                TransactionDBOpenHelper.DATABASE_NAME, null,
                TransactionDBOpenHelper.DATABASE_VERSION);
        SQLiteDatabase db = tHelper.getWritableDatabase();
        Cursor cur = null;
        String upit = "SELECT * FROM " + TransactionDBOpenHelper.ACCOUNT_TABLE;
        cur = db.rawQuery(upit, null);
        if(cur.getCount() > 0) {
            cur.close();
            return true;
        } else {
            cur.close();
            return false;
        }
    }
}
