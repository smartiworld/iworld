package iworld.rpc.utils;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.time.FastDateFormat;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class DateUtil {
	
	public static final Integer HALFDAY_DEFAULT_HOUR = 4;
	
	/**
	 * 秒/每天
	 */
	public static final long ONE_DAY_SEC = 24 * 60 * 60;
	
	/**
	 * 秒/每小时
	 */
	public static final long ONE_HOUR_SEC = 60 * 60;
	/**
	 * 60秒/每分钟
	 */
	private static final int SECOND_OF_MINUTE = 60;
	/**
	 * 1000毫秒/每秒
	 */
	private static final int MILLION_SECOND_OF_SECOND = 1000;
	/**
	 * 60分钟/小时
	 */
	private static final int MINUTE_OF_HOUR = 60;
	/**
	 * 24小时/每天
	 */
	public static final int HOUR_OF_DAY = 24;
	/**
	 * 毫秒/每天
	 */
	public static final long ONEDAY_MIL = 24 * 60 * 60 * 1000;
	/**
	 * 秒/每天
	 */
	public static final long ONEDAY_SEC = 24 * 60 * 60;
	/**
	 * 毫秒/每小时
	 */
	public static final long ONEHOUR_MIL = 60 * 60 * 1000;
	/**
	 * 秒/每小时
	 */
	public static final long ONEHOUR_SECOND = 60 * 60;
	/**
	 * 日期格式化 yyyy-MM-dd
	 */
	public static final String DEFAULT_PATTERN = "yyyy-MM-dd";
	public static final String DEFAULT_PATTERN_POINT = "yyyy.MM.dd";
	public static final String DEFAULT_PATTERN_PACKAGE = "yyyyMM";
	/**
	 * 日期格式化 yyyy-MM-dd HH:mm
	 */
	public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm";
	/**
	 * 日期格式化 yyyy-MM-dd HH:mm:ss
	 */
	public static final String DATE_SECOND_PATTERN = "yyyy-MM-dd HH:mm:ss";
	/**
	 * 年标记 y
	 */
	public static final String YEAR_FLAG_Y = "y";
	/**
	 * 年标记 year
	 */
	public static final String YEAR_FLAG_YEAR = "year";
	/**
	 * 月标记 M
	 */
	public static final String MONTH_FLAG_M = "M";
	/**
	 * 月标记 month
	 */
	public static final String MONTH_FLAG_MONTH = "month";
	/**
	 * 天标记 d
	 */
	public static final String DAY_FLAG_D = "d";
	/**
	 * 天标记 date
	 */
	public static final String DAY_FLAG_DAY = "date";
	/**
	 * 时标记 h
	 */
	public static final String HOUR_FLAG_H = "h";
	/**
	 * 时标记 hour
	 */
	public static final String HOUR_FLAG_HOUR = "hour";
	/**
	 * 分标记 m
	 */
	public static final String MINUTE_FLAG_M = "m";
	/**
	 * 时标记 minute
	 */
	public static final String MINUTE_FLAG_MINUTE = "minute";
	/**
	 * 分标记 s
	 */
	public static final String SECOND_FLAG_S = "s";
	/**
	 * 时标记 second
	 */
	public static final String SECOND_FLAG_SECOND = "second";
	/**
	 * 时标记 second
	 */
	public static final String STRING_SPLIT = ",";
	/**
	 * 3
	 */
	public static final int THREE = 3;


	/**
	 * ThreadLocal
	 */
	private static final ThreadLocal<HashMap<String, SimpleDateFormat>> CUSTOMER_MAP_THREAD = new
			ThreadLocal<>();

	private static FastDateFormat dateFormat = FastDateFormat.getInstance(DEFAULT_PATTERN);

	private static FastDateFormat dateTimeFormat = FastDateFormat.getInstance(DATE_TIME_PATTERN);

	private static FastDateFormat dateSecondFormat = FastDateFormat.getInstance(DATE_SECOND_PATTERN);

	/**
	 * description:  日期加
	 *
	 * @param date  date
	 * @param type  type
	 * @param value value
	 * @return
	 * @author liang.xu01
	 * @date 2019/11/19 14:15
	 */
	public static Date dateAdd(Date date, String type, int value) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		if (YEAR_FLAG_Y.equals(type.toLowerCase()) || YEAR_FLAG_YEAR.equals(type.toLowerCase())) {
			c.add(Calendar.YEAR, value);
		} else if (MONTH_FLAG_M.equals(type) || MONTH_FLAG_MONTH.equals(type.toLowerCase())) {
			c.add(Calendar.MONTH, value);
		} else if (DAY_FLAG_D.equals(type.toLowerCase())
				|| DAY_FLAG_DAY.equals(type.toLowerCase())) {
			c.add(Calendar.DATE, value);
		} else if (HOUR_FLAG_H.equals(type.toLowerCase())
				|| HOUR_FLAG_HOUR.equals(type.toLowerCase())) {
			c.add(Calendar.HOUR, value);
		} else if (MINUTE_FLAG_M.equals(type) || MINUTE_FLAG_MINUTE.equals(type.toLowerCase())) {
			c.add(Calendar.MINUTE, value);
		} else if (SECOND_FLAG_S.equals(type.toLowerCase())
				|| SECOND_FLAG_SECOND.equals(type.toLowerCase())) {
			c.add(Calendar.SECOND, value);
		}
		return c.getTime();

	}

	/**
	 * description:获取全年12个月
	 *
	 * @param leaseTime leaseTime
	 * @param leaseDays leaseDays
	 * @return
	 * @author liang.xu01
	 * @date 2019/11/19 14:18
	 */
	public static String[] getallyearMonth(Date leaseTime, int leaseDays) {
		List<String> yearList = new ArrayList<String>();
		List<String> monthList = new ArrayList<String>();
		String yearString;
		String monthString;
		String dateString = null;
		StringBuffer sBuffer = new StringBuffer();
		String[] returnResult = new String[THREE];
		for (int i = 0; i < leaseDays; i++) {
			String correctDate = dateFormat.format(dateAdd(leaseTime, DAY_FLAG_D, i));
			String year = correctDate.split("-")[0];
			String month = correctDate.split("-")[1];
			if (!yearList.contains(year)) {
				yearList.add(year);
			}
			if (!monthList.contains(month)) {
				monthList.add(month);
			}
			if (i == leaseDays - 1) {
				dateString += correctDate;
			} else {
				dateString += correctDate + STRING_SPLIT;
			}
		}
		for (String month : monthList) {
			sBuffer.append(month).append(STRING_SPLIT);
		}
		monthString = sBuffer.toString();
		sBuffer.delete(0, sBuffer.length());
		for (String year : yearList) {
			sBuffer.append(year).append(STRING_SPLIT);
		}
		yearString = sBuffer.toString();
		if (monthString.length() > 0
				&& monthString.lastIndexOf(STRING_SPLIT) == monthString.length() - 1) {
			monthString = monthString.substring(0, monthString.length() - 1);
		}
		if (yearString.lastIndexOf(STRING_SPLIT) == yearString.length() - 1) {
			yearString = yearString.substring(0, yearString.length() - 1);
		}
		returnResult[0] = yearString;
		returnResult[1] = monthString;
		returnResult[2] = dateString;
		return returnResult;

	}

	/**
	 * description:获取全年12个月
	 *
	 * @param year     year
	 * @param month    month
	 * @param fromDate fromDate
	 * @param toDate   toDate
	 * @return
	 * @author liang.xu01
	 * @date 2019/11/19 14:19
	 */
	public final static List<String> getRightYearMonth(String[] year,
													   String[] month, String fromDate, String toDate) {
		List<String> dayArray = getRightDay(year, month, fromDate, toDate);
		if (dayArray != null && dayArray.size() > 0) {
			for (int i = 0, len = dayArray.size(); i < len; i++) {
				dayArray.set(i, dayArray.get(i).substring(0, 7));
			}
		}
		return dayArray;
	}

	private final static List<String> getRightDay(String[] year,
												  String[] month, String fromDate, String toDate) {
		String tempFromDate = fromDate.substring(0, 7) + "-01";
		String tempToDate = toDate.substring(0, 7) + "-01";
		List<String> dayArray = new ArrayList<String>();
		for (int i = 0, len = year.length; i < len; i++) {
			for (int j = 0, monthLen = month.length; j < monthLen; j++) {
				dayArray.add(year[i] + "-" + month[j] + "-01");
			}
		}
		String date;
		Long diff = 0L;
		Long diff1 = 0L;
		for (int i = dayArray.size() - 1; i > 0; i--) {
			date = dayArray.get(i);
			diff = getTermDateDiff(date, tempFromDate);
			diff1 = getTermDateDiff(date, tempToDate);
			if (diff < 0 || diff1 > 0) {
				dayArray.remove(i);
			}
		}
		return dayArray;
	}

	public final static Integer getTermMaxLeng(String[] year, String[] month,
											   String fromDate, String toDate) {
		List<String> dayArray = getRightDay(year, month, fromDate, toDate);
		Integer size = 0;
		if (dayArray != null) {
			size = dayArray.size();
		}
		return size;
	}

	private static Long getTermDateDiff(String dateStr, String date1Str) {
		Long diff = 0L;
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date date = format.parse(dateStr);
			Date date1 = format.parse(date1Str);
			diff = date.getTime() - date1.getTime();
		} catch (Exception e) {
			// handle exception
		}
		return diff;
	}


	/**
	 * Description: 日期转化指定格式的字符串型日期
	 *
	 * @param pUtilDate java.util.Date
	 * @param pFormat   日期格式
	 * @return 字符串格式日期
	 * @Version1.0 2012-11-5 上午08:58:44 by 万久卫（jw.wan@zuche.com）
	 */

	public static String date2String(Date pUtilDate, String pFormat) {
		String result = "";
		if (pUtilDate != null) {
			SimpleDateFormat sdf = getSimpleDateFormat(pFormat);
			result = sdf.format(pUtilDate);
		}
		return result;
	}

	/**
	 * Description: 日期转化指定格式的字符串型日期
	 *
	 * @param pUtilDate java.util.Date
	 * @return 字符串格式日期
	 * @Version1.0 2012-11-5 上午08:58:58 by 万久卫（jw.wan@zuche.com）
	 */
	public static String date2String(
			Date pUtilDate) {
		return dateFormat.format(pUtilDate);
	}


	public static String dateTimeToString(Date pUtilDate) {
		if (pUtilDate == null) {
			return "";
		}
		return dateTimeFormat.format(pUtilDate);
	}


	/**
	 * Description: 日期转化指定格式的字符串型日期
	 *
	 * @param pUtilDate java.util.Date
	 * @return 字符串格式日期
	 * @Version1.0 2012-11-5 上午08:58:58 by 万久卫（jw.wan@zuche.com）
	 */
	public static String date2SecondString(
			Date pUtilDate) {
		return dateSecondFormat.format(pUtilDate);
	}

	/**
	 * Description:
	 *
	 * @param date
	 * @param type
	 * @param value
	 * @return
	 * @Version1.0 2012-11-5 上午09:39:21 by 万久卫（jw.wan@zuche.com）
	 */
	public static Date dateAdd2Date(Date date, String type, int value) {
		return dateAdd(date, type, value);
	}


	public static Date dateAdd2Date(Date date, TimeTypeEnum type, int value) {
		return dateAdd(date, type.getName(), value);
	}


	public static Date dateAdd2Date(String dateStr, TimeTypeEnum type, int value) {
		Date date = dateString2Date(dateStr, DEFAULT_PATTERN);
		return dateAdd(date, type.getName(), value);
	}

	/**
	 * Description:
	 *
	 * @param dateStr
	 * @param type
	 * @param value
	 * @param pattern
	 * @return
	 * @Version1.0 2012-11-5 上午09:18:13 by 万久卫（jw.wan@zuche.com）
	 */
	public static Date dateAdd2Date(String dateStr, String type, int value, String pattern) {
		Date date = dateString2Date(dateStr, pattern);
		return dateAdd(date, type, value);

	}

	/**
	 * Description:
	 *
	 * @param dateStr
	 * @param type
	 * @param value
	 * @return
	 * @Version1.0 2012-11-5 上午09:19:59 by 万久卫（jw.wan@zuche.com）
	 */
	public static Date dateAdd2Date(String dateStr, String type, int value) {
		Date date = dateString2Date(dateStr, DEFAULT_PATTERN);
		return dateAdd(date, type, value);

	}

	/**
	 * Description:
	 *
	 * @param date
	 * @param type
	 * @param value
	 * @return
	 * @Version1.0 2012-11-5 上午09:43:47 by 万久卫（jw.wan@zuche.com）
	 */
	public static String dateAdd2String(Date date, String type, int value) {
		Date dateD = dateAdd2Date(date, type, value);
		return date2String(dateD);
	}

	public static String dateAdd2String(String dateStr, TimeTypeEnum type, int value) {
		Date date = dateString2Date(dateStr, DEFAULT_PATTERN);
		Date dateD = dateAdd2Date(date, type, value);
		return date2String(dateD);
	}


	public static String dateAdd2String(Date date, TimeTypeEnum type, int value, String pattern) {
		Date dateD = dateAdd2Date(date, type, value);
		return date2String(dateD, pattern);
	}

	/**
	 * Description:
	 *
	 * @param date
	 * @param type
	 * @param value
	 * @param pattern
	 * @return
	 * @Version1.0 2012-11-5 上午10:01:50 by 万久卫（jw.wan@zuche.com）
	 */
	public static String dateAdd2String(Date date, String type, int value, String pattern) {
		Date dateD = dateAdd2Date(date, type, value);
		return date2String(dateD, pattern);
	}

	/**
	 * Description:
	 *
	 * @param dateStr
	 * @param type
	 * @param value
	 * @param pattern
	 * @return
	 * @Version1.0 2012-11-5 上午09:43:24 by 万久卫（jw.wan@zuche.com）
	 */
	public static String dateAdd2String(String dateStr, String type, int value, String pattern) {
		Date date = dateAdd2Date(dateStr, type, value, pattern);
		return date2String(date);
	}

	/**
	 * Description:
	 *
	 * @param dateStr
	 * @param type
	 * @param value
	 * @return
	 * @Version1.0 2012-11-5 上午09:42:12 by 万久卫（jw.wan@zuche.com）
	 */
	public static String dateAdd2String(String dateStr, String type, int value) {
		Date date = dateAdd2Date(dateStr, type, value);
		return date2String(date);
	}


	/**
	 * Description: 将日期字符串转换成日期型
	 *
	 * @param dateStr
	 * @return
	 * @Version1.0 2012-11-5 上午08:50:21 by 万久卫（jw.wan@zuche.com）
	 */
	public static Date dateString2Date(String dateStr) {
		return dateString2Date(dateStr, DEFAULT_PATTERN);
	}

	/**
	 * Description: 将日期字符串转换成年月日时分秒类型
	 *
	 * @param dateStr
	 * @return
	 * @Version1.0 2012-11-5 上午08:50:21 by 万久卫（jw.wan@zuche.com）
	 */
	public static Date dateString2MinDate(String dateStr) {
		return dateString2Date(dateStr, DATE_TIME_PATTERN);
	}

	/**
	 * Description: 将日期字符串转换成年月日时分秒类型
	 *
	 * @param dateStr
	 * @return
	 * @Version1.0 2012-11-5 上午08:50:21 by 万久卫（jw.wan@zuche.com）
	 */
	public static Date dateString2SecondDate(String dateStr) {
		return dateString2Date(dateStr, DATE_SECOND_PATTERN);
	}

	/**
	 * Description: 将日期字符串转换成指定格式日期
	 *
	 * @param dateStr
	 * @param partner
	 * @return
	 * @Version1.0 2012-11-5 上午08:50:55 by 万久卫（jw.wan@zuche.com）
	 */
	public static Date dateString2Date(String dateStr, String partner) {

		try {
			SimpleDateFormat formatter = getSimpleDateFormat(partner);
			ParsePosition pos = new ParsePosition(0);
			return formatter.parse(dateStr, pos);
		} catch (NullPointerException e) {
			return null;
		}
	}

	private static SimpleDateFormat getSimpleDateFormat(String pattern) {
		SimpleDateFormat simpleDateFormat;
		HashMap<String, SimpleDateFormat> simpleDateFormatMap = CUSTOMER_MAP_THREAD.get();
		if (simpleDateFormatMap != null && simpleDateFormatMap.containsKey(pattern)) {
			simpleDateFormat = simpleDateFormatMap.get(pattern);
		} else {
			simpleDateFormat = new SimpleDateFormat(pattern);
			if (simpleDateFormatMap == null) {
				simpleDateFormatMap = Maps.newHashMap();
			}
			simpleDateFormatMap.put(pattern, simpleDateFormat);
			CUSTOMER_MAP_THREAD.set(simpleDateFormatMap);
		}

		return simpleDateFormat;
	}


	/**
	 * 获取指定日期的周 与 Date .getDay()兼容
	 *
	 * @param date String 日期
	 * @return int 周
	 */
	public static int getWeekOfDate(String date) {
		Date pDate = dateString2Date(date);
		return getWeekOfDate(pDate);
	}

	/**
	 * 获取指定日期的周 与 Date .getDay()兼容
	 *
	 * @param date util.Date日期
	 * @return int 周
	 */
	public static int getWeekOfDate(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.DAY_OF_WEEK) - 1;
	}


	/**
	 * 获取指定日期的年份
	 *
	 * @param pDate util.Date日期
	 * @return int 年份
	 */
	public static int getYearOfDate(Date pDate) {
		Calendar c = Calendar.getInstance();
		c.setTime(pDate);
		return c.get(Calendar.YEAR);
	}

	/**
	 * Description: 获取日期字符串的年份
	 *
	 * @param pDate 字符串日期
	 * @return int 年份
	 * @Version1.0 2012-11-5 上午08:51:51 by 万久卫（jw.wan@zuche.com）
	 */
	public static int getYearOfDate(String pDate) {
		return getYearOfDate(dateString2Date(pDate));
	}

	/**
	 * Description: 获取指定日期的月份
	 *
	 * @param pDate java.util.Date
	 * @return int 月份
	 * @Version1.0 2012-11-5 上午08:52:14 by 万久卫（jw.wan@zuche.com）
	 */
	public static int getMonthOfDate(Date pDate) {
		Calendar c = Calendar.getInstance();
		c.setTime(pDate);
		return c.get(Calendar.MONTH) + 1;
	}

	/**
	 * Description: 获取日期字符串的月份
	 *
	 * @param date 字符串日期
	 * @return int 月份
	 * @Version1.0 2012-11-5 上午08:53:22 by 万久卫（jw.wan@zuche.com）
	 */
	public static int getMonthOfDate(String date) {
		return getMonthOfDate(dateString2Date(date));
	}

	/**
	 * 获取指定日期的日份
	 *
	 * @param pDate util.Date日期
	 * @return int 日份
	 */
	public static int getDayOfDate(Date pDate) {
		Calendar c = Calendar.getInstance();
		c.setTime(pDate);
		return c.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * Description:
	 *
	 * @param dateStr
	 * @return
	 * @Version1.0 2012-11-5 上午10:23:46 by 万久卫（jw.wan@zuche.com）
	 */
	public static String dateAdd2String(String dateStr) {
		return dateAdd2String(dateStr, false);
	}

	/**
	 * Description:
	 *
	 * @param dateStr
	 * @param isAddDay
	 * @return
	 * @Version1.0 2012-11-5 上午10:19:24 by 万久卫（jw.wan@zuche.com）
	 */
	public static String dateAdd2String(String dateStr, boolean isAddDay) {
		String returndateStr = dateStr;
		try {
			if (isAddDay) {
				dateStr = dateAdd2String(dateStr, DAY_FLAG_D, 1);
			}
			Date date = dateString2Date(dateStr);
			int month = getMonthOfDate(date);
			int day = getDayOfDate(date);
			returndateStr = month + "." + day;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return returndateStr;
	}

	/**
	 * 获得指定日期及指定天数之内的所有日期列表
	 *
	 * @param pDate 指定日期 格式:yyyy-MM-dd
	 * @param count 取指定日期后的count天
	 * @return
	 * @throws ParseException
	 */
	public static List<String> getDatePeriodDay(String pDate, int count)
			throws ParseException {
		List<String> v = new ArrayList<String>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(sdf.parse(pDate));
		v.add(sdf.format(calendar.getTime()));

		for (int i = 0; i < count - 1; i++) {
			calendar.add(Calendar.DATE, 1);
			v.add(dateFormat.format(calendar.getTime()));
		}

		return v;
	}


	/**
	 * 获得指定日期内的所有日期列表
	 *
	 * @param sDate 指定开始日期 格式:yyyy-MM-dd
	 * @param sDate 指定开始日期 格式:yyyy-MM-dd
	 * @return String[]
	 * @throws ParseException
	 */
	public static String[] getDatePeriodDay(String sDate, String eDate)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		Calendar calendar2 = Calendar.getInstance();
		calendar.setTime(sdf.parse(sDate));
		long l1 = calendar.getTimeInMillis();
		calendar2.setTime(sdf.parse(eDate));
		long l2 = calendar2.getTimeInMillis();
		// 计算天数
		long days = (l2 - l1) / (HOUR_OF_DAY * MINUTE_OF_HOUR * SECOND_OF_MINUTE * MILLION_SECOND_OF_SECOND) + 1;

		String[] dates = new String[(int) days];
		dates[0] = (dateFormat.format(calendar.getTime()));
		for (int i = 1; i < days; i++) {
			calendar.add(Calendar.DATE, 1);
			dates[i] = (dateFormat.format(calendar.getTime()));
		}
		return dates;
	}

	/**
	 * Description: 得到两个时间差
	 *
	 * @param startTime 开始时间
	 * @param toTime    结束时间
	 * @param pattern   日期格式字符串
	 * @return long 时间差
	 * @Version1.0 2012-11-5 上午09:04:45 by 万久卫（jw.wan@zuche.com）
	 */
	public static long getDateDiff(String startTime, String toTime, String pattern) {
		long diff = getDateDiffLong(startTime, toTime, pattern);
		diff = diff / 1000 / 60;
		return diff;
	}

	/**
	 * Description:
	 *
	 * @param startTime
	 * @param toTime
	 * @param pattern
	 * @return
	 * @Version1.0 2012-11-9 上午10:25:23 by 万久卫（jw.wan@zuche.com）
	 */
	public static long getDateDiffLong(String startTime, String toTime, String pattern) {
		long diff = 0L;
		if (StringUtils.checkEmpty(startTime) != null && StringUtils.checkEmpty(toTime) != null) {
			SimpleDateFormat format = getSimpleDateFormat(pattern);
			ParsePosition pos = new ParsePosition(0);
			Date startTimeD = format.parse(startTime, pos);
			pos.setIndex(0);
			Date toTimeD = format.parse(toTime, pos);
			if (startTimeD != null && toTimeD != null) {
				diff = startTimeD.getTime() - toTimeD.getTime();
			}
		}
		return diff;
	}

	/**
	 * description: 计算两个日期相差天数
	 *
	 * @param startDate 开始日期
	 * @param endDate   结束日期
	 * @return
	 * @author 王海涛（haitao.wang@ucarinc.com）
	 * @date 2019/6/26 16:41
	 */
	public static int getDiffDaysOfDate(Date startDate, Date endDate) {
		long diffMillis = getDiffMillisOfDate(startDate, endDate);
		long diffDays = diffMillis / ONEDAY_MIL;
		return Integer.parseInt(String.valueOf(diffDays));
	}

	/**
	 * description: 计算两个日期相差毫秒数
	 *
	 * @param startDate 开始日期
	 * @param endDate   结束日期
	 * @return
	 * @author 王海涛（haitao.wang@ucarinc.com）
	 * @date 2019/6/26 16:59
	 */
	public static long getDiffMillisOfDate(Date startDate, Date endDate) {
		return endDate.getTime() - startDate.getTime();
	}

	/**
	 * 是否是同一天
	 *
	 * @return
	 */
	public static boolean isSameDate(Date date1, Date date2) {
	    if (date1 == null || date2 == null) {
	        return false;
        }
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date1);
		int year1 = calendar.get(Calendar.YEAR);
		int month1 = calendar.get(Calendar.MONTH);
		int day1 = calendar.get(Calendar.DAY_OF_MONTH);
		calendar.setTime(date2);
		int year2 = calendar.get(Calendar.YEAR);
		int month2 = calendar.get(Calendar.MONTH);
		int day2 = calendar.get(Calendar.DAY_OF_MONTH);
		if (year1 == year2 && month1 == month2 && day1 == day2) {
			return true;
		}
		return false;
	}

	/**
	 * 是否是同一天
	 *
	 * @return
	 */
	public static boolean isSameDate(Calendar calendar1, Calendar calendar2) {

		int year1 = calendar1.get(Calendar.YEAR);
		int month1 = calendar1.get(Calendar.MONTH);
		int day1 = calendar1.get(Calendar.DAY_OF_MONTH);

		int year2 = calendar2.get(Calendar.YEAR);
		int month2 = calendar2.get(Calendar.MONTH);
		int day2 = calendar2.get(Calendar.DAY_OF_MONTH);
		if (year1 == year2 && month1 == month2 && day1 == day2) {
			return true;
		}
		return false;
	}

	/**
	 * 神州租车计算租车天数，超时4小时算一天
	 *
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static int getRentDay(Date startDate, Date endDate) {
        return getRealRentDay(startDate, endDate);
	}

	/**
	  * 获取超时小时数, 半小时内不计算
	  * @author xuliang04
	  * @date 2020/9/7 10:18
	  * @since 1.0
	  * @param pickupDate 取车时间
	  * @param returnDate 还车时间
	  * @return void
	  */
	public static int getOverTime(String pickupDate, String returnDate) {
        if (StringUtils.isBlank(pickupDate) || StringUtils.isBlank(returnDate)) {
            return 0;
        }
        return getOverTime(DateUtil.dateString2Date(pickupDate, DATE_SECOND_PATTERN),
                DateUtil.dateString2Date(returnDate, DATE_SECOND_PATTERN), null);
    }
	
	public static int getRealRentDay(Date startDate, Date endDate) {
		if (startDate == null || endDate == null) {
			return 0;
		}
		long startTime = startDate.getTime();
		long endTime = endDate.getTime();
		long periodTime = (endTime - startTime) / 1000;
		long day = periodTime / ONE_DAY_SEC;
		boolean add = periodTime - ONE_DAY_SEC * day > ONE_HOUR_SEC * 4;
		if (add) {
			day++;
		}
		return day <= 0 ? 1 : (int) day;
	}

    /**
     * 获取超时小时数, 半小时内不计算
     * @author xuliang04
     * @date 2020/9/7 10:18
     * @since 1.0
     * @param pickupDate 取车时间
     * @param returnDate 还车时间
     * @param priceType 计价类型
     * @return void
     */
    public static int getOverTime(Date pickupDate, Date returnDate, Integer priceType) {
        int hour = 0;
        int second;
        long dayDiff = returnDate.getTime() - pickupDate.getTime();
        long diffMod = dayDiff % (ONEDAY_MIL);
        if (diffMod != 0) {
            second = (int) ((returnDate.getTime() - pickupDate.getTime()) / 1000);
            // 不足24小时按一天算，否则超时分钟大于0，计算超时小时
            if (second > 0  && second / (ONEHOUR_SECOND * HOUR_OF_DAY) > 0 ) {
                second = (int) (second % (ONEHOUR_SECOND * HOUR_OF_DAY));
                // 大于0.5h 小于等于4h
                if (second > DateUtil.ONEHOUR_SECOND * 0.5 && second <= DateUtil.ONEHOUR_SECOND * 4) {
                    hour = (second - 1) / (int)ONEHOUR_SECOND + 1;
                }
            }
        }
        return hour;
    }

	/**
	 * 获取超时小时数, 半小时内不计算，一天内计算
	 */
	public static int getOverTimeHour(Date pickupDate, Date returnDate) {
		if (pickupDate == null || returnDate == null) {
			return 0;
		}
		int second;
		int hour = 0;
		second = (int) ((returnDate.getTime() - pickupDate.getTime()) / 1000);
		// 超时分钟大于0，计算超时小时
		if (second > 0) {
			second = (int) (second % (ONEHOUR_SECOND * HOUR_OF_DAY));
			// 大于0.5h 小于等于4h
			if (second > DateUtil.ONEHOUR_SECOND * 0.5 && second <= DateUtil.ONEHOUR_SECOND * 4) {
				hour = (second - 1) / (int)ONEHOUR_SECOND + 1;
			}
		}
		return hour;
	}

	/**
	 * 获取结束时间和起始时间的间隔小时数
	 * @author xuliang04
	 * @date 2020/9/7 10:18
	 * @since 1.0
	 * @param startDate 取车时间
	 * @param endDate 还车时间
	 * @return void
	 */
    public static int getDistanceHours(Date startDate,Date endDate){
		if (startDate == null || endDate == null) {
			return 0;
		}

		long startDateTime = startDate.getTime();
		long endDateTime = endDate.getTime();
		long distanceTimes = (endDateTime - startDateTime);

		return (int) (distanceTimes / (MINUTE_OF_HOUR * SECOND_OF_MINUTE * MILLION_SECOND_OF_SECOND));
	}

	/**
	 * 获取结束时间和起始时间的小时余数
	 * @author xuliang04
	 * @date 2020/9/7 10:18
	 * @since 1.0
	 * @param startDate 取车时间
	 * @param endDate 还车时间
	 * @return void
	 */
	public static int getModHours(Date startDate,Date endDate){
		if (startDate == null || endDate == null) {
			return 0;
		}

		long startDateTime = startDate.getTime();
		long endDateTime = endDate.getTime();
		long distanceTimes = (endDateTime - startDateTime);
		long modTimes = distanceTimes % (ONEDAY_MIL);

		return (int) (modTimes / (MINUTE_OF_HOUR * SECOND_OF_MINUTE * MILLION_SECOND_OF_SECOND));
	}

	/**
	 * 获取超时小时数, 半小时内不计算
	 * @author xuliang04
	 * @date 2020/9/7 10:18
	 * @since 1.0
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @return void
	 */
	public static boolean isOverTime(Date startDate, Date endDate, Integer priceType) {

		long startTimes = startDate.getTime();
		long endTimes = endDate.getTime();
		long distanceTimes = endTimes - startTimes;
		long modTimes = distanceTimes % (ONEDAY_MIL);

		return modTimes > 0 ;
	}

	/**
	 * description: 计算最后一天是否为半日租，第一天不足4小时按一天计算
	 * 半日租计价 8.0 一期去除，改为超时服务费
	 * @param startDate startDate
	 * @param endDate   endDate
	 * @return Boolean
	 * @author liang.xu01
	 * @date 2019/11/16 18:00
	 */
	@Deprecated
    public static Boolean getHalfdayFlag(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            return false;
        }
        long startTime = startDate.getTime();
        long endTime = endDate.getTime();
        long periodTime = (endTime - startTime);
        long day = periodTime / DateUtil.ONEDAY_MIL;
        // 第一天不足4小时按一天计算
        if (day == 0) {
            return false;
        }
        // 正好是整天
        if (periodTime - DateUtil.ONEDAY_MIL * day == 0) {
            return false;
        }
        return periodTime - DateUtil.ONEDAY_MIL * day <= ONEHOUR_MIL * HALFDAY_DEFAULT_HOUR;
    }

	/**
	 * description: 获取报价结束时间
	 * @author 王海涛（haitao.wang@ucarinc.com）
	 * @date 2019-11-26 16:14
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @return 报价结束时间
	 */
	public static Date getEndTime4Calc(Date startDate, Date endDate) {
		if (startDate == null || endDate == null) {
			return null;
		}
		long startTime = startDate.getTime();
		long endTime = endDate.getTime();
		long periodTime = (endTime - startTime);
		long day = periodTime / DateUtil.ONEDAY_MIL;
		// 第一天不足4小时按一天计算
		if (day == 0) {
			return dateAdd2Date(startDate, TimeTypeEnum.DATE, 1);
		}
		// 正好是整天
		if (periodTime - DateUtil.ONEDAY_MIL * day == 0) {
			return endDate;
		}
		// 如果小于等于4小时，则按4小时计算，否则按整天计算
		if (periodTime - DateUtil.ONEDAY_MIL * day <= ONEHOUR_MIL * HALFDAY_DEFAULT_HOUR) {
			return dateAdd2Date(dateAdd2Date(startDate, TimeTypeEnum.DATE, (int)day),
					TimeTypeEnum.HOUR, HALFDAY_DEFAULT_HOUR);
		} else {
			return dateAdd2Date(startDate, TimeTypeEnum.DATE, (int)day + 1);
		}
	}

	public static boolean isdelayThreeDays(Date startDate, Date endDate) {
		long startTime = startDate.getTime();
		long endTime = endDate.getTime();
		long periodTime = endTime - startTime;
		if (periodTime > ONEDAY_MIL * THREE) {
			return false;
		} else {
			return true;
		}
	}

	public static Date getStartOfDate(Date fromDate) {
		Calendar c = Calendar.getInstance();
		c.setTime(fromDate);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}

	/**
	 * 将时间的秒去掉
	 *
	 * @param date
	 * @return
	 */
	public static Date date2MinutesDate(Date date) {
		String dateStr = dateTimeToString(date);
		return dateString2Date(dateStr, DATE_TIME_PATTERN);
	}

	/**
	 * 将时间的时分秒抹掉 yyyy-MM-dd
	 *
	 * @param date
	 * @return
	 */
	public static Date date2DayDate(Date date) {
		String dateStr = date2String(date);
		return dateString2Date(dateStr);
	}

	/**
	 * 比较end时间和begin时间相差是否大于seconds，是，返回true；否，返回，false
	 *
	 * @param end
	 * @param begin
	 * @param seconds
	 * @return
	 */
	public static boolean dateCompare(Date end, Date begin, Long seconds) {
		long between = (end.getTime() - begin.getTime()) / 1000;
		return between > seconds;
	}

	/**
	 * @param end
	 * @param begin
	 * @Description: 日期比较
	 * @Return: boolean
	 * @Author: bin.yang (bin.yang05@ucarinc.com)
	 * @Date: 2019/2/27 11:28
	 */
	public static boolean dateCompare(Date end, Date begin) {
		return end.getTime() >= begin.getTime();
	}

	/**
	 * 目标时间是否在当前开始结束时间内
	 *
	 * @param targetDate
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static boolean isBetweenDate(Date targetDate, Date startDate, Date endDate) {
		return targetDate.getTime() >= startDate.getTime() && targetDate.getTime() <= endDate.getTime();
	}

	/**
	 * 是否是周n
	 *
	 * @param dateTime
	 * @param day
	 * @return
	 */
	public static boolean isWeekDay(String dateTime, int day) {
		Calendar c = Calendar.getInstance();
		c.setTime(DateUtil.dateString2Date(dateTime));
		return c.get(Calendar.DAY_OF_WEEK) == day;
	}

	/**
	 * 是否在周weekday的hour点之后
	 *
	 * @param dateTime 要比较的日期时间
	 * @param weekday  周几
	 * @param hour     HH:mm:ss
	 * @return
	 * @author yongjun.ji
	 * @date 2018/3/27
	 */
	public static boolean afterTime(String dateTime, int weekday, String hour) {
		Calendar instance = Calendar.getInstance();
		instance.setTime(DateUtil.dateString2Date(dateTime));
		int i = instance.get(Calendar.DAY_OF_WEEK);
		if (i == Calendar.SUNDAY) {
			i = Calendar.SATURDAY + 1;
		}
		if (weekday == Calendar.SUNDAY) {
			weekday = Calendar.SATURDAY + 1;
		}
		return (i > weekday) || ((i == weekday) && timeLaterThan(dateTime, hour));
	}

	/**
	 * 是否在周weekday的hour点之前
	 *
	 * @param dateTime 要比较的日期时间
	 * @param weekday
	 * @param hour
	 * @return
	 * @date 2018/3/27
	 */
	public static boolean beforeTime(String dateTime, int weekday, String hour) {
		Calendar instance = Calendar.getInstance();
		instance.setTime(DateUtil.dateString2Date(dateTime));
		int i = instance.get(Calendar.DAY_OF_WEEK);
		if (i == Calendar.SUNDAY) {
			i = Calendar.SATURDAY + 1;
		}
		if (weekday == Calendar.SUNDAY) {
			weekday = Calendar.SATURDAY + 1;
		}
		return (i < weekday) || ((i == weekday) && timeEarlierThan(dateTime, hour));
	}

	/**
	 * 判断dateTime是否早于当天hour时间
	 *
	 * @param dateTime
	 * @param hour     HH:mm:ss
	 * @return
	 */
	public static boolean timeEarlierThan(String dateTime, String hour) {
		Date date = DateUtil.dateString2SecondDate(dateTime);
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		String compareTime = format.format(date);
		return compareTime.compareTo(hour) <= 0;
	}

	/**
	 * 判断dateTime是否晚于当天hour时间
	 *
	 * @param dateTime
	 * @param hour     HH:mm:ss
	 * @return
	 */
	public static boolean timeLaterThan(String dateTime, String hour) {
		Date date = DateUtil.dateString2SecondDate(dateTime);
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		String compareTime = format.format(date);
		return compareTime.compareTo(hour) >= 0;
	}

	/**
	 * 是否在当前时间之前
	 *
	 * @param dateTime
	 * @return
	 */
	public static boolean beforeNow(Date dateTime) {
		Date now = new Date();
		return dateTime.compareTo(now) == -1;
	}
	
	/**
	 * 合并时间 将date 日期和time 时分秒合并一起
	 * @param date
	 * @param time
	 * @return
	 */
	public static Date mergeDate(Date date, Date time) {
		Calendar cDate = Calendar.getInstance();
		cDate.setTime(date);
		Calendar cTime = Calendar.getInstance();
		cTime.setTime(time);
		cDate.set(Calendar.HOUR, cTime.get(Calendar.HOUR));
		cDate.set(Calendar.MINUTE, cTime.get(Calendar.MINUTE));
		cDate.set(Calendar.SECOND, cTime.get(Calendar.SECOND));
		cDate.set(Calendar.AM_PM, cTime.get(Calendar.AM_PM));
		return cDate.getTime();
	}
	
	/**
	 * 获取超时秒数
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static int getOverSecond(Date startDate, Date endDate) {
		if (startDate == null || endDate == null) {
			return 0;
		}
		// 时间差毫秒
		long millionSecond = endDate.getTime() - startDate.getTime();
		if (millionSecond <= 0) {
			return 0;
		}
		long second = millionSecond / MILLION_SECOND_OF_SECOND;
		return (int) (second % ONEDAY_SEC);
	}
	
	/**
	 * 获取超时小时数
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static int getOverHour(Date startDate, Date endDate) {
		if (startDate == null || endDate == null) {
			return 0;
		}
		// 时间差毫秒
		long millionSecond = endDate.getTime() - startDate.getTime();
		if (millionSecond <= 0) {
			return 0;
		}
		long second = millionSecond / MILLION_SECOND_OF_SECOND;
		return (int) ((second - 1) / ONEHOUR_SECOND + 1);
	}
	
	/**
	 * 获取超时小时数
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static int getOverDayHour(Date startDate, Date endDate) {
		int overSecond = getOverSecond(startDate, endDate);
		if (overSecond <= 0) {
			return 0;
		}
		return (int) ((overSecond - 1) / ONEHOUR_SECOND + 1);
	}
	
	public static void main(String[] args) {
		Date startDate = DateUtil.dateString2Date("2021-11-23 10:00:00", DATE_SECOND_PATTERN);
		Date endDate = DateUtil.dateString2Date("2021-11-25 09:00:01", DATE_SECOND_PATTERN);
		System.out.println(getOverDayHour(startDate, endDate));
		System.out.println(getOverHour(startDate, endDate));
	}
	
}
