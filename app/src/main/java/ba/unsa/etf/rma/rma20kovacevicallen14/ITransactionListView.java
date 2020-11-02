package ba.unsa.etf.rma.rma20kovacevicallen14;

import java.util.ArrayList;

public interface ITransactionListView {
    void setTransactions(ArrayList<Transaction> transactions);
    void notifyTransactionListDataSetChanged();
    void setDetails();
}
