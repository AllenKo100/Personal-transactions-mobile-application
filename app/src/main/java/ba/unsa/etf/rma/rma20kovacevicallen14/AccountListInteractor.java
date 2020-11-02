package ba.unsa.etf.rma.rma20kovacevicallen14;

import java.util.ArrayList;

public class AccountListInteractor implements IAccountListInteractor {

    @Override
    public ArrayList<Account> get() {
        return AccountsModel.accounts;
    }

    public Double getTotatlLimit() {
        return get().get(0).getTotalLimit();
    }

    public Double getMonthLimit() {
        return get().get(0).getMonthLimit();
    }
}
