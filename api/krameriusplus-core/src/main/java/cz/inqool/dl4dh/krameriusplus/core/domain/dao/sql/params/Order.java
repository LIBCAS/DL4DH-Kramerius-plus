package cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.params;

public enum Order {
    asc,
    desc;

    public com.querydsl.core.types.Order query() {
        return com.querydsl.core.types.Order.valueOf(this.name().toUpperCase());
    }

}
