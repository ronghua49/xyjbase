package com.xyjsoft.core.query;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;

import java.io.File;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class QueryUtils {
	  protected static final Logger LOGGER = LoggerFactory.getLogger(QueryUtils.class);
	  private static ApplicationContext context;

	  public void setApplicationContext(ApplicationContext _context)
	    throws BeansException
	  {
	    context = _context;
	  }

	  public static ApplicationContext getApplicaitonContext()
	  {
	    return context;
	  }

	  public static List<Class> getImplClass(Class clazz)
	    throws ClassNotFoundException
	  {
	    List list = new ArrayList();

	    Map map = context.getBeansOfType(clazz);
	    for (Iterator localIterator = map.values().iterator(); localIterator.hasNext(); ) { Object obj = localIterator.next();
	      String name = obj.getClass().getName();
	      int pos = name.indexOf("$$");
	      if (pos > 0) {
	        name = name.substring(0, name.indexOf("$$"));
	      }
	      Class cls = Class.forName(name);

	      list.add(cls);
	    }
	    return list;
	  }

	  public static Map<String, Object> getImplInstance(Class clazz)
	    throws ClassNotFoundException
	  {
	    Map map = context.getBeansOfType(clazz);
	    return map;
	  }

	  public static void publishEvent(ApplicationEvent event)
	  {
	    if (context != null)
	      context.publishEvent(event);
	  }

	  public static void publishEvent(Object var)
	  {
	    if (context != null)
	      context.publishEvent(var);
	  }

	  public static String getClasspath()
	  {
	    String classPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
	    String rootPath = "";

	    if ("\\".equals(File.separator)) {
	      rootPath = classPath.substring(1);
	      rootPath = rootPath.replace("/", "\\");
	    }

	    if ("/".equals(File.separator)) {
	      rootPath = classPath.substring(1);
	      rootPath = rootPath.replace("\\", "/");
	    }
	    return rootPath;
	  }
	  
	  public static boolean isEmpty(Object o)
	  {
	    if (o == null)
	      return true;
	    if ((o instanceof String)) {
	      if (((String)o).trim().length() == 0)
	        return true;
	    } else if ((o instanceof Collection)) {
	      if (((Collection)o).size() == 0)
	        return true;
	    } else if (o.getClass().isArray()) {
	      if (((Object[])(Object[])o).length == 0)
	        return true;
	    } else if ((o instanceof Map)) {
	      if (((Map)o).size() == 0)
	        return true;
	    } else {
	      if ((o instanceof Serializable)) {
	        return ((Serializable)o).toString().trim().length() == 0;
	      }
	      if ((o instanceof ArrayNode)) {
	        ArrayNode an = (ArrayNode)o;
	        return an.isEmpty(null);
	      }
	    }
	    return false;
	  }

	public static boolean isNotEmpty(Object value) {
		return !isEmpty(value);
	}

	public static boolean isNotEmpty(String str)
	  {
	    return !isEmpty(str);
	  }

	  public static boolean isEmpty(String str)
	  {
	    if (str == null) {
	      return true;
	    }
	    return str.trim().equals("");
	  }

	  public static boolean isEmpty(Long value)
	  {
	    if (value == null) {
	      return true;
	    }
	    return value.longValue() == 0L;
	  }
	
	public static String format(LocalDateTime date, String style) {
	    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(style);
	    return date.format(dateFormat);
	}
	
	public static String convertArrayToString(String[] arr) {
	    return convertArrayToString(arr, ",");
	}

	public static String convertArrayToString(String[] arr, String split) {
	    if ((arr == null) || (arr.length == 0))
	      return "";
	    StringBuffer sb = new StringBuffer();
	    for (int i = 0; i < arr.length; i++) {
	      sb.append(arr[i]);
	      sb.append(split);
	    }
	    return sb.substring(0, sb.length() - split.length());
	}
}
