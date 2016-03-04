package zgan.ohos.Models;

import android.os.Parcelable;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.io.Serializable;
import java.util.Hashtable;

/**
 * Created by yajunsun on 2015/11/24.
 */
public class Event_Product extends BaseModel implements Serializable{
   // private static final long serialVersionUID = getserialVersionUID("Event_Product");
    public Event_Product() {
    }

    public Event_Product(SoapObject soapObject) {
        if (soapObject != null) {
            setId(soapObject.getProperty("Id"));
            if (soapObject.getProperty("Event") != null) {
                zgan.ohos.Models.Event event = new Event((SoapObject) soapObject.getProperty("Event"));
                setEvent(event);
            }
            if (soapObject.getProperty("Product") != null) {
                zgan.ohos.Models.Product product = new Product((SoapObject) soapObject.getProperty("Product"));
                setProduct(product);
            }
        }
    }

    public Event getEvent() {
        return Event;
    }

    public void setEvent(Object value) {
        if (value != null)
            this.Event = (Event) value;
    }

    public int getId() {
        return Id;
    }

    public void setId(Object value) {
        if (value != null)
            Id = Integer.valueOf(value.toString());
    }

    public Product getProduct() {
        return Product;
    }

    public void setProduct(Object value) {
        if (value != null)
            this.Product = (Product) value;
    }

    private int Id;
    private Product Product;
    private Event Event;

    @Override
    public <T> T getnewinstance() {
        return null;
    }
}