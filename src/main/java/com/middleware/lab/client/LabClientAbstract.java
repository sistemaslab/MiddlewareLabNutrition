package com.middleware.lab.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.middleware.lab.model.rest.AccessTokenRequest;
import com.middleware.lab.model.rest.AccessTokenResponse;
import com.middleware.provider.exception.ServiceConnectionException;
import com.middleware.provider.utils.Parser;
import java.net.URI;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 *
 * @author Natalia Manrique
 */
public abstract class LabClientAbstract {

    private final int maxNumOfRetries;
    private final int waitTimeBetweenRetries;
    private final int increaseTimeBetweenRetries;
    private AccessTokenResponse accessTokenResponse;
    private AccessTokenRequest accessTokenRequest;
    protected final ObjectMapper mapper = new ObjectMapper();
    private static final Logger LOGGER = LogManager.getLogger(LabClientAbstract.class);
    private static final java.util.logging.Logger LOG = java.util.logging.Logger.getLogger(LabClientAbstract.class.getName());

    /**
     *
     * @param maxNumOfRetries
     * @param waitTimeBetweenRetries
     * @param increaseTimeBetweenRetries
     */
    public LabClientAbstract(int maxNumOfRetries, int waitTimeBetweenRetries, int increaseTimeBetweenRetries) {
        this.maxNumOfRetries = maxNumOfRetries;
        this.waitTimeBetweenRetries = waitTimeBetweenRetries;
        this.increaseTimeBetweenRetries = increaseTimeBetweenRetries;
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    }

    /**
     *
     * @param uri es la URL de comunicacion con LAB
     * @param httpMethod Metodo de comunicación
     * @param bodyRequest cuerppo del request
     * @return
     * @throws ServiceConnectionException
     * @throws com.fasterxml.jackson.core.JsonProcessingException
     */
    protected boolean callRestServiceBool(URI uri, HttpMethod httpMethod, Object bodyRequest)
            throws ServiceConnectionException, JsonProcessingException, Exception {
        LOG.info("URL   --- >" + uri);
        HttpEntity<String> entity = this.getCoolboxHttpEntity(bodyRequest);
        RestTemplate restTemplate = new RestTemplate();
        int delay = waitTimeBetweenRetries;
        boolean respuesta = false;

        for (int retry = 1; retry <= maxNumOfRetries; retry++) {
            try {
                LOG.info("ENTITY   --- >" + entity.getBody());

                ResponseEntity<String> response = restTemplate.exchange(uri, httpMethod, entity, String.class);
                HttpStatus httpStatus = response.getStatusCode();
                LOG.info("ESTATUS" + response.getStatusCode());
                if (httpStatus == HttpStatus.OK
                        || httpStatus == HttpStatus.ACCEPTED
                        || httpStatus == HttpStatus.CONTINUE
                        || httpStatus == HttpStatus.CREATED
                        || httpStatus == HttpStatus.NO_CONTENT) {
                    LOG.info("RESPUESTA" + response.getBody());
                    respuesta = true;
                    return respuesta;
                } else {
                    String message = "callRestService - Respuesta de COOLBOX diferente de OK"
                            + ": " + uri + ", httpStatus: " + response.getStatusCodeValue()
                            + ", bodyResponse:" + response.getBody()
                            + ", bodyRequest:" + bodyRequest;
                    Exception ex = new Exception(message);
                    LOGGER.error(message, ex);
                    throw new ServiceConnectionException(uri, ex);
                }
            } catch (org    .springframework.web.client.HttpClientErrorException ex) {
                String message = "callRestService - Respuesta de COOLBOX diferente de OK"
                        + ": " + uri + ", httpStatus: " + ex.getStatusText()
                        + ", bodyResponse:" + ex.getResponseBodyAsString()
                        + ", bodyRequest:" + bodyRequest;
                LOGGER.error("callRestService", ex, message);
                System.out.println("message: " + message);
                throw new ServiceConnectionException(uri, ex);
            } catch (org.springframework.web.client.ResourceAccessException ex) {
                String message = "callRestService - El servicio de COOLBOX no responde: " + uri;
                delay = logErrorAndWaitBeforeRetry(uri, message, ex, retry, delay);
            } catch (RestClientException ex) {
                String message = "callRestService - Error del cliente Rest en COOLBOX: " + uri + ex;
                delay = logErrorAndWaitBeforeRetry(uri, message, ex, retry, delay);
            }
        }
        throw new ServiceConnectionException(uri, "No se pudo establecer conexión");
    }

    protected <T> T callRestService(URI uri, HttpMethod httpMethod, Object bodyRequest, Class<T> clazz)
            throws ServiceConnectionException, JsonProcessingException, Exception {
        LOG.info("URL   --- >" + uri);

        HttpEntity<String> entity = this.getCoolboxHttpEntity(bodyRequest);
        System.out.println("CUERPO" + entity.toString());
        RestTemplate restTemplate = new RestTemplate();
        int delay = waitTimeBetweenRetries;
        ResponseEntity<T> response;

        for (int retry = 1; retry <= maxNumOfRetries; retry++) {
            try {
                LOG.info("ENTITY   --- >" + entity.getBody());

                response = restTemplate.exchange(uri, httpMethod, entity, clazz);
                HttpStatus httpStatus = response.getStatusCode();
                LOG.info("ESTATUS" + response.getStatusCode());
                System.out.println("BODY" + response.getBody());
                if (httpStatus == HttpStatus.OK
                        || httpStatus == HttpStatus.ACCEPTED
                        || httpStatus == HttpStatus.CONTINUE
                        || httpStatus == HttpStatus.CREATED
                        || httpStatus == HttpStatus.NO_CONTENT) {
                    LOG.info("RESPUESTA" + response.getBody());

                    T bodyResponse = response.getBody();
                    return bodyResponse;
                } else {
                    String message = "callRestService - Respuesta de COOLBOX diferente de OK"
                            + ": " + uri + ", httpStatus: " + response.getStatusCodeValue()
                            + ", bodyResponse:" + response.getBody()
                            + ", bodyRequest:" + bodyRequest;
                    Exception ex = new Exception(message);
                    LOGGER.error(message, ex);
                    throw new ServiceConnectionException(uri, ex);
                }
            } catch (org.springframework.web.client.HttpClientErrorException ex) {
                String message = "callRestService - Respuesta de COOLBOX diferente de OK"
                        + ": " + uri + ", httpStatus: " + ex.getStatusText()
                        + ", bodyResponse:" + ex.getResponseBodyAsString()
                        + ", bodyRequest:" + bodyRequest;
                LOGGER.error("callRestService", ex, message);
                System.out.println("message: " + message);
                throw new ServiceConnectionException(uri, ex);
            } catch (org.springframework.web.client.ResourceAccessException ex) {
                String message = "callRestService - El servicio de COOLBOX no responde: " + uri;
                delay = logErrorAndWaitBeforeRetry(uri, message, ex, retry, delay);
            } catch (RestClientException ex) {
                String message = "callRestService - Error del cliente Rest en COOLBOX: " + uri + ex;
                delay = logErrorAndWaitBeforeRetry(uri, message, ex, retry, delay);
            }
        }
        throw new ServiceConnectionException(uri, "No se pudo establecer conexión");
    }

    protected <T> T callRestService(URI uri, HttpMethod httpMethod, Class<T> clazz)
            throws ServiceConnectionException, JsonProcessingException {
        return callRestService(uri, httpMethod, null);
    }

//    protected String callRestService(URI uri, HttpMethod httpMethod, Object bodyRequest) 
//            throws ServiceConnectionException, JsonProcessingException{
//        return callRestService(uri, httpMethod, bodyRequest, String.class);
//    }
    protected String callRestService(URI uri, HttpMethod httpMethod)
            throws ServiceConnectionException, JsonProcessingException {
        return callRestService(uri, httpMethod, String.class);
    }
    protected <T> T callRestServiceAuth(URI uri, HttpMethod httpMethod, Object bodyRequest,Class<T> clazz )
            throws ServiceConnectionException, JsonProcessingException {
        this.initAccessToken();
        HttpEntity<String> entity = this.getVtexHttpEntity(accessTokenResponse, bodyRequest);
        RestTemplate restTemplate = new RestTemplate();
        int delay = waitTimeBetweenRetries;
        ResponseEntity<T> response;

        for (int retry = 1; retry <= maxNumOfRetries; retry++) {
            try {
                LOGGER.info(entity.getBody());

                response = restTemplate.exchange(uri, httpMethod, entity,clazz);
                System.out.println("|" + response.getHeaders());
                HttpStatus httpStatus = response.getStatusCode();
                if (httpStatus == HttpStatus.OK
                        || httpStatus == HttpStatus.ACCEPTED
                        || httpStatus == HttpStatus.CONTINUE
                        || httpStatus == HttpStatus.CREATED
                        || httpStatus == HttpStatus.NO_CONTENT) {
                    LOGGER.info("OK "+response.getBody());
                    T bodyResponse = response.getBody();

                    return bodyResponse;
                } else {
                    String message = "callRestService - Respuesta de Coolbox diferente de OK"
                            + ": " + uri + ", httpStatus: " + response.getStatusCodeValue()
                            + ", bodyResponse:" + response.getBody()
                            + ", bodyRequest:" + bodyRequest;
                    Exception ex = new Exception(message);
                    LOGGER.error(message, ex);
                    throw new ServiceConnectionException(uri, ex);
                }
            } catch (org.springframework.web.client.HttpClientErrorException ex) {
                String message = "callRestService - Respuesta de Coolbox diferente de OK"
                        + ": " + uri + ", httpStatus: " + ex.getStatusText()
                        + ", bodyResponse:" + ex.getResponseBodyAsString()
                        + ", bodyRequest:" + bodyRequest;
                LOGGER.error("callRestService", ex, message);
                System.out.println("message: " + message);
                throw new ServiceConnectionException(uri, ex);
            } catch (org.springframework.web.client.ResourceAccessException ex) {
                String message = "callRestService - El servicio de Coolbox no responde: " + uri;
                delay = logErrorAndWaitBeforeRetry(uri, message, ex, retry, delay);
            } catch (RestClientException ex) {
                String message = "callRestService - Error del cliente Rest en Coolbox: " + uri + ex;
                delay = logErrorAndWaitBeforeRetry(uri, message, ex, retry, delay);
            }
        }
        throw new ServiceConnectionException(uri, "No se pudo establecer conexión");
    }

    /**
     *
     * @param uri
     * @param message
     * @param ex
     * @param retry
     * @param delay
     * @return
     * @throws ServiceConnectionException
     */
    private int logErrorAndWaitBeforeRetry(String uri, String message, Throwable ex, int retry, int delay) throws ServiceConnectionException {
        if (retry >= maxNumOfRetries) {
            LOGGER.error(message, ex);
            throw new ServiceConnectionException(uri, ex);
        }
        try {
            message = "retry: " + retry + ", message: " + message;
            LOGGER.warn(message);
            if (delay > 30) {
                delay = 30;
            }
            Thread.sleep(delay * 1000);
        } catch (InterruptedException iex) {
            String iemessage = "logErrorAndWaitBeforeRetry - Error de interrupción con método sleep()";
            LOGGER.warn(iemessage, iex);
        }
        return delay += increaseTimeBetweenRetries;
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
        return logErrorAndWaitBeforeRetry(uri.toString(), message, ex, retry, delay);
    }

    /**
     *
     * @param accessTokenResponse
     * @return
     */
    protected HttpHeaders getCoolboxHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Auth-key", "0a97d18d-9104-42d1-ac5a-db2b5444fa8f");
        System.out.println("Entre al headers ******" + headers);

        return headers;
    }

    /**
     *
     * @param accessTokenResponse
     * @param bodyRequest
     * @return
     * @throws com.fasterxml.jackson.core.JsonProcessingException
     */
    protected HttpEntity getCoolboxHttpEntity(Object bodyRequest) throws JsonProcessingException {
        HttpHeaders headers = this.getCoolboxHeaders();

        System.out.println("headers=" + headers.toString());
        if (bodyRequest != null) {
            if (!(bodyRequest instanceof String)) {
                try {
                    bodyRequest = mapper.writeValueAsString(bodyRequest);

                } catch (JsonProcessingException ex) {
                    bodyRequest = bodyRequest.toString();

                    LOGGER.error("BodyRequest: " + bodyRequest, ex);
                    throw ex;
                }
            }
            System.out.println("BodyRequestEntity: " + bodyRequest);
            return new HttpEntity(bodyRequest, headers);
        }
        return new HttpEntity<>(headers);
    }

    protected HttpEntity getVtexHttpEntity(AccessTokenResponse accessTokenResponse, Object bodyRequest) throws JsonProcessingException {
        HttpHeaders headers = this.getVtexHeaders(accessTokenResponse);

        System.out.println("headers=" + headers.toString());
        if (bodyRequest != null) {
            if (!(bodyRequest instanceof String)) {
                try {
                    bodyRequest = mapper.writeValueAsString(bodyRequest);

                } catch (JsonProcessingException ex) {
                    bodyRequest = bodyRequest.toString();
                    LOGGER.error("BodyRequest: " + bodyRequest, ex);
                    throw ex;
                }
            }
            LOGGER.debug("BodyRequest: " + bodyRequest);
            return new HttpEntity(bodyRequest, headers);
        }
        return new HttpEntity<>(headers);
    }

    /**
     *
     * @return
     */
    protected HttpHeaders getVtexHeaders(AccessTokenResponse accessTokenResponse) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type","application/json; charset=utf-8");
        headers.set("Authorization", "Bearer " + accessTokenResponse.getToken());
        System.out.println("Entre al metodo OAUTH ******" + headers);

        return headers;
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

    protected void initAccessToken() throws ServiceConnectionException, JsonProcessingException {        
        if (accessTokenResponse == null) {
            accessTokenRequest = new AccessTokenRequest("vtex", "vtex");
            accessTokenResponse = accessToken(accessTokenRequest);
        }
    }

    public AccessTokenResponse accessToken(AccessTokenRequest accessTokenRequest) throws ServiceConnectionException, JsonProcessingException {
        String uri = "http://200.48.243.162:8080/WebApi/WebApiSistemaCoolbox/api/Auth/login";
        if (accessTokenRequest != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity(accessTokenRequest, headers);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<AccessTokenResponse> response;
            int delay = waitTimeBetweenRetries;
            for (int retry = 1; retry <= maxNumOfRetries; retry++) {
                try {
                    response = restTemplate.exchange(uri, HttpMethod.POST, entity, AccessTokenResponse.class);
                    HttpStatus httpStatus = response.getStatusCode();
                    if (httpStatus == HttpStatus.OK) {
                        AccessTokenResponse bodyResponse = response.getBody();
                        return bodyResponse;
                    } else {
                        String message = "accessToken - Respuesta de Coolbox diferente de OK"
                                + ": " + uri + ", httpStatus: " + response.getStatusCodeValue()
                                + ", bodyResponse:" + response.getBody()
                                + ", bodyRequest:" + accessTokenRequest.toString();
                        Exception ex = new Exception(message);
                        LOGGER.error(message, ex);
                        throw new ServiceConnectionException(uri, ex);
                    }
                } catch (org.springframework.web.client.HttpClientErrorException ex) {
                    String message = "accessToken - Respuesta de Coolbox diferente de OK"
                            + ": " + uri + ", httpStatus: " + ex.getStatusText()
                            + ", bodyResponse:" + ex.getResponseBodyAsString()
                            + ", bodyRequest:" + accessTokenRequest.toString();
                    LOGGER.error("callRestService", ex, message);
                    throw new ServiceConnectionException(uri, ex);
                } catch (org.springframework.web.client.ResourceAccessException ex) {
                    String message = "accessToken - El servicio de Coolbox no responde: " + uri + ", bodyRequest:" + accessTokenRequest.toString();
                    delay = logErrorAndWaitBeforeRetry(uri, message, ex, retry, delay);
                } catch (RestClientException ex) {
                    String message = "accessToken - Error del cliente Rest en Coolbox: " + uri + ", bodyRequest:" + accessTokenRequest.toString();
                    delay = logErrorAndWaitBeforeRetry(uri, message, ex, retry, delay);
                }
            }
            throw new ServiceConnectionException(uri, "No se pudo establecer conexión" + ", bodyRequest:" + accessTokenRequest.toString());
        } else {
            throw new ServiceConnectionException(uri, "¡accessTokenRequest = null!" + ", bodyRequest:" + accessTokenRequest.toString());
        }
    }

}
