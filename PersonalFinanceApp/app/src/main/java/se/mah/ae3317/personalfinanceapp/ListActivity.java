package se.mah.ae3317.personalfinanceapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Method;

/**
 * Created by xisth on 2016-09-15.
 */
public class ListActivity extends Activity {
    private TextView headerText;
    private ListView list;
    private TextView title;
    private TextView type;
    private TextView amount;
    private TextView date;
    private int textSize = 18;
    private MainController controller = new MainController(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        headerText = (TextView) findViewById(R.id.listHeaderText);
        list = (ListView) findViewById(R.id.list);
        switch (States.getCurrentState()) {
            case States.STATE_iNCOME: {
                headerText.setText(R.string.headerIncomesText);
                break;
            }
            case States.STATE_EXPENSE: {
                headerText.setText(R.string.headerExpensesText);
                break;
            }
        }
        Intent intent = getIntent();
        new ListController(this, intent);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, final long id) {
                final Item item = (Item) parent.getItemAtPosition(position);

                AlertDialog.Builder dialog = new AlertDialog.Builder(view.getContext());
                int paddingSpace = 50;
                LinearLayout layout = new LinearLayout(view.getContext());
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setPadding(paddingSpace, paddingSpace, 0, 0);

                title = new TextView(view.getContext());
                title.setTextSize(textSize);
                title.setText("Title: " + item.getTitle());
                layout.addView(title);

                type = new TextView(view.getContext());
                type.setTextSize(textSize);
                type.setText("Type: " + item.getType());
                layout.addView(type);

                amount = new TextView(view.getContext());
                amount.setTextSize(textSize);
                amount.setText("Amount: " + item.getMoney());
                layout.addView(amount);

                date = new TextView(view.getContext());
                date.setTextSize(textSize);
                date.setText("Date: " + item.getDate());
                layout.addView(date);

                int image = controller.getImage(item.getType());

                if (image != -1) {
                    ImageView imageView = new ImageView(view.getContext());
                    imageView.setPadding(-paddingSpace, 0, 0, 0);
                    imageView.setImageResource(controller.getImage(item.getType()));
                    layout.addView(imageView);
                }
                dialog.setView(layout);
                dialog.setCancelable(false);
                dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {}
                });

                dialog.setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        controller.deleteId(item);
                    }
                });
                dialog.show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public void setAdapter(ArrayAdapter<Item> listAdapter) {
        list.setAdapter(listAdapter);
    }
}
