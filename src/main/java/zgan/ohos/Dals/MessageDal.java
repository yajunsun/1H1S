package zgan.ohos.Dals;

import org.ksoap2.serialization.SoapObject;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import zgan.ohos.Models.BaseModel;
import zgan.ohos.Models.Message;
import zgan.ohos.utils.NetUtils;
import zgan.ohos.utils.XmlParser;
import zgan.ohos.utils.XmlParser_model;

/**
 * Created by yajunsun on 2015/12/28.
 */
public class MessageDal extends baseDal<Message>{
    public List<Message> GetMessages(String xmlString) {
        return getModelList(xmlString,new Message());
    }

    public Message GetMessage(String xmlString) {
        return  GetSingleModel(xmlString,new Message());
    }
}
