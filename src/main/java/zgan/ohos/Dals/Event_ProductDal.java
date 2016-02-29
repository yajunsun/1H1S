package zgan.ohos.Dals;

import org.ksoap2.serialization.SoapObject;

import java.util.List;

import zgan.ohos.Models.Event;
import zgan.ohos.Models.Event_Product;

/**
 * Created by Administrator on 2015/11/25.
 */
public class Event_ProductDal extends baseDal<Event_Product> {
    public List<Event_Product> getEventProducts() throws Exception {
        String SOAP_ACTION = "http://tempuri.org/IEventsContract/getEventProducts";
        String MethodName = "getEventProducts";
        SoapObject request = new SoapObject(NameSpace, MethodName);
        return getnetobjectlist(new Event_Product(), request, URL, SOAP_ACTION);
    }

    public List<Event_Product> getEventProductByEvent(int EventId) throws Exception {
        String SOAP_ACTION = "http://tempuri.org/IEventsContract/getEventProductByEvent";
        String MethodName = "getEventProductByEvent";
        SoapObject request = new SoapObject(NameSpace, MethodName);
        request.addProperty("EventId", EventId);
        return getnetobjectlist(new Event_Product(), request, URL, SOAP_ACTION);
    }

    public List<Event_Product> getEventProductByProduct(int ProductId) throws Exception {
        String SOAP_ACTION = "http://tempuri.org/IEventsContract/getEventProductByProduct";
        String MethodName = "getEventProductByProduct";
        SoapObject request = new SoapObject(NameSpace, MethodName);
        request.addProperty("ProductId", ProductId);
        return getnetobjectlist(new Event_Product(), request, URL, SOAP_ACTION);
    }

    public List<Event_Product> getCurrentEventsProducts(String datestr) throws Exception {
        String SOAP_ACTION = "http://tempuri.org/IEventsContract/getCurrentEventsProducts";
        String MethodName = "getCurrentEventsProducts";
        SoapObject request = new SoapObject(NameSpace, MethodName);
        request.addProperty("datestr", datestr);
        return getnetobjectlist(new Event_Product(), request, URL, SOAP_ACTION);
    }

    public List<Event_Product> getPreEventsProducts(String datestr) throws Exception {
        String SOAP_ACTION = "http://tempuri.org/IEventsContract/getPreEventsProducts";
        String MethodName = "getPreEventsProducts";
        SoapObject request = new SoapObject(NameSpace, MethodName);
        request.addProperty("datestr", datestr);
        return getnetobjectlist(new Event_Product(), request, URL, SOAP_ACTION);
    }
}