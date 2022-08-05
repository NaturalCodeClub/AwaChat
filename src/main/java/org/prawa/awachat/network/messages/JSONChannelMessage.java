package org.prawa.awachat.network.messages;

import com.google.gson.Gson;
import java.io.Serializable;

public class JSONChannelMessage implements Serializable {
    private static final Gson gson = new Gson();
    private final int version = -1;
    private final String head;
    private final String[] tags;
    private final Object[] data;
    public JSONChannelMessage(String head, String[] tags, Object[] dats){
        this.head = head;
        this.tags = tags;
        this.data = dats;
    }

    public JSONChannelMessage(String head){
        this(head,new String[256],new Object[256]);
    }

    public JSONChannelMessage(String head, int l){
        this(head,new String[l],new Object[l]);
    }

    public void setData(int pos,Object data){
        this.data[pos] = data;
    }

    public void setTag(int pos,String tag){
        this.tags[pos] = tag;
    }

    public String buildJson(){
        return gson.toJson(this);
    }

    public String getHead(){
        return this.head;
    }

    public Object[] getData(){
        return this.data;
    }

    public String[] getTags(){
        return this.tags;
    }

}
