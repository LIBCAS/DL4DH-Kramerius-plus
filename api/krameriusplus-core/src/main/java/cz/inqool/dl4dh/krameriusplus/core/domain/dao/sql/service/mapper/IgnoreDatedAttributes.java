package cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.service.mapper;

import org.mapstruct.Mapping;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.CLASS)
@Mapping(target = "created", ignore = true)
@Mapping(target = "updated", ignore = true)
@Mapping(target = "deleted", ignore = true)
public @interface IgnoreDatedAttributes {}
