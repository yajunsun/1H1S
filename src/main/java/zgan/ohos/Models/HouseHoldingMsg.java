package zgan.ohos.Models;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.Hashtable;

/**
 * Created by yajunsun on 2016/1/13.
 */
public class HouseHoldingMsg extends BaseModel {

    private int Id;
    private String Ffrom;
    private String Fto;
    private String Fcontent;
    private String Fimage;
    private String Ftime;

    public HouseHoldingMsg() {
    }

    public HouseHoldingMsg(SoapObject soapObject) {
        if (soapObject != null) {
            setId(soapObject.getProperty("Id"));
            setFfrom(soapObject.getProperty("Ffrom"));
            setFto(soapObject.getProperty("Fto"));
            setFcontent(soapObject.getProperty("Fcontent"));
            setFimage(soapObject.getProperty("Fimage"));
            setFtime(soapObject.getProperty("Ftime"));
        }
    }

    public int getId() {
        return Id;
    }

    public void setId(Object value) {
        if (value != null)
            Id = Integer.valueOf(value.toString());
    }

    public String getFcontent() {
        return Fcontent;
    }

    public void setFcontent(Object value) {
        if (value != null)
            Fcontent = value.toString();
    }

    public String getFfrom() {
        return Ffrom;
    }

    public void setFfrom(Object value) {
        if (value != null)
            Ffrom = value.toString();
    }

    public String getFimage() {
        return Fimage;
    }

    public void setFimage(Object value) {
        if (value != null)
            Fimage = value.toString();
    }

    public String getFtime() {
        return Ftime;
    }

    public void setFtime(Object value) {
        if (value != null)
            Ftime = value.toString();
    }

    public String getFto() {
        return Fto;
    }

    public void setFto(Object value) {
        if (value != null)
            Fto = value.toString();
    }
    @Override
    public <T> T getnewinstance() {
        return null;
    }
}
