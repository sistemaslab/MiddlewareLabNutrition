package com.middleware.provider.consumer.services;

import java.net.URI;
import java.util.Date;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.middleware.provider.exception.ServiceConnectionException;
import com.middleware.provider.serializer.VtexDateSerializer;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

/**
 *
 * @author Alejandro Cadena
 */
public abstract class VtexApiRestAbstractClient {

    @Value("${vtex.appkey}")
    private String appkey;

    @Value("${vtex.appToken}")
    private String appToken;

    // Máximo número de reintentos de conexión con VTEX
    @Value("${vtex.maxNumberOfRetries}")
    private int maxNumOfRetries;

    // Tiempo inicial de espera entre reintentos (segundos)
    @Value("${vtex.waitTimeBetweenRetries}")
    private int waitTimeBetweenRetries;

    // Entre cada reintento se incrementa el tiempo de espera en este valor (segundos)
    @Value("${vtex.increaseTimeBetweenRetries}")
    private int increaseTimeBetweenRetries;

    private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger(VtexApiRestAbstractClient.class);

    protected final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    protected final static ObjectMapper mapper = new ObjectMapper();

    public VtexApiRestAbstractClient() {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        SimpleModule module = new SimpleModule("DateModule", new Version(2, 0, 0, null, null, null));
        module.addSerializer(Date.class, new VtexDateSerializer(DATE_FORMAT));
        mapper.registerModule(module);

    }

    /**
     *
     * @return
     */
    protected HttpHeaders getVtexHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("X-VTEX-API-Appkey", this.appkey);
        headers.add("X-VTEX-API-AppToken", this.appToken);
        return headers;
    }

    /**
     *
     * @param bodyRequest
     * @return
     */
    protected HttpEntity getVtexHttpEntity(Object bodyRequest) {
        LOGGER.error("RA al entrar ------->" + bodyRequest);

        HttpHeaders headers = this.getVtexHeaders();
        if (bodyRequest != null) {
            if (!(bodyRequest instanceof String)) {
                bodyRequest = bodyRequest.toString();
                System.out.println("CUERPO" + bodyRequest);
            }

            return new HttpEntity(bodyRequest, headers);
        } else {
            return new HttpEntity<>(headers);
        }
    }

    

    /**
     *
     * @return
     */
    protected HttpHeaders getVtexHeadersPatch() {
        HttpHeaders headers = new HttpHeaders();
        MediaType mediaType = new MediaType("application", "merge-patch+json");
        headers.setContentType(mediaType);
        headers.add("X-VTEX-API-Appkey", this.appkey);
        headers.add("X-VTEX-API-AppToken", this.appToken);
        return headers;
    }

    /**
     *
     * @param bodyRequest
     * @return
     */
    protected HttpEntity getVtexHttpEntityPatch(Object bodyRequest) {
        LOGGER.error("RA al entrar ------->" + bodyRequest);

        HttpHeaders headers = this.getVtexHeadersPatch();
        if (bodyRequest != null) {
            if (!(bodyRequest instanceof String)) {
                bodyRequest = bodyRequest.toString();
                System.out.println("CUERPO" + bodyRequest);
            }

            return new HttpEntity(bodyRequest, headers);
        } else {
            return new HttpEntity<>(headers);
        }
    }

    /**
     *
     * @param url
     * @return
     */
    protected static URI buildUri(String url) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        return builder.build().toUri();
    }

    /**
     *
     * @param url
     * @param queryParams
     * @return
     */
    protected URI buildUri(String url, Map<String, Object> queryParams) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        queryParams.entrySet().forEach((entry) -> {
            builder.queryParam(entry.getKey(), entry.getValue());
        });
        return builder.build().toUri();
    }

    /**
     *
     * @param url
     * @param uriParams
     * @param queryParams
     * @return
     */
    protected URI buildUri(String url, Map<String, String> uriParams, Map<String, Object> queryParams) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        if (queryParams != null) {
            queryParams.entrySet().forEach((entry) -> {
                builder.queryParam(entry.getKey(), entry.getValue());
            });
        }
        if (uriParams != null) {
            return builder.buildAndExpand(uriParams).toUri();
        }
        return builder.build().toUri();
    }

    /**
     *
     * @param uri
     * @return
     * @throws blacksip.generic.exception.ServiceConnectionException
     */
    protected ResponseEntity<String> callVtexService(URI uri) throws ServiceConnectionException {
        return this.callVtexService(uri, HttpMethod.GET, null);
    }

    /**
     *
     * @param uri
     * @param httpMethod
     * @return
     * @throws blacksip.generic.exception.ServiceConnectionException
     */
    protected ResponseEntity<String> callVtexService(URI uri, HttpMethod httpMethod) throws ServiceConnectionException {
        return this.callVtexService(uri, httpMethod, null);
    }

    /**
     *
     * @param uri
     * @param httpMethod
     * @param bodyRequest
     * @return
     * @throws com.middleware.provider.exception.ServiceConnectionException
     */
    protected ResponseEntity<String> callVtexService(
            URI uri, HttpMethod httpMethod, Object bodyRequest) throws ServiceConnectionException {
        this.debugBodyRequest(bodyRequest);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response;
        HttpEntity<String> entity = this.getVtexHttpEntity(bodyRequest);
        int delay = waitTimeBetweenRetries;
        System.out.println("Request Url: " + uri);

        for (int retry = 1; retry <= maxNumOfRetries; retry++) {
            try {
                response = restTemplate.exchange(
                        uri, httpMethod, entity, String.class);
                HttpStatus httpStatus = response.getStatusCode();

                if (httpStatus == HttpStatus.OK
                        || httpStatus == HttpStatus.ACCEPTED
                        || httpStatus == HttpStatus.CONTINUE
                        || httpStatus == HttpStatus.CREATED
                        || httpStatus == HttpStatus.NO_CONTENT) {
                    System.out.println("Estado del servicio " + response.getStatusCode());
                    return response;
                } else {
                    if (httpStatus == HttpStatus.INTERNAL_SERVER_ERROR) {
                        System.out.println("ERROR");
                    } else {
                        String message = "Respuesta de VTEX diferente de OK"
                                + ": " + uri + ", httpStatus: " + response.getStatusCodeValue()
                                + ", bodyResponse:" + response.getBody();
                        Exception ex = new Exception(message);
                        LOGGER.error("callVtexService Error " + message, ex);
                        delay = logErrorAndWaitBeforeRetry(uri, message, ex, retry, delay);
                    }
                }
            } catch (org.springframework.web.client.HttpClientErrorException ex) {
                String message = "Respuesta de VTEX diferente de OK"
                        + ": " + uri + ", httpStatus: " + ex.getStatusText()
                        + ", bodyResponse:" + ex.getResponseBodyAsString();
                LOGGER.error("callVtexService Error " + message, ex);
                delay = logErrorAndWaitBeforeRetry(uri, message, ex, retry, delay);
            } catch (org.springframework.web.client.ResourceAccessException ex) {
                String message = "El servicio de VTEX no responde: " + uri;
                LOGGER.error("callVtexService Error " + message, ex);
                delay = logErrorAndWaitBeforeRetry(uri, message, ex, retry, delay);
            } catch (RestClientException ex) {
                String message = "Error del cliente Rest en VTEX: " + uri;
                LOGGER.error("callVtexService Error " + message, ex);
                delay = logErrorAndWaitBeforeRetry(uri, message, ex, retry, delay);
            }
        }
        LOGGER.error("callVtexService Error: " + "No se pudo establecer conexion");
        throw new ServiceConnectionException(uri, "No se pudo establecer conexion");
    }


    protected ResponseEntity<String> callVtexServicePatch(
            URI uri, HttpMethod httpMethod, Object bodyRequest) throws ServiceConnectionException {
        this.debugBodyRequest(bodyRequest);

        ResponseEntity<String> response;
        HttpEntity<String> entity = this.getVtexHttpEntity(bodyRequest);
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        int delay = waitTimeBetweenRetries;
        System.out.println("Request Url: " + uri);

        for (int retry = 1; retry <= maxNumOfRetries; retry++) {
            try {
                response = restTemplate.exchange(
                        uri, httpMethod, entity, String.class);
                HttpStatus httpStatus = response.getStatusCode();

                if (httpStatus == HttpStatus.OK
                        || httpStatus == HttpStatus.ACCEPTED
                        || httpStatus == HttpStatus.CONTINUE
                        || httpStatus == HttpStatus.CREATED
                        || httpStatus == HttpStatus.NO_CONTENT) {
                    return response;
                } else {
                    if (httpStatus == HttpStatus.INTERNAL_SERVER_ERROR) {
                        System.out.println("ERROR");
                    } else {
                        String message = "Respuesta de VTEX diferente de OK"
                                + ": " + uri + ", httpStatus: " + response.getStatusCodeValue()
                                + ", bodyResponse:" + response.getBody();
                        Exception ex = new Exception(message);
                        LOGGER.error("callVtexService Error " + message, ex);
                        delay = logErrorAndWaitBeforeRetry(uri, message, ex, retry, delay);
                    }
                }
            } catch (org.springframework.web.client.HttpClientErrorException ex) {
                String message = "Respuesta de VTEX diferente de OK"
                        + ": " + uri + ", httpStatus: " + ex.getStatusText()
                        + ", bodyResponse:" + ex.getResponseBodyAsString();
                LOGGER.error("callVtexService Error " + message, ex);
                delay = logErrorAndWaitBeforeRetry(uri, message, ex, retry, delay);
            } catch (org.springframework.web.client.ResourceAccessException ex) {
                String message = "El servicio de VTEX no responde: " + uri;
                LOGGER.error("callVtexService Error " + message, ex);
                delay = logErrorAndWaitBeforeRetry(uri, message, ex, retry, delay);
            } catch (RestClientException ex) {
                String message = "Error del cliente Rest en VTEX: " + uri;
                LOGGER.error("callVtexService Error " + message, ex);
                delay = logErrorAndWaitBeforeRetry(uri, message, ex, retry, delay);
            }
        }
        LOGGER.error("callVtexService Error: " + "No se pudo establecer conexion");
        throw new ServiceConnectionException(uri, "No se pudo establecer conexion");
    }

    /**
     *
     * @param message
     * @param ex
     * @param retry
     * @param delay
     * @return
     */
    private int logErrorAndWaitBeforeRetry(URI uri, String message, Throwable ex, int retry, int delay) throws ServiceConnectionException {
        if (retry == maxNumOfRetries) {
            LOGGER.error("logErrorAndWaitBeforeRetry: se alcanzó el máximo número de intentos", ex);
            throw new ServiceConnectionException(uri, ex);
        }
        try {
            message = "retry: " + retry + ", message: " + message;
            LOGGER.warn(message);
            Thread.sleep(delay * 1000);
        } catch (InterruptedException iex) {
            String iemessage = "Error de interrupción con método sleep()";
            LOGGER.error("logErrorAndWaitBeforeRetry: " + iemessage, iex);
        }
        return delay += increaseTimeBetweenRetries;
    }

    protected void debugBodyRequest(Object object) {
        if (object != null) {
            try {
                if (!(object instanceof String)) {
                    object = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
                }
                LOGGER.info("BodyRequest=" + object);
            } catch (JsonProcessingException ex) {
                String message = "Error parseando el objeto: " + object.getClass().getName() + " a json";
                LOGGER.error("debugBodyRequest: " + message, ex);
            }
        } else {
            LOGGER.warn("BodyRequest=null");
        }
    }

}
