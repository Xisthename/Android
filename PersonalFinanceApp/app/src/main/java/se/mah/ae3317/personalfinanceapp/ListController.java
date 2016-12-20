package se.mah.ae3317.personalfinanceapp;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by xisth on 2016-09-15.
 */
public class ListController {
    private ListActivity activity;
    private Item[] items;
    private Parcelable[] parcelables;

    public ListController(ListActivity activity, Intent intent) {
        this.activity = activity;

        parcelables = intent.getParcelableArrayExtra(MainController.ITEMS);
        items = new Item[parcelables.length];
        for (int i = 0; i < items.length; i++) {
            items[i] = (Item) parcelables[i];
        }
        activity.setAdapter(new ListAdapter(activity, items));
    }

    private class ListAdapter extends ArrayAdapter<Item> {
        private LayoutInflater inflater;

        public ListAdapter(Context context, Item[] items) {
            super(context,R.layout.item_row, items);
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            Item item = getItem(position);
            ViewHolder holder;

            if (view == null) {
                holder = new ViewHolder();
                view = inflater.inflate(R.layout.item_row, parent, false);
                holder.tvTitle = (TextView) view.findViewById(R.id.tvTitle);
                holder.tvType = (TextView) view.findViewById(R.id.tvType);
                holder.tvMoney = (TextView) view.findViewById(R.id.tvMoney);
                holder.tvDate = (TextView) view.findViewById(R.id.tvDate);
                view.setTag(holder);
            } else {
                holder = (ViewHolder)view.getTag();
            }
            holder.tvTitle.setText(item.getTitle());
            holder.tvType.setText(item.getType());
            holder.tvMoney.setText(Integer.toString(item.getMoney()));
            holder.tvDate.setText(item.getDate());
            return view;
        }
    }

    private class ViewHolder {
        private TextView tvTitle;
        private TextView tvType;
        private TextView tvMoney;
        private TextView tvDate;
    }
}
