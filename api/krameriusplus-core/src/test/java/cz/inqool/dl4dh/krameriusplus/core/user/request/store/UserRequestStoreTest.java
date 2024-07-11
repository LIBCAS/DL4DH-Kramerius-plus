package cz.inqool.dl4dh.krameriusplus.core.user.request.store;

import cz.inqool.dl4dh.krameriusplus.api.request.ListFilterDto;
import cz.inqool.dl4dh.krameriusplus.api.request.Sort;
import cz.inqool.dl4dh.krameriusplus.api.user.UserRole;
import cz.inqool.dl4dh.krameriusplus.core.CoreBaseTest;
import cz.inqool.dl4dh.krameriusplus.core.user.User;
import cz.inqool.dl4dh.krameriusplus.core.user.UserStore;
import cz.inqool.dl4dh.krameriusplus.core.user.request.entity.UserRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;

import javax.transaction.Transactional;
import java.time.LocalDate;

import static cz.inqool.dl4dh.krameriusplus.api.request.UserRequestState.CREATED;
import static cz.inqool.dl4dh.krameriusplus.api.request.UserRequestState.WAITING_FOR_USER;
import static cz.inqool.dl4dh.krameriusplus.api.request.UserRequestType.ENRICHMENT;
import static cz.inqool.dl4dh.krameriusplus.api.request.UserRequestType.EXPORT;
import static cz.inqool.dl4dh.krameriusplus.core.user.UserProvider.MOCK_USERNAME;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class UserRequestStoreTest extends CoreBaseTest {

    @Autowired
    private UserRequestStore store;

    @Autowired
    private UserStore userStore;

    private User user;

    private User user1;

    @BeforeEach
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void initUsers() {
        store.deleteAll();

        if (!(userStore.count() == 0)) {
            return;
        }

        User user = new User();
        user.setUsername(MOCK_USERNAME);
        user.setRole(UserRole.USER);
        this.user = userStore.save(user);

        User user1 = new User();
        user1.setUsername("test");
        user1.setRole(UserRole.USER);
        this.user1 = userStore.save(user1);
    }

    @Test
    public void filterByType_oneRequestMatches_oneResultReturned() {
        UserRequest enrichment = new UserRequest();
        enrichment.setUser(user);
        enrichment.setState(CREATED);
        enrichment.setType(ENRICHMENT);
        enrichment = store.save(enrichment);

        UserRequest export = new UserRequest();
        export.setUser(user);
        export.setState(CREATED);
        export.setType(EXPORT);
        store.save(export);

        Page<UserRequest> result = store.findAllForUser(Pageable.ofSize(10), ListFilterDto.builder()
                .type(ENRICHMENT).build(), user.getId(), false);

        Assertions.assertThat(result.getTotalElements()).isEqualTo(1);
        Assertions.assertThat(result.getTotalPages()).isEqualTo(1);
        Assertions.assertThat(result.getContent().get(0).getId()).isEqualTo(enrichment.getId());
    }

    @Test
    public void filterByState_oneRequestMatches_oneResultReturned() {
        UserRequest enrichment = new UserRequest();
        enrichment.setUser(user);
        enrichment.setState(WAITING_FOR_USER);
        enrichment.setType(ENRICHMENT);
        enrichment = store.save(enrichment);

        UserRequest export = new UserRequest();
        export.setUser(user);
        export.setState(CREATED);
        export.setType(EXPORT);
        store.save(export);

        Page<UserRequest> result = store.findAllForUser(Pageable.ofSize(10), ListFilterDto.builder()
                .state(WAITING_FOR_USER).build(), user.getId(), false);

        Assertions.assertThat(result.getTotalElements()).isEqualTo(1);
        Assertions.assertThat(result.getTotalPages()).isEqualTo(1);
        Assertions.assertThat(result.getContent().get(0).getId()).isEqualTo(enrichment.getId());
    }

    @Test
    public void filterByYear_oneRequestMatches_oneResultReturned() {
        int year = LocalDate.now().getYear();

        UserRequest enrichment = new UserRequest();
        enrichment.setUser(user);
        enrichment.setState(WAITING_FOR_USER);
        enrichment.setType(ENRICHMENT);
        store.save(enrichment);

        UserRequest export = new UserRequest();
        export.setUser(user);
        export.setState(CREATED);
        export.setType(EXPORT);
        store.save(export);

        Page<UserRequest> result = store.findAllForUser(Pageable.ofSize(10), ListFilterDto.builder()
                .year(year).build(), user.getId(), false);

        Assertions.assertThat(result.getTotalElements()).isEqualTo(2);
        Assertions.assertThat(result.getTotalPages()).isEqualTo(1);
    }

    @Test
    public void filterByUser_multipleRequestMatch_oneResultReturned() {
        UserRequest enrichment = new UserRequest();
        enrichment.setUser(user);
        enrichment.setState(WAITING_FOR_USER);
        enrichment.setType(ENRICHMENT);
        enrichment = store.save(enrichment);

        UserRequest export = new UserRequest();
        export.setUser(user1);
        export.setState(CREATED);
        export.setType(EXPORT);
        store.save(export);

        Page<UserRequest> result = store.findAll(Pageable.ofSize(10), ListFilterDto.builder()
                .username(user.getUsername()).build(), false);

        Assertions.assertThat(result.getTotalElements()).isEqualTo(1);
        Assertions.assertThat(result.getTotalPages()).isEqualTo(1);
        Assertions.assertThat(result.getContent().get(0).getId()).isEqualTo(enrichment.getId());
    }

    @Test
    public void andFilters_oneRequestMatches_oneResultReturned() {
        UserRequest enrichment = new UserRequest();
        enrichment.setUser(user);
        enrichment.setState(CREATED);
        enrichment.setType(ENRICHMENT);
        enrichment = store.save(enrichment);

        UserRequest export = new UserRequest();
        export.setUser(user);
        export.setState(CREATED);
        export.setType(EXPORT);
        store.save(export);

        Page<UserRequest> result = store.findAll(Pageable.ofSize(10), ListFilterDto.builder()
                .rootFilterOperation(ListFilterDto.RootFilterOperation.AND)
                .state(CREATED)
                .type(ENRICHMENT)
                .build(), false);

        Assertions.assertThat(result.getTotalElements()).isEqualTo(1);
        Assertions.assertThat(result.getTotalPages()).isEqualTo(1);
        Assertions.assertThat(result.getContent().get(0).getId()).isEqualTo(enrichment.getId());
    }

    @Test
    public void orFilterSpecified_twoRequestMatch_twoResultsReturned() {
        UserRequest enrichment = new UserRequest();
        enrichment.setUser(user);
        enrichment.setState(CREATED);
        enrichment.setType(ENRICHMENT);
        store.save(enrichment);

        UserRequest export = new UserRequest();
        export.setUser(user);
        export.setState(CREATED);
        export.setType(EXPORT);
        store.save(export);

        Page<UserRequest> result = store.findAll(Pageable.ofSize(10), ListFilterDto.builder()
                .rootFilterOperation(ListFilterDto.RootFilterOperation.OR)
                .state(CREATED)
                .type(ENRICHMENT)
                .build(), false);

        Assertions.assertThat(result.getTotalElements()).isEqualTo(2);
        Assertions.assertThat(result.getTotalPages()).isEqualTo(1);
    }

    @Test
    public void orderByTimestamp_multipleRequestsExist_returnsCorrectOrder() {
        UserRequest enrichment = new UserRequest();
        enrichment.setUser(user);
        enrichment.setState(CREATED);
        enrichment.setType(ENRICHMENT);
        enrichment = store.save(enrichment);

        UserRequest export = new UserRequest();
        export.setUser(user);
        export.setState(CREATED);
        export.setType(EXPORT);
        export = store.save(export);

        Page<UserRequest> result = store.findAll(Pageable.ofSize(10), ListFilterDto.builder()
                .order(Sort.Order.DESC)
                .field(Sort.Field.CREATED)
                .build(), false);

        Assertions.assertThat(result.getTotalElements()).isEqualTo(2);
        Assertions.assertThat(result.getTotalPages()).isEqualTo(1);
        Assertions.assertThat(result.getContent().get(1).getId()).isEqualTo(enrichment.getId());
        Assertions.assertThat(result.getContent().get(0).getId()).isEqualTo(export.getId());
    }
}