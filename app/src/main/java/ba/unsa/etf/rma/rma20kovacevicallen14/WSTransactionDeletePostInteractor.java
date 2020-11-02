package ba.unsa.etf.rma.rma20kovacevicallen14;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class WSTransactionDeletePostInteractor extends AsyncTask<String, Integer, Void> {

    private String root = "http://rma20-app-rmaws.apps.us-west-1.starter.openshift-online.com/";
    private String api_id = "5c27c0d3-5f37-4971-bb20-7d75d752e663";
    private TransactionListPresenter caller;
    private ConnectivityBroadcastReceiver caller2;
    private Transaction transaction;

    public WSTransactionDeletePostInteractor(TransactionListPresenter caller) {
        this.caller = caller;
        this.transaction = new Transaction();
    }

    public WSTransactionDeletePostInteractor(TransactionListPresenter caller, Transaction transaction) {
        this.caller = caller;
        this.transaction = transaction;
    }

    public WSTransactionDeletePostInteractor(ConnectivityBroadcastReceiver caller, Transaction transaction) {
        this.caller2 = caller;
        this.transaction = transaction;
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

    @Override
    protected Void doInBackground(String... strings) {
        try {
            String url1 = root + "account/" + api_id + "/transactions/" + transaction.getId();
            URL url = new URL(url1);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("DELETE");
            urlConnection.setDoOutput(true);
            urlConnection.connect();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            String result = convertStreamToString(in);
            JSONObject jo = new JSONObject(result);
            String rezultat = jo.getString("success");
            Log.e("rezultat:", rezultat);
            in.close();
            urlConnection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if(caller != null) caller.onDeleteDone();
        else if(caller2 != null) caller2.onDeleteDone();
    }

    public interface OnTransactionDelete {
        public void onDeleteDone();
    }
}
