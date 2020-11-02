package ba.unsa.etf.rma.rma20kovacevicallen14;

import android.os.AsyncTask;
import android.renderscript.ScriptGroup;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WSTransactionListInteractor extends AsyncTask<String, Integer, Void> {

    private String root = "http://rma20-app-rmaws.apps.us-west-1.starter.openshift-online.com/";
    private String api_id = "5c27c0d3-5f37-4971-bb20-7d75d752e663";
    ArrayList<Transaction> transactions;
    //private TransactionListInteractor caller;
    private TransactionListPresenter caller;

    public WSTransactionListInteractor(TransactionListPresenter p) {
        caller = p;
        transactions = new ArrayList<Transaction>();
    }

    public String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {

        } finally {
            try {
                is.close();
            } catch (IOException e) {

            }
        }
        return sb.toString();
    }

    private PaymentType dajTipTransakcije(int id) {
        if(id == 1) return PaymentType.REGULARPAYMENT;
        if(id == 2) return PaymentType.REGULARINCOME;
        if(id == 3) return PaymentType.PURCHASE;
        if(id == 4) return PaymentType.INDIVIDUALINCOME;
        if(id == 5) return PaymentType.INDIVIDUALPAYMENT;
        return null;
    }

    @Override
    protected Void doInBackground(String... strings) {
        int trenutnaStranica = 0;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

        try {
            for(;;) {
                String url1 = root + "account/" + api_id + "/transactions/?page=" + trenutnaStranica;
                Log.d("trenutna stranica", url1);
                URL url = new URL(url1);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
                urlConnection.setRequestProperty("Accept-Charset", "UTF-8");
                urlConnection.setDoInput(true);
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                String result = convertStreamToString(in);
                JSONObject jo = new JSONObject(result);
                JSONArray results = jo.getJSONArray("transactions");
                if(results.length() == 0) break;
                for(int i = 0; i < results.length(); i++) {
                    JSONObject transaction = results.getJSONObject(i);
                    Integer id = transaction.getInt("id");
                    String title = transaction.getString("title");
                    String stringDate = transaction.getString("date");
                    Date date = simpleDateFormat.parse(stringDate);
                    Double amount = transaction.getDouble("amount");
                    int tip = transaction.getInt("TransactionTypeId");
                    PaymentType type = dajTipTransakcije(tip);
                    String description = transaction.getString("itemDescription");
                    //koristimo sada optString i optInt jer oni vraćaju prazan string tj null ukoliko je prazan objekat
                    String stringEndDate = transaction.optString("endDate");
                    Integer transactionInterval = transaction.optInt("transactionInterval");
                    Date endDate = null;
                    if(stringEndDate != null && !stringEndDate.equals("null")) {
                        endDate = simpleDateFormat.parse(stringEndDate);
                    }
                    transactions.add(new Transaction(id, date, amount, title, type, description, transactionInterval, endDate));
                }
                trenutnaStranica++;
            }
        } catch(MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        caller.onDone(transactions);
    }

    public interface OnTrasactionsRefreshDone {
        public void onDone(ArrayList<Transaction> results);
    }
}
