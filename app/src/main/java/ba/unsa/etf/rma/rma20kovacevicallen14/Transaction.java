package ba.unsa.etf.rma.rma20kovacevicallen14;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Transaction implements Parcelable {
    private Integer id;
    private Date date;
    private Double amount;
    private String title;
    private PaymentType type;
    private String itemDescription;
    private Integer transactionInterval = null;
    private Date endDate = null;
    private Integer internalId = null;
    //atribut potreban zbog undo delete
    private String mode;

    public Transaction() {
    }

    public Transaction(Integer id, Date date, Double amount, String title, PaymentType type, String itemDescription, Integer transactionInterval, Date endDate, Integer internalId, String mode) {
        this.id = id;
        this.date = date;
        this.amount = amount;
        this.title = title;
        this.type = type;
        this.itemDescription = itemDescription;
        this.transactionInterval = transactionInterval;
        this.endDate = endDate;
        this.internalId = internalId;
        this.mode = mode;
    }

    public Transaction(Integer id, Date date, Double amount, String title, PaymentType type, String itemDescription, Integer transactionInterval, Date endDate) {
        this.id = id;
        this.date = date;
        this.amount = amount;
        this.title = title;
        this.type = type;
        this.itemDescription = itemDescription;
        this.transactionInterval = transactionInterval;
        this.endDate = endDate;
    }

    public Transaction(Date date, Double amount, String title, PaymentType type, String itemDescription, Integer transactionInterval, Date endDate) {
        this.date = date;
        this.amount = amount;
        this.title = title;
        this.type = type;
        this.itemDescription = itemDescription;
        this.transactionInterval = transactionInterval;
        this.endDate = endDate;
    }

    protected Transaction(Parcel in) {
        date = new Date();
        long tmp1 = in.readLong();
        date.setTime(tmp1);
        amount = in.readDouble();
        title = in.readString();
        type = (PaymentType) in.readSerializable();
        itemDescription = in.readString();
        transactionInterval = in.readInt();
        endDate = new Date();
        long tmp2 = in.readLong();
        endDate.setTime(tmp2);
    }

    public static final Creator<Transaction> CREATOR = new Creator<Transaction>() {
        @Override
        public Transaction createFromParcel(Parcel in) {
            return new Transaction(in);
        }

        @Override
        public Transaction[] newArray(int size) {
            return new Transaction[size];
        }
    };

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public PaymentType getType() {
        return type;
    }

    public void setType(PaymentType type) {
        this.type = type;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public Integer getTransactionInterval() {
        return transactionInterval;
    }

    public void setTransactionInterval(Integer transactionInterval) {
        this.transactionInterval = transactionInterval;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getInternalId() {
        return internalId;
    }

    public void setInternalId(Integer internalId) {
        this.internalId = internalId;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(date.getTime());
        dest.writeDouble(amount);
        dest.writeString(title);
        dest.writeSerializable(type);
        dest.writeString(itemDescription);
        if(type == PaymentType.REGULARPAYMENT || type == PaymentType.REGULARINCOME) {
            dest.writeInt(transactionInterval);
            dest.writeLong(endDate.getTime());
        } else {

        }
    }
}