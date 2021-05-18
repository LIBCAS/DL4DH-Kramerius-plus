package cz.inqool.dl4dh.krameriusplus;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

/**
 * @author Norbert Bodnar
 */
@SpringBootConfiguration
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
@ComponentScan(basePackages = "cz.inqool.dl4dh.krameriusplus")
@EnableAspectJAutoProxy
public class EnricherApplicationContext {

    @Bean
    public RestTemplate restTemplate(SSLContext sslContext) {
        RestTemplate restTemplate = new RestTemplateBuilder() {
            @Override
            public ClientHttpRequestFactory buildRequestFactory() {
                return new HttpComponentsClientHttpRequestFactory(
                        HttpClients.custom().setSSLSocketFactory(
                                new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE)).build());
            }
        }.build();

        StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter(StandardCharsets.UTF_8);
        stringHttpMessageConverter.setWriteAcceptCharset(true);
        for (int i = 0; i < restTemplate.getMessageConverters().size(); i++) {
            if (restTemplate.getMessageConverters().get(i) instanceof StringHttpMessageConverter) {
                restTemplate.getMessageConverters().remove(i);
                restTemplate.getMessageConverters().add(i, stringHttpMessageConverter);
                break;
            }
        }

        return restTemplate;
    }

    @Bean
    public SSLContext insecureSslContext() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        return SSLContexts.custom()
                .loadTrustMaterial(null, (x509Certificates, s) -> true)
                .build();
    }
}
