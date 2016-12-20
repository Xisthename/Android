package se.mah.ae3317.friendfinder;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by xisth on 2016-09-30.
 */
public class NetworkReceiver {

    private MainActivity activity;
    private ServiceConnection serviceConnection;

    private TCPConnection tcpConnection;
    private boolean isBound;

    public ReceiverListener listener;

    public NetworkReceiver(MainActivity activity) {
        this.activity = activity;
    }

    public void connect(boolean serviceAlive) {
        Intent intent = new Intent(activity, TCPConnection.class);

        if (!serviceAlive) {
            activity.startService(intent);
        }
        serviceConnection = new ServiceConnection();
        activity.bindService(intent, serviceConnection, 0);
    }

    public boolean isBound() {
        return isBound;
    }

    public void save(Bundle bundle) {
        bundle.putBoolean("BOUND", isBound);
    }

    public void disconnect() {
        if (serviceConnection != null) {
            activity.unbindService(serviceConnection);
            listener.stopListener();
            tcpConnection.disconnect();
        }
    }

    public void send(String message) {
        tcpConnection.send(message);
    }

    private class ServiceConnection implements android.content.ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            TCPConnection.LocalService ls = (TCPConnection.LocalService) service;
            tcpConnection = ls.getService();

            tcpConnection.connect();
            isBound = true;

            listener = new ReceiverListener();
            listener.start();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    }

    private class ReceiverListener extends Thread {
        public void stopListener() {
            interrupt();
            listener = null;
        }

        public void run() {
            String message;
            while (listener != null) {
                try {
                    message = tcpConnection.receive();

                    if (message.equals("exception")) {
                        activity.getController().lostConnection();
                        tcpConnection.setConnected(false);
                        tcpConnection.connect();
                    }
                    else {
                        if (!message.equals("CONNECTED")) {
                            JSONObject object = new JSONObject(message);
                            String type = object.getString("type");

                            if (type.equals("register")) {
                                Log.d("Register: ", message);
                                activity.getController().getGroupHandler().updateRegister(object);
                                activity.getController().getNetworkManager().send(JsonBuilder.buildCurrentGroups());
                            }
                            else if (type.equals("unregister")) {
                                String id = object.getString("id");
                                String[] tempID = id.split(",");
                                String groupName = tempID[0];
                                for (int i = 0; i < activity.getController().getGroupHandler().getRegisters().size(); i++) {
                                    if (activity.getController().getGroupHandler().getRegisters().get(i).equals(id)) {
                                        Log.d("Unregister from group: ", groupName);
                                        activity.getController().getGroupHandler().getRegisters().remove(i);
                                    }
                                }
                                activity.runOnUiThread(new RemoveMarkers(groupName));
                            }
                            else if (type.equals("groups")) {
                                Log.d("Groups: ", message);
                                activity.getController().getGroupHandler().updateGroups(object);
                                for (Group group : activity.getController().getGroupHandler().getGroups()) {
                                    activity.getController().getNetworkManager().send(JsonBuilder.buildMembersInGroup(group.getName()));
                                }
                            }
                            else if (type.equals("members")) {
                                Log.d("Members: ", message);
                                activity.getController().getGroupHandler().updateMembers(object);
                                activity.runOnUiThread(new UpdateUI());
                            }
                            else if (type.equals("locations")) {
                                String group = object.getString("group");
                                JSONArray groupsArray = object.getJSONArray("location");
                                for (int i = 0; i < groupsArray.length(); i++) {
                                    JSONObject singleGroup = groupsArray.getJSONObject(i);

                                    String userName = singleGroup.getString("member");
                                    String longitude = singleGroup.getString("longitude");
                                    String latitude = singleGroup.getString("latitude");

                                    if (!latitude.equals("NaN") && !longitude.equals("NaN")) {
                                        Log.d("Locations: ", group + " " + userName + " " + latitude + " " + longitude);
                                        activity.runOnUiThread(new UpdateMap(userName, group, latitude, longitude));
                                    }
                                }
                            }
                        }
                    }
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class UpdateUI implements Runnable {
        @Override
        public void run() {
            activity.getController().updateGroupList();
        }
    }

    private class UpdateMap implements Runnable {
        private String userName;
        private String groupName;
        private String latitude;
        private String longitude;

        public UpdateMap(String userName, String groupName, String latitude, String longitude) {
            this.userName = userName;
            this.groupName = groupName;
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public void run() {
            activity.updateMaker(userName, groupName, latitude, longitude);
        }
    }

    private class RemoveMarkers implements Runnable {
        String groupName;

        public RemoveMarkers(String groupName) {
            this.groupName = groupName;
        }

        public void run() {
            activity.removeMarkers(groupName);
        }
    }
}
