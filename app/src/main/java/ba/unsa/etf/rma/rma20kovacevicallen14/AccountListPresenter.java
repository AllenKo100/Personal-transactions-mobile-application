package ba.unsa.etf.rma.rma20kovacevicallen14;

import android.content.Context;

public class AccountListPresenter implements IAccountListPresenter {

    private IAccountListView view;
    private IAccountListInteractor interactor;
    private Context context;

    public AccountListPresenter(IAccountListView view, Context context) {
        this.view = view;
        this.interactor = new AccountListInteractor();
        this.context = context;
    }

    @Override
    public void refreshAccounts() {
        //view.setAccounts(interactor.get());
        //view.notifyAccountListDataSetChanged();
    }
}
