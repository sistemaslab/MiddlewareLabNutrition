package com.middleware.provider.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Alejandro cadena
 */
public class DateMapper {
    
    private static final Logger LOGGER = LogManager.getLogger(DateMapper.class);
    
    
    /**
     * @return current XMLGregorianCalendar
     */
    public static XMLGregorianCalendar getNewXMLGregorianCalendar(){
        return dateToXMLGregorianCalendar(new Date());
    }
    
    /**
     * Create a XMLGregorianCalendar with the param values
     * @param year YYYY
     * @param month MM: 1 to 12
     * @param day dd
     * @return XMLGregorianCalendar
     */
    public static XMLGregorianCalendar getNewXMLGregorianCalendar(int year, int month, int day){
        return getNewXMLGregorianCalendar(year, month, day, 0, 0, 0);
    }
    
    /**
     * Create a XMLGregorianCalendar with the param values
     * @param year YYYY
     * @param month MM: 1 to 12
     * @param day dd
     * @param hour HH
     * @param minute mm
     * @param second ss
     * @return XMLGregorianCalendar
     */
    public static XMLGregorianCalendar getNewXMLGregorianCalendar(int year, int month, int day, int hour, int minute, int second){
        Date date = getDate(year, month, day, hour, minute, second);
        return dateToXMLGregorianCalendar(date);
    }
    
    /**
     * Convert java.util.Date to XMLGregorianCalendar
     * @param date java.util.Date
     * @return XMLGregorianCalendar
     */
    public static XMLGregorianCalendar dateToXMLGregorianCalendar(Date date){
        if(date == null){
            return null;
        }
        GregorianCalendar gCalendar = new GregorianCalendar();
        gCalendar.setTime(date);
        XMLGregorianCalendar xmlCalendar = null;
        try {
            xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gCalendar);
        } catch (DatatypeConfigurationException ex) {
            String message = "Error parsing <"+date.toString()+"> to XMLGregorianCalendar";
            LOGGER.error(message, ex);
        }
        return xmlCalendar;
    }
    
    /**
     * Convert XMLGregorianCalendar to java.util.Date
     * @param calendar XMLGregorianCalendar to convert
     * @return java.util.Date
     */
    public static Date xmlGregorianCalendarToDate(XMLGregorianCalendar calendar){
        if(calendar == null) {
            return null;
        }
        return calendar.toGregorianCalendar().getTime();
    }
    
    /**
     * Convert XMLGregorianCalendar to java.sql.Timestamp
     * @param calendar XMLGregorianCalendar to convert
     * @return java.sql.Timestamp
     */
    public static Timestamp xmlGregorianCalendarToTimestamp(XMLGregorianCalendar calendar){
        if(calendar == null) {
            return null;
        }
        return new Timestamp(calendar.toGregorianCalendar().getTimeInMillis());
    }
    
    /**
     * Convert java.sql.Timestamp to XMLGregorianCalendar
     * @param timestamp java.sql.Timestamp to convert
     * @return XMLGregorianCalendar
     */
    public static XMLGregorianCalendar timestampToXMLGregorianCalendar(Timestamp timestamp){
        if(timestamp == null){
            return null;
        }
        return dateToXMLGregorianCalendar(timestampToDate(timestamp));
    }
    
    /**
     * Convert java.sql.Timestamp to java.util.Date
     * @param timestamp java.sql.Timestamp to convert
     * @return java.util.Date
     */
    public static Date timestampToDate(Timestamp timestamp){
        if (timestamp != null){
            return new Date(timestamp.getTime());
        }
        return null;
    }
    
    /**
     * Compares the time values (millisecond offsets from the Epoch) represented
     * by two Xml Calendar objects.
     *
     * @param firstXGCalendar the first Calendar
     * @param secondXGCalendar the second Calendar
     * @return the value 0 if the time represented by the argument
     * firstXGCalendar is equal to the time represented by the argument
     * secondXGCalendar; a value less than 0 if the time of firstXGCalendar
     * is before the time represented by secondXGCalendar; and a value
     * greater than 0 if the time of firstXGCalendar is after the time
     * represented by secondXGCalendar.
     */
    public static int compareXmlGregorianCalendar(
            XMLGregorianCalendar firstXGCalendar,
            XMLGregorianCalendar secondXGCalendar){
        if(secondXGCalendar == null){
            secondXGCalendar = DateMapper.getNewXMLGregorianCalendar();
        }
        if(firstXGCalendar == null){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date()); // Configuramos la fecha que se recibe
            calendar.add(Calendar.YEAR, -2000);
            firstXGCalendar = DateMapper.dateToXMLGregorianCalendar(calendar.getTime());
        }
        GregorianCalendar firstGCalendar = firstXGCalendar.toGregorianCalendar();
        GregorianCalendar secondGCalendar = secondXGCalendar.toGregorianCalendar();
        return firstGCalendar.compareTo(secondGCalendar);
    }
    
    /**
     * Create a date with the param values
     * @param year
     * @param month
     * @param day
     * @return Date
     */
    public static Date getDate(int year, int month, int day){
        Calendar cal = Calendar.getInstance();
        cal.set(year, month-1, day);
        //cal.set(year, month, day);
        return cal.getTime();
    }
    
    /**
     * Create a date with the param values
     * @param year YYYY
     * @param month MM: 1 to 12
     * @param day dd
     * @param hour HH
     * @param minute mm
     * @param second ss
     * @return Date
     */
    public static Date getDate(int year, int month, int day, int hour, int minute, int second){
        Calendar cal = Calendar.getInstance();
        cal.set(year, month-1, day, hour, minute, second);
        //cal.set(year, month, day, hour, minute, second);
        return cal.getTime();
    }
    
    public static Date addMilliSeconds(Date date, int seconds){
        Calendar calendar = toCalendar(date);
        calendar.add(Calendar.MILLISECOND, seconds);
        return calendar.getTime();
    }
    
    public static Date setTime(Date date, int hour, int minute, int second){
        Calendar calendar = toCalendar(date);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), hour, minute, second);
        return calendar.getTime();
    }
    
    public static Date addHours(Date date, int hours){
        Calendar calendar = toCalendar(date);
        calendar.add(Calendar.HOUR, hours);
        return calendar.getTime();
    }
     
    public static Date addDays(Date date, int days){
        Calendar calendar = toCalendar(date);
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }
    
    public static String formatDate(Date date, String format){
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }
    
    public static String dateToYYYYMMDD(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");
        return dateFormat.format(date);
    }
    
    public static Calendar toCalendar(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }
}
