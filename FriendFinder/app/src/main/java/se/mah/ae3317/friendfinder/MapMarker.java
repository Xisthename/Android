package se.mah.ae3317.friendfinder;

import com.google.android.gms.maps.model.Marker;

/**
 * Created by xisth on 2016-10-03.
 */
public class MapMarker {
    private Marker marker;
    private String groupName;

    public MapMarker(Marker marker, String groupName) {
        this.marker = marker;
        this.groupName = groupName;
    }

    public Marker getMarker() {
        return marker;
    }

    public String getGroupName() {
        return groupName;
    }
}
