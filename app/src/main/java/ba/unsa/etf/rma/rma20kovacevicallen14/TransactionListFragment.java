package ba.unsa.etf.rma.rma20kovacevicallen14;

import android.content.ContentResolver;
import android.icu.util.Calendar;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionListFragment extends Fragment implements ITransactionListView, IAccountView {

    private TextView globalAmountText;
    private TextView limitText;
    private TextView dateText;
    private ImageButton leftButton;
    private ImageButton rightButton;
    private ListView transactionView;
    private Button addButton;
    private Button editButton;
    private Button graphsButton;

    private TransactionListPresenter transactionListPresenter;

    private TransactionListAdapter transactionListAdapter;

    private Spinner transactionSpinner;
    private String[] transactionsTypes;
    private Integer[] transactionsImages;

    private Date currentDate = new Date();
    private int filterId = 0;
    private int sortId = 0;

    Transaction transactionDelete;

    private Spinner sortSpinner;
    String[] sorts = {"Price - Ascending", "Price - Descending", "Title - Ascending", "Title - Descending", "Date - Ascending", "Date - Descending"};

    public TransactionListPresenter getPresenter() {
        if(transactionListPresenter == null) {
            transactionListPresenter = new TransactionListPresenter(this, getActivity());
        }
        return transactionListPresenter;
    }

    private AccountDetailPresenter accountDetailPresenter;

    public AccountDetailPresenter getAccountDetailPresenter() {
        if(accountDetailPresenter == null) {
            accountDetailPresenter = new AccountDetailPresenter(this, getActivity());
        }
        return accountDetailPresenter;
    }

    public TransactionListFragment() {
        // Required empty public constructor
    }

    private OnSwipeAction onSwipeAction;

    public interface OnSwipeAction {
        public void onSwipeLeft();
        public void onSwipeRight();
    }

    private OnItemClick onItemClick;
    private OnItemClick onAddClick;
    public interface OnItemClick {
        public void onItemClicked(Transaction transaction);
        public void onAddClicked();
        public void onEditClicked();
        public void onGraphsClicked();
    }

    private int currentPosition;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_transaction_list, container, false);

        globalAmountText = fragmentView.findViewById(R.id.globalAmountTextView);
        limitText = fragmentView.findViewById(R.id.limitTextView);

        transactionListAdapter = new TransactionListAdapter(getActivity(), R.layout.list_element, new ArrayList<Transaction>());
        transactionView = fragmentView.findViewById(R.id.transactionView);
        transactionView.setAdapter(transactionListAdapter);
        transactionView.setOnItemClickListener(listItemClickListener);

        if(ConnectionChecker.connectionStatus(getContext())) {
            getPresenter().refreshTransactions(filterId, sortId, currentDate);
        } else {
            getPresenter().refreshTransactionsOffline(filterId, sortId, currentDate);
        }

        if(ConnectionChecker.connectionStatus(getContext())) {
            getAccountDetailPresenter().refreshStats();
        } else {
            getAccountDetailPresenter().refreshStats();
        }


        onItemClick= (OnItemClick) getActivity();
        onAddClick = (OnItemClick) getActivity();

        getAccountDetailPresenter().refreshStats();
        globalAmountText.setText(getAccountDetailPresenter().getCurrentStats().toString());
        limitText.setText(getAccountDetailPresenter().getTotalLimit().toString());

        getAccountDetailPresenter().refreshBudget();

        setInfo(AccountsModel.account);


        editButton = fragmentView.findViewById(R.id.editButton);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.onEditClicked();
            }
        });

        graphsButton = fragmentView.findViewById(R.id.graphsButton);

        graphsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.onGraphsClicked();
            }
        });

        onSwipeAction = (OnSwipeAction) getActivity();

        fragmentView.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
            @Override
            public void onSwipeLeft() {
                onSwipeAction.onSwipeLeft();
            }
            @Override
            public void onSwipeRight() {
                onSwipeAction.onSwipeRight();
            }
        });

        addButton = fragmentView.findViewById(R.id.addButton);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddClick.onAddClicked();
            }
        });

        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy MMMM");
        dateText = fragmentView.findViewById(R.id.dateText);
        dateText.setText(simpleDateFormat.format(currentDate));

        rightButton = fragmentView.findViewById(R.id.rightButton);
        leftButton = fragmentView.findViewById(R.id.leftButton);

        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(currentDate);
                cal.add(Calendar.MONTH, 1);
                currentDate = cal.getTime();

                dateText.setText(simpleDateFormat.format(currentDate));
                //getPresenter().refreshTransactions(filterId, sortId, currentDate);
                if(ConnectionChecker.connectionStatus(getContext())) {
                    getPresenter().refreshTransactions(filterId, sortId, currentDate);
                } else {
                    getPresenter().refreshTransactionsOffline(filterId, sortId, currentDate);
                }
            }
        });

        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(currentDate);
                cal.add(Calendar.MONTH, -1);
                currentDate = cal.getTime();

                dateText.setText(simpleDateFormat.format(currentDate));
                //getPresenter().refreshTransactions(filterId, sortId, currentDate);
                if(ConnectionChecker.connectionStatus(getContext())) {
                    getPresenter().refreshTransactions(filterId, sortId, currentDate);
                } else {
                    getPresenter().refreshTransactionsOffline(filterId, sortId, currentDate);
                }
            }
        });

        transactionsTypes = new String[]{"All transactions", "Individual payment", "Regular payment", "Purchase", "Individual income", "Regular income"};
        transactionsImages = new Integer[]{R.drawable.all_transactions, R.drawable.individual_payment, R.drawable.regular_payment, R.drawable.purchase, R.drawable.individual_income, R.drawable.regular_income};
        transactionSpinner = fragmentView.findViewById(R.id.filterBy);
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(getActivity(), R.layout.transaction_spinner_row, transactionsTypes, transactionsImages);
        transactionSpinner.setAdapter(spinnerAdapter);
        transactionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position >= 0 && position < transactionsTypes.length) {
                    filterId = position;
                    //getPresenter().refreshTransactions(filterId, sortId, currentDate);
                    if(ConnectionChecker.connectionStatus(getContext())) {
                        getPresenter().refreshTransactions(filterId, sortId, currentDate);
                    } else {
                        getPresenter().refreshTransactionsOffline(filterId, sortId, currentDate);
                    }
                } else {
                    //getPresenter().refreshTransactions(filterId, sortId, currentDate);
                    if(ConnectionChecker.connectionStatus(getContext())) {
                        getPresenter().refreshTransactions(filterId, sortId, currentDate);
                    } else {
                        getPresenter().refreshTransactionsOffline(filterId, sortId, currentDate);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        sortSpinner = fragmentView.findViewById(R.id.sortSpinner);
        ArrayAdapter sortSpinnerAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, sorts);
        sortSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(sortSpinnerAdapter);
        sortSpinner.setSelection(0);
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //ifologija sad
                if(sortSpinner.getSelectedItem().equals("Price - Ascending")) {
                    sortId = 0;
                    //getPresenter().refreshTransactions(filterId, sortId, currentDate);
                    if(ConnectionChecker.connectionStatus(getContext())) {
                        getPresenter().refreshTransactions(filterId, sortId, currentDate);
                    } else {
                        getPresenter().refreshTransactionsOffline(filterId, sortId, currentDate);
                    }
                } else if(sortSpinner.getSelectedItem().equals("Price - Descending")) {
                    sortId = 1;
                    //getPresenter().refreshTransactions(filterId, sortId, currentDate);
                    if(ConnectionChecker.connectionStatus(getContext())) {
                        getPresenter().refreshTransactions(filterId, sortId, currentDate);
                    } else {
                        getPresenter().refreshTransactionsOffline(filterId, sortId, currentDate);
                    }
                } else if(sortSpinner.getSelectedItem().equals("Title - Ascending")) {
                    sortId = 2;
                    //getPresenter().refreshTransactions(filterId, sortId, currentDate);
                    if(ConnectionChecker.connectionStatus(getContext())) {
                        getPresenter().refreshTransactions(filterId, sortId, currentDate);
                    } else {
                        getPresenter().refreshTransactionsOffline(filterId, sortId, currentDate);
                    }
                } else if(sortSpinner.getSelectedItem().equals("Title - Descending")) {
                    sortId = 3;
                    //getPresenter().refreshTransactions(filterId, sortId, currentDate);
                    if(ConnectionChecker.connectionStatus(getContext())) {
                        getPresenter().refreshTransactions(filterId, sortId, currentDate);
                    } else {
                        getPresenter().refreshTransactionsOffline(filterId, sortId, currentDate);
                    }
                } else if(sortSpinner.getSelectedItem().equals("Date - Ascending")) {
                    sortId = 4;
                    //getPresenter().refreshTransactions(filterId, sortId, currentDate);
                    if(ConnectionChecker.connectionStatus(getContext())) {
                        getPresenter().refreshTransactions(filterId, sortId, currentDate);
                    } else {
                        getPresenter().refreshTransactionsOffline(filterId, sortId, currentDate);
                    }
                } else if(sortSpinner.getSelectedItem().equals("Date - Descending")) {
                    sortId = 5;
                    //getPresenter().refreshTransactions(filterId, sortId, currentDate);
                    if(ConnectionChecker.connectionStatus(getContext())) {
                        getPresenter().refreshTransactions(filterId, sortId, currentDate);
                    } else {
                        getPresenter().refreshTransactionsOffline(filterId, sortId, currentDate);
                    }
                } else {
                    //getPresenter().refreshTransactions(filterId, sortId, currentDate);
                    if(ConnectionChecker.connectionStatus(getContext())) {
                        getPresenter().refreshTransactions(filterId, sortId, currentDate);
                    } else {
                        getPresenter().refreshTransactionsOffline(filterId, sortId, currentDate);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return fragmentView;
    }

    private AdapterView.OnItemClickListener listItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Transaction transaction = transactionListAdapter.getTransaction(position);
            onItemClick.onItemClicked(transaction);
        }
    };

    @Override
    public void setTransactions(ArrayList<Transaction> transactions) {
        transactionListAdapter.setTransactions(transactions);
    }

    @Override
    public void notifyTransactionListDataSetChanged() {
        transactionListAdapter.notifyDataSetChanged();
    }

    public void setDetails() {
        if(ConnectionChecker.connectionStatus(getActivity())) {
            globalAmountText.setText(getAccountDetailPresenter().getCurrentStats().toString());
            limitText.setText(getAccountDetailPresenter().getTotalLimit().toString());
        } else {
            globalAmountText.setText(AccountsModel.account.getBudget().toString());
            limitText.setText(AccountsModel.account.getTotalLimit().toString());
        }
    }

    @Override
    public void setInfo(Account account) {
        globalAmountText.setText(getAccountDetailPresenter().getCurrentStats().toString());
        //limitText.setText(AccountsModel.account.getTotalLimit().toString());
        limitText.setText(getAccountDetailPresenter().getTotalLimit().toString());
    }
}
