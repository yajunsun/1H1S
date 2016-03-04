package zgan.ohos.Models;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Objects;

/**
 * Created by yajunsun on 2015/11/24.
 */
public class Product extends BaseModel implements Serializable{
    private int Id;
    private String Name;
    private String Description;
    private int Mount;
    private float Price;
    private String Unit;
    private int Instore;
    private ProductType Type;
    //private static final long serialVersionUID = getserialVersionUID("Product");
    public Product() {
    }

    public Product(SoapObject soapObject) {
        if (soapObject != null) {
            setId(soapObject.getProperty("Id"));
            setName(soapObject.getProperty("Name"));
            setDescription(soapObject.getProperty("Description"));
            setMount(soapObject.getProperty("Mount"));
            setPrice(soapObject.getProperty("Price"));
            setUnit(soapObject.getProperty("Unit"));
            setInstore(soapObject.getProperty("Instore"));
            if (soapObject.getProperty("Type")!=null)
            {
                ProductType type=new ProductType((SoapObject)soapObject.getProperty("Type"));
                setType(type);
            }
        }
    }

    /**
     * @return 返回 id。
     */
    public int getId() {
        return Id;
    }

    /**
     * @param value 要设置的 id。
     */
    public void setId(Object value) {
        if (value != null)
            Id = Integer.valueOf(value.toString());
    }

    /**
     * @return 返回 name。
     */
    public String getName() {
        return Name;
    }

    /**
     * @param value 要设置的 name。
     */
    public void setName(Object value) {
        if (value != null)
            this.Name = value.toString();
    }

    /**
     * @return 返回 description。
     */
    public String getDescription() {
        return Description;
    }

    /**
     * @param value 要设置的 description。
     */
    public void setDescription(Object value) {
        if (value != null)
            this.Description = value.toString();
    }

    /**
     * @return 返回 mount。
     */
    public int getMount() {
        return Mount;
    }

    /**
     * @param value 要设置的 mount。
     */
    public void setMount(Object value) {
        if (value != null)
            this.Mount = Integer.valueOf(value.toString());
    }

    /**
     * @return 返回 price。
     */
    public float getPrice() {
        return Price;
    }

    /**
     * @param value 要设置的 price。
     */
    public void setPrice(Object value) {
        if (value != null)
            this.Price = Float.valueOf(value.toString());
    }

    /**
     * @return 返回 unit。
     */
    public String getUnit() {
        return Unit;
    }

    /**
     * @param value 要设置的 unit。
     */
    public void setUnit(Object value) {
        if (value != null)
            this.Unit = value.toString();
    }
    public int getInstore(){return this.Instore;}
    public void setInstore(Object value)
    {
        if (value!=null)
            this.Instore=Integer.valueOf(value.toString());
    }
    public ProductType getType()
    {
        return this.Type;
    }
    public void setType(Object value)
    {
        if (value!=null)
            Type=(ProductType)value;
    }

    @Override
    public <T> T getnewinstance() {
        return null;
    }
}