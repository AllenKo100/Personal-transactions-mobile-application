package ba.unsa.etf.rma.rma20kovacevicallen14;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class WSAccountInteractor extends AsyncTask<String, Integer, Void> {

    private String root = "http://rma20-app-rmaws.apps.us-west-1.starter.openshift-online.com/";
    private String api_id = "5c27c0d3-5f37-4971-bb20-7d75d752e663";
    private AccountDetailPresenter caller;
    private Account account;

    public WSAccountInteractor(AccountDetailPresenter caller) {
        this.caller = caller;
        account = new Account();
    }

    public String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new
                InputStreamReader(is));
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
            String url1 = root + "account/" + api_id;
            URL url = new URL(url1);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
            urlConnection.setRequestProperty("Accept-Charset", "UTF-8");
            urlConnection.setDoInput(true);
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            String result = convertStreamToString(in);
            JSONObject jo = new JSONObject(result);
            Integer id = jo.getInt("id");
            Double budget = jo.getDouble("budget");
            Double totalLimit = jo.getDouble("totalLimit");
            Double monthLimit = jo.getDouble("monthLimit");
            account.setId(id);
            account.setBudget(budget);
            account.setTotalLimit(totalLimit);
            account.setMonthLimit(monthLimit);

        } catch (MalformedURLException e) {
            e.printStackTrace();
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
        caller.onDone(account);
    }

    public interface OnAccountRefreshDone {
        public void onDone(Account account);
    }
}
