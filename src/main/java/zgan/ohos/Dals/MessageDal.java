package zgan.ohos.Dals;

import org.ksoap2.serialization.SoapObject;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import zgan.ohos.Models.Message;
import zgan.ohos.utils.XmlParser;

/**
 * Created by yajunsun on 2015/12/28.
 */
public class MessageDal {
    public List<Message> GetMessages(String xmlString) {
        try {
// 这里我们实现了本地解析，所以注掉了这个取网络数据的。
//            URL url = new URL(rssUrl);
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            XMLReader reader = parser.getXMLReader();
            XmlParser<Message> handler = new XmlParser<>();
            reader.setContentHandler(handler);
            //this.getClassLoader().getResourceAsStream("rssxml.xml")
            InputSource is = new InputSource(new ByteArrayInputStream(xmlString.getBytes()));//取得本地xml文件
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
}
