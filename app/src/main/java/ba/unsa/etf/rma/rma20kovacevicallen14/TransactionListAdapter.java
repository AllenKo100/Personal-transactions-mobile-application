package ba.unsa.etf.rma.rma20kovacevicallen14;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class TransactionListAdapter extends ArrayAdapter<Transaction> {

    private int resource;
    private TextView amountText;
    private TextView titleText;
    private ImageView icon;

    public TransactionListAdapter(@NonNull Context context, int _resource, ArrayList<Transaction> items) {
        super(context, _resource, items);
        resource = _resource;
    }

    public void setTransactions(ArrayList<Transaction> transactions) {
        this.clear();
        this.addAll(transactions);
    }

    public void deleteTransaction(Transaction transaction) {
        this.remove(transaction);
    }

    public Transaction getTransaction(int position) {
        return this.getItem(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LinearLayout newView;
        if(convertView == null) {
            newView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater li;
            li = (LayoutInflater)getContext().getSystemService(inflater);
            li.inflate(resource, newView, true);
        } else {
            newView = (LinearLayout)convertView;
        }

        Transaction transaction = getItem(position);

        amountText = newView.findViewById(R.id.amountText);
        titleText = newView.findViewById(R.id.titleText);
        icon = newView.findViewById(R.id.icon);

        amountText.setText(transaction.getAmount().toString());
        titleText.setText(transaction.getTitle());

        if(transaction.getType()==PaymentType.INDIVIDUALINCOME) {
            icon.setImageResource(R.drawable.individual_income);
        } else if(transaction.getType()==PaymentType.REGULARINCOME) {
            icon.setImageResource(R.drawable.regular_income);
        } else if(transaction.getType()==PaymentType.PURCHASE) {
            icon.setImageResource(R.drawable.purchase);
        } else if(transaction.getType()==PaymentType.REGULARPAYMENT) {
            icon.setImageResource(R.drawable.regular_payment);
        } else if(transaction.getType()==PaymentType.INDIVIDUALPAYMENT) {
            icon.setImageResource(R.drawable.individual_payment);
        }

        return newView;
    }
}
