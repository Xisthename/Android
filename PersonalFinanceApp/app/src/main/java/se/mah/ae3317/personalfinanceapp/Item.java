package se.mah.ae3317.personalfinanceapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by xisth on 2016-09-14.
 */
public class Item implements Parcelable {
    private String title;
    private String type;
    private int money;
    private String date;

    public Item(String name, String type, int money, String date) {
        this.title = name;
        this.type = type;
        this.money = money;
        this.date = date;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getMoney() {
        return money;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(type);
        dest.writeInt(money);
        dest.writeString(date);
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        public Item createFromParcel(Parcel source) {
            return new Item(source.readString(), source.readString(), source.readInt(), source.readString());
        }

        public Item[] newArray(int size) {
            return new Item[size];
        }
    };
}
