package zgan.ohos.utils;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import zgan.ohos.Models.BaseModel;

/**
 * Created by Administrator on 16-3-3.
 */
public class XmlParser<T extends BaseModel> extends DefaultHandler {

    T model;
    public List<T> list;
    @Override
    public void startDocument() throws SAXException {
        // TODO Auto-generated method stub
        list = new ArrayList<>();
    }

    @Override
    public void endDocument() throws SAXException {
        // TODO Auto-generated method stub

    }

    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        // TODO Auto-generated method stub
        model = model.getnewinstance();
        try {
            Method m = model.getClass().getMethod("set" + localName, Object.class);
            m.invoke("value", qName);
        } catch (Exception e) {
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        // TODO Auto-generated method stub
//        if (localName.equals("item")) {
//            RssFeed.addItem(RssItem);
        // return;
        // }
        list.add(model);
    }

    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        // TODO Auto-generated method stub
        String theString = new String(ch, start, length);

    }
}

