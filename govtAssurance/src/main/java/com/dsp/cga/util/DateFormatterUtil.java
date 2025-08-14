package com.dsp.cga.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Locale;

public class DateFormatterUtil {

	private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH);
	
	// Flexible SQL-style formatter: allows 0–6 fractional digits
    private static final DateTimeFormatter FLEXIBLE_SQL_FORMATTER =
        new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd HH:mm:ss")
            .optionalStart()
            .appendFraction(ChronoField.NANO_OF_SECOND, 1, 6, true)
            .optionalEnd()
            .toFormatter(Locale.ENGLISH);

    // ISO_OFFSET_DATE_TIME formatter (handles +05:30 offsets)
    private static final DateTimeFormatter ISO_OFFSET_FORMATTER =
        DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    public static String format(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.format(FORMATTER); // Month will be title case (e.g., Jun)
    }

    public static void main(String[] args) {
        LocalDate exampleDate = LocalDate.of(2025, 7, 31);
        System.out.println(format(exampleDate)); // Output: 10 Jun 2025
    }
    
    /**
     * Format date String to new String
     * @param inputDate original date string (e.g., "2025-07-31")
     * @param inputPattern pattern of the input date (e.g., "yyyy-MM-dd")
     */
    public static String format(String inputDateTime) {
    	if (inputDateTime == null || inputDateTime.trim().isEmpty()) {
            return null;
        }

        try {
            // First try ISO_OFFSET_DATE_TIME
            OffsetDateTime odt = OffsetDateTime.parse(inputDateTime, ISO_OFFSET_FORMATTER);
            return odt.format(FORMATTER);
        } catch (Exception e) {
            // Fallback to SQL-style parsing
            LocalDateTime ldt = LocalDateTime.parse(inputDateTime, FLEXIBLE_SQL_FORMATTER);
            return ldt.format(FORMATTER);
        }
    }
    
    public static LocalDate addDatePlusOne(LocalDate date)
	{
		return date.plusDays(1); //Add 1 Day to toDate filter
	}
    
}
