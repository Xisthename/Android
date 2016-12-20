package se.mah.ae3317.friendfinder;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by xisth on 2016-09-30.
 */
public class Controller {
    private MainActivity activity;

    private GroupHandler groupHandler;
    private NetworkReceiver networkmanager;
    private ListView listGroups;

    private GPSListener gpsListener;

    private Thread updaterLoc;
    public static String name;

    public Controller(MainActivity activity, GPSListener gpsListener, Bundle bundle) {
        this.activity = activity;
        activity.setController(this);

        this.gpsListener = gpsListener;
        groupHandler = new GroupHandler();
        networkmanager = new NetworkReceiver(activity);
        networkmanager.connect(bundle != null);

        updaterLoc = new Updater();
        updaterLoc.start();
    }

    public void showRegister() {
        Intent i = new Intent(activity, RegisterActivity.class);
        activity.startActivity(i);
    }

    public MainActivity getActivity() {
        return activity;
    }

    public NetworkReceiver getNetworkManager() {
        return networkmanager;
    }

    public void setListGroups(ListView listGroups) {
        this.listGroups = listGroups;
    }

    public GroupHandler getGroupHandler() {
        return groupHandler;
    }

    public void updateGroupList() {
        listGroups.setAdapter(new GroupAdapter(activity, groupHandler.getGroups().toArray(), this));
    }

    public void subscribe(String groupName, boolean createGroup) {
        if (createGroup) {
            groupHandler.addGroup(groupName);
        }
        groupHandler.getGroup(groupName).setSubscribe(true);
        Log.d("isSubscribing: ", groupHandler.getGroup(groupName).isSubscribing() + "");
        networkmanager.send(JsonBuilder.buildRegister(groupName, groupHandler.getName()));
    }

    public void unSubscribe(int index) {
        groupHandler.getGroups().get(index).setSubscribe(false);
        networkmanager.send(JsonBuilder.buildDeregister(groupHandler.getRegisters().get(index)));
    }


    public void updateInfoManager() {
        networkmanager.send(JsonBuilder.buildCurrentGroups());
    }

    public void onDestroy() {
        if (networkmanager.isBound()) {
            networkmanager.disconnect();
        }
    }

    public void onSaveInstanceState(Bundle bundle) {
        networkmanager.save(bundle);
    }

    public void lostConnection() {
        Toast.makeText(activity, R.string.lostConnection, Toast.LENGTH_LONG).show();
    }

    private class Updater extends Thread {
        @Override
        public void run() {
            while (updaterLoc != null) {
                try {
                    for (String id: groupHandler.getRegisters()) {
                        networkmanager.send(JsonBuilder.buildSetPosition(id, gpsListener.getLongitude(), gpsListener.getLatitude()));
                    }
                    synchronized (updaterLoc) {
                        Log.d("Tick: ", "Update");
                        wait(15000);
                    }
                }
                catch (InterruptedException e) {}
            }
        }
    }
}
