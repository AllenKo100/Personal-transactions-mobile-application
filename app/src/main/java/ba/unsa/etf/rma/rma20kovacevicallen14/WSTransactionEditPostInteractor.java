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
import java.text.SimpleDateFormat;

public class WSTransactionEditPostInteractor extends AsyncTask<String, Integer, Void> {

    private String root = "http://rma20-app-rmaws.apps.us-west-1.starter.openshift-online.com/";
    private String api_id = "5c27c0d3-5f37-4971-bb20-7d75d752e663";
    private TransactionListPresenter caller;
    private ConnectivityBroadcastReceiver caller2;
    private Transaction transaction;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

    public WSTransactionEditPostInteractor(TransactionListPresenter caller) {
        this.caller = caller;
        this.transaction = new Transaction();
    }

    public WSTransactionEditPostInteractor(TransactionListPresenter caller, Transaction transaction) {
        this.caller = caller;
        this.transaction = transaction;
    }

    public WSTransactionEditPostInteractor(ConnectivityBroadcastReceiver caller, Transaction transaction) {
        this.caller2 = caller;
        this.transaction = transaction;
    }

    private int dajTipTransakcije(PaymentType type) {
        if(type == PaymentType.REGULARPAYMENT) return 1;
        if(type == PaymentType.REGULARINCOME) return 2;
        if(type == PaymentType.PURCHASE) return 3;
        if(type == PaymentType.INDIVIDUALINCOME) return 4;
        if(type == PaymentType.INDIVIDUALPAYMENT) return 5;
        return 0;
    }

    public String convertToJson(Transaction transaction) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"date\": " + "\"" + simpleDateFormat.format(transaction.getDate()) + "\",");
        sb.append("\"amount\": " + transaction.getAmount() + ",");
        sb.append("\"TransactionTypeId\": " + dajTipTransakcije(transaction.getType()) + ",");
        sb.append("\"title\": " + "\"" + transaction.getTitle() + "\"");
        if(transaction.getItemDescription() != null && !transaction.getItemDescription().equals("") && !transaction.getItemDescription().equals("null")) {
            sb.append(",");
            sb.append("\"itemDescription\": " + "\"" + transaction.getItemDescription() + "\"");
        }
        if(transaction.getType().equals(PaymentType.REGULARINCOME) || transaction.getType().equals(PaymentType.REGULARPAYMENT)) {
            sb.append(",");
            sb.append("\"transactionInterval\": " + transaction.getTransactionInterval() + ",");
            sb.append("\"endDate\": " + "\"" +simpleDateFormat.format(transaction.getEndDate()) + "\"");
        }
        sb.append("}");

        return sb.toString();
    }

    @Override
    protected Void doInBackground(String... strings) {
        try {
            String url1 = root + "account/" + api_id + "/transactions/" + strings[0];
            URL url = new URL(url1);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            String jsonInputString = convertToJson(transaction);
            try(OutputStream os = urlConnection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            try(BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while((responseLine = br.readLine()) != null) {
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
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if(caller != null) caller.onEditDone(transaction);
        else if(caller2 != null) caller2.onEditDone(transaction);
    }

    public interface OnTransactionAddDone {
        public void onEditDone(Transaction transaction);
    }
}
