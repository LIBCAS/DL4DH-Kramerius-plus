package cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql;

import org.hibernate.Session;
import org.hibernate.tuple.ValueGenerator;

import java.time.Instant;

public class InstantGenerator implements ValueGenerator<Instant> {

    @Override
    public Instant generateValue(Session session, Object owner) {
        return Instant.now();
    }
}
