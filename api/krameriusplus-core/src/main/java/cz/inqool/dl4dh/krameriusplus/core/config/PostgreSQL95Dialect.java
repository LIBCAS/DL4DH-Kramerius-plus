package cz.inqool.dl4dh.krameriusplus.core.config;

import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.type.LocalDateType;
import org.hibernate.type.LongType;

import java.sql.Types;

/**
 * Custom PostgreSQL dialect for version 9.5 and newer.
 * <p>
 * Provides additional configuration for hibernate to properly create SQL queries.
 */
public class PostgreSQL95Dialect extends org.hibernate.dialect.PostgreSQL95Dialect {

    public PostgreSQL95Dialect() {
        super();

        registerColumnType(Types.NCHAR,    "char(1)");
        registerColumnType(Types.NVARCHAR, "varchar($l)");
        registerColumnType(Types.NCLOB,    "text");

        registerFunction("add_years",   new SQLFunctionTemplate(LocalDateType.INSTANCE, "?1 + interval '1' year * ?2"));
        registerFunction("add_months",  new SQLFunctionTemplate(LocalDateType.INSTANCE, "?1 + interval '1' month * ?2"));
        registerFunction("add_weeks",   new SQLFunctionTemplate(LocalDateType.INSTANCE, "?1 + interval '1' week * ?2"));
        registerFunction("add_days",    new SQLFunctionTemplate(LocalDateType.INSTANCE, "?1 + interval '1' day * ?2"));
        registerFunction("add_hours",   new SQLFunctionTemplate(LocalDateType.INSTANCE, "?1 + interval '1' hour * ?2"));
        registerFunction("add_minutes", new SQLFunctionTemplate(LocalDateType.INSTANCE, "?1 + interval '1' minute * ?2"));
        registerFunction("add_seconds", new SQLFunctionTemplate(LocalDateType.INSTANCE, "?1 + interval '1' second * ?2"));

        registerFunction("diff_years", new SQLFunctionTemplate(LongType.INSTANCE, "EXTRACT(YEAR FROM ?1) - EXTRACT(YEAR FROM ?2)"));
    }
}
