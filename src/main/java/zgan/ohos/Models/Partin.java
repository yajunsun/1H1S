package zgan.ohos.Models;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.io.Serializable;
import java.util.Hashtable;

/**
 * Created by yajunsun on 2015/11/24.
 */
public class Partin extends BaseModel implements Serializable{
    //private static final long serialVersionUID = getserialVersionUID("Partin");
    public Partin() {
    }

    public Partin(SoapObject soapObject) {
        if (soapObject != null) {
            setId(soapObject.getProperty("Id"));
            setEventId(soapObject.getProperty("EventId"));
            setPhone(soapObject.getProperty("Phone"));
            setAdress(soapObject.getProperty("Adress"));
        }
    }

    public int Id;

    public String getAdress() {
        return Adress;
    }

    public void setAdress(Object value) {
        if (value != null)
            Adress = value.toString();
    }

    public int getEventId() {
        return EventId;
    }

    public void setEventId(Object value) {
        if (value != null)
            EventId = Integer.valueOf( value.toString());
    }

    public int getId() {
        return Id;
    }

    public void setId(Object id) {
        if (id != null)
            Id = Integer.valueOf(id.toString());
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(Object value) {
        if (value != null)
            Phone = value.toString();
    }

    public int EventId;

    public String Phone;

    public String Adress;

    @Override
    public <T> T getnewinstance() {
        return null;
    }
}