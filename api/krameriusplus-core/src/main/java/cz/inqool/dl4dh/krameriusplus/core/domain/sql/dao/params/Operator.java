package cz.inqool.dl4dh.krameriusplus.core.domain.sql.dao.params;

import com.querydsl.core.types.Ops;

public enum Operator {

    CONTAINS(Ops.STRING_CONTAINS_IC),
    EQ(Ops.EQ);

    private Ops ops;

    Operator(Ops ops) {
        this.ops = ops;
    }

    public Ops ops() {
        return ops;
    }

}
