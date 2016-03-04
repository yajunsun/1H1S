package zgan.ohos.Models;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Objects;

/**
 * Created by yajunsun on 2015/11/24.
 */
public class Event extends BaseModel implements Serializable{
    private String ePlace;
    private String eRules;
    private int Id;
    private String bTime;
    private String eTime;
    private int restrict_Members;
    private float pre_Money;

    //private static final long serialVersionUID = getserialVersionUID("Event");
    public Event() {
    }

    public Event(SoapObject soapObject) {
        if (soapObject != null) {
            setId(soapObject.getProperty("Id"));
            setBtime(soapObject.getProperty("bTime"));
            setEtime(soapObject.getProperty("eTime"));
            setEplace(soapObject.getProperty("ePlace"));
            setErules(soapObject.getProperty("eRules"));
            setRestrict_members(soapObject.getProperty("restrict_Members"));
            setPre_money(soapObject.getProperty("pre_Money"));
        }
    }

    public int getId() {
        return Id;
    }

    public void setId(Object id) {
        if (id != null)
            Id = Integer.valueOf(id.toString());
    }

    /**
     * @return 返回 btime。
     */
    public String getBtime() {
        return bTime;
    }

    /**
     * @param value 要设置的 btime。
     */
    public void setBtime(Object value) {
        if (value != null)
            this.bTime = value.toString();
    }

    /**
     * @return 返回 etime。
     */
    public String getEtime() {
        return eTime;
    }

    /**
     * @param value 要设置的 etime。
     */
    public void setEtime(Object value) {
        if (value != null)
            this.eTime = value.toString();
    }

    /**
     * @return 返回 eplace。
     */
    public String getEplace() {
        return ePlace;
    }

    /**
     * @param value 要设置的 eplace。
     */
    public void setEplace(Object value) {
        if (value != null)
            this.ePlace = value.toString();
    }

    /**
     * @return 返回 restrict_members。
     */
    public int getRestrict_members() {
        return restrict_Members;
    }

    /**
     * @param value 要设置的 restrict_members。
     */
    public void setRestrict_members(Object value) {
        if (value != null)
            this.restrict_Members = Integer.valueOf(value.toString());
    }

    /**
     * @return 返回 pre_money。
     */
    public float getPre_money() {
        return pre_Money;
    }

    /**
     * @param value 要设置的 pre_money。
     */
    public void setPre_money(Object value) {

        if (value != null)
            this.pre_Money = Float.valueOf(value.toString());
    }

    /**
     * @return 返回 erules。
     */
    public String getErules() {
        return eRules;
    }

    /**
     * @param value 要设置的 erules。
     */
    public void setErules(Object value) {
        if (value != null)
            this.eRules = value.toString();
    }
    @Override
    public <T> T getnewinstance() {
        return null;
    }
}