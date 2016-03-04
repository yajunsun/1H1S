package zgan.ohos.Models;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.Hashtable;
import java.util.Objects;

/**
 * Created by yajunsun on 2015/12/28.
 */
public class MessageType extends BaseModel {

   // private static final long serialVersionUID = getserialVersionUID("MessageType");

    public MessageType() {
    }

    public MessageType(SoapObject soapObject) {
        if (soapObject != null) {
            setMsgTypeId(soapObject.getProperty("MsgTypeId"));
            setMsgTypeName(soapObject.getProperty("MsgTypeName"));
            setMsgTypePId(soapObject.getProperty("MsgTypePId"));
        }
    }

    public int getMsgTypeId() {
        return MsgTypeId;
    }

    public void setMsgTypeId(Object value) {
        if (value != null)
            MsgTypeId = Integer.valueOf(value.toString());
    }

    public String getMsgTypeName() {
        return MsgTypeName;
    }

    public void setMsgTypeName(Object value) {
        if (value != null) {
            if (value.toString().length() > 50)
                throw new IndexOutOfBoundsException("消息类型必须在50字内");
            MsgTypeName = value.toString();
        }
    }

    public int getMsgTypePId() {
        return MsgTypePId;
    }

    public void setMsgTypePId(Object value) {
        if (value != null)
            MsgTypePId = Integer.valueOf( value.toString());
    }

    private int MsgTypeId;

    private String MsgTypeName;

    private int MsgTypePId;

    @Override
    public <T> T getnewinstance() {
        return null;
    }
}
