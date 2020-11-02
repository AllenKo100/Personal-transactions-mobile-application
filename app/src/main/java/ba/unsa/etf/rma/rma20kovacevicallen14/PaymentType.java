package ba.unsa.etf.rma.rma20kovacevicallen14;

public enum PaymentType {
    INDIVIDUALPAYMENT(1),
    REGULARPAYMENT(2),
    PURCHASE(3),
    INDIVIDUALINCOME(4),
    REGULARINCOME(5);

    private final int id;

    private PaymentType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}