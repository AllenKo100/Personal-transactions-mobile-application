package ba.unsa.etf.rma.rma20kovacevicallen14;

import java.util.ArrayList;

public interface IAccountListView {
    void setAccounts(ArrayList<Account> accounts);
    void notifyAccountListDataSetChanged();
}
