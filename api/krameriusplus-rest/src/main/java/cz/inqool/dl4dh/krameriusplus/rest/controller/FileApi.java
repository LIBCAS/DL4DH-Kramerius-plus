package cz.inqool.dl4dh.krameriusplus.rest.controller;

import cz.inqool.dl4dh.krameriusplus.api.domain.FileRefDto;
import cz.inqool.dl4dh.krameriusplus.api.export.FileFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@Tag(name = "File", description = "Soubory")
@RestController
@RequestMapping("/api/files")
public class FileApi {

    private FileFacade fileFacade;

    @Operation(summary = "Download file.")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<InputStreamResource> download(@PathVariable("id") String fileRefId) {
        FileRefDto fileRef = fileFacade.getFile(fileRefId);

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + fileRef.getName()+"\"")
                .header("Content-Length", String.valueOf(fileRef.getSize()))
                .contentType(MediaType.parseMediaType(fileRef.getContentType()))
                .body(new InputStreamResource(fileFacade.getFileContent(fileRefId)));
    }

    @Autowired
    public void setFileService(FileFacade fileFacade) {
        this.fileFacade = fileFacade;
    }
}
