package cz.inqool.dl4dh.krameriusplus.api.user.request.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@Validated
@AllArgsConstructor
@NoArgsConstructor
public class MessageCreateDto {

    @NotNull
    @NotBlank
    private String message;

    private List<MultipartFile> files;
}
