package cn.pay.quickpay.utils.platform;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader; 
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
 
import javax.xml.bind.JAXBContext; 
import javax.xml.bind.JAXBException; 
import javax.xml.bind.Marshaller; 
import javax.xml.bind.Unmarshaller; 

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

 
/**
 *  
 * @author BYSocket
 * Jaxb2.0 处理Xml与Object转换
 *
 */
public class JaxbObjectAndXmlUtil
{ 
       
    /**
     * @param xmlStr 字符串
     * @param c 对象Class类型
     * @return 对象实例
     */
    @SuppressWarnings("unchecked")
    public static <T> T xml2Object(String xmlStr,Class<T> c)
    { 
        try
        { 
            JAXBContext context = JAXBContext.newInstance(c); 
            Unmarshaller unmarshaller = context.createUnmarshaller(); 
             
            T t = (T) unmarshaller.unmarshal(new StringReader(xmlStr)); 
             
            return t; 
             
        } catch (JAXBException e) {  e.printStackTrace();  return null; } 
         
    } 
       
    /**
     * @param object 对象
     * @return 返回xmlStr
     */
    public static String object2Xml(Object object)
    { 
        try
        {   
            StringWriter writer = new StringWriter();
            JAXBContext context = JAXBContext.newInstance(object.getClass()); 
            Marshaller  marshal = context.createMarshaller();
             
            marshal.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true); // 格式化输出 
            marshal.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");// 编码格式,默认为utf-8 
            marshal.setProperty(Marshaller.JAXB_FRAGMENT, false);// 是否省略xml头信息 
            marshal.setProperty("jaxb.encoding", "utf-8"); 
            marshal.marshal(object,writer);
             
            return new String(writer.getBuffer());
             
        } catch (Exception e) { e.printStackTrace(); return null;}    
         
    } 
    
    /**  
     * Map转换成Xml  将map进行循环   拼接key和value为xml需要的格式样式    需修改
     * @param map  
     * @return  
     */  
    public static String map2Xml(Map<String,Object> map){  
        StringBuffer sb = new StringBuffer("");  
        sb.append("<?xml version=\"1.0\" encoding=\"utf-8\" ?>");  
          
        Set<String> set = map.keySet();  
        for(Iterator<String> it=set.iterator(); it.hasNext();){  
            String key = it.next();  
            Object value = map.get(key);  
            sb.append("<").append(key).append(">");  
            sb.append(value);  
            sb.append("</").append(key).append(">");  
        }  
//        sb.append("</xml>");  
        return sb.toString();  
    }  
    
    
    /**  
     * Xml转换成Map  
     * @param xmlStr  
     * @return  
     */  
    public static Map<String,Object> xml2Map(String xmlStr){  
        Map<String,Object> map = new HashMap<String,Object>();  
        Document doc;  
        try {  
            doc = DocumentHelper.parseText(xmlStr);  
            Element el = doc.getRootElement();  
            map = recGetXmlElementValue(el,map);  
        } catch (DocumentException e) {  
            e.printStackTrace();  
        }  
        return map;  
    }  
    
    /**  
     * 循环解析xml  
     * @param ele  
     * @param map  
     * @return  
     */  
    @SuppressWarnings({ "unchecked" })  
    private static Map<String, Object> recGetXmlElementValue(Element ele, Map<String, Object> map){  
        List<Element> eleList = ele.elements();  
        if (eleList.size() == 0) {  
            map.put(ele.getName(), ele.getTextTrim());  
            return map;  
        } else {  
            for (Iterator<Element> iter = eleList.iterator(); iter.hasNext();) {  
                Element innerEle = iter.next();  
                recGetXmlElementValue(innerEle, map);  
            }  
            return map;  
        }  
    }  

    
    
    public static String readFileByBytes(String fileName) {  
    	String text = "";
    	File myFile=new File(fileName);
        if(!myFile.exists())
        { 
            System.err.println("Can't Find " + fileName);
        }

        try 
        {
            BufferedReader in = new BufferedReader(new FileReader(myFile));
            String str;
            while ((str = in.readLine()) != null) 
            {	
            		text+=str;
//                  System.out.println(str);
            }
            in.close();
        } 
        catch (IOException e) 
        {
            e.getStackTrace();
        }
        return text;
    }  
}
