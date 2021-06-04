package cz.inqool.dl4dh.krameriusplus.service.filler.dataprovider;

import cz.inqool.dl4dh.krameriusplus.domain.entity.DomainObject;
import cz.inqool.dl4dh.krameriusplus.dto.DigitalObjectDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Takes care of providing digital objects from Kramerius based on ID. It downloads the object with given ID with
 * its full hierarchy of children objects.
 *
 * @author Norbert Bodnar
 */
@Service
public class KrameriusProvider {

    private final DataProvider dataProvider;

    private final KrameriusPublicationAssemblerVisitor assembler;

    @Autowired
    public KrameriusProvider(DataProvider dataProvider, KrameriusPublicationAssemblerVisitor assembler) {
        this.dataProvider = dataProvider;
        this.assembler = assembler;
    }

    public <E extends DomainObject> E getDigitalObject(String id) {
        DigitalObjectDto<E> digitalObject = dataProvider.getDigitalObject(id);

        return digitalObject.accept(assembler);
    }
}
