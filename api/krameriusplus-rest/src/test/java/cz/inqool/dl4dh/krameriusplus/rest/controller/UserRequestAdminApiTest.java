package cz.inqool.dl4dh.krameriusplus.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.inqool.dl4dh.krameriusplus.api.request.UserRequestFacade;
import cz.inqool.dl4dh.krameriusplus.api.request.UserRequestState;
import cz.inqool.dl4dh.krameriusplus.api.request.document.ChangeDocumentStatesDto;
import cz.inqool.dl4dh.krameriusplus.api.request.document.DocumentState;
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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static cz.inqool.dl4dh.krameriusplus.rest.controller.UserRequestAdminApi.USER_REQUEST_ADMIN_PATH;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(UserRequestAdminApi.class)
@WebMvcTest(UserRequestAdminApi.class)
@ContextConfiguration(classes = {RestExceptionHandler.class,
        RestExceptionHandlerManager.class, GeneralExceptionHandler.class,
        DefaultExceptionHandler.class})
class UserRequestAdminApiTest {

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

    @WithMockUser(roles = {RoleNames.ADMIN})
    @Test
    void callChangeState_userIsAdmin_facadeCalled() throws Exception {
        when(userRequestFacade.changeRequestState(any(), any(), anyBoolean())).thenReturn(true);

        mockMvc.perform(put(USER_REQUEST_ADMIN_PATH + "/test")
                        .param("state", "CREATED")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk());

        verify(userRequestFacade, times(1)).changeRequestState("test", UserRequestState.CREATED, false);
    }

    @WithMockUser(roles = {RoleNames.ADMIN})
    @Test
    void callChangeDocumentState_userIsAdmin_facadeCalled() throws Exception {
        ChangeDocumentStatesDto dto = new ChangeDocumentStatesDto();
        dto.setState(DocumentState.APPROVED);
        dto.setPublicationIds(List.of("test"));

        when(userRequestFacade.changeDocumentState(any(), any(), any(), anyBoolean()))
                .thenReturn(true);

        mockMvc.perform(put(USER_REQUEST_ADMIN_PATH + "/test/document")
                        .content(objectMapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk());

        verify(userRequestFacade, times(1)).changeDocumentState(eq("test"),
                argThat(list -> list.size() == 1),
                eq(DocumentState.APPROVED), eq(false));
    }

    @WithMockUser(roles = {RoleNames.ADMIN})
    @Test
    void callChangeDocumentState_invalidRequest_badRequest() throws Exception {
        ChangeDocumentStatesDto dto = new ChangeDocumentStatesDto();
        dto.setState(DocumentState.APPROVED);
        dto.setPublicationIds(List.of());

        mockMvc.perform(put(USER_REQUEST_ADMIN_PATH + "/test")
                        .content(objectMapper.writeValueAsString(dto))
                        .param("state", "CREATED")
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }
}