package cz.inqool.dl4dh.krameriusplus.core;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@AutoConfigureMockMvc
@ActiveProfiles(profiles = "test")
@SpringBootTest(classes = TestCoreContext.class)
abstract public class CoreBaseTest {
}
