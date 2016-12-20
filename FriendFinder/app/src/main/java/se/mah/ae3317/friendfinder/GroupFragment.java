package se.mah.ae3317.friendfinder;


import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by xisth on 2016-10-04.
 */
public class GroupFragment {
    private Button updateButton;
    private Button createButton;
    private EditText nameOfGroup;
    private ListView list;
    private Controller controller;

    public GroupFragment(View view, Controller controller) {
        this.controller = controller;
        initComponents(view);
    }

    private void initComponents(View view) {
        updateButton = (Button) view.findViewById(R.id.updateButton);
        updateButton.setOnClickListener(new ButtonListener());

        createButton = (Button) view.findViewById(R.id.createGroup);
        createButton.setOnClickListener(new ButtonListener());

        nameOfGroup = (EditText) view.findViewById(R.id.nameOfGroup);

        list = (ListView) view.findViewById(R.id.listGroups);
        controller.setListGroups(list);
    }

    private class ButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.updateButton: {
                    controller.updateInfoManager();
                    break;
                }
                case R.id.createGroup: {
                    if (nameOfGroup.length() > 3) {
                        controller.subscribe(nameOfGroup.getText().toString(), true);
                    }
                    else {
                        Toast.makeText(controller.getActivity(), controller.getActivity().getString(R.string.tooShort),
                                Toast.LENGTH_LONG).show();
                    }
                    break;
                }
            }
        }
    }
}
