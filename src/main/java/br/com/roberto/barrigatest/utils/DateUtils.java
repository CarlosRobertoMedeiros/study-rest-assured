package br.com.roberto.barrigatest.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtils {
	
	public static String getDataComDiferencaDias(Integer qtdDias) {
		Calendar cal = GregorianCalendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, qtdDias);
		return getDataFormatada(cal.getTime());
		

	}
	
	public static String getDataFormatada(Date data) {
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		return format.format(data);
	}

}
