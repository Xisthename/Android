package se.mah.ae3317.personalfinanceapp;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * Created by xisth on 2016-09-12.
 */
public class OverallFragment extends Fragment {
    private MainController controller;
    private TextView currentBalance;
    private TextView incomeBalance;
    private TextView expenseBalance;

    public OverallFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_overall, container, false);
        currentBalance = (TextView) rootView.findViewById(R.id.currentBalance);
        incomeBalance = (TextView) rootView.findViewById(R.id.incomeBalance);
        expenseBalance = (TextView) rootView.findViewById(R.id.expenseBalance);
        controller = new MainController(getActivity());
        setText();
        return rootView;
    }

    public void setText() {
        currentBalance.setText("Current balance: " + controller.getTotalMoney());
        incomeBalance.setText("Incomes: " + controller.getIncomes());
        expenseBalance.setText("Expenses: " + controller.getExpenses());
    }
}
