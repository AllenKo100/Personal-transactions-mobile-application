package ba.unsa.etf.rma.rma20kovacevicallen14;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.ImageView;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

public class TransactionListCursorAdapter extends ResourceCursorAdapter {

    private TextView amountText;
    private TextView titleText;
    private ImageView icon;

    public TransactionListCursorAdapter(Context context, int layout, Cursor c, boolean autoRequery) {
        super(context, layout, c, autoRequery);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        amountText = view.findViewById(R.id.amountText);
        titleText = view.findViewById(R.id.titleText);
        icon = view.findViewById(R.id.icon);
        amountText.setText(cursor.getString(cursor.getColumnIndexOrThrow(TransactionDBOpenHelper.TRANSACTION_AMOUNT)));
        titleText.setText(cursor.getString(cursor.getColumnIndexOrThrow(TransactionDBOpenHelper.TRANSACTION_TITLE)));

        if(cursor.getString(cursor.getColumnIndexOrThrow(TransactionDBOpenHelper.TRANSACTION_TYPE)).equals("individualincome")) {
            icon.setImageResource(R.drawable.individual_income);
        } else if(cursor.getString(cursor.getColumnIndexOrThrow(TransactionDBOpenHelper.TRANSACTION_TYPE)).equals("regularincome")) {
            icon.setImageResource(R.drawable.regular_income);
        } else if(cursor.getString(cursor.getColumnIndexOrThrow(TransactionDBOpenHelper.TRANSACTION_TYPE)).equals("purchase")) {
            icon.setImageResource(R.drawable.purchase);
        } else if(cursor.getString(cursor.getColumnIndexOrThrow(TransactionDBOpenHelper.TRANSACTION_TYPE)).equals("regularpayment")) {
            icon.setImageResource(R.drawable.regular_payment);
        } else if(cursor.getString(cursor.getColumnIndexOrThrow(TransactionDBOpenHelper.TRANSACTION_TYPE)).equals("individualpayment")) {
            icon.setImageResource(R.drawable.individual_payment);
        }
    }
}
