package com.greco.cablemodem.cablemodemutils;


import org.apache.commons.lang3.StringUtils;
import org.htmlcleaner.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class CablemodemutilsApplication implements CommandLineRunner {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public static void main(String[] args) {

        SpringApplication.run(CablemodemutilsApplication.class, args);
    }

    public void run(String[] args) throws Exception {

        try{
            logger.info("Channel,Channel ID,Frequency,Width,Power");
            while (true) {
                TagNode tagNode = new HtmlCleaner().clean(new URL("http://192.168.100.1/"));
                org.w3c.dom.Document doc = new DomSerializer(new CleanerProperties()).createDOM(tagNode);
                XPath xpath = XPathFactory.newInstance().newXPath();
                String str = (String) xpath.evaluate("//*[@id=\"bg3\"]/div[2]/div[3]/center[3]/table", doc, XPathConstants.STRING);
                String split[];
                split = str.split("\n");
                int counter = 0;

                StringBuilder sb = new StringBuilder();
                for (String s : split) {
                    String sTrim = StringUtils.trimToNull(s);
                    if (sTrim != null && Character.isDigit(sTrim.charAt(0))) {
                        counter++;
                        sb.append(sTrim);
                        if (counter == 5) {
                            counter = 0;
                            logger.info(sb.toString());
                            sb.delete(0, sb.length());
                        } else {
                            sb.append(",");
                        }
                    }
                }
                Thread.sleep(300000);
            }
        }
        catch (Exception e){
            logger.error(e.getMessage());
        }
    }

}
