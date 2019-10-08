package com.example.bot.spring.echo;

import java.io.Writer;

import org.apache.axis.utils.StringUtils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamDriver;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.naming.NoNameCoder;
import com.thoughtworks.xstream.io.xml.CompactWriter;
import com.thoughtworks.xstream.io.xml.Xpp3DomDriver;

public class NSPXmlUtil {

	/**
	 * @notes Created by Tabris <br>
	 *        Created on 2015年8月13日
	 * 
	 * @description 將 xml 轉換成對應的 bean p.s bean 需設定 annotation class
	 *              設定 @XStreamAlias("XX") field 設定 @XStreamAlias("XX")
	 *
	 * @param xml
	 *            : 要轉換的 xml
	 * @param obj
	 *            : 要轉換 bean 的 class
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T parseXmlToBean(String xml, Class<T> obj) {
		T result = null;
		if (xml != null && obj != null) {
			try {
				XStream xstream = new XStream();
				xstream.processAnnotations(obj);
				// Integer 轉換會噴 自訂轉換
				xstream.registerConverter(new Converter() {

					@SuppressWarnings("rawtypes")
					public boolean canConvert(Class type) {
						return type.equals(Integer.class);
					}

					public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
						String value = reader.getValue();
						if (StringUtils.isEmpty(value)) {
							return null;
						}
						return Integer.valueOf(value);
					}

					public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {

					}
				});

				result = (T) xstream.fromXML(xml);
			} catch (Exception e) {

			}
		}
		return result;
	}

	/**
	 * @notes Created by Tabris <br>
	 *        Created on 2015年8月13日
	 * 
	 * @description 將 Bean 轉成 xml p.s bean 需設定 annotation class
	 *              設定 @XStreamAlias("XX") field 設定 @XStreamAlias("XX")
	 * 
	 * @param beanObject
	 *            : 要轉換的 Bean
	 * @param filterTitle
	 *            : 是否移除 xml title
	 * @param newLine
	 *            : 是否換行
	 * @param beanObject
	 * @param filterTitle
	 * @param clazz
	 *            別名 class
	 * @param aliasStr
	 *            別名名稱
	 * @return xml string
	 */
	public static String parseBeanToXml(Object beanObject, boolean filterTitle, boolean newLine, Class<?> clazz,
			String aliasStr) {
		String result = null;
		if (beanObject != null) {
			try {
				HierarchicalStreamDriver domDriver = null;
				NoNameCoder noNameCoder = new NoNameCoder();
				if (newLine) {
					domDriver = new Xpp3DomDriver(noNameCoder);
				} else {
					domDriver = new Xpp3DomDriver(noNameCoder) {
						public HierarchicalStreamWriter createWriter(Writer out) {
							return new CompactWriter(out) {
							};
						}
					};
				}
				// XStream xstream = new XStream(new DomDriver("UTF_8", new
				// NoNameCoder()));
				XStream xstream = new XStream(domDriver);
				xstream.autodetectAnnotations(true);
				if (clazz != null && aliasStr != null && !"".equals(aliasStr)) {
					xstream.alias(aliasStr, clazz);
				}
				result = xstream.toXML(beanObject).replace("__", "_");
				if (!filterTitle) {
					result += "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>" + result;
				}
				// result.replace("\\\r\\\n","").replace("\\\n","").replace(" ",
				// "").replace("__", "_");
			} catch (Exception e) {

			}
		}
		return result;
	}

	public static String parseBeanToXml(Object beanObject, boolean filterTitle) {
		return parseBeanToXml(beanObject, filterTitle, false);
	}

	public static String parseBeanToXml(Object beanObject, boolean filterTitle, boolean newLine) {
		return parseBeanToXml(beanObject, filterTitle, newLine, null, null);
	}
}
