package mywebserver.Sensor;

public enum Months {
    JAN(1, 31),
    FEB(2, 28),
    MAR(3, 31),
    APR(4, 30),
    MAY(5, 31),
    JUN(6, 30),
    JUL(7, 31),
    AUG(8, 31),
    SEP(9, 30),
    OCT(10, 31),
    NOV(11, 30),
    DEZ(12, 31);
    private int idx;
    private int numOfDays;
    private static final int size = Months.values().length;

    Months(int idx, int numOfDays) {
        this.idx = idx;
        this.numOfDays = numOfDays;
    }

    public static int getSize() {
        return size;
    }

    public static int getnumOfDays(int idx) {
        for (Months m : Months.values()) {
            if (m.idx == idx) {
                return m.numOfDays;
            }
        }
        return -1;
    }
}
