package cz.inqool.dl4dh.krameriusplus.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.inqool.dl4dh.krameriusplus.api.Result;
import cz.inqool.dl4dh.krameriusplus.api.request.UserRequestCreateDto;
import cz.inqool.dl4dh.krameriusplus.api.request.UserRequestDto;
import cz.inqool.dl4dh.krameriusplus.api.request.UserRequestFacade;
import cz.inqool.dl4dh.krameriusplus.api.request.UserRequestType;
import cz.inqool.dl4dh.krameriusplus.api.request.message.MessageCreateDto;
import cz.inqool.dl4dh.krameriusplus.api.user.RoleNames;
import cz.inqool.dl4dh.krameriusplus.rest.exceptions.handler.DefaultExceptionHandler;
import cz.inqool.dl4dh.krameriusplus.rest.exceptions.handler.GeneralExceptionHandler;
import cz.inqool.dl4dh.krameriusplus.rest.exceptions.rest.RestExceptionHandler;
import cz.inqool.dl4dh.krameriusplus.rest.exceptions.rest.RestExceptionHandlerManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static cz.inqool.dl4dh.krameriusplus.rest.controller.UserRequestApi.USER_REQUEST_PATH;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(UserRequestApi.class)
@WebMvcTest(UserRequestApi.class)
@ContextConfiguration(classes = {RestExceptionHandler.class,
        RestExceptionHandlerManager.class, GeneralExceptionHandler.class,
        DefaultExceptionHandler.class})
class UserRequestApiTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private UserRequestFacade userRequestFacade;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @WithMockUser(roles = {RoleNames.USER})
    @Test
    void createUserRequest_validRequest_callsFacade() throws Exception {
        UserRequestCreateDto userRequestCreateDto = new UserRequestCreateDto();
        userRequestCreateDto.setType(UserRequestType.ENRICHMENT);
        userRequestCreateDto.setPublicationIds(List.of("publication-1"));
        userRequestCreateDto.setMessage("test");
        MockMultipartFile file = new MockMultipartFile("test", "test.txt",
                "text/plain", "content".getBytes(StandardCharsets.UTF_8));

        MockMultipartFile file2 = new MockMultipartFile("test", "test.txt",
                "text/plain", "content".getBytes(StandardCharsets.UTF_8));

        when(userRequestFacade.createUserRequest(any(UserRequestCreateDto.class), any()))
                .thenReturn(new UserRequestDto());

        mockMvc.perform(multipart(USER_REQUEST_PATH + "/")
                                .file("files", file.getBytes())
                                .file("files", file2.getBytes())
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .content(objectMapper.writeValueAsString(userRequestCreateDto))
                )
                .andExpect(status().isCreated());

        verify(userRequestFacade, times(1)).createUserRequest(any(), argThat(arr -> arr.size() == 2));
    }

    @WithMockUser(roles = {RoleNames.USER})
    @Test
    void createUserRequest_invalidRequest_badRequest() throws Exception {
        UserRequestCreateDto userRequestCreateDto = new UserRequestCreateDto();
        userRequestCreateDto.setType(UserRequestType.ENRICHMENT);
        userRequestCreateDto.setMessage("test");
        MockMultipartFile file = new MockMultipartFile("test", "test.txt",
                "text/plain", "content".getBytes(StandardCharsets.UTF_8));

        MockMultipartFile file2 = new MockMultipartFile("test", "test.txt",
                "text/plain", "content".getBytes(StandardCharsets.UTF_8));

        mockMvc.perform(multipart(USER_REQUEST_PATH + "/")
                        .file("files", file.getBytes())
                        .file("files", file2.getBytes())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(userRequestCreateDto))
                )
                .andExpect(status().isBadRequest());

        verify(userRequestFacade, never()).createUserRequest(any(), any());
    }

    @WithMockUser(roles = {RoleNames.USER})
    @Test
    void listUserRequests_validRequest_callsFacade() throws Exception {
        when(userRequestFacade.listPage(any(Pageable.class), eq(false)))
                .thenReturn(new Result<>(0, 0, 0, new ArrayList<>()));

        mockMvc.perform(get(USER_REQUEST_PATH + "/").with(csrf()))
                .andExpect(status().isOk());

        verify(userRequestFacade, times(1)).listPage(any(Pageable.class), eq(false));
    }

    @WithMockUser(roles = {RoleNames.USER})
    @Test
    void findById_pathVariableSpecified_callsFacade() throws Exception {
        when(userRequestFacade.findById("test")).thenReturn(new UserRequestDto());

        mockMvc.perform(get(USER_REQUEST_PATH + "/test").with(csrf()))
                .andExpect(status().isOk());

        verify(userRequestFacade, times(1)).findById("test");
    }

    @WithMockUser(roles = {RoleNames.USER})
    @Test
    void checkFileAccessible_pathVariablesSpecified_callsFacade() throws Exception {
        when(userRequestFacade.checkFileAccessible("request", "fileId"))
                .thenReturn(true);

        mockMvc.perform(get(USER_REQUEST_PATH + "/test/file/fileId").with(csrf()))
                .andExpect(status().isOk());

        verify(userRequestFacade,
                times(1)).checkFileAccessible("test", "fileId");
    }

    @WithMockUser(roles = {RoleNames.USER})
    @Test
    void createMessage_pathVariableValidBody_callsFacade() throws Exception {
        MessageCreateDto messageCreateDto = new MessageCreateDto();
        messageCreateDto.setMessage("test");
        MockMultipartFile file = new MockMultipartFile("test", "test.txt",
                "text/plain", "content".getBytes(StandardCharsets.UTF_8));

        MockMultipartFile file2 = new MockMultipartFile("test", "test.txt",
                "text/plain", "content".getBytes(StandardCharsets.UTF_8));

        doNothing().when(userRequestFacade).createMessage(eq("test"), any(), any());

        mockMvc.perform(multipart(USER_REQUEST_PATH + "/test/message")
                        .file("files", file.getBytes())
                        .file("files", file2.getBytes())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(messageCreateDto))
                        .with(csrf())
                )
                .andExpect(status().isCreated());

        verify(userRequestFacade,
                times(1))
                .createMessage(eq("test"), any(), argThat(arg -> arg.size() == 2));
    }

    @WithMockUser(roles = {RoleNames.USER})
    @Test
    void createMessage_invalidBody_badRequest() throws Exception {
        MessageCreateDto messageCreateDto = new MessageCreateDto();
        MockMultipartFile file = new MockMultipartFile("test", "test.txt",
                "text/plain", "content".getBytes(StandardCharsets.UTF_8));

        MockMultipartFile file2 = new MockMultipartFile("test", "test.txt",
                "text/plain", "content".getBytes(StandardCharsets.UTF_8));


        mockMvc.perform(multipart(USER_REQUEST_PATH + "/test/message")
                        .file("files", file.getBytes())
                        .file("files", file2.getBytes())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(messageCreateDto))
                        .with(csrf())
                )
                .andExpect(status().isBadRequest());

        verify(userRequestFacade, never()).createMessage(any(), any(), any());
    }
}