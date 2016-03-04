package zgan.ohos.Dals;


import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import zgan.ohos.Models.BaseModel;
import zgan.ohos.utils.NetUtils;
import zgan.ohos.utils.XmlParser;
import zgan.ohos.utils.XmlParser_model;

public class baseDal<T extends BaseModel> {

    /****************
     * sunyajun
     ****************/
    public List<T> getModelList(String xmlString,T Modelinstance)
    {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            XMLReader reader = parser.getXMLReader();
            XmlParser<T> handler = new XmlParser<>(Modelinstance);
            reader.setContentHandler(handler);
            StringReader read = new StringReader(NetUtils.buildXMLfromNetData(xmlString));
            InputSource is = new InputSource(read);
            reader.parse(is);
            return handler.list;
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public T GetSingleModel(String xmlString,T modelInstance) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            XMLReader reader = parser.getXMLReader();
            XmlParser_model<T> handler = new XmlParser_model<>(modelInstance);
            reader.setContentHandler(handler);
            StringReader read = new StringReader(NetUtils.buildXMLfromNetData(xmlString));
            InputSource is = new InputSource(read);
            reader.parse(is);
            return handler.modelInstance;
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
