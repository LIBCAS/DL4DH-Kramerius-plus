package cz.inqool.dl4dh.krameriusplus.service.filler.dataprovider;

import cz.inqool.dl4dh.krameriusplus.domain.entity.DomainObject;
import cz.inqool.dl4dh.krameriusplus.dto.DigitalObjectDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Norbert Bodnar
 */
@Service
public class KrameriusProvider {

    private final DataProvider dataProvider;

    private final PublicationAssemblerVisitor assembler;

    @Autowired
    public KrameriusProvider(DataProvider dataProvider, PublicationAssemblerVisitor assembler) {
        this.dataProvider = dataProvider;
        this.assembler = assembler;
    }

    public <E extends DomainObject> E getDigitalObject(String id) {
        DigitalObjectDto<E> digitalObject = dataProvider.getDigitalObject(id);

        return digitalObject.accept(assembler);
    }
}
