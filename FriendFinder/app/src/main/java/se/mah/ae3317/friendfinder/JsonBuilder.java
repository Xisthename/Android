package se.mah.ae3317.friendfinder;

import android.util.JsonWriter;
import java.io.IOException;
import java.io.StringWriter;

/**
 * Created by xisth on 2016-10-07.
 */
public class JsonBuilder {
    public static String buildRegister(String group, String name){
        StringWriter stringWriter = new StringWriter();
        JsonWriter writer = new JsonWriter( stringWriter );

        try {
            writer.beginObject().name("type").value("register")
                    .name("group").value(group)
                    .name("member").value(name)
                    .endObject();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return stringWriter.toString();
    }

    public static String buildDeregister(String id){
        StringWriter stringWriter = new StringWriter();
        JsonWriter writer = new JsonWriter(stringWriter);

        try {
            writer.beginObject().name("type").value("unregister")
                    .name("id").value(id)
                    .endObject();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return stringWriter.toString();
    }

    public static String buildMembersInGroup(String group){
        StringWriter stringWriter = new StringWriter();
        JsonWriter writer = new JsonWriter(stringWriter);

        try {
            writer.beginObject().name("type").value("members")
                    .name("group").value(group)
                    .endObject();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return stringWriter.toString();
    }

    public static String buildCurrentGroups(){
        StringWriter stringWriter = new StringWriter();
        JsonWriter writer = new JsonWriter(stringWriter);

        try {
            writer.beginObject().name("type").value("groups")
                    .endObject();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return stringWriter.toString();
    }

    public static String buildSetPosition(String id, double longitude, double latitude){
        StringWriter stringWriter = new StringWriter();
        JsonWriter writer = new JsonWriter(stringWriter);

        try {
            writer.beginObject().name("type").value("location")
                    .name("id").value(id)
                    .name("longitude").value(longitude + "")
                    .name("latitude").value(latitude + "")
                    .endObject();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return stringWriter.toString();
    }
}
