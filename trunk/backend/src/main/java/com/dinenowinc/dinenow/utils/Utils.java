package com.dinenowinc.dinenow.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.dinenowinc.dinenow.model.Hour;
import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.MessageFactory;
import com.twilio.sdk.resource.instance.Message;

public class Utils {

	public static final String ACCOUNT_SID = "ACe43b06a0bb51f04624f7efb57e927a18";
	public static final String AUTH_TOKEN = "ebf698ffd85f188d552223873b77d80d";

	public static final double DUMMY_POINT= 10;

	public static Boolean sendSMSCode(String content,String code,String phone) throws TwilioRestException {
		TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("Body", content+" Key: " + code));
		params.add(new BasicNameValuePair("To", phone));
		params.add(new BasicNameValuePair("From", "+16477242087"));

		MessageFactory messageFactory = client.getAccount().getMessageFactory();
		Message message= messageFactory.create(params);
		System.out.println(message);
		return true;        

	}

	public final static long TENMINUTE = 1 * 60000 *10;
	public final static String TIMEFORMAT1 = "yyyy-MM-dd'T'HH:mm:ss";
	public final static String TIMEFORMAT2 = "yyyy-MM-dd' 'HH:mm:ss";
	public final static String UTC = "UTC";

	public static SimpleDateFormat DATEMORMAT1 = new SimpleDateFormat(TIMEFORMAT1);

	public static void main(String[] args) {

		double l= 1.82;



		int i = (int) l;
		System.out.println(i);
		System.out.println(isValidTimeZone("Asia/Kolkata"));
		System.out.println(convertTimeZones("Asia/Kolkata", "UTC", TIMEFORMAT1));

		SimpleDateFormat formatter = new SimpleDateFormat(TIMEFORMAT1);
		String format = formatter.format(new Date());
		// System.out.println(format);

		final List<String> list = new LinkedList<String>();
		// System.out.println(DateTimeZone.getAvailableIDs());

		Set<String> zoneIds = DateTimeZone.getAvailableIDs();
		for(String zoneId :zoneIds){
			long offset = DateTimeZone.forID(zoneId).getOffset(new DateTime());

			String offsetInMillis = String.format("%02d:%02d", Math.abs(offset / 3600000),
					Math.abs((offset / 60000) % 60));
			offsetInMillis = (offset >= 0 ? "+" : "-") + offsetInMillis; 
			String longName = TimeZone.getTimeZone(zoneId).getDisplayName();

			list.add((zoneId+":("+offsetInMillis+"):"+longName));
		}
		// return list;
	}

	public static Date convertTimeZones(final String fromTimeZoneString, 
			final String toTimeZoneString, final String fromDateTime) {

		final DateTimeZone fromTimeZone = DateTimeZone.forID(fromTimeZoneString);
		final DateTimeZone toTimeZone = DateTimeZone.forID(toTimeZoneString);
		final DateTime dateTime = new DateTime(fromDateTime, fromTimeZone);

		final DateTimeFormatter outputFormatter 
		= DateTimeFormat.forPattern(TIMEFORMAT2).withZone(toTimeZone);

		SimpleDateFormat formatter = new SimpleDateFormat(TIMEFORMAT2);
		Date date = null;
		try {
			date = formatter.parse(outputFormatter.print(dateTime));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return date;
	}

	public static boolean isValidTimeZone(String tz){
		Set<String> zoneIds = DateTimeZone.getAvailableIDs();
		for(String zoneId :zoneIds){
			if(tz.equalsIgnoreCase(zoneId)){
				return true;
			}
		}
		return false;
	}

	public static DateTime newUTCDateTime() {
		return new DateTime(DateTimeZone.UTC);
	}

	//	 static final String DATEFORMAT = "yyyy-MM-dd HH:mm:ss";

	public static Date GetUTCdatetimeAsDate()
	{
		//note: doesn't check for null
		return StringDateToDate(GetUTCdatetimeAsString());
	}

	public static String GetUTCdatetimeAsString()
	{
		final SimpleDateFormat sdf = new SimpleDateFormat(TIMEFORMAT2);
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		final String utcTime = sdf.format(new Date());

		return utcTime;
	}

	public static Date StringDateToDate(String StrDate)
	{
		Date dateToReturn = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat(TIMEFORMAT2);

		try
		{
			dateToReturn = (Date)dateFormat.parse(StrDate);
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}

		return dateToReturn;
	}

	public static long getTimeInLongByDate(Date date) throws ParseException{
		SimpleDateFormat printFormat = new SimpleDateFormat("HH:mm:ss");
		return printFormat.parse(printFormat.format(date)).getTime();
	}

	public static boolean inRange(ArrayList<Hour> hours,String zone){
		for(Hour hour :hours){
			Date serverDate = Utils.convertTimeZones(TimeZone.getDefault().getID(),zone, Utils.DATEMORMAT1.format(new Date()));
			Date restfromDate = Utils.convertTimeZones(Utils.UTC,zone, Utils.DATEMORMAT1.format(hour.getFromTime()));
			Date resttoDate = Utils.convertTimeZones(Utils.UTC,zone, Utils.DATEMORMAT1.format(hour.getToTime()));
			if(!(restfromDate.getTime() <= serverDate.getTime() && resttoDate.getTime() >= (serverDate.getTime()+Utils.TENMINUTE))){
				return false;
			}
		}
		return true;
	}

	public static boolean inRange(ArrayList<Hour> dineInHours, String zone, List<Hour> menuhours) {
		for(Hour hour : dineInHours){
			Date restfromDate = hour.getFromTime();
			Date resttoDate = hour.getToTime();
			for(Hour menuhour :menuhours){
				Date menufromDate = menuhour.getFromTime();
				Date menutoDate = menuhour.getToTime();
				if(!(restfromDate.getTime() <= menufromDate.getTime() && resttoDate.getTime() >= (menutoDate.getTime()))){
					return false;
				}
			}

		}
		return true;
	}
}


