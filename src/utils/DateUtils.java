package utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;

public class DateUtils {
    
    // Common date formats
    public static final String DATE_FORMAT = "dd/MM/yyyy";
    public static final String DATETIME_FORMAT = "dd/MM/yyyy HH:mm:ss";
    public static final String TIME_FORMAT = "HH:mm:ss";
    public static final String DISPLAY_DATE_FORMAT = "dd MMM yyyy";
    public static final String DISPLAY_DATETIME_FORMAT = "dd MMM yyyy, HH:mm:ss";
    
    /**
     * Formats date to string using default format (dd/MM/yyyy)
     */
    public static String formatDate(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        return formatter.format(date);
    }
    
    /**
     * Formats date to string using specified format
     */
    public static String formatDate(Date date, String format) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }
    
    /**
     * Formats date and time to string using default format
     */
    public static String formatDateTime(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat formatter = new SimpleDateFormat(DATETIME_FORMAT);
        return formatter.format(date);
    }
    
    /**
     * Formats date and time for display (more readable format)
     */
    public static String formatDateTimeForDisplay(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat formatter = new SimpleDateFormat(DISPLAY_DATETIME_FORMAT);
        return formatter.format(date);
    }
    
    /**
     * Formats date for display (more readable format)
     */
    public static String formatDateForDisplay(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat formatter = new SimpleDateFormat(DISPLAY_DATE_FORMAT);
        return formatter.format(date);
    }
    
    /**
     * Formats time only
     */
    public static String formatTime(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat formatter = new SimpleDateFormat(TIME_FORMAT);
        return formatter.format(date);
    }
    
    /**
     * Gets current date
     */
    public static Date getCurrentDate() {
        return new Date();
    }
    
    /**
     * Gets current date as formatted string
     */
    public static String getCurrentDateString() {
        return formatDate(new Date());
    }
    
    /**
     * Gets current date and time as formatted string
     */
    public static String getCurrentDateTimeString() {
        return formatDateTime(new Date());
    }
    
    /**
     * Adds days to a given date
     */
    public static Date addDays(Date date, int days) {
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, days);
        return cal.getTime();
    }
    
    /**
     * Adds months to a given date
     */
    public static Date addMonths(Date date, int months) {
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, months);
        return cal.getTime();
    }
    
    /**
     * Adds years to a given date
     */
    public static Date addYears(Date date, int years) {
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, years);
        return cal.getTime();
    }
    
    /**
     * Checks if two dates are on the same day
     */
    public static boolean isSameDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return false;
        }
        
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
               cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }
    
    /**
     * Gets the difference in days between two dates
     */
    public static long getDaysDifference(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            return 0;
        }
        
        long diffInMillies = Math.abs(endDate.getTime() - startDate.getTime());
        return diffInMillies / (24 * 60 * 60 * 1000);
    }
    
    /**
     * Gets start of day for a given date (00:00:00)
     */
    public static Date getStartOfDay(Date date) {
        if (date == null) {
            return null;
        }
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
    
    /**
     * Gets end of day for a given date (23:59:59)
     */
    public static Date getEndOfDay(Date date) {
        if (date == null) {
            return null;
        }
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }
    
    /**
     * Checks if a date is today
     */
    public static boolean isToday(Date date) {
        return isSameDay(date, new Date());
    }
    
    /**
     * Gets a user-friendly time difference string
     */
    public static String getTimeAgoString(Date date) {
        if (date == null) {
            return "";
        }
        
        long diff = System.currentTimeMillis() - date.getTime();
        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        
        if (seconds < 60) {
            return "Just now";
        } else if (minutes < 60) {
            return minutes + " minute" + (minutes == 1 ? "" : "s") + " ago";
        } else if (hours < 24) {
            return hours + " hour" + (hours == 1 ? "" : "s") + " ago";
        } else if (days < 7) {
            return days + " day" + (days == 1 ? "" : "s") + " ago";
        } else {
            return formatDateForDisplay(date);
        }
    }
}