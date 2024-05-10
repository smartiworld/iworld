package iworld.rpc.utils;

import java.util.Calendar;

/**
 *
 * description:
 * @author liang.xu01
 * @date 2019/11/15 10:38
 * @param
 * @return
 */
public enum TimeTypeEnum {
	/**
	 *
	 */
	YEAR(Calendar.YEAR, "year"),
	MONTH(Calendar.MONTH, "month"),
	DATE(Calendar.DATE, "date"),
	HOUR(Calendar.HOUR, "hour"),
	MINUTE(Calendar.MINUTE, "minute"),
	SECOND(Calendar.SECOND, "second");

	private final int value;
	private final String name;
	
	TimeTypeEnum(int value, String name) {
		this.value = value;
		this.name = name;
	}
	
	public int getValue() {
		return value;
	}
	
	public String getName() {
		return name;
	}
}
