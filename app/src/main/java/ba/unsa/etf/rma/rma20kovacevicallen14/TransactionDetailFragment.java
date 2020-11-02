package ba.unsa.etf.rma.rma20kovacevicallen14;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static androidx.core.content.ContextCompat.getSystemService;


/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionDetailFragment extends Fragment implements ITransactionListView, ConnectivityBroadcastReceiver.OnEditModeText {

    private EditText titleEditText;
    private EditText amountEditText;
    private EditText dateEditText;
    private EditText descEditText;
    private EditText endDateEditText;
    private EditText intervalEditText;
    private EditText typeEditText;
    //novo
    private ConnectivityBroadcastReceiver connectivityBroadcastReceiver = new ConnectivityBroadcastReceiver(this);

    private TextView rezimTextView;

    private Button saveButton;
    private Button deleteButton;

    private boolean modeUndo = false;

    private TransactionListAdapter transactionListAdapter;

    private TransactionListPresenter transactionListPresenter;

    public TransactionListPresenter getTransactionListPresenterPresenter() {
        if(transactionListPresenter == null) {
            transactionListPresenter = new TransactionListPresenter(this, getActivity());
            //transactionListPresenter = new TransactionListPresenter();
        }
        return transactionListPresenter;
    }

    private AccountDetailPresenter accountDetailPresenter;

    public AccountDetailPresenter getPresenter() {
        if(accountDetailPresenter == null) {
            accountDetailPresenter = new AccountDetailPresenter(getActivity());
        }
        return accountDetailPresenter;
    }

    String numberRegex = "^[0-9]*\\.?[0-9]+$";
    String dateRegex = "^\\d{4}-\\d{2}-\\d{2}$";

    private Transaction transaction;
    Transaction editTransaction;
    int position;

    int defaultBoja;

    Date d1 = new Date();
    Date d2 = new Date();

    boolean regularEnable = false;

    public TransactionDetailFragment() {
        // Required empty public constructor
    }

    private OnAddClick onAddClicked;

    @Override
    public void onEditModeText() {
        rezimTextView.setText("");
    }

    public interface OnAddClick {
        public void onAddClicked(Transaction transaction);
    }

    private OnOptionClick onOptionClicked;
    public interface OnOptionClick {
        public void onSaveClicked(int position, Transaction transaction);
        public void onDeleteClicked(int position);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_transaction_detail, container, false);

        if(getArguments() != null && getArguments().containsKey("transaction")) {
            transactionListAdapter = new TransactionListAdapter(getActivity(), R.layout.list_element, new ArrayList<Transaction>());

            transaction = getArguments().getParcelable("transaction");

            titleEditText = fragmentView.findViewById(R.id.titleEditText);
            amountEditText = fragmentView.findViewById(R.id.amountEditText);
            dateEditText = fragmentView.findViewById(R.id.dateEditText);
            descEditText = fragmentView.findViewById(R.id.descEditText);
            endDateEditText = fragmentView.findViewById(R.id.endDateEditText);
            intervalEditText = fragmentView.findViewById(R.id.intervalEditText);
            typeEditText = fragmentView.findViewById(R.id.typeEditText);
            //novo
            rezimTextView = fragmentView.findViewById(R.id.rezimTextView);

            if(!ConnectionChecker.connectionStatus(getActivity()) && getArguments().getString("mode").equals("edit")) {
                rezimTextView.setText("Offline izmjena");
            } else if(!ConnectionChecker.connectionStatus(getActivity()) && transaction.getMode().equals("delete")){
                rezimTextView.setText("Offline brisanje");
            }

            defaultBoja = titleEditText.getCurrentTextColor();

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

            if(transaction.getType() == PaymentType.INDIVIDUALINCOME) {
                typeEditText.setText("INDIVIDUALINCOME");
                regularEnable = false;
            }
            else if(transaction.getType() == PaymentType.INDIVIDUALPAYMENT) {
                typeEditText.setText("INDIVIDUALPAYMENT");
                regularEnable = false;
            }
            else if(transaction.getType() == PaymentType.PURCHASE) {
                typeEditText.setText("PURCHASE");
                regularEnable = false;
            }
            else if(transaction.getType() == PaymentType.REGULARPAYMENT) {
                typeEditText.setText("REGULARPAYMENT");
                regularEnable = true;
            }
            else if(transaction.getType() == PaymentType.REGULARINCOME) {
                typeEditText.setText("REGULARINCOME");
                regularEnable = true;
            }

            if(regularEnable) {
                titleEditText.setText(transaction.getTitle());
                amountEditText.setText(transaction.getAmount().toString());
                dateEditText.setText(simpleDateFormat.format(transaction.getDate()));
                descEditText.setText(transaction.getItemDescription());
                endDateEditText.setText(simpleDateFormat.format(transaction.getEndDate()));
                intervalEditText.setText(transaction.getTransactionInterval().toString());
            } else {

                endDateEditText.setEnabled(false);
                intervalEditText.setEnabled(false);

                titleEditText.setText(transaction.getTitle());
                amountEditText.setText(transaction.getAmount().toString());
                dateEditText.setText(simpleDateFormat.format(transaction.getDate()));
                descEditText.setText(transaction.getItemDescription());
                //endDateEditText.setText("/");
                //intervalEditText.setText("0");
            }


            try {
                d1 = simpleDateFormat.parse(dateEditText.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                d2 = simpleDateFormat.parse(endDateEditText.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            titleEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    titleValidate();
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    titleValidate();
                }

                @Override
                public void afterTextChanged(Editable s) {
                    titleValidate();
                }
            });

            amountEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    amountValidate();
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    amountValidate();
                }

                @Override
                public void afterTextChanged(Editable s) {
                    amountValidate();
                }
            });

            intervalEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    intervalValidate();
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    intervalValidate();
                }

                @Override
                public void afterTextChanged(Editable s) {
                    intervalValidate();
                }
            });

            dateEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    if(regularEnable) {
                        dateValidate(d1, d2);
                        endDateValidate(d1, d2);
                    } else {
                        onlyDateValidate();
                    }
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(regularEnable) {
                        dateValidate(d1, d2);
                        endDateValidate(d1, d2);
                    } else {
                        onlyDateValidate();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(regularEnable) {
                        dateValidate(d1, d2);
                        endDateValidate(d1, d2);
                    } else {
                        onlyDateValidate();
                    }
                }
            });

            endDateEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    try {
                        d1 = simpleDateFormat.parse(dateEditText.getText().toString());
                        d2 = simpleDateFormat.parse(endDateEditText.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    endDateValidate(d1, d2);
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    try {
                        d1 = simpleDateFormat.parse(dateEditText.getText().toString());
                        d2 = simpleDateFormat.parse(endDateEditText.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    endDateValidate(d1, d2);
                }

                @Override
                public void afterTextChanged(Editable s) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    Date d1 = new Date();
                    Date d2 = new Date();
                    try {
                        d1 = simpleDateFormat.parse(dateEditText.getText().toString());
                        d2 = simpleDateFormat.parse(endDateEditText.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    endDateValidate(d1, d2);
                }
            });

            typeEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    typeValidate();
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    typeValidate();
                }

                @Override
                public void afterTextChanged(Editable s) {
                    typeValidate();
                }
            });

            saveButton = fragmentView.findViewById(R.id.saveButton);
            deleteButton = fragmentView.findViewById(R.id.deleteButton);
            onOptionClicked = (OnOptionClick) getActivity();

            //novo
            if(transaction.getMode() != null && transaction.getMode().toLowerCase().equals("delete")) {
                deleteButton.setText("Undo");
                rezimTextView.setText("Offline brisanje");
                titleEditText.setEnabled(false);
                amountEditText.setEnabled(false);
                dateEditText.setEnabled(false);
                descEditText.setEnabled(false);
                endDateEditText.setEnabled(false);
                intervalEditText.setEnabled(false);
                typeEditText.setEnabled(false);
                modeUndo = true;
            }

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer pozicija = getTransactionListPresenterPresenter().getTransactions().indexOf(transaction);
                    Log.d("pozicija", pozicija.toString());

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    try {
                        d1 = simpleDateFormat.parse(dateEditText.getText().toString());
                        d2 = simpleDateFormat.parse(endDateEditText.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if(validiraj()) {

                        String noviAmountString = amountEditText.getText().toString();
                        Double noviAmount = Double.parseDouble(noviAmountString);
                        boolean prekoracenje = false;
                        String provjera = typeEditText.getText().toString().toUpperCase();
                        if(provjera.equals("REGULARINCOME") || provjera.equals("INDIVIDUALINCOME")) {
                            if(getPresenter().getCurrentStats() - noviAmount > getPresenter().getMonthLimit()) prekoracenje = true;
                        } else if(provjera.equals("PURCHASE") || provjera.equals("INDIVIDUALPAYMENT") || provjera.equals("REGULARPAYMENT")) {
                            if(getPresenter().getCurrentStats() + noviAmount > getPresenter().getMonthLimit()) prekoracenje = true;
                        }

                        if (prekoracenje) {
                            AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setTitle("Izmjenom ove transakcije prelazite mjesečni ili totalni limit!")
                                    .setMessage("Želite li ipak izmijeniti transakciju?")
                                    .setPositiveButton("Da", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            if(regularEnable) {
                                                Integer pozicija = getTransactionListPresenterPresenter().getTransactions().indexOf(transaction);

                                                Transaction newTransaction = new Transaction();
                                                String title = titleEditText.getText().toString();
                                                String amount = amountEditText.getText().toString();
                                                String date = dateEditText.getText().toString();
                                                String type = typeEditText.getText().toString().toUpperCase();
                                                String desc = descEditText.getText().toString();
                                                String endDate = endDateEditText.getText().toString();
                                                String interval = intervalEditText.getText().toString();
                                                titleEditText.getBackground().setColorFilter(defaultBoja, PorterDuff.Mode.SRC_ATOP);
                                                amountEditText.getBackground().setColorFilter(defaultBoja, PorterDuff.Mode.SRC_ATOP);
                                                dateEditText.getBackground().setColorFilter(defaultBoja, PorterDuff.Mode.SRC_ATOP);
                                                typeEditText.getBackground().setColorFilter(defaultBoja, PorterDuff.Mode.SRC_ATOP);
                                                descEditText.getBackground().setColorFilter(defaultBoja, PorterDuff.Mode.SRC_ATOP);
                                                endDateEditText.getBackground().setColorFilter(defaultBoja, PorterDuff.Mode.SRC_ATOP);
                                                intervalEditText.getBackground().setColorFilter(defaultBoja, PorterDuff.Mode.SRC_ATOP);

                                                newTransaction.setAmount(Double.parseDouble(amount));
                                                newTransaction.setTitle(title);
                                                newTransaction.setDate(d1);

                                                if (type.toUpperCase().equals("INDIVIDUALINCOME")) {
                                                    newTransaction.setType(PaymentType.INDIVIDUALINCOME);
                                                    newTransaction.setEndDate(null);
                                                    newTransaction.setTransactionInterval(null);
                                                }
                                                else if (type.toUpperCase().equals("INDIVIDUALPAYMENT")) {
                                                    newTransaction.setType(PaymentType.INDIVIDUALPAYMENT);
                                                    newTransaction.setEndDate(null);
                                                    newTransaction.setTransactionInterval(null);
                                                }
                                                else if (type.toUpperCase().equals("PURCHASE")) {
                                                    newTransaction.setType(PaymentType.PURCHASE);
                                                    newTransaction.setEndDate(null);
                                                    newTransaction.setTransactionInterval(null);
                                                }
                                                else if (type.toUpperCase().equals("REGULARINCOME")) {
                                                    newTransaction.setType(PaymentType.REGULARINCOME);
                                                    newTransaction.setEndDate(d2);
                                                    newTransaction.setTransactionInterval(Integer.parseInt(interval));
                                                }
                                                else if (type.toUpperCase().equals("REGULARPAYMENT")) {
                                                    newTransaction.setType(PaymentType.REGULARPAYMENT);
                                                    newTransaction.setEndDate(d2);
                                                    newTransaction.setTransactionInterval(Integer.parseInt(interval));
                                                }

                                                newTransaction.setItemDescription(desc);

                                                //edit offline dodane
                                                if(transaction.getMode() != null && transaction.getMode().equals("addOffline") && ConnectionChecker.connectionStatus(getActivity()) == false) {
                                                    getTransactionListPresenterPresenter().undoDeleteTransaction(transaction.getId());
                                                    transaction.setMode("");
                                                    TransactionsModel.transactions.remove(transaction);
                                                    newTransaction.setMode("addOffline");
                                                    TransactionsModel.transactions.add(newTransaction);
                                                    getTransactionListPresenterPresenter().addNewTransaction(newTransaction);

                                                    onOptionClicked.onSaveClicked(pozicija, newTransaction);
                                                }
                                                //edit offline dodane

                                                else {
                                                    //novi dio sad
                                                    newTransaction.setId(transaction.getId());
                                                    TransactionsModel.transactions.remove(transaction);
                                                    TransactionsModel.transactions.add(newTransaction);
                                                    getTransactionListPresenterPresenter().editTransaction(newTransaction);
                                                    //novi dio sad
                                                    getPresenter().refreshBudget();

                                                    onOptionClicked.onSaveClicked(pozicija, newTransaction);
                                                }

                                                Toast.makeText(getActivity(), "Izmjene uspješno spašene!", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Integer pozicija = getTransactionListPresenterPresenter().getTransactions().indexOf(transaction);

                                                Transaction newTransaction = new Transaction();
                                                String title = titleEditText.getText().toString();
                                                String amount = amountEditText.getText().toString();
                                                String date = dateEditText.getText().toString();
                                                String type = typeEditText.getText().toString().toUpperCase();
                                                String desc = descEditText.getText().toString();
                                                titleEditText.getBackground().setColorFilter(defaultBoja, PorterDuff.Mode.SRC_ATOP);
                                                amountEditText.getBackground().setColorFilter(defaultBoja, PorterDuff.Mode.SRC_ATOP);
                                                dateEditText.getBackground().setColorFilter(defaultBoja, PorterDuff.Mode.SRC_ATOP);
                                                typeEditText.getBackground().setColorFilter(defaultBoja, PorterDuff.Mode.SRC_ATOP);
                                                descEditText.getBackground().setColorFilter(defaultBoja, PorterDuff.Mode.SRC_ATOP);

                                                newTransaction.setAmount(Double.parseDouble(amount));
                                                newTransaction.setTitle(title);
                                                newTransaction.setDate(d1);

                                                if (type.toUpperCase().equals("INDIVIDUALINCOME"))
                                                    newTransaction.setType(PaymentType.INDIVIDUALINCOME);
                                                else if (type.toUpperCase().equals("INDIVIDUALPAYMENT"))
                                                    newTransaction.setType(PaymentType.INDIVIDUALPAYMENT);
                                                else if (type.toUpperCase().equals("PURCHASE"))
                                                    newTransaction.setType(PaymentType.PURCHASE);
                                                else if (type.toUpperCase().equals("REGULARINCOME"))
                                                    newTransaction.setType(PaymentType.REGULARINCOME);
                                                else if (type.toUpperCase().equals("REGULARPAYMENT"))
                                                    newTransaction.setType(PaymentType.REGULARPAYMENT);

                                                newTransaction.setItemDescription(desc);
                                                newTransaction.setEndDate(null);
                                                newTransaction.setTransactionInterval(null);

                                                //edit offline dodane
                                                if(transaction.getMode() != null && transaction.getMode().equals("addOffline") && ConnectionChecker.connectionStatus(getActivity()) == false) {
                                                    getTransactionListPresenterPresenter().undoDeleteTransaction(transaction.getId());
                                                    transaction.setMode("");
                                                    TransactionsModel.transactions.remove(transaction);
                                                    newTransaction.setMode("addOffline");
                                                    TransactionsModel.transactions.add(newTransaction);
                                                    getTransactionListPresenterPresenter().addNewTransaction(newTransaction);

                                                    onOptionClicked.onSaveClicked(pozicija, newTransaction);
                                                }
                                                //edit offline dodane

                                                else {
                                                    //novi dio sad
                                                    newTransaction.setId(transaction.getId());
                                                    TransactionsModel.transactions.remove(transaction);
                                                    TransactionsModel.transactions.add(newTransaction);
                                                    getTransactionListPresenterPresenter().editTransaction(newTransaction);
                                                    //novi dio sad
                                                    getPresenter().refreshBudget();

                                                    onOptionClicked.onSaveClicked(pozicija, newTransaction);
                                                }

                                                Toast.makeText(getActivity(), "Izmjene uspješno spašene!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    })
                                    .setNegativeButton("Ne", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            if(regularEnable) {
                                                titleEditText.getBackground().setColorFilter(defaultBoja, PorterDuff.Mode.SRC_ATOP);
                                                amountEditText.getBackground().setColorFilter(defaultBoja, PorterDuff.Mode.SRC_ATOP);
                                                dateEditText.getBackground().setColorFilter(defaultBoja, PorterDuff.Mode.SRC_ATOP);
                                                typeEditText.getBackground().setColorFilter(defaultBoja, PorterDuff.Mode.SRC_ATOP);
                                                descEditText.getBackground().setColorFilter(defaultBoja, PorterDuff.Mode.SRC_ATOP);
                                                endDateEditText.getBackground().setColorFilter(defaultBoja, PorterDuff.Mode.SRC_ATOP);
                                                intervalEditText.getBackground().setColorFilter(defaultBoja, PorterDuff.Mode.SRC_ATOP);
                                                Toast.makeText(getActivity(), "Prekid izmjene transakcije!", Toast.LENGTH_LONG).show();
                                            } else {
                                                titleEditText.getBackground().setColorFilter(defaultBoja, PorterDuff.Mode.SRC_ATOP);
                                                amountEditText.getBackground().setColorFilter(defaultBoja, PorterDuff.Mode.SRC_ATOP);
                                                dateEditText.getBackground().setColorFilter(defaultBoja, PorterDuff.Mode.SRC_ATOP);
                                                typeEditText.getBackground().setColorFilter(defaultBoja, PorterDuff.Mode.SRC_ATOP);
                                                descEditText.getBackground().setColorFilter(defaultBoja, PorterDuff.Mode.SRC_ATOP);
                                                //endDateEditText.getBackground().setColorFilter(defaultBoja, PorterDuff.Mode.SRC_ATOP);
                                                //intervalEditText.getBackground().setColorFilter(defaultBoja, PorterDuff.Mode.SRC_ATOP);
                                                Toast.makeText(getActivity(), "Prekid izmjene transakcije!", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    })
                                    .show();
                        } else {
                            if(regularEnable) {

                                Transaction newTransaction = new Transaction();
                                String title = titleEditText.getText().toString();
                                String amount = amountEditText.getText().toString();
                                String date = dateEditText.getText().toString();
                                String type = typeEditText.getText().toString().toUpperCase();
                                String desc = descEditText.getText().toString();
                                String endDate = endDateEditText.getText().toString();
                                String interval = intervalEditText.getText().toString();
                                titleEditText.getBackground().setColorFilter(defaultBoja, PorterDuff.Mode.SRC_ATOP);
                                amountEditText.getBackground().setColorFilter(defaultBoja, PorterDuff.Mode.SRC_ATOP);
                                dateEditText.getBackground().setColorFilter(defaultBoja, PorterDuff.Mode.SRC_ATOP);
                                typeEditText.getBackground().setColorFilter(defaultBoja, PorterDuff.Mode.SRC_ATOP);
                                descEditText.getBackground().setColorFilter(defaultBoja, PorterDuff.Mode.SRC_ATOP);
                                endDateEditText.getBackground().setColorFilter(defaultBoja, PorterDuff.Mode.SRC_ATOP);
                                intervalEditText.getBackground().setColorFilter(defaultBoja, PorterDuff.Mode.SRC_ATOP);

                                newTransaction.setAmount(Double.parseDouble(amount));
                                newTransaction.setTitle(title);
                                newTransaction.setDate(d1);

                                if (type.toUpperCase().equals("INDIVIDUALINCOME")) {
                                    newTransaction.setType(PaymentType.INDIVIDUALINCOME);
                                    newTransaction.setEndDate(null);
                                    newTransaction.setTransactionInterval(null);
                                }
                                else if (type.toUpperCase().equals("INDIVIDUALPAYMENT")) {
                                    newTransaction.setType(PaymentType.INDIVIDUALPAYMENT);
                                    newTransaction.setEndDate(null);
                                    newTransaction.setTransactionInterval(null);
                                }
                                else if (type.toUpperCase().equals("PURCHASE")) {
                                    newTransaction.setType(PaymentType.PURCHASE);
                                    newTransaction.setEndDate(null);
                                    newTransaction.setTransactionInterval(null);
                                }
                                else if (type.toUpperCase().equals("REGULARINCOME")) {
                                    newTransaction.setType(PaymentType.REGULARINCOME);
                                    newTransaction.setEndDate(d2);
                                    newTransaction.setTransactionInterval(Integer.parseInt(interval));
                                }
                                else if (type.toUpperCase().equals("REGULARPAYMENT")) {
                                    newTransaction.setType(PaymentType.REGULARPAYMENT);
                                    newTransaction.setEndDate(d2);
                                    newTransaction.setTransactionInterval(Integer.parseInt(interval));
                                }

                                newTransaction.setItemDescription(desc);

                                //edit offline dodane
                                if(transaction.getMode().equals("addOffline") && ConnectionChecker.connectionStatus(getActivity()) == false) {
                                    getTransactionListPresenterPresenter().undoDeleteTransaction(transaction.getId());
                                    transaction.setMode("");
                                    TransactionsModel.transactions.remove(transaction);
                                    newTransaction.setMode("addOffline");
                                    TransactionsModel.transactions.add(newTransaction);
                                    getTransactionListPresenterPresenter().addNewTransaction(newTransaction);

                                    onOptionClicked.onSaveClicked(pozicija, newTransaction);
                                }
                                //edit offline dodane

                                else {
                                    //novi dio sad
                                    newTransaction.setId(transaction.getId());
                                    TransactionsModel.transactions.remove(transaction);
                                    TransactionsModel.transactions.add(newTransaction);
                                    getTransactionListPresenterPresenter().editTransaction(newTransaction);
                                    //novi dio sad
                                    getPresenter().refreshBudget();

                                    onOptionClicked.onSaveClicked(pozicija, newTransaction);
                                }

                                Toast.makeText(getActivity(), "Izmjene uspješno spašene!", Toast.LENGTH_SHORT).show();
                            } else {
                                Transaction newTransaction = new Transaction();
                                String title = titleEditText.getText().toString();
                                String amount = amountEditText.getText().toString();
                                String date = dateEditText.getText().toString();
                                String type = typeEditText.getText().toString().toUpperCase();
                                String desc = descEditText.getText().toString();
                                titleEditText.getBackground().setColorFilter(defaultBoja, PorterDuff.Mode.SRC_ATOP);
                                amountEditText.getBackground().setColorFilter(defaultBoja, PorterDuff.Mode.SRC_ATOP);
                                dateEditText.getBackground().setColorFilter(defaultBoja, PorterDuff.Mode.SRC_ATOP);
                                typeEditText.getBackground().setColorFilter(defaultBoja, PorterDuff.Mode.SRC_ATOP);
                                descEditText.getBackground().setColorFilter(defaultBoja, PorterDuff.Mode.SRC_ATOP);

                                newTransaction.setAmount(Double.parseDouble(amount));
                                newTransaction.setTitle(title);
                                newTransaction.setDate(d1);

                                if (type.toUpperCase().equals("INDIVIDUALINCOME"))
                                    newTransaction.setType(PaymentType.INDIVIDUALINCOME);
                                else if (type.toUpperCase().equals("INDIVIDUALPAYMENT"))
                                    newTransaction.setType(PaymentType.INDIVIDUALPAYMENT);
                                else if (type.toUpperCase().equals("PURCHASE"))
                                    newTransaction.setType(PaymentType.PURCHASE);
                                else if (type.toUpperCase().equals("REGULARINCOME"))
                                    newTransaction.setType(PaymentType.REGULARINCOME);
                                else if (type.toUpperCase().equals("REGULARPAYMENT"))
                                    newTransaction.setType(PaymentType.REGULARPAYMENT);

                                newTransaction.setItemDescription(desc);
                                newTransaction.setEndDate(null);
                                newTransaction.setTransactionInterval(null);

                                //edit offline dodane
                                if(transaction.getMode() != null && transaction.getMode().equals("addOffline") && ConnectionChecker.connectionStatus(getActivity()) == false) {
                                    getTransactionListPresenterPresenter().undoDeleteTransaction(transaction.getId());
                                    transaction.setMode("");
                                    TransactionsModel.transactions.remove(transaction);
                                    newTransaction.setMode("addOffline");
                                    TransactionsModel.transactions.add(newTransaction);
                                    getTransactionListPresenterPresenter().addNewTransaction(newTransaction);

                                    onOptionClicked.onSaveClicked(pozicija, newTransaction);
                                }
                                //edit offline dodane

                                else {
                                    //novi dio sad
                                    newTransaction.setId(transaction.getId());
                                    TransactionsModel.transactions.remove(transaction);
                                    TransactionsModel.transactions.add(newTransaction);
                                    getTransactionListPresenterPresenter().editTransaction(newTransaction);
                                    //novi dio sad
                                    getPresenter().refreshBudget();

                                    onOptionClicked.onSaveClicked(pozicija, newTransaction);
                                }


                                Toast.makeText(getActivity(), "Izmjene uspješno spašene!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Toast.makeText(getActivity(), "Nisu ispravna sva unesena polja!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer pozicija = getTransactionListPresenterPresenter().getTransactions().indexOf(transaction);

                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Da li ste sigurni da želite obrisati odabranu transakciju?")
                            .setMessage("Ukoliko izbrišete transakcije, ne možete je vratiti nazad!")
                            .setPositiveButton("Da", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Integer pozicija = getTransactionListPresenterPresenter().getTransactions().indexOf(transaction);
                                    Log.e("pozicija", Integer.toString(pozicija));
                                    //onOptionClicked.onDeleteClicked(pozicija);

                                    if(ConnectionChecker.connectionStatus(getActivity())) {
                                        TransactionsModel.transactions.remove(transaction);
                                    }

                                    //novo
                                    if(modeUndo == true) {
                                        Log.e("id u fragmentu", transaction.getId().toString());
                                        getTransactionListPresenterPresenter().undoDeleteTransaction(transaction.getId());
                                        transaction.setMode("");
                                        onOptionClicked.onDeleteClicked(1);
                                    }


                                    else {
                                        Log.e("Poziva", "delete");
                                        getTransactionListPresenterPresenter().deleteTransaction(transaction);

                                        getPresenter().refreshBudget();
                                        onOptionClicked.onDeleteClicked(1);
                                    }

                                }
                            })
                            .setNegativeButton("Ne", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Toast.makeText(getActivity(),"Prekid brisanja transakcije!",Toast.LENGTH_LONG).show();
                                }
                            })
                            .show();
                }
            });
        }  else if(getArguments() == null) {

            rezimTextView = fragmentView.findViewById(R.id.rezimTextView);

            if(!ConnectionChecker.connectionStatus(getActivity())) {
                rezimTextView.setText("Offline dodavanje");
            }

            onAddClicked = (OnAddClick) getActivity();
            onOptionClicked = (OnOptionClick) getActivity();

            deleteButton = fragmentView.findViewById(R.id.deleteButton);

            deleteButton.setEnabled(false);

            titleEditText = fragmentView.findViewById(R.id.titleEditText);
            amountEditText = fragmentView.findViewById(R.id.amountEditText);
            dateEditText = fragmentView.findViewById(R.id.dateEditText);
            descEditText = fragmentView.findViewById(R.id.descEditText);
            endDateEditText = fragmentView.findViewById(R.id.endDateEditText);
            intervalEditText = fragmentView.findViewById(R.id.intervalEditText);
            typeEditText = fragmentView.findViewById(R.id.typeEditText);

            endDateEditText.setEnabled(false);
            intervalEditText.setEnabled(false);

            defaultBoja = titleEditText.getCurrentTextColor();

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

            try {
                d1 = simpleDateFormat.parse(dateEditText.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                d2 = simpleDateFormat.parse(endDateEditText.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            titleEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    titleValidate();
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    titleValidate();
                }

                @Override
                public void afterTextChanged(Editable s) {
                    titleValidate();
                }
            });

            amountEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    amountValidate();
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    amountValidate();
                }

                @Override
                public void afterTextChanged(Editable s) {
                    amountValidate();
                }
            });

            intervalEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    intervalValidate();
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    intervalValidate();
                }

                @Override
                public void afterTextChanged(Editable s) {
                    intervalValidate();
                }
            });

            dateEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    if(regularEnable) {
                        dateValidate(d1, d2);
                        endDateValidate(d1, d2);
                    } else {
                        onlyDateValidate();
                    }
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(regularEnable) {
                        dateValidate(d1, d2);
                        endDateValidate(d1, d2);
                    } else {
                        onlyDateValidate();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(regularEnable) {
                        dateValidate(d1, d2);
                        endDateValidate(d1, d2);
                    } else {
                        onlyDateValidate();
                    }
                }
            });

            endDateEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    try {
                        d1 = simpleDateFormat.parse(dateEditText.getText().toString());
                        d2 = simpleDateFormat.parse(endDateEditText.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    endDateValidate(d1, d2);
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    try {
                        d1 = simpleDateFormat.parse(dateEditText.getText().toString());
                        d2 = simpleDateFormat.parse(endDateEditText.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    endDateValidate(d1, d2);
                }

                @Override
                public void afterTextChanged(Editable s) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    try {
                        d1 = simpleDateFormat.parse(dateEditText.getText().toString());
                        d2 = simpleDateFormat.parse(endDateEditText.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    endDateValidate(d1, d2);
                }
            });

            typeEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    typeValidate();
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    typeValidate();
                }

                @Override
                public void afterTextChanged(Editable s) {
                    typeValidate();
                }
            });

            saveButton = fragmentView.findViewById(R.id.saveButton);

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final Transaction newTransaction = new Transaction();

                    final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    try {
                        d1 = simpleDateFormat.parse(dateEditText.getText().toString());
                        d2 = simpleDateFormat.parse(endDateEditText.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if(validiraj()) {
                        String noviAmountString = amountEditText.getText().toString();
                        Double noviAmount = Double.parseDouble(noviAmountString);
                        boolean prekoracenje = false;
                        String provjera = typeEditText.getText().toString().toUpperCase();
                        if(provjera.equals("REGULARINCOME") || provjera.equals("INDIVIDUALINCOME")) {
                            if(getPresenter().getCurrentStats() - noviAmount > getPresenter().getMonthLimit()) prekoracenje = true;
                        } else if(provjera.equals("PURCHASE") || provjera.equals("INDIVIDUALPAYMENT") || provjera.equals("REGULARPAYMENT")) {
                            if(getPresenter().getCurrentStats() + noviAmount > getPresenter().getMonthLimit()) prekoracenje = true;
                        }

                        if(prekoracenje) {
                            AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setTitle("Dodavanjem ove transakcije prelazite mjesečni ili totalni limit!")
                                    .setMessage("Želite li ipak dodati transakciju?")
                                    .setPositiveButton("Da", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            String title = "";
                                            String amount = "";
                                            String date = "";
                                            String type = "";
                                            String desc = "";
                                            String endDate = "";
                                            String interval = "";
                                            if(regularEnable) {
                                                title = titleEditText.getText().toString();
                                                amount = amountEditText.getText().toString();
                                                date = dateEditText.getText().toString();
                                                type = typeEditText.getText().toString();
                                                desc = descEditText.getText().toString();
                                                endDate = endDateEditText.getText().toString();
                                                interval = intervalEditText.getText().toString();

                                                newTransaction.setTitle(title);
                                                newTransaction.setAmount(Double.parseDouble(amount));
                                                try {
                                                    newTransaction.setDate(simpleDateFormat.parse(date));
                                                    newTransaction.setEndDate(simpleDateFormat.parse(endDate));
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }

                                                if (type.toUpperCase().equals("INDIVIDUALINCOME"))
                                                    newTransaction.setType(PaymentType.INDIVIDUALINCOME);
                                                else if (type.toUpperCase().equals("INDIVIDUALPAYMENT"))
                                                    newTransaction.setType(PaymentType.INDIVIDUALPAYMENT);
                                                else if (type.toUpperCase().equals("PURCHASE"))
                                                    newTransaction.setType(PaymentType.PURCHASE);
                                                else if (type.toUpperCase().equals("REGULARINCOME"))
                                                    newTransaction.setType(PaymentType.REGULARINCOME);
                                                else if (type.toUpperCase().equals("REGULARPAYMENT"))
                                                    newTransaction.setType(PaymentType.REGULARPAYMENT);

                                                newTransaction.setItemDescription(desc);
                                                newTransaction.setTransactionInterval(Integer.parseInt(interval));

                                                //najnovije
                                                if(!ConnectionChecker.connectionStatus(getActivity())) {
                                                    newTransaction.setMode("addOffline");
                                                }

                                                //novi dio
                                                TransactionsModel.transactions.add(newTransaction);
                                                getTransactionListPresenterPresenter().addNewTransaction(newTransaction);
                                                //novi dio
                                                getPresenter().refreshBudget();

                                                onAddClicked.onAddClicked(newTransaction);

                                            } else {
                                                title = titleEditText.getText().toString();
                                                amount = amountEditText.getText().toString();
                                                date = dateEditText.getText().toString();
                                                type = typeEditText.getText().toString();
                                                desc = descEditText.getText().toString();

                                                newTransaction.setTitle(title);
                                                newTransaction.setAmount(Double.parseDouble(amount));
                                                try {
                                                    newTransaction.setDate(simpleDateFormat.parse(date));
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }

                                                if (type.toUpperCase().equals("INDIVIDUALINCOME"))
                                                    newTransaction.setType(PaymentType.INDIVIDUALINCOME);
                                                else if (type.toUpperCase().equals("INDIVIDUALPAYMENT"))
                                                    newTransaction.setType(PaymentType.INDIVIDUALPAYMENT);
                                                else if (type.toUpperCase().equals("PURCHASE"))
                                                    newTransaction.setType(PaymentType.PURCHASE);
                                                else if (type.toUpperCase().equals("REGULARINCOME"))
                                                    newTransaction.setType(PaymentType.REGULARINCOME);
                                                else if (type.toUpperCase().equals("REGULARPAYMENT"))
                                                    newTransaction.setType(PaymentType.REGULARPAYMENT);

                                                newTransaction.setItemDescription(desc);

                                                //najnovije
                                                if(!ConnectionChecker.connectionStatus(getActivity())) {
                                                    newTransaction.setMode("addOffline");
                                                }

                                                //novi dio
                                                TransactionsModel.transactions.add(newTransaction);
                                                getTransactionListPresenterPresenter().addNewTransaction(newTransaction);
                                                //novi dio
                                                getPresenter().refreshBudget();

                                                onAddClicked.onAddClicked(newTransaction);


                                            }

                                            titleEditText.getBackground().setColorFilter(defaultBoja, PorterDuff.Mode.SRC_ATOP);
                                            amountEditText.getBackground().setColorFilter(defaultBoja, PorterDuff.Mode.SRC_ATOP);
                                            dateEditText.getBackground().setColorFilter(defaultBoja, PorterDuff.Mode.SRC_ATOP);
                                            typeEditText.getBackground().setColorFilter(defaultBoja, PorterDuff.Mode.SRC_ATOP);
                                            descEditText.getBackground().setColorFilter(defaultBoja, PorterDuff.Mode.SRC_ATOP);
                                            endDateEditText.getBackground().setColorFilter(defaultBoja, PorterDuff.Mode.SRC_ATOP);
                                            intervalEditText.getBackground().setColorFilter(defaultBoja, PorterDuff.Mode.SRC_ATOP);

                                            Toast.makeText(getActivity(),"Izmjene uspješno spašene!",Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .setNegativeButton("Ne", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Toast.makeText(getActivity(),"Prekid dodavanja transakcije!",Toast.LENGTH_LONG).show();
                                        }
                                    })
                                    .show();
                        } else {
                            String title = "";
                            String amount = "";
                            String date = "";
                            String type = "";
                            String desc = "";
                            String endDate = "";
                            String interval = "";
                            if(regularEnable) {
                                title = titleEditText.getText().toString();
                                amount = amountEditText.getText().toString();
                                date = dateEditText.getText().toString();
                                type = typeEditText.getText().toString();
                                desc = descEditText.getText().toString();
                                endDate = endDateEditText.getText().toString();
                                interval = intervalEditText.getText().toString();

                                newTransaction.setTitle(title);
                                newTransaction.setAmount(Double.parseDouble(amount));
                                try {
                                    newTransaction.setDate(simpleDateFormat.parse(date));
                                    newTransaction.setEndDate(simpleDateFormat.parse(endDate));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                if (type.toUpperCase().equals("INDIVIDUALINCOME"))
                                    newTransaction.setType(PaymentType.INDIVIDUALINCOME);
                                else if (type.toUpperCase().equals("INDIVIDUALPAYMENT"))
                                    newTransaction.setType(PaymentType.INDIVIDUALPAYMENT);
                                else if (type.toUpperCase().equals("PURCHASE"))
                                    newTransaction.setType(PaymentType.PURCHASE);
                                else if (type.toUpperCase().equals("REGULARINCOME"))
                                    newTransaction.setType(PaymentType.REGULARINCOME);
                                else if (type.toUpperCase().equals("REGULARPAYMENT"))
                                    newTransaction.setType(PaymentType.REGULARPAYMENT);

                                newTransaction.setItemDescription(desc);
                                newTransaction.setTransactionInterval(Integer.parseInt(interval));

                                //najnovije
                                if(!ConnectionChecker.connectionStatus(getActivity())) {
                                    newTransaction.setMode("addOffline");
                                }

                                //novi dio
                                TransactionsModel.transactions.add(newTransaction);
                                getTransactionListPresenterPresenter().addNewTransaction(newTransaction);
                                //novi dio
                                getPresenter().refreshBudget();

                                onAddClicked.onAddClicked(newTransaction);

                            } else {
                                title = titleEditText.getText().toString();
                                amount = amountEditText.getText().toString();
                                date = dateEditText.getText().toString();
                                type = typeEditText.getText().toString();
                                desc = descEditText.getText().toString();

                                newTransaction.setTitle(title);
                                newTransaction.setAmount(Double.parseDouble(amount));
                                try {
                                    newTransaction.setDate(simpleDateFormat.parse(date));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                if (type.toUpperCase().equals("INDIVIDUALINCOME"))
                                    newTransaction.setType(PaymentType.INDIVIDUALINCOME);
                                else if (type.toUpperCase().equals("INDIVIDUALPAYMENT"))
                                    newTransaction.setType(PaymentType.INDIVIDUALPAYMENT);
                                else if (type.toUpperCase().equals("PURCHASE"))
                                    newTransaction.setType(PaymentType.PURCHASE);
                                else if (type.toUpperCase().equals("REGULARINCOME"))
                                    newTransaction.setType(PaymentType.REGULARINCOME);
                                else if (type.toUpperCase().equals("REGULARPAYMENT"))
                                    newTransaction.setType(PaymentType.REGULARPAYMENT);

                                newTransaction.setItemDescription(desc);

                                //najnovije
                                if(!ConnectionChecker.connectionStatus(getActivity())) {
                                    newTransaction.setMode("addOffline");
                                }

                                //novi dio
                                TransactionsModel.transactions.add(newTransaction);
                                getTransactionListPresenterPresenter().addNewTransaction(newTransaction);
                                //novi dio
                                getPresenter().refreshBudget();

                                onAddClicked.onAddClicked(newTransaction);

                            }

                            titleEditText.getBackground().setColorFilter(defaultBoja, PorterDuff.Mode.SRC_ATOP);
                            amountEditText.getBackground().setColorFilter(defaultBoja, PorterDuff.Mode.SRC_ATOP);
                            dateEditText.getBackground().setColorFilter(defaultBoja, PorterDuff.Mode.SRC_ATOP);
                            typeEditText.getBackground().setColorFilter(defaultBoja, PorterDuff.Mode.SRC_ATOP);
                            descEditText.getBackground().setColorFilter(defaultBoja, PorterDuff.Mode.SRC_ATOP);
                            endDateEditText.getBackground().setColorFilter(defaultBoja, PorterDuff.Mode.SRC_ATOP);
                            intervalEditText.getBackground().setColorFilter(defaultBoja, PorterDuff.Mode.SRC_ATOP);

                            Toast.makeText(getActivity(),"Izmjene uspješno spašene!",Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(),"Nisu ispravna sva unesena polja!",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        return fragmentView;
    }

    private boolean validiraj() {
        if(regularEnable) {
            if (titleValidate() && amountValidate() && intervalValidate() && dateValidate(d1, d2) && endDateValidate(d1, d2) && typeValidate()) {
                return true;
            }
            else return false;
        } else {
            if (titleValidate() && amountValidate() && onlyDateValidate() && typeValidate()) {
                return true;
            }
            else return false;
        }
    }

    private boolean titleValidate() {
        if(titleEditText.getText().length() < 3 || titleEditText.getText().length() > 15) {
            titleEditText.setError("Title mora imati između 3 i 15 karaktera!");
            titleEditText.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
            return false;
        } else {
            titleEditText.setError(null);
            titleEditText.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP);
            return true;
        }
    }

    private boolean amountValidate() {
        if(amountEditText.getText().toString().isEmpty()) {
            amountEditText.setError("Ovo polje ne može biti prazno!");
            amountEditText.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
            return false;
        } else if(amountEditText.getText().toString().matches(numberRegex)) {
            amountEditText.setError(null);
            amountEditText.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP);
            return true;
        } else if(!amountEditText.getText().toString().matches(numberRegex)) {
            amountEditText.setError("Samo brojevi smiju biti u ovom polju!");
            amountEditText.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
            return false;
        } else {
            amountEditText.setError(null);
            amountEditText.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP);
            return true;
        }
    }

    private boolean intervalValidate() {
        if(intervalEditText.getText().toString().isEmpty()) {
            intervalEditText.setError("Ovo polje ne može biti prazno!");
            intervalEditText.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
            return false;
        } else if(intervalEditText.getText().toString().length() > 8) {
            intervalEditText.setError("Ne smije biti više od 8 brojeva!");
            intervalEditText.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
            return false;
        } else if(intervalEditText.getText().toString().matches(numberRegex)) {
            intervalEditText.setError(null);
            intervalEditText.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP);
            return true;
        } else if(!intervalEditText.getText().toString().matches(numberRegex)) {
            intervalEditText.setError("Samo brojevi smiju biti u ovom polju!");
            intervalEditText.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
            return false;
        } else {
            intervalEditText.setError(null);
            intervalEditText.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP);
            return true;
        }
    }

    private boolean dateValidate(Date date, Date endDate) {
        if(!daLiJePoslijeDatum(date, endDate)) {
            dateEditText.setError("Date ne može biti poslije End Date!");
            dateEditText.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
            return false;
        } else if(!dateEditText.getText().toString().matches("^([0-2][0-9]||3[0-1])/(0[0-9]||1[0-2])/([0-9][0-9])?[0-9][0-9]$")) {
            dateEditText.setError("Niste unijeli ispravnu formu datuma!");
            dateEditText.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
            return false;
        } else {
            dateEditText.setError(null);
            dateEditText.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP);
            return true;
        }
    }

    private boolean endDateValidate(Date date, Date endDate) {
        if(!daLiJePoslijeDatum(date, endDate)) {
            endDateEditText.setError("End date ne može biti prije Date!");
            endDateEditText.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
            return false;
        } else if(!endDateEditText.getText().toString().matches("^([0-2][0-9]||3[0-1])/(0[0-9]||1[0-2])/([0-9][0-9])?[0-9][0-9]$")) {
            endDateEditText.setError("Niste unijeli ispravnu formu datuma!");
            endDateEditText.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
            return false;
        } else {
            endDateEditText.setError(null);
            endDateEditText.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP);
            return true;
        }
    }

    private boolean typeValidate() {
        String input = typeEditText.getText().toString().toUpperCase();
        if(input.equals("REGULARINCOME") || input.equals("REGULARPAYMENT")) {
            endDateEditText.setEnabled(true);
            intervalEditText.setEnabled(true);
            regularEnable = true;
        } else if(input.equals("INDIVIDUALINCOME") || input.equals("INDIVIDUALPAYMENT") || input.equals("PURCHASE")) {
            endDateEditText.setEnabled(false);
            intervalEditText.setEnabled(false);
            regularEnable = false;
        }
        if(input.equals("INDIVIDUALINCOME") || input.equals("INDIVIDUALPAYMENT") || input.equals("PURCHASE") || input.equals("REGULARINCOME") || input.equals("REGULARPAYMENT")) {
            typeEditText.setError(null);
            typeEditText.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP);
            return true;
        } else {
            endDateEditText.setEnabled(false);
            intervalEditText.setEnabled(false);
            regularEnable = true;
            typeEditText.setError("Niste unijeli ispravan tip transakcije!");
            typeEditText.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
            return false;
        }
    }

    private boolean daLiJePoslijeDatum(Date d1, Date d2) {
        if(d1.before(d2)) return true;
        return false;
    }

    private boolean onlyDateValidate() {
        if(!dateEditText.getText().toString().matches("^([0-2][0-9]||3[0-1])/(0[0-9]||1[0-2])/([0-9][0-9])?[0-9][0-9]$")) {
            dateEditText.setError("Niste unijeli ispravnu formu datuma!");
            dateEditText.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
            return false;
        } else {
            dateEditText.setError(null);
            dateEditText.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP);
            return true;
        }
    }

    @Override
    public void setTransactions(ArrayList<Transaction> transactions) {
        transactionListAdapter.setTransactions(transactions);
    }

    @Override
    public void notifyTransactionListDataSetChanged() {
        transactionListAdapter.notifyDataSetChanged();
    }

    @Override
    public void setDetails() {

    }
}
