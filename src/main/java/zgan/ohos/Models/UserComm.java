package zgan.ohos.Models;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.Hashtable;

/**
 * Created by yajunsun on 2015/12/10.
 */
public class UserComm extends BaseModel {

    private int Comm_Id;
    private String Comm_Name;

    public int getComm_Id() {
        return Comm_Id;
    }

    public void setComm_Id(Object value) {
        if (value != null)
            Comm_Id = Integer.valueOf(value.toString());
    }

    public String getComm_Name() {
        return Comm_Name;
    }

    public void setComm_Name(Object value) {
        if (value != null)
            Comm_Name = value.toString();
    }

    public int getFComm_Id() {
        return FComm_Id;
    }

    public void setFComm_Id(Object value) {
        if (value != null)
            this.FComm_Id = Integer.valueOf(value.toString());
    }

    public int getHasChild() {
        return HasChild;
    }

    public void setHasChild(Object value) {
        if (value != null)
            HasChild = Integer.valueOf(value.toString());
    }

    private int FComm_Id;
    private int HasChild;

    public UserComm() {
    }

    public UserComm(SoapObject soapObject) {
        if (soapObject != null) {
            setComm_Id(soapObject.getProperty("Comm_Id"));
            setComm_Name(soapObject.getProperty("Comm_Name"));
            setFComm_Id(soapObject.getProperty("FComm_Id"));
            setHasChild(soapObject.getProperty("HasChild"));
        }
    }

    @Override
    public <T> T getnewinstance() {
        return null;
    }
}
