package cz.inqool.dl4dh.krameriusplus.core.kramerius;

import cz.inqool.dl4dh.krameriusplus.core.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.publication.Publication;
import lombok.Getter;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Component
public class MessengerTestHelper {

    @Getter
    @Autowired
    private MockWebServer mockServer;

    public static void testPublication(Publication publication, String expectedTitle, String expectedRootTitle,
                                 String expectedRootId, String expectedPolicy, boolean expectedPdf) {
        assertThat(publication.getTitle()).isEqualTo(expectedTitle);
        assertThat(publication.getRootTitle()).isEqualTo(expectedRootTitle);
        assertThat(publication.getPolicy()).isEqualTo(expectedPolicy);
        assertThat(publication.getRootId()).isEqualTo(expectedRootId);
        assertThat(publication.isPdf()).isEqualTo(expectedPdf);
    }

    public DigitalObject testAndGetDigitalObject(String digitalObjectResponse, Class<?> expectedClass, String expectedId, KrameriusMessenger krameriusMessenger) {
        mockServer.enqueue(new MockResponse().setResponseCode(200)
                .setBody(digitalObjectResponse)
                .addHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON));

        DigitalObject digitalObject = krameriusMessenger.getDigitalObject("test");

        assertThat(digitalObject.getClass()).isEqualTo(expectedClass);
        assertThat(digitalObject.getId()).isEqualTo(expectedId);

        return digitalObject;
    }

    public List<DigitalObject> testAndGetChildren(String childrenResponse, Class<?> expectedClass, int expectedCount, KrameriusMessenger krameriusMessenger) {
        mockServer.enqueue(new MockResponse().setResponseCode(200)
                .setBody(childrenResponse)
                .addHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON));

        List<DigitalObject> digitalObjects = krameriusMessenger.getDigitalObjectsForParent("test");

        assertThat(digitalObjects.size()).isEqualTo(expectedCount);
        assertThat(digitalObjects.stream().allMatch(object -> object.getClass().equals(expectedClass))).isTrue();

        return digitalObjects;
    }

    public List<DigitalObject> testAndGetChildrenMultipleResponses(List<String> responses, Class<?> expectedClass, int expectedCount, KrameriusMessenger krameriusMessenger) {
        if (responses.size() != expectedCount + 1) {
            throw new IllegalArgumentException("Wrong number of responses, expected: " + (expectedCount + 1) + ", got: " + responses.size());
        }

        responses.forEach(response -> mockServer.enqueue(new MockResponse().setResponseCode(200)
                .setBody(response)
                .addHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON)));

        List<DigitalObject> digitalObjects = krameriusMessenger.getDigitalObjectsForParent("test");

        assertThat(digitalObjects.size()).isEqualTo(expectedCount);
        assertThat(digitalObjects.stream().allMatch(object -> object.getClass().equals(expectedClass))).isTrue();

        return digitalObjects;
    }
}
