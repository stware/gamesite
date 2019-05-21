package it.sturrini.common;

import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;

public class DateUtils {

	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	public static SimpleDateFormat minuteSecond = new SimpleDateFormat("mmss");

	public static Long nowInMillis() {
		OffsetDateTime utc = OffsetDateTime.now(ZoneOffset.UTC);
		Date date = Date.from(utc.toInstant());
		return utc.toEpochSecond() * 1000;
	}

	public static String print(Long millis) {
		return sdf.format(new Date(millis));
	}

	public static String getMinuteSecond(Long millis) {
		Date date = new Date(millis);
		return minuteSecond.format(date);
	}
}
