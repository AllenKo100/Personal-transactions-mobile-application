package ba.unsa.etf.rma.rma20kovacevicallen14;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TransactionAddActivity extends AppCompatActivity {

    private EditText titleEditText;
    private EditText amountEditText;
    private EditText dateEditText;
    private EditText descEditText;
    private EditText endDateEditText;
    private EditText intervalEditText;
    private EditText typeEditText;

    private Button saveButton;
    private Button deleteButton;

    private AccountDetailPresenter accountDetailPresenter;

    public AccountDetailPresenter getPresenter() {
        if(accountDetailPresenter == null) {
            accountDetailPresenter = new AccountDetailPresenter();
        }
        return accountDetailPresenter;
    }

    String numberRegex = "^[0-9]*\\.?[0-9]+$";
    String dateRegex = "^\\d{4}-\\d{2}-\\d{2}$";

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

    Date d1 = new Date();
    Date d2 = new Date();

    Double budget = getPresenter().getBudget();
    Double totalLimit = getPresenter().getTotalLimit();
    Double monthLimit = getPresenter().getMonthLimit();
    Double current = getPresenter().getCurrentStats();

    boolean regularEnable = false;

    int defaultBoja;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_detail);

        /*for(Transaction trans : TransactionsModel.transactions) {
            if(trans.getType() == PaymentType.REGULARINCOME || trans.getType() == PaymentType.INDIVIDUALINCOME) {
                current = current - trans.getAmount();
            } else {
                current = current - trans.getAmount();
            }
        }*/

        deleteButton = (Button)findViewById(R.id.deleteButton);

        deleteButton.setEnabled(false);

        titleEditText = (EditText)findViewById(R.id.titleEditText);
        amountEditText = (EditText)findViewById(R.id.amountEditText);
        dateEditText = (EditText)findViewById(R.id.dateEditText);
        descEditText = (EditText)findViewById(R.id.descEditText);
        endDateEditText = (EditText)findViewById(R.id.endDateEditText);
        intervalEditText = (EditText)findViewById(R.id.intervalEditText);
        typeEditText = (EditText)findViewById(R.id.typeEditText);

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

        saveButton = (Button)findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    //if(getPresenter().getCurrentStats() + minus*noviAmount > getPresenter().getMonthLimit()) prekoracenje = true;
                    //else if(getPresenter().getCurrentStats() + minus*noviAmount > getPresenter().getTotalLimit()) prekoracenje = true;
                    //else prekoracenje = false;

                    if(prekoracenje) {
                        AlertDialog alertDialog = new AlertDialog.Builder(TransactionAddActivity.this)
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
                                            Transaction newTransaction = new Transaction();
                                            title = titleEditText.getText().toString();
                                            amount = amountEditText.getText().toString();
                                            date = dateEditText.getText().toString();
                                            type = typeEditText.getText().toString();
                                            desc = descEditText.getText().toString();
                                            endDate = endDateEditText.getText().toString();
                                            interval = intervalEditText.getText().toString();
                                        } else {
                                            Transaction newTransaction = new Transaction();
                                            title = titleEditText.getText().toString();
                                            amount = amountEditText.getText().toString();
                                            date = dateEditText.getText().toString();
                                            type = typeEditText.getText().toString();
                                            desc = descEditText.getText().toString();

                                        }

                                        titleEditText.getBackground().setColorFilter(defaultBoja, PorterDuff.Mode.SRC_ATOP);
                                        amountEditText.getBackground().setColorFilter(defaultBoja, PorterDuff.Mode.SRC_ATOP);
                                        dateEditText.getBackground().setColorFilter(defaultBoja, PorterDuff.Mode.SRC_ATOP);
                                        typeEditText.getBackground().setColorFilter(defaultBoja, PorterDuff.Mode.SRC_ATOP);
                                        descEditText.getBackground().setColorFilter(defaultBoja, PorterDuff.Mode.SRC_ATOP);
                                        endDateEditText.getBackground().setColorFilter(defaultBoja, PorterDuff.Mode.SRC_ATOP);
                                        intervalEditText.getBackground().setColorFilter(defaultBoja, PorterDuff.Mode.SRC_ATOP);

                                        Intent intent = new Intent();
                                        intent.putExtra("title", title);
                                        intent.putExtra("amount", amount);
                                        intent.putExtra("date", date);
                                        intent.putExtra("type", type);
                                        intent.putExtra("desc", desc);
                                        intent.putExtra("endDate", endDate);
                                        intent.putExtra("interval", interval);
                                        setResult(RESULT_OK, intent);
                                        Toast.makeText(getApplicationContext(),"Izmjene uspješno spašene!",Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                })
                                .setNegativeButton("Ne", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Toast.makeText(getApplicationContext(),"Prekid dodavanja transakcije!",Toast.LENGTH_LONG).show();
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
                            Transaction newTransaction = new Transaction();
                            title = titleEditText.getText().toString();
                            amount = amountEditText.getText().toString();
                            date = dateEditText.getText().toString();
                            type = typeEditText.getText().toString();
                            desc = descEditText.getText().toString();
                            endDate = endDateEditText.getText().toString();
                            interval = intervalEditText.getText().toString();
                        } else {
                            Transaction newTransaction = new Transaction();
                            title = titleEditText.getText().toString();
                            amount = amountEditText.getText().toString();
                            date = dateEditText.getText().toString();
                            type = typeEditText.getText().toString();
                            desc = descEditText.getText().toString();
                        }

                        titleEditText.getBackground().setColorFilter(defaultBoja, PorterDuff.Mode.SRC_ATOP);
                        amountEditText.getBackground().setColorFilter(defaultBoja, PorterDuff.Mode.SRC_ATOP);
                        dateEditText.getBackground().setColorFilter(defaultBoja, PorterDuff.Mode.SRC_ATOP);
                        typeEditText.getBackground().setColorFilter(defaultBoja, PorterDuff.Mode.SRC_ATOP);
                        descEditText.getBackground().setColorFilter(defaultBoja, PorterDuff.Mode.SRC_ATOP);
                        endDateEditText.getBackground().setColorFilter(defaultBoja, PorterDuff.Mode.SRC_ATOP);
                        intervalEditText.getBackground().setColorFilter(defaultBoja, PorterDuff.Mode.SRC_ATOP);

                        Intent i = new Intent();
                        i.putExtra("title", title);
                        i.putExtra("amount", amount);
                        i.putExtra("date", date);
                        i.putExtra("type", type);
                        i.putExtra("desc", desc);
                        i.putExtra("endDate", endDate);
                        i.putExtra("interval", interval);
                        setResult(RESULT_OK, i);
                        Toast.makeText(getApplicationContext(),"Izmjene uspješno spašene!",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    Toast.makeText(getApplicationContext(),"Nisu ispravna sva unesena polja!",Toast.LENGTH_SHORT).show();
                }
            }
        });
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
}
