package se.mah.ae3317.friendfinder;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

/**
 * Created by xisth on 2016-10-11.
 */
public class GroupAdapter extends ArrayAdapter<Object> {
    private Controller controller;
    private LayoutInflater inflate;

    public GroupAdapter(Context context, Object[] objects, Controller controller) {
        super(context, R.layout.fragment_list, objects);
        this.controller = controller;
        inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        Holder holder;

        if (view == null) {
            view = (LinearLayout) inflate.inflate(R.layout.fragment_list, parent, false);

            holder = new Holder();
            holder.amount = (TextView) view.findViewById(R.id.textGroupAmount);
            holder.name = (TextView) view.findViewById(R.id.textGroupName);
            holder.subscribed = (Switch) view.findViewById(R.id.subscribeSwitch);
            view.setTag(holder);
        }
        else {
            holder = (Holder)view.getTag();
        }

        final Group group = (Group) getItem(position);
        holder.name.setText(group.getName());
        holder.amount.setText(group.size() + " " + view.getResources().getString(R.string.groupAmount));

        if (group.isSubscribing()) {
            holder.subscribed.setChecked(true);
        }
        else {
            holder.subscribed.setChecked(false);
        }
        holder.subscribed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Position: ", position + " < " + controller.getGroupHandler().getGroups().size());
                Log.d("ISSubing: ", group.isSubscribing() + "");
                if (position < controller.getGroupHandler().getGroups().size()) {
                    if (group.isSubscribing()) {
                        Log.d("ISSubing: ", "UNSUB");
                        controller.unSubscribe(position);
                    }
                    else {
                        Log.d("ISSubing: ", "SUB");
                        controller.subscribe(group.getName(), false);
                    }
                }
            }
        });
        return view;
    }

    private class Holder{
        TextView name;
        TextView amount;
        Switch subscribed;
    }
}
