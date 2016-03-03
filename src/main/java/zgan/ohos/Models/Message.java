package zgan.ohos.Models;

import android.os.Parcelable;

import org.ksoap2.serialization.SoapObject;

import java.io.Serializable;

/**
 * Created by yajunsun on 2015/12/28.
 */
public class Message extends BaseModel {

    /****
     * <msg_id>消息id</msg_id>
     * <title>标题</title>
     * <firsttime>信息发布时间</firsttime>
     * <updatetime>信息更新时间</updatetime>
     * <offtime>消息下架时间</offtime>
     * <author>发布人</author>
     * <msgtype>消息类型</msgtype>
     ****/
    private int MsgId;
    private int MsgPlace;
    private String MsgTitle;
    private String MsgAddTime;
    private int MsgAddUId;
    private String MsgPublishTime;
    private int MsgPublishUId;
    private String MsgOffTime;
    private int MsgOffUId;
    private int MsgState;
    private String MsgContent;
    private MessageType MsgType;

    public Message() {
    }

    public String getMsgAddTime() {
        return MsgAddTime;
    }

    public void setMsgAddTime(Object value) {
        if (value != null)
            MsgAddTime = value.toString();
    }

    public int getMsgAddUId() {
        return MsgAddUId;
    }

    public void setMsgAddUId(Object value) {
        if (value != null)
            MsgAddUId = Integer.valueOf(value.toString());
    }

    public String getMsgContent() {
        return MsgContent;
    }

    public void setMsgContent(Object value) {
        if (value != null)
            MsgContent = value.toString();
    }

    public int getMsgId() {
        return MsgId;
    }

    public void setMsgId(Object value) {
        if (value != null)
            MsgId = Integer.valueOf(value.toString());
    }

    public String getMsgOffTime() {
        return MsgOffTime;
    }

    public void setMsgOffTime(Object value) {
        if (value != null)
            MsgOffTime = value.toString();
    }

    public int getMsgOffUId() {
        return MsgOffUId;
    }

    public void setMsgOffUId(Object value) {
        if (value != null)
            MsgOffUId = Integer.valueOf(value.toString());
    }

    public int getMsgPlace() {
        return MsgPlace;
    }

    public void setMsgPlace(Object value) {
        if (value != null)
            MsgPlace = Integer.valueOf(value.toString());
    }

    public String getMsgPublishTime() {
        return MsgPublishTime;
    }

    public void setMsgPublishTime(Object value) {
        if (value != null)
            MsgPublishTime = value.toString();
    }

    public int getMsgPublishUId() {
        return MsgPublishUId;
    }

    public void setMsgPublishUId(Object value) {
        if (value != null)
            MsgPublishUId = Integer.valueOf(value.toString());
    }

    public int getMsgState() {
        return MsgState;
    }

    public void setMsgState(Object value) {
        if (value != null)
            MsgState = Integer.valueOf(value.toString());
    }

    public String getMsgTitle() {
        return MsgTitle;
    }

    public void setMsgTitle(Object value) {
        if (value != null)
            MsgTitle = value.toString();
    }

    public MessageType getMsgType() {
        return MsgType;
    }

    public void setMsgType(Object value) {
        if (value != null)
            MsgType = (MessageType) value;
    }

    //    @Override
//    public String gettablename() {
//        return getClass().getCanonicalName();
//    }
//
    @Override
    public Message getnewinstance() {
        return new Message();
    }
}
