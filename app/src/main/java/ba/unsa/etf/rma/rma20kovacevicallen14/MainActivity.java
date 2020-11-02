package ba.unsa.etf.rma.rma20kovacevicallen14;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements TransactionListFragment.OnItemClick, TransactionDetailFragment.OnOptionClick, TransactionDetailFragment.OnAddClick, TransactionListFragment.OnSwipeAction, BudgetInfo.OnSwipeAction, GraphsFragment.OnSwipeAction/*, ConnectivityBroadcastReceiver.OnSyncRefreshList*/ {

    private boolean landscapeOn;
    //novo
    private ConnectivityBroadcastReceiver receiver = new ConnectivityBroadcastReceiver();
    private IntentFilter filter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
    //novo
    Transaction currentTransaction;
    private boolean isConnected;


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isConnected = isNetworkAvailable();

        Log.d("aaa", "oncreate");

        FragmentManager fragmentManager = getSupportFragmentManager();
        FrameLayout details = findViewById(R.id.transaction_detail);
        if (details != null) {
            landscapeOn = true;
            TransactionDetailFragment detailFragment = (TransactionDetailFragment) fragmentManager.findFragmentById(R.id.transaction_detail);
            if (detailFragment==null) {
                detailFragment = new TransactionDetailFragment();
                fragmentManager.beginTransaction().
                        replace(R.id.transaction_detail,detailFragment)
                        .commit();
            }
        } else {
            landscapeOn = false;
        }


        Fragment list = fragmentManager.findFragmentById(R.id.transactions_list);
        if(list == null) {
            list = new TransactionListFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.transactions_list, list)
                    .commit();
        } else {
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(receiver, filter);
    }

    @Override
    public void onPause() {
        unregisterReceiver(receiver);
        super.onPause();
    }

    @Override
    public void onItemClicked(Transaction transaction) {
        Bundle arguments = new Bundle();
        currentTransaction = transaction;
        arguments.putParcelable("transaction", transaction);
        if(isConnected) arguments.putString("connection", "on");
        else arguments.putString("connection", "off");
        TransactionDetailFragment detailFragment = new TransactionDetailFragment();
        arguments.putString("mode", "edit");
        detailFragment.setArguments(arguments);
        if (landscapeOn){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.transaction_detail, detailFragment)
                    .commit();
        }
        else{
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.transactions_list, detailFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void onAddClicked() {
        TransactionDetailFragment detailFragment = new TransactionDetailFragment();
        Bundle arguments = new Bundle();
        detailFragment.setArguments(null);
        if (landscapeOn){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.transaction_detail, detailFragment)
                    .commit();
        }
        else{
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.transactions_list, detailFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void onEditClicked() {
        BudgetInfo budgetInfo = new BudgetInfo();
        getSupportFragmentManager().beginTransaction().replace(R.id.transactions_list, budgetInfo).addToBackStack(null).commit();
    }

    @Override
    public void onGraphsClicked() {
        GraphsFragment graphsFragment = new GraphsFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.transactions_list, graphsFragment).addToBackStack(null).commit();
    }

    @Override
    public void onSaveClicked(int position, Transaction transaction) {
        Bundle arguments = new Bundle();
        Integer pozicija = position;
        Log.d("pozicija", pozicija.toString());
        /*TransactionsModel.transactions.remove(position);
        TransactionsModel.transactions.add(transaction);*/
        TransactionListFragment listFragment = new TransactionListFragment();
        listFragment.setArguments(arguments);
        if (landscapeOn){
            /*Transaction t = TransactionsModel.transactions.get(position);
            t.setTitle(transaction.getTitle());
            t.setAmount(transaction.getAmount());
            t.setItemDescription(transaction.getItemDescription());
            t.setDate(transaction.getDate());
            t.setEndDate(transaction.getEndDate());
            t.setTransactionInterval(transaction.getTransactionInterval());
            t.setType(transaction.getType());*/
            //TransactionsModel.transactions.add(transaction);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.transactions_list, listFragment)
                    //.replace(R.id.transaction_detail, new TransactionDetailFragment())
                    .commit();
        }
        else{
            /*Transaction t = TransactionsModel.transactions.get(position);
            t.setTitle(transaction.getTitle());
            t.setAmount(transaction.getAmount());
            t.setItemDescription(transaction.getItemDescription());
            t.setDate(transaction.getDate());
            t.setEndDate(transaction.getEndDate());
            t.setTransactionInterval(transaction.getTransactionInterval());
            t.setType(transaction.getType());*/
            //TransactionsModel.transactions.remove(position);
            //TransactionsModel.transactions.add(transaction);
            /*getSupportFragmentManager().beginTransaction()
                    .replace(R.id.transaction_detail, listFragment)
                    .addToBackStack(null)
                    .commit();*/
            //getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public void onDeleteClicked(int position) {
        Bundle arguments = new Bundle();
        /*Integer pozicija = position;
        Log.d("pozicija", pozicija.toString());
        TransactionsModel.transactions.remove(position);*/
        TransactionDetailFragment detailFragment = new TransactionDetailFragment();
        detailFragment.setArguments(arguments);
        //arguments.putString("mode", "delete");
        TransactionListFragment listFragment = new TransactionListFragment();
        listFragment.setArguments(arguments);
        if (landscapeOn){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.transaction_detail, detailFragment)
                    .replace(R.id.transactions_list, listFragment)
                    .commit();
        }
        else{
            /*getSupportFragmentManager().beginTransaction()
                    .replace(R.id.transaction_detail, listFragment)
                    .addToBackStack(null)
                    .commit();*/
            getSupportFragmentManager().popBackStack();
            //getSupportFragmentManager().beginTransaction().replace(R.id.transactions_list, new TransactionListFragment());
            //getSupportFragmentManager().beginTransaction().replace(R.id.transactions_list, listFragment).commit();
        }
    }



    @Override
    public void onAddClicked(Transaction transaction) {
        Bundle arguments = new Bundle();
        //TransactionsModel.transactions.add(transaction);
        TransactionListFragment listFragment = new TransactionListFragment();
        listFragment.setArguments(arguments);
        if (landscapeOn){
            //TransactionsModel.transactions.add(transaction);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.transactions_list, listFragment)
                    .replace(R.id.transaction_detail, new TransactionDetailFragment())
                    .commit();
        }
        else{
            //TransactionsModel.transactions.add(transaction);
            /*getSupportFragmentManager().beginTransaction()
                    .replace(R.id.transaction_detail, listFragment)
                    .addToBackStack(null)
                    .commit();*/
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public void onSwipeLeft() {
        BudgetInfo budgetInfo = new BudgetInfo();
        getSupportFragmentManager().beginTransaction().replace(R.id.transactions_list, budgetInfo).addToBackStack(null).commit();
    }

    @Override
    public void onSwipeRight() {
        GraphsFragment graphsFragment = new GraphsFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.transactions_list, graphsFragment).addToBackStack(null).commit();
    }

    @Override
    public void onSwipeLeftBudget() {
        GraphsFragment graphsFragment = new GraphsFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.transactions_list, graphsFragment).addToBackStack(null).commit();
    }

    @Override
    public void onSwipeRightBudget() {
        TransactionListFragment listFragment = new TransactionListFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.transactions_list, listFragment).addToBackStack(null).commit();
    }

    @Override
    public void onSwipeLeftGraph() {
        TransactionListFragment listFragment = new TransactionListFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.transactions_list, listFragment).addToBackStack(null).commit();
    }

    @Override
    public void onSwipeRightGraph() {
        BudgetInfo budgetInfo = new BudgetInfo();
        getSupportFragmentManager().beginTransaction().replace(R.id.transactions_list, budgetInfo).addToBackStack(null).commit();
    }
}
