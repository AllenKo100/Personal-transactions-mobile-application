package ba.unsa.etf.rma.rma20kovacevicallen14;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class TransactionsModel {
    /*public static ArrayList<Transaction> transactions = new ArrayList<Transaction>() {
        {
            String datum1 = "02/02/2020";
            String datum2 = "03/04/2020";
            String datum3 = "01/01/2020";
            String datum4 = "10/04/2020";
            String datum5 = "20/04/2020";
            String datum6 = "11/03/2020";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            try {
                Date d1 = simpleDateFormat.parse(datum1);
                Date d2 = simpleDateFormat.parse(datum2);
                Date d3 = simpleDateFormat.parse(datum3);
                Date d4 = simpleDateFormat.parse(datum4);
                Date d5 = simpleDateFormat.parse(datum5);
                Date d6 = simpleDateFormat.parse(datum6);
                add(new Transaction(d1,125.6,"Transakcija 1", PaymentType.INDIVIDUALINCOME,"Transakcija 1", null,null));
                add(new Transaction(d2,22.33,"Transakcija 2", PaymentType.INDIVIDUALPAYMENT,"Transakcija 2",null,null));
                add(new Transaction(d1,11.34,"Transakcija 3", PaymentType.PURCHASE,"Transakcija 3",null,null));
                add(new Transaction(d2,43.00,"Transakcija 4", PaymentType.REGULARINCOME,"Transakcija 4", 40,new Date(System.currentTimeMillis())));
                add(new Transaction(d1,110.1,"Transakcija 5", PaymentType.REGULARPAYMENT,"Transakcija 5", 50,new Date(System.currentTimeMillis())));
                add(new Transaction(d2,11.0,"Transakcija 6", PaymentType.INDIVIDUALINCOME,"Transakcija 6", null,null));
                add(new Transaction(d1,12.32,"Transakcija 7", PaymentType.INDIVIDUALPAYMENT,"Transakcija 7",null,null));
                add(new Transaction(d2,122.34,"Transakcija 8", PaymentType.PURCHASE,"Transakcija 8",null,null));
                add(new Transaction(d1,63.00,"Transakcija 9", PaymentType.REGULARINCOME,"Transakcija 9", 90,new Date(System.currentTimeMillis())));
                add(new Transaction(d2,10.1,"Transakcija 10", PaymentType.REGULARPAYMENT,"Transakcija 10", 100,new Date(System.currentTimeMillis())));
                add(new Transaction(d1,12.6,"Transakcija 11", PaymentType.INDIVIDUALINCOME,"Transakcija 11", null,null));
                add(new Transaction(d2,22.33,"Transakcija 12", PaymentType.INDIVIDUALPAYMENT,"Transakcija 12",null,null));
                add(new Transaction(d1,12.34,"Transakcija 13", PaymentType.PURCHASE,"Transakcija 13",null,null));
                add(new Transaction(d2,63.00,"Transakcija 14", PaymentType.REGULARINCOME,"Transakcija 14", 140,new Date(System.currentTimeMillis())));
                add(new Transaction(d1,10.1,"Transakcija 15", PaymentType.REGULARPAYMENT,"Transakcija 15", 150,new Date(System.currentTimeMillis())));
                add(new Transaction(d2,11.0,"Transakcija 16", PaymentType.INDIVIDUALINCOME,"Transakcija 16", null,null));
                add(new Transaction(d1,13.32,"Transakcija 17", PaymentType.INDIVIDUALPAYMENT,"Transakcija 17",null,null));
                add(new Transaction(d2,12.34,"Transakcija 18", PaymentType.PURCHASE,"Transakcija 18",null,null));
                add(new Transaction(d1,65.00,"Transakcija 19", PaymentType.REGULARINCOME,"Transakcija 19", 190,new Date(System.currentTimeMillis())));
                add(new Transaction(d2,10.1,"Transakcija 20", PaymentType.REGULARPAYMENT,"Transakcija 20", 200,new Date(System.currentTimeMillis())));
                add(new Transaction(d3,21.1,"Jedna nova", PaymentType.REGULARPAYMENT,"Jedna nova", 222,new Date(System.currentTimeMillis())));
                add(new Transaction(d3,21.1,"Još jedna nova", PaymentType.REGULARPAYMENT,"Jedna nova", 222,d2));
                add(new Transaction(d4,61.00,"Račun za struju", PaymentType.INDIVIDUALPAYMENT,"plaćanje računa", null,null));
                add(new Transaction(d5,156.1,"Playstation 4", PaymentType.INDIVIDUALPAYMENT,"kupljen ps4", null,null));
                add(new Transaction(d4,1.1,"Plata legla", PaymentType.INDIVIDUALINCOME,"Jedna nova", null,null));
                add(new Transaction(d5,1.1,"Dodatak", PaymentType.INDIVIDUALINCOME,"Jedna nova", null,null));
                add(new Transaction(d6,16.1,"Bakšiš", PaymentType.INDIVIDUALPAYMENT,"počastili me", null,null));
                add(new Transaction(d6,12.1,"Prodano", PaymentType.INDIVIDUALINCOME,"Prodao neke stvari", null,null));
                add(new Transaction(d6,13.4,"Dodatak", PaymentType.INDIVIDUALINCOME,"Jedna nova", null,null));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    };*/
    public static ArrayList<Transaction> transactions = new ArrayList<>();

    public static ArrayList<Transaction> getTransactions() {
        return transactions;
    }
}
