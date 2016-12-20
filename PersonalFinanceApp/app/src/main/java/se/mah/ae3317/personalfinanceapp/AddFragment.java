package se.mah.ae3317.personalfinanceapp;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

/**
 * Created by xisth on 2016-09-12.
 */
public class AddFragment extends Fragment {
    private EditText title;
    private ArrayAdapter<CharSequence> adapter;
    private Spinner spinner;
    private EditText money;
    private static EditText calenderText;
    private ImageButton calenderButton;
    private Button okButton;
    private MainController controller;
    private Calender calender;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add, container, false);
        controller = new MainController(getActivity());
        initComponents(rootView);
        initState(rootView);
        registerListeners();
        return rootView;
    }

    @TargetApi(Build.VERSION_CODES.N)
    private void initComponents(View rootView) {
        title = (EditText) rootView.findViewById(R.id.titleText);
        money = (EditText) rootView.findViewById(R.id.amountText);
        calenderText = (EditText) rootView.findViewById(R.id.calenderText);

        calender = new Calender();
        calenderText.setText(calender.getCurrentDate());

        calenderButton = (ImageButton) rootView.findViewById(R.id.calenderButton);
        calenderButton.setImageResource(R.drawable.calender);

        okButton = (Button)  rootView.findViewById(R.id.okButton);
    }

    private void initState(View rootView) {

        spinner = (Spinner) rootView.findViewById(R.id.spinner);
        switch (States.CURRENT_STATE) {
            case States.STATE_iNCOME: {
                adapter = ArrayAdapter.createFromResource(getActivity(), R.array.income_array, android.R.layout.simple_spinner_item);
                okButton.setText(R.string.buttonAddIncome);
                break;
            }
            case States.STATE_EXPENSE: {
                adapter = ArrayAdapter.createFromResource(getActivity(), R.array.expense_array, android.R.layout.simple_spinner_item);
                okButton.setText(R.string.buttonAddExpense);
                break;
            }
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);
    }

    private void registerListeners() {
        calenderButton.setOnClickListener(new ButtonListeners());
        okButton.setOnClickListener(new ButtonListeners());
    }

    public static void setDate(String date) {
        calenderText.setText(date);
    }

    private class ButtonListeners implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.calenderButton: {
                    calender.show(getFragmentManager(), "");
                    break;
                }
                case R.id.okButton: {
                    try {
                        int moneyText = Integer.parseInt(money.getText().toString().trim());
                        String titleText = title.getText().toString().trim();
                        String typeText = spinner.getSelectedItem().toString().trim();
                        String dateText = calenderText.getText().toString().trim();

                        if (!titleText.isEmpty() && !typeText.isEmpty() && moneyText != 0 && !dateText.isEmpty()) {
                            switch (States.getCurrentState()) {
                                case States.STATE_iNCOME: {
                                    controller.addItem(titleText, typeText, moneyText, dateText);
                                    controller.showList();
                                    break;
                                }
                                case States.STATE_EXPENSE: {
                                    controller.addItem(titleText, typeText, moneyText, dateText);
                                    controller.showList();
                                    break;
                                }
                            }
                        }
                    }
                    catch (NumberFormatException e) {}
                    break;
                }
            }
        }
    }
}