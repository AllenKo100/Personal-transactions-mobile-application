package ba.unsa.etf.rma.rma20kovacevicallen14;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;

public class WSAccountPostInteractor extends AsyncTask<String, Integer, Void> {

    private String root = "http://rma20-app-rmaws.apps.us-west-1.starter.openshift-online.com/";
    private String api_id = "5c27c0d3-5f37-4971-bb20-7d75d752e663";
    private AccountDetailPresenter caller;
    private Account account;
    private ConnectivityBroadcastReceiver caller2;

    public WSAccountPostInteractor(AccountDetailPresenter p) {
        this.caller = p;
        account = new Account();
    }

    public WSAccountPostInteractor(AccountDetailPresenter p, Account account) {
        this.caller = p;
        this.account = account;
    }

    public WSAccountPostInteractor(ConnectivityBroadcastReceiver p, Account account) {
        this.caller2 = p;
        this.account = account;
    }

    public Double getNewBudget() {
        /*AccountDetailInteractor interactor = new AccountDetailInteractor();
        return interactor.getCurrentStats();*/
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

    public String convertToJson(Account account) {
        StringBuilder sb = new StringBuilder();
        sb.append("{" + "\"budget\":" + getNewBudget());
        sb.append(",");
        sb.append("\"totalLimit\":" + account.getTotalLimit());
        sb.append(",");
        sb.append("\"monthLimit\":" + account.getMonthLimit());
        sb.append("}");
        return sb.toString();
    }

    public String convertBudgetRefreshToJson(Account account) {
        StringBuilder sb = new StringBuilder();
        sb.append("{" + "\"budget\":" + getNewBudget());
        sb.append("}");
        return sb.toString();
    }



    @Override
    protected Void doInBackground(String... strings) {
        if(strings[0].equals("refreshBudget")) {
            String url1 = root + "account/" + api_id;
            try {
                URL url = new URL(url1);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setDoOutput(true);
                String jsonInputString = convertBudgetRefreshToJson(account);
                try (OutputStream os = urlConnection.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }
                try (BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine = null;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    Log.d("RESPONSE", response.toString());
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            String url1 = root + "account/" + api_id;
            try {
                URL url = new URL(url1);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setDoOutput(true);
                String jsonInputString = convertToJson(account);
                try (OutputStream os = urlConnection.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }
                try (BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine = null;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    Log.d("RESPONSE", response.toString());
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if(caller != null) caller.onEditDone(account);
        else if(caller2 != null) caller2.onEditDone(account);
    }

    public interface OnAccountEditDone {
        public void onEditDone(Account account);
    }
}
