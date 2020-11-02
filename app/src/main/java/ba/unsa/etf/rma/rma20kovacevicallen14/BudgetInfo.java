package ba.unsa.etf.rma.rma20kovacevicallen14;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class BudgetInfo extends Fragment implements IAccountView {

    String numberRegex = "^[0-9]*\\.?[0-9]+$";

    private TextView budget;
    private TextView totalLimit;
    private TextView monthLimit;
    private Button editSaveButton;
    private AccountDetailPresenter accountDetailPresenter;

    public AccountDetailPresenter getAccountDetailPresenter() {
        if(accountDetailPresenter == null) {
            accountDetailPresenter = new AccountDetailPresenter(this, getActivity());
        }
        return accountDetailPresenter;
    }

    public BudgetInfo() {
        // Required empty public constructor
    }

    private OnSwipeAction onSwipeAction;

    public interface OnSwipeAction {
        public void onSwipeLeftBudget();
        public void onSwipeRightBudget();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_budget_info, container, false);

        accountDetailPresenter = new AccountDetailPresenter(this, getActivity());

        budget = view.findViewById(R.id.budget);
        totalLimit = view.findViewById(R.id.totalLimit);
        monthLimit = view.findViewById(R.id.monthLimit);
        editSaveButton = view.findViewById(R.id.editSave);

        getAccountDetailPresenter().refresh();

        budget.setText(accountDetailPresenter.getCurrentStats().toString());
        totalLimit.setText(accountDetailPresenter.getTotalLimit().toString());
        monthLimit.setText(accountDetailPresenter.getMonthLimit().toString());

        //novi dio
        String newBudget = budget.getText().toString();
        String newTotalLimit = totalLimit.getText().toString();
        String newMonthLimit = monthLimit.getText().toString();
        final Account acc = new Account(Double.parseDouble(newBudget), Double.parseDouble(newTotalLimit), Double.parseDouble(newMonthLimit));
        accountDetailPresenter.refreshEdit(acc);
        //kraj novog dijela

        onSwipeAction = (OnSwipeAction) getActivity();

        editSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newBudget = budget.getText().toString();
                String newTotalLimit = totalLimit.getText().toString();
                String newMonthLimit = monthLimit.getText().toString();
                Account account = new Account(Double.parseDouble(newBudget), Double.parseDouble(newTotalLimit), Double.parseDouble(newMonthLimit));

                if(amountValidate()) {
                    /*accountDetailPresenter.setBudget(Double.parseDouble(newBudget));
                    accountDetailPresenter.setTotalLimit(Double.parseDouble(newTotalLimit));
                    accountDetailPresenter.setMonthLimit(Double.parseDouble(newMonthLimit));
                    accountDetailPresenter.refreshEdit(account);*/
                    getAccountDetailPresenter().setTotalLimit(Double.parseDouble(newTotalLimit));
                    getAccountDetailPresenter().setMonthLimit(Double.parseDouble(newMonthLimit));
                    if(ConnectionChecker.connectionStatus(getContext())) {
                        getAccountDetailPresenter().refreshEdit(account);
                    } else {
                        getAccountDetailPresenter().refreshEditOffline(account);
                    }
                }
            }
        });

        view.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
            @Override
            public void onSwipeLeft() {
                onSwipeAction.onSwipeLeftBudget();
            }
            @Override
            public void onSwipeRight() {
                onSwipeAction.onSwipeRightBudget();
            }
        });

        return view;

    }

    private boolean amountValidate() {
        if(totalLimit.getText().toString().isEmpty() || monthLimit.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(),"Nisu ispravna sva unesena polja!",Toast.LENGTH_SHORT).show();
            return false;
        } else if(!totalLimit.getText().toString().matches(numberRegex) || !monthLimit.getText().toString().matches(numberRegex)) {
            Toast.makeText(getActivity(),"Nisu ispravna sva unesena polja!",Toast.LENGTH_SHORT).show();
            return false;
        } else if(totalLimit.getText().toString().matches(numberRegex) || monthLimit.getText().toString().matches(numberRegex)) {
            return true;
        } else {
            return true;
        }
    }

    @Override
    public void setInfo(Account account) {
        budget.setText(getAccountDetailPresenter().getCurrentStats().toString());
        totalLimit.setText(AccountsModel.account.getTotalLimit().toString());
        monthLimit.setText(AccountsModel.account.getMonthLimit().toString());
    }
}
