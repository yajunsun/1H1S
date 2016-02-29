package zgan.ohos.Models;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.Hashtable;

/**
 * Created by yajunsun on 2015/12/28.
 */
public class Message extends BaseObject {
    private static final long serialVersionUID = getserialVersionUID("Message");

    public Message() {
    }

    public Message(SoapObject soapObject) {
        if (soapObject != null) {
            setMsgId(soapObject.getProperty("MsgId"));
            setMsgPlace(soapObject.getProperty("MsgPlace"));
            setMsgTitle(soapObject.getProperty("MsgTitle"));
            setMsgAddTime(soapObject.getProperty("MsgAddTime"));
            setMsgAddUId(soapObject.getProperty("MsgAddUId"));
            setMsgPublishTime(soapObject.getProperty("MsgPublishTime"));
            setMsgPublishUId(soapObject.getProperty("MsgPublishUId"));
            setMsgOffTime(soapObject.getProperty("MsgOffTime"));
            setMsgOffUId(soapObject.getProperty("MsgOffUId"));
            setMsgState(soapObject.getProperty("MsgState"));
            setMsgContent(soapObject.getProperty("MsgContent"));

            if (soapObject.getProperty("MsgType") != null) {
                MessageType messageType = new MessageType((SoapObject) soapObject.getProperty("MsgType"));
                setMsgType(messageType);
            }
        }

    }

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

    @Override
    public String gettablename() {
        return getClass().getCanonicalName();
    }

    @Override
    public Message getnewinstance(SoapObject soapObject) {
        return soapObject==null?new Message():new Message(soapObject);
    }

    @Override
    public Object getProperty(int i) {
        switch (i) {
            case 0:
                return MsgId;
            case 1:
                return MsgPlace;
            case 2:
                return MsgTitle;
            case 3:
                return MsgAddTime;
            case 4:
                return MsgAddUId;
            case 5:
                return MsgPublishTime;
            case 6:
                return MsgPublishUId;
            case 7:
                return MsgOffTime;
            case 8:
                return MsgOffUId;
            case 9:
                return MsgState;
            case 10:
                return MsgContent;
            case 11:
                return MsgType;
            default:
                return null;
        }
    }

    @Override
    public int getPropertyCount() {
        return 12;
    }

    @Override
    public void setProperty(int i, Object o) {
        switch (i) {
            case 0:
                setMsgId(o);
                break;
            case 1:
                setMsgPlace(o);
                break;
            case 2:
                setMsgTitle(o);
                break;
            case 3:
                setMsgAddTime(o);
                break;
            case 4:
                setMsgAddUId(o);
                break;
            case 5:
                setMsgPublishTime(o);
                break;
            case 6:
                setMsgPublishUId(o);
                break;
            case 7:
                setMsgOffTime(o);
                break;
            case 8:
                setMsgOffUId(o);
                break;
            case 9:
                setMsgState(o);
                break;
            case 10:
                setMsgContent(o);
                break;
            case 11:
                setMsgType(o);
                break;
        }
    }

    @Override
    public void getPropertyInfo(int i, Hashtable hashtable, PropertyInfo propertyInfo) {
        propertyInfo.namespace = super.NAMESPACE;
        switch (i) {
            case 0:
                propertyInfo.type = PropertyInfo.INTEGER_CLASS;
                propertyInfo.name = "MsgId";
                break;
            case 1:
                propertyInfo.type = PropertyInfo.INTEGER_CLASS;
                propertyInfo.name = "MsgPlace";
                break;
            case 2:
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name = "MsgTitle";
                break;
            case 3:
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name = "MsgAddTime";
                break;
            case 4:
                propertyInfo.type = PropertyInfo.INTEGER_CLASS;
                propertyInfo.name = "MsgAddUId";
                break;
            case 5:
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name = "MsgPublishTime";
                break;
            case 6:
                propertyInfo.type = PropertyInfo.INTEGER_CLASS;
                propertyInfo.name = "MsgPublishUId";
                break;
            case 7:
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name = "MsgOffTime";
                break;
            case 8:
                propertyInfo.type = PropertyInfo.INTEGER_CLASS;
                propertyInfo.name = "MsgOffUId";
                break;
            case 9:
                propertyInfo.type = PropertyInfo.INTEGER_CLASS;
                propertyInfo.name = "MsgState";
                break;
            case 10:
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name = "MsgContent";
                break;
            case 11:
                propertyInfo.type = MessageType.class;
                propertyInfo.name = "MsgType";
                break;
        }
    }
}
