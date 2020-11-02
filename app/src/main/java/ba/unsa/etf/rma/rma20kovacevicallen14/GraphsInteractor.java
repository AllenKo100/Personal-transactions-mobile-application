package ba.unsa.etf.rma.rma20kovacevicallen14;

import android.util.Log;

import java.util.Calendar;
import java.util.Date;

public class GraphsInteractor {
    public Integer getMonthIncome(int month) {
        Integer sum = 0;
        for(Transaction t : TransactionsModel.transactions) {
            Date d = t.getDate();
            Calendar cal = Calendar.getInstance();
            cal.setTime(d);
            int dateMonth = cal.get(Calendar.MONTH);
            if(dateMonth == month && t.getType().equals(PaymentType.INDIVIDUALINCOME)) {
                sum = sum + t.getAmount().intValue();
            } else if(t.getType().equals(PaymentType.REGULARINCOME)) {
                Date pocetak = (Date) t.getDate().clone();
                Date kraj = (Date) t.getEndDate().clone();
                Calendar pocetakCal = Calendar.getInstance();
                Calendar krajCal = Calendar.getInstance();
                pocetakCal.setTime(pocetak);
                krajCal.setTime(kraj);
                while(krajCal.after(pocetakCal)) {
                    if(pocetakCal.get(Calendar.MONTH) == month) {
                        sum = sum + t.getAmount().intValue();
                    }
                    pocetakCal.add(Calendar.DATE, t.getTransactionInterval());
                }
            }
        }
        return sum;
    }

    public Integer getMonthOutcome(int month) {
        Integer sum = 0;
        for(Transaction t : TransactionsModel.transactions) {
            Date d = t.getDate();
            Calendar cal = Calendar.getInstance();
            cal.setTime(d);
            int dateMonth = cal.get(Calendar.MONTH);
            if(dateMonth == month && (t.getType().equals(PaymentType.INDIVIDUALPAYMENT) || t.getType().equals(PaymentType.PURCHASE))) {
                sum = sum + t.getAmount().intValue();
            } else if(t.getType().equals(PaymentType.REGULARPAYMENT)) {
                Date pocetak = (Date) t.getDate().clone();
                Date kraj = (Date) t.getEndDate().clone();
                Calendar pocetakCal = Calendar.getInstance();
                Calendar krajCal = Calendar.getInstance();
                pocetakCal.setTime(pocetak);
                krajCal.setTime(kraj);
                while(krajCal.after(pocetakCal)) {
                    if(pocetakCal.get(Calendar.MONTH) == month) {
                        sum = sum + t.getAmount().intValue();
                    }
                    pocetakCal.add(Calendar.DATE, t.getTransactionInterval());
                }
            }
        }
        return sum;
    }

    public Integer getTotal(int month) {
        Integer sum = 0;
        for(Transaction t : TransactionsModel.transactions) {
            Date d = t.getDate();
            Calendar cal = Calendar.getInstance();
            cal.setTime(d);
            int dateMonth = cal.get(Calendar.MONTH);
            if(dateMonth == month && (t.getType().equals(PaymentType.INDIVIDUALPAYMENT) || t.getType().equals(PaymentType.PURCHASE))) {
                sum = sum - t.getAmount().intValue();
            } else if(dateMonth == month && (t.getType().equals(PaymentType.INDIVIDUALINCOME))) {
                sum = sum + t.getAmount().intValue();
            } else if(t.getType().equals(PaymentType.REGULARPAYMENT)) {
                Date pocetak = (Date) t.getDate().clone();
                Date kraj = (Date) t.getEndDate().clone();
                Calendar pocetakCal = Calendar.getInstance();
                Calendar krajCal = Calendar.getInstance();
                pocetakCal.setTime(pocetak);
                krajCal.setTime(kraj);
                while(krajCal.after(pocetakCal)) {
                    if(pocetakCal.get(Calendar.MONTH) == month) {
                        sum = sum - t.getAmount().intValue();
                    }
                    pocetakCal.add(Calendar.DATE, t.getTransactionInterval());
                }
            } else if(t.getType().equals(PaymentType.REGULARINCOME)) {
                Date pocetak = (Date) t.getDate().clone();
                Date kraj = (Date) t.getEndDate().clone();
                Calendar pocetakCal = Calendar.getInstance();
                Calendar krajCal = Calendar.getInstance();
                pocetakCal.setTime(pocetak);
                krajCal.setTime(kraj);
                while(krajCal.after(pocetakCal)) {
                    if(pocetakCal.get(Calendar.MONTH) == month) {
                        sum = sum + t.getAmount().intValue();
                    }
                    pocetakCal.add(Calendar.DATE, t.getTransactionInterval());
                }
            }
        }
        return sum;
    }

    public Integer getDayIncome(int day) {
        Integer sum = 0;
        for(Transaction t : TransactionsModel.transactions) {
            Date currentDate = new Date();
            Date d = t.getDate();
            Calendar cal = Calendar.getInstance();
            cal.setTime(d);
            int dateDay = cal.get(Calendar.DAY_OF_MONTH);
            int dateMonth = cal.get(Calendar.MONTH);
            int dateYear = cal.get(Calendar.YEAR);
            Calendar calCurrent = Calendar.getInstance();
            calCurrent.setTime(currentDate);
            int currentMonth = calCurrent.get(Calendar.MONTH);
            int currentYear = calCurrent.get(Calendar.YEAR);
            if(dateDay == day && dateMonth == currentMonth && dateYear == currentYear && t.getType().equals(PaymentType.INDIVIDUALINCOME)) {
                sum = sum + t.getAmount().intValue();
            } else if(t.getType().equals(PaymentType.REGULARINCOME)) {
                Date pocetak = (Date) t.getDate().clone();
                Date kraj = (Date) t.getEndDate().clone();
                Calendar pocetakCal = Calendar.getInstance();
                Calendar krajCal = Calendar.getInstance();
                pocetakCal.setTime(pocetak);
                krajCal.setTime(kraj);
                while(krajCal.after(pocetakCal)) {
                    if(pocetakCal.get(Calendar.DATE) == day && pocetakCal.get(Calendar.MONTH) == currentMonth) {
                        sum = sum + t.getAmount().intValue();
                    }
                    pocetakCal.add(Calendar.DATE, t.getTransactionInterval());
                }
            }
        }

        return sum;
    }

    public Integer getDayOutcome(int day) {
        Integer sum = 0;
        for(Transaction t : TransactionsModel.transactions) {
            Date currentDate = new Date();
            Date d = t.getDate();
            Calendar cal = Calendar.getInstance();
            cal.setTime(d);
            int dateDay = cal.get(Calendar.DAY_OF_MONTH);
            int dateMonth = cal.get(Calendar.MONTH);
            int dateYear = cal.get(Calendar.YEAR);
            Calendar calCurrent = Calendar.getInstance();
            calCurrent.setTime(currentDate);
            int currentMonth = calCurrent.get(Calendar.MONTH);
            int currentYear = calCurrent.get(Calendar.YEAR);
            if(dateDay == day && dateMonth == currentMonth && dateYear == currentYear && (t.getType().equals(PaymentType.PURCHASE) || t.getType().equals(PaymentType.INDIVIDUALPAYMENT))) {
                sum = sum + t.getAmount().intValue();
            } else if(t.getType().equals(PaymentType.REGULARPAYMENT)) {
                Date pocetak = (Date) t.getDate().clone();
                Date kraj = (Date) t.getEndDate().clone();
                Calendar pocetakCal = Calendar.getInstance();
                Calendar krajCal = Calendar.getInstance();
                pocetakCal.setTime(pocetak);
                krajCal.setTime(kraj);
                while(krajCal.after(pocetakCal)) {
                    if(pocetakCal.get(Calendar.DATE) == day && pocetakCal.get(Calendar.MONTH) == currentMonth) {
                        sum = sum + t.getAmount().intValue();
                    }
                    pocetakCal.add(Calendar.DATE, t.getTransactionInterval());
                }
            }
        }

        return sum;
    }

    public Integer getDayTotal(int day) {
        Integer sum = 0;
        for(Transaction t : TransactionsModel.transactions) {
            Date currentDate = new Date();
            Date d = t.getDate();
            Calendar cal = Calendar.getInstance();
            cal.setTime(d);
            int dateDay = cal.get(Calendar.DAY_OF_MONTH);
            int dateMonth = cal.get(Calendar.MONTH);
            int dateYear = cal.get(Calendar.YEAR);
            Calendar calCurrent = Calendar.getInstance();
            calCurrent.setTime(currentDate);
            int currentMonth = calCurrent.get(Calendar.MONTH);
            int currentYear = calCurrent.get(Calendar.YEAR);
            if(dateDay == day && dateMonth == currentMonth && dateYear == currentYear && (t.getType().equals(PaymentType.PURCHASE) || t.getType().equals(PaymentType.INDIVIDUALPAYMENT))) {
                sum = sum - t.getAmount().intValue();
            } else if(dateDay == day && dateMonth == currentMonth && dateYear == currentYear && t.getType().equals(PaymentType.INDIVIDUALINCOME)) {
                sum = sum + t.getAmount().intValue();
            } else if(t.getType().equals(PaymentType.REGULARINCOME)) {
                Date pocetak = (Date) t.getDate().clone();
                Date kraj = (Date) t.getEndDate().clone();
                Calendar pocetakCal = Calendar.getInstance();
                Calendar krajCal = Calendar.getInstance();
                pocetakCal.setTime(pocetak);
                krajCal.setTime(kraj);
                while(krajCal.after(pocetakCal)) {
                    if(pocetakCal.get(Calendar.DATE) == day && pocetakCal.get(Calendar.MONTH) == currentMonth) {
                        sum = sum + t.getAmount().intValue();
                    }
                    pocetakCal.add(Calendar.DATE, t.getTransactionInterval());
                }
            } else if(t.getType().equals(PaymentType.REGULARPAYMENT)) {
                Date pocetak = (Date) t.getDate().clone();
                Date kraj = (Date) t.getEndDate().clone();
                Calendar pocetakCal = Calendar.getInstance();
                Calendar krajCal = Calendar.getInstance();
                pocetakCal.setTime(pocetak);
                krajCal.setTime(kraj);
                while(krajCal.after(pocetakCal)) {
                    if(pocetakCal.get(Calendar.DATE) == day && pocetakCal.get(Calendar.MONTH) == currentMonth) {
                        sum = sum - t.getAmount().intValue();
                    }
                    pocetakCal.add(Calendar.DATE, t.getTransactionInterval());
                }
            }
        }

        return sum;
    }

    public Integer getWeekIncome(int week) {
        Integer sum = 0;
        for(Transaction t : TransactionsModel.transactions) {
            Date currentDate = new Date();
            Date d = t.getDate();
            Calendar cal = Calendar.getInstance();
            cal.setTime(d);
            int dateDay = cal.get(Calendar.DAY_OF_MONTH);
            int dateMonth = cal.get(Calendar.MONTH);
            int dateYear = cal.get(Calendar.YEAR);
            int dateWeek = cal.get(Calendar.WEEK_OF_MONTH);
            Calendar calCurrent = Calendar.getInstance();
            calCurrent.setTime(currentDate);
            int currentMonth = calCurrent.get(Calendar.MONTH);
            int currentYear = calCurrent.get(Calendar.YEAR);
            if(dateWeek == week && dateMonth == currentMonth && dateYear == currentYear && t.getType().equals(PaymentType.INDIVIDUALINCOME)) {
                sum = sum + t.getAmount().intValue();
            } else if(t.getType().equals(PaymentType.REGULARINCOME)) {
                Date pocetak = (Date) t.getDate().clone();
                Date kraj = (Date) t.getEndDate().clone();
                Calendar pocetakCal = Calendar.getInstance();
                Calendar krajCal = Calendar.getInstance();
                pocetakCal.setTime(pocetak);
                krajCal.setTime(kraj);
                while(krajCal.after(pocetakCal)) {
                    if(pocetakCal.get(Calendar.WEEK_OF_MONTH) == week && pocetakCal.get(Calendar.MONTH) == currentMonth) {
                        sum = sum + t.getAmount().intValue();
                    }
                    pocetakCal.add(Calendar.DATE, t.getTransactionInterval());
                }
            }
        }

        return sum;
    }

    public Integer getWeekOutcome(int week) {
        Integer sum = 0;
        for(Transaction t : TransactionsModel.transactions) {
            Date currentDate = new Date();
            Date d = t.getDate();
            Calendar cal = Calendar.getInstance();
            cal.setTime(d);
            int dateDay = cal.get(Calendar.DAY_OF_MONTH);
            int dateMonth = cal.get(Calendar.MONTH);
            int dateYear = cal.get(Calendar.YEAR);
            int dateWeek = cal.get(Calendar.WEEK_OF_MONTH);
            Calendar calCurrent = Calendar.getInstance();
            calCurrent.setTime(currentDate);
            int currentMonth = calCurrent.get(Calendar.MONTH);
            int currentYear = calCurrent.get(Calendar.YEAR);
            if(dateWeek == week && dateMonth == currentMonth && dateYear == currentYear && (t.getType().equals(PaymentType.PURCHASE) || t.getType().equals(PaymentType.INDIVIDUALPAYMENT))) {
                sum = sum + t.getAmount().intValue();
            } else if(t.getType().equals(PaymentType.REGULARPAYMENT)) {
                Date pocetak = (Date) t.getDate().clone();
                Date kraj = (Date) t.getEndDate().clone();
                Calendar pocetakCal = Calendar.getInstance();
                Calendar krajCal = Calendar.getInstance();
                pocetakCal.setTime(pocetak);
                krajCal.setTime(kraj);
                while(krajCal.after(pocetakCal)) {
                    if(pocetakCal.get(Calendar.WEEK_OF_MONTH) == week && pocetakCal.get(Calendar.MONTH) == currentMonth) {
                        sum = sum + t.getAmount().intValue();
                    }
                    pocetakCal.add(Calendar.DATE, t.getTransactionInterval());
                }
            }
        }

        return sum;
    }

    public Integer getWeekTotal(int week) {
        Integer sum = 0;
        for(Transaction t : TransactionsModel.transactions) {
            Date currentDate = new Date();
            Date d = t.getDate();
            Calendar cal = Calendar.getInstance();
            cal.setTime(d);
            int dateDay = cal.get(Calendar.DAY_OF_MONTH);
            int dateMonth = cal.get(Calendar.MONTH);
            int dateYear = cal.get(Calendar.YEAR);
            int dateWeek = cal.get(Calendar.WEEK_OF_MONTH);
            Calendar calCurrent = Calendar.getInstance();
            calCurrent.setTime(currentDate);
            int currentMonth = calCurrent.get(Calendar.MONTH);
            int currentYear = calCurrent.get(Calendar.YEAR);
            if(dateWeek == week && dateMonth == currentMonth && dateYear == currentYear && (t.getType().equals(PaymentType.PURCHASE) || t.getType().equals(PaymentType.INDIVIDUALPAYMENT))) {
                sum = sum - t.getAmount().intValue();
            } else if(dateWeek == week && dateMonth == currentMonth && dateYear == currentYear && t.getType().equals(PaymentType.INDIVIDUALINCOME)) {
                sum = sum + t.getAmount().intValue();
            } else if(t.getType().equals(PaymentType.REGULARPAYMENT)) {
                Date pocetak = (Date) t.getDate().clone();
                Date kraj = (Date) t.getEndDate().clone();
                Calendar pocetakCal = Calendar.getInstance();
                Calendar krajCal = Calendar.getInstance();
                pocetakCal.setTime(pocetak);
                krajCal.setTime(kraj);
                while(krajCal.after(pocetakCal)) {
                    if(pocetakCal.get(Calendar.WEEK_OF_MONTH) == week && pocetakCal.get(Calendar.MONTH) == currentMonth) {
                        sum = sum - t.getAmount().intValue();
                    }
                    pocetakCal.add(Calendar.DATE, t.getTransactionInterval());
                }
            } else if(t.getType().equals(PaymentType.REGULARINCOME)) {
                Date pocetak = (Date) t.getDate().clone();
                Date kraj = (Date) t.getEndDate().clone();
                Calendar pocetakCal = Calendar.getInstance();
                Calendar krajCal = Calendar.getInstance();
                pocetakCal.setTime(pocetak);
                krajCal.setTime(kraj);
                while(krajCal.after(pocetakCal)) {
                    if(pocetakCal.get(Calendar.WEEK_OF_MONTH) == week && pocetakCal.get(Calendar.MONTH) == currentMonth) {
                        sum = sum + t.getAmount().intValue();
                    }
                    pocetakCal.add(Calendar.DATE, t.getTransactionInterval());
                }
            }
        }

        return sum;
    }
}
