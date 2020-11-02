package ba.unsa.etf.rma.rma20kovacevicallen14;

public class Account {
    private Integer id;
    private Double budget;
    private Double totalLimit;
    private Double monthLimit;

    public Account() {
    }

    public Account(Double budget, Double totalLimit, Double monthLimit) {
        this.budget = budget;
        this.totalLimit = totalLimit;
        this.monthLimit = monthLimit;
    }

    public Account(Integer id, Double budget, Double totalLimit, Double monthLimit) {
        this.id = id;
        this.budget = budget;
        this.totalLimit = totalLimit;
        this.monthLimit = monthLimit;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getBudget() {
        return budget;
    }

    public void setBudget(Double budget) {
        this.budget = budget;
    }

    public Double getTotalLimit() {
        return totalLimit;
    }

    public void setTotalLimit(Double totalLimit) {
        this.totalLimit = totalLimit;
    }

    public Double getMonthLimit() {
        return monthLimit;
    }

    public void setMonthLimit(Double monthLimit) {
        this.monthLimit = monthLimit;
    }
}
