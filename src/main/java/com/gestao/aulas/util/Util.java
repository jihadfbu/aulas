package com.gestao.aulas.util;

import java.util.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Util {
	
	public static LocalDate ConvStrToLocalDate(String date) {
		
	if(date != null && !date.equals("")){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate newDate = LocalDate.parse(date, formatter);
        return newDate;
	}else {
		return null;
	}
	}
	
	public static String ConvLocalDateToStr(LocalDate date) {
		if (date == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return date.format(formatter);
	}
	
	public static LocalDateTime ConvStrToLocalDateTime(String date) {
		
		if(date != null && !date.equals("")){
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
			LocalDateTime newDate = LocalDateTime.parse(date, formatter);
			return newDate;
		}else {
			return null;
		}
	}
	
	public static String ConvLocalDateTimeToStr(LocalDateTime date) {
		if (date == null) {
			return null;
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		return date.format(formatter);
	}	
	public static String ConvTimeToStr(Timestamp date) {
		if (date == null) {
			return null;
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		return dateFormat.format(date);
	}
	public static String ConvDateToStr(Date date) {
		if (date == null) {
			return null;
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		return dateFormat.format(date);
	}
}
