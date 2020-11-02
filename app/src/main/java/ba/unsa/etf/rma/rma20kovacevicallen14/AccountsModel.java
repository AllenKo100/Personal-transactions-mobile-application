package ba.unsa.etf.rma.rma20kovacevicallen14;

import java.util.ArrayList;

public class AccountsModel {
    //public static Account account = new Account(100000.0, 20000.0, 2000.0);
    public static Account account = new Account(0.0, 0.0, 0.0);
    public static ArrayList<Account> accounts = new ArrayList<Account>() {
        {
            add(new Account(10000.0, 20000.0, 2000.0));
        }
    };
}
