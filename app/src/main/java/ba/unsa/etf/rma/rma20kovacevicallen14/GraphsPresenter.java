package ba.unsa.etf.rma.rma20kovacevicallen14;


public class GraphsPresenter {
    private GraphsInteractor interactor;

    public GraphsPresenter() {
        this.interactor = new GraphsInteractor();
    }

    public Integer getMonthIncome(int month) {
        return interactor.getMonthIncome(month);
    }

    public Integer getMonthOutcome(int month) {
        return interactor.getMonthOutcome(month);
    }

    public Integer getTotal(int month) {
        return interactor.getTotal(month);
    }

    public Integer getDayIncome(int day) {
        return interactor.getDayIncome(day);
    }

    public Integer getDayOutcome(int day) {
        return interactor.getDayOutcome(day);
    }

    public Integer getDayTotal(int day) {
        return interactor.getDayTotal(day);
    }

    public Integer getWeekIncome(int week) {
        return interactor.getWeekIncome(week);
    }

    public Integer getWeekOutcome(int week) {
        return interactor.getWeekOutcome(week);
    }

    public Integer getWeekTotal(int week) {
        return interactor.getWeekTotal(week);
    }
}
