public class GregorianDate extends Date {

    private static final int[] MONTH_LENGTHS = {
        31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31
    };

    public GregorianDate(int year, int month, int dayOfMonth) {
        super(year, month, dayOfMonth);
    }

    @Override
    public int dayOfYear() {
        int precedingMonthDays = 0;
        for (int m = 1; m < month; m += 1) {
            precedingMonthDays += getMonthLength(m);
        }
        return precedingMonthDays + dayOfMonth;
    }

    private static int getMonthLength(int m) {
        return MONTH_LENGTHS[m - 1];
    }

    @Override
    public Date nextDate() {
        int newDay;
        int newMonth = month;
        int newYear = year;
        if (month == 2 && dayOfMonth == 28) {
            newDay = 1;
            newMonth++;
        } else if ((month == 1 || month == 3 || month == 5 || month == 7 || month == 8
                || month == 10) && dayOfMonth == 31) {
            newDay = 1;
            newMonth++;
        } else if ((month == 4 || month == 6 || month == 9 || month == 11) && dayOfMonth == 30) {
            newDay = 1;
            newMonth++;
        } else if (month == 12 && dayOfMonth == 31) {
            newDay = 1;
            newMonth = 1;
            newYear++;
        } else {
            newDay = dayOfMonth + 1;
        }
        return new GregorianDate(newYear, newMonth, newDay);
    }
}
