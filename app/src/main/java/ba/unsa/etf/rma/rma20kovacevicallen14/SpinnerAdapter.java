package ba.unsa.etf.rma.rma20kovacevicallen14;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class SpinnerAdapter extends ArrayAdapter {

    private String[] transactionsTypes;
    private Integer[] transactionsImages;

    public SpinnerAdapter(Context context, int resource, String[] transactionsTypes, Integer[] transactionsImages) {
        super(context, resource, transactionsTypes);
        this.transactionsTypes = transactionsTypes;
        this.transactionsImages = transactionsImages;
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        String inflater = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li;
        li = (LayoutInflater)getContext().getSystemService(inflater);
        View view = li.inflate(R.layout.transaction_spinner_row, parent, false);

        TextView transactionName = (TextView)view.findViewById(R.id.text_view_transaction);
        ImageView transactionImage = (ImageView)view.findViewById(R.id.image_view_transaction);

        transactionName.setText(transactionsTypes[position]);
        transactionImage.setImageResource(transactionsImages[position]);

        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }
}
