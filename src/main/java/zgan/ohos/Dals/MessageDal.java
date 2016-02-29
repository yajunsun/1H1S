package zgan.ohos.Dals;

import org.ksoap2.serialization.SoapObject;

import java.util.List;

import zgan.ohos.Models.Message;

/**
 * Created by yajunsun on 2015/12/28.
 */
public class MessageDal extends baseDal<Message> {
    public List<Message> GetMessages(int pagesize, int pageindex, int type) throws Exception {
        String SOAP_ACTION = "http://tempuri.org/IEventsContract/GetMessages";
        String MethodName = "GetMessages";
        SoapObject request = new SoapObject(NameSpace, MethodName);
        request.addProperty("pagesize", pagesize);
        request.addProperty("pageindex", pageindex);
        request.addProperty("type", type);
        return getnetobjectlist(new Message(), request, URL, SOAP_ACTION);
    }
}
