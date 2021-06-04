package cz.inqool.dl4dh.krameriusplus.service.dataaccess;

import cz.inqool.dl4dh.krameriusplus.domain.entity.Publication;

/**
 * @author Norbert Bodnar
 */
public interface DatabasePublicationAssemblerVisitor {

    Publication assemble(String id);
}
