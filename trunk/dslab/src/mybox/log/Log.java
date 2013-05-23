package mybox.log;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class Log {
	
	public static void debug(final String output) {
		String debug = System.getenv("MYBOX_DEBUG");
		if(debug != null && debug.equalsIgnoreCase("true")) {
			print(output, LogType.DEBUG);
		}
	}
	
	public static void info(final String output) {
		print(output, LogType.INFORMATION);
	}
	
	public static void warn(final String output) {
		print(output, LogType.WARNING);
	}
	
	public static void error(final String output) {
		print(output, LogType.ERROR);
	}
	
	private static void print(String output, LogType type) {
		output = currentTimestamp() + " " + type.toString() + ": " + output;
		if (type == LogType.ERROR) {
			System.err.println(output);
		} else {
			System.out.println(output);
		}
	}
	
	public static String currentTimestamp() {
		Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		DateFormat f = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
		return f.format(c.getTime());
	}
}
