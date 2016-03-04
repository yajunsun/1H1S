package zgan.ohos.Models;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.io.Serializable;
import java.util.Hashtable;

/**
 * Created by yajunsun on 2015/11/24.
 */
public class Product_Pics extends BaseModel implements Serializable {
    //private static final long serialVersionUID = getserialVersionUID("Product_Pics");

    public Product_Pics() {
    }

    public Product_Pics(SoapObject soapObject) {
        if (soapObject != null) {
            setId(soapObject.getProperty("Id"));
            setProductId(soapObject.getProperty("ProductId"));
            setPicName(soapObject.getProperty("PicName"));
        }
    }

    private int Id;

    private int ProductId;

    public int getId() {
        return Id;
    }

    public void setId(Object id) {
        if (id != null)
            Id = Integer.valueOf(id.toString());
    }

    public String getPicName() {
        return PicName;
    }

    public void setPicName(Object
                                   value) {
        if (value != null)
            PicName = value.toString();
    }

    public int getProductId() {
        return ProductId;
    }

    public void setProductId(Object value) {
        if (value != null)
            ProductId = Integer.valueOf(value.toString());
    }

    private String PicName;

    @Override
    public <T> T getnewinstance() {
        return null;
    }
}