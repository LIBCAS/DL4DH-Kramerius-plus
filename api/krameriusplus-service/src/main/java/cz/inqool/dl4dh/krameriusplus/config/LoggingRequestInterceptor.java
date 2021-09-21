package cz.inqool.dl4dh.krameriusplus.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Slf4j
@Component
public class LoggingRequestInterceptor implements ClientHttpRequestInterceptor {

    private final BodyLength bodyLength;

    public LoggingRequestInterceptor(@Value("${logging.interceptor.body:SHORT}") String bodyLength) {
        this.bodyLength = BodyLength.valueOf(bodyLength);
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        traceRequest(request, body);
        ClientHttpResponse response = execution.execute(request, body);
        traceResponse(response);
        return response;
    }

    private void traceRequest(HttpRequest request, byte[] body) {
        log.debug("===========================request begin================================================");
        log.debug("URI         : {}", request.getURI());
        log.debug("Method      : {}", request.getMethod());
        log.debug("Headers     : {}", request.getHeaders());
        log.debug("Request body: {}", getBody(body));
        log.debug("==========================request end================================================");
    }

    private void traceResponse(ClientHttpResponse response) throws IOException {
        StringBuilder inputStringBuilder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getBody(), StandardCharsets.UTF_8));
        String line = bufferedReader.readLine();
        while (line != null) {
            inputStringBuilder.append(line);
            inputStringBuilder.append('\n');
            line = bufferedReader.readLine();
        }
        log.debug("============================response begin==========================================");
        log.debug("Status code  : {}", response.getStatusCode());
        log.debug("Status text  : {}", response.getStatusText());
        log.debug("Headers      : {}", response.getHeaders());
        log.debug("Response body: {}", getBody(inputStringBuilder.toString().getBytes(StandardCharsets.UTF_8)));
        log.debug("=======================response end=================================================");
    }

    private String getBody(byte[] body) {
        String bodyStr;
        if (bodyLength == BodyLength.SHORT && body.length > 1024) {
            bodyStr = new String(Arrays.copyOf(body, 1024), StandardCharsets.UTF_8);
        } else {
            bodyStr = new String(body, StandardCharsets.UTF_8);
        }

        return bodyStr + System.lineSeparator() + "...";
    }

    private enum BodyLength {
        SHORT,
        FULL
    }
}
