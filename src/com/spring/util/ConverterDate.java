/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spring.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 *
 * @author pc
 */
public class ConverterDate {

    public static Date getDate(LocalDate localDate) {
        if (localDate != null) {
            Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
            Date date = Date.from(instant);
            return date;
        }
        return null;
    }

    public static LocalDate getLocalDate(java.util.Date date) {
        if (date != null) {
            Instant instant = Instant.ofEpochMilli(date.getTime()); //date.toInstant();
            LocalDate localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
            return localDate;
        }
        return null;
    }

}
