package se.mah.ae3317.personalfinanceapp;

/**
 * Created by xisth on 2016-09-16.
 */
public class States {
    public static String CURRENT_STATE = null;
    final static String STATE_iNCOME = "income";
    final static String STATE_EXPENSE = "expense";

    public static void setCurrentState(String state) {
        CURRENT_STATE = state;
    }

    public static String getCurrentState() {
        return CURRENT_STATE;
    }
}
