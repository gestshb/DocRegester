/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spring.util;

import org.hibernate.HibernateException;

import java.sql.Types;

/**
 * A proper dialect for Microsoft SQL Server 2000 and 2005.
 *
 * @author Yuri Sakhno (George1)
 */
public class SQLServerDialect extends org.hibernate.dialect.SQLServerDialect {

    public SQLServerDialect() {
        super();
        registerColumnType(Types.VARCHAR, "nvarchar($l)");
        registerColumnType(Types.CLOB, "nvarchar(max)");
    }

    public String getTypeName(int code, int length, int precision, int scale) throws HibernateException {
        if (code != 2005) {
            return super.getTypeName(code, length, precision, scale);
        } else {
            return "ntext";
        }
    }

}
