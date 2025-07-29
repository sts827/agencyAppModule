package kr.co.wayplus.travel.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import kr.co.wayplus.travel.model.NCloudBizMessageContainer;
import kr.co.wayplus.travel.model.NCloudBizMessageResult;
import kr.co.wayplus.travel.model.NCloudSmsMessageContainer;
import kr.co.wayplus.travel.model.NCloudSmsMessageResult;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Component
public class NCloudApiUtil {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${key.api.ncloud.access}")
    private String NCLOUD_ACCESS_KEY;
    @Value("${key.api.ncloud.secret}")
    private String NCLOUD_SECRET_KEY;
    @Value("${key.api.ncloud.BizMessage}")
    private String NCLOUD_BIZ_MESSAGE_KEY;
    @Value("${key.api.ncloud.SMS}")
    private String NCLOUD_SMS_MESSAGE_KEY;

    private String url;
    private String format;

    public void setApiBaseUrlAndFormat(String url, String format){
        this.url = url;
        this.format = format;
    }

    public NCloudSmsMessageResult sendSmsMessage(NCloudSmsMessageContainer messageContainer) throws JsonProcessingException, URISyntaxException, RuntimeException {
        String apiUrl = String.format("%s/services/%s/messages", url, NCLOUD_SMS_MESSAGE_KEY);
        logger.debug(String.format("[API REQUEST URL]: %s", apiUrl));

        HttpHeaders headers = getNCloudUserApiHeader(HttpMethod.POST, apiUrl, format);
        ObjectMapper objectMapper = new ObjectMapper();
        HttpEntity<String> httpEntity = new HttpEntity<>(objectMapper.writeValueAsString(messageContainer), headers);
        logger.debug(String.format("[HTTP HEADERS]: %s", httpEntity.getHeaders()));
        logger.debug(String.format("[HTTP BODY]: %s" ,httpEntity.getBody()));
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new SimpleClientHttpRequestFactory());
        return restTemplate.postForObject(new URI(apiUrl), httpEntity, NCloudSmsMessageResult.class);
    }

    public NCloudBizMessageResult sendBizMessage(NCloudBizMessageContainer messageContainer) throws JsonProcessingException, URISyntaxException, RuntimeException {
        String apiUrl = String.format("%s/services/%s/messages", url, NCLOUD_BIZ_MESSAGE_KEY);
        logger.debug(String.format("[API REQUEST URL]: %s", apiUrl));

        HttpHeaders headers = getNCloudUserApiHeader(HttpMethod.POST, apiUrl, format);
        ObjectMapper objectMapper = new ObjectMapper();
        HttpEntity<String> httpEntity = new HttpEntity<>(objectMapper.writeValueAsString(messageContainer), headers);
        logger.debug(String.format("[HTTP HEADERS]: %s", httpEntity.getHeaders()));
        logger.debug(String.format("[HTTP BODY]: %s" ,httpEntity.getBody()));
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new SimpleClientHttpRequestFactory());
        return restTemplate.postForObject(new URI(apiUrl), httpEntity, NCloudBizMessageResult.class);
    }

    private HttpHeaders getNCloudUserApiHeader(HttpMethod method, String url, String format) {
        try {
            //Signature 설정 시 도메인 제거 해야함
            url = url.replace("https://sens.apigw.ntruss.com", "");
            String timeStamp = String.valueOf(System.currentTimeMillis());
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("x-ncp-apigw-timestamp", timeStamp);
            httpHeaders.add("x-ncp-iam-access-key", NCLOUD_ACCESS_KEY);
            httpHeaders.add("x-ncp-apigw-signature-v2", makeNCloudSignature(method.name(), url, timeStamp));
            if(format.equals("JSON")){
                MediaType mediaType = new MediaType("application", "json", Charset.forName("UTF-8"));
                httpHeaders.setContentType(mediaType);
            }
            return httpHeaders;
        } catch (Exception ex) {
            return null;
        }
    }

    private String makeNCloudSignature(String method, String url, String timestamp){
        String space = " ";
        String newLine = "\n";
        String message = new StringBuilder()
                .append(method)
                .append(space)
                .append(url)
                .append(newLine)
                .append(timestamp)
                .append(newLine)
                .append(NCLOUD_ACCESS_KEY)
                .toString();
        logger.debug(String.format("Signature String : \n%s", message));
        try {
            SecretKeySpec signingKey = new SecretKeySpec(NCLOUD_SECRET_KEY.getBytes("UTF-8"), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));
            String encodeBase64String = Base64.encodeBase64String(rawHmac);

            return encodeBase64String;
        } catch (NoSuchAlgorithmException | InvalidKeyException | UnsupportedEncodingException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return "";
        }
    }

    public void sendMessageTest(NCloudBizMessageContainer messageContainer){

        /*
         * 오류로 인한 HTTP CONNECTION 세부 테스트 용 코드입니다.
         *
         */
        try{
            String apiUrl = String.format("%s/services/%s/messages", url, NCLOUD_BIZ_MESSAGE_KEY);
            logger.debug(String.format("[API REQUEST URL]: %s", apiUrl));

            HttpsURLConnection conn;
            OutputStreamWriter writer = null;
            BufferedReader reader = null;
            InputStreamReader isr = null;

            String timestamp = String.valueOf(System.currentTimeMillis());
            Gson gson = new Gson();
            final URL url = new URL(apiUrl);
            conn = (HttpsURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("accept", "application/json; charset=UTF-8");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("x-ncp-apigw-timestamp", timestamp);
            conn.setRequestProperty("x-ncp-iam-access-key", NCLOUD_ACCESS_KEY);
            conn.setRequestProperty("x-ncp-apigw-signature-v2", makeNCloudSignature("POST", apiUrl.replace("https://sens.apigw.ntruss.com", ""), timestamp));
            conn.setDoOutput(true);
            writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write(gson.toJson(messageContainer));
            logger.debug(gson.toJson(messageContainer));
            writer.flush();

            final int responseCode = conn.getResponseCode();
            logger.debug(String.format("\nSending '%s' request to URL : %s", "POST", apiUrl));
            logger.debug("Response Code : " + responseCode);

            if (responseCode == 202)
                isr = new InputStreamReader(conn.getInputStream());
            else
                isr = new InputStreamReader(conn.getErrorStream());

            reader = new BufferedReader(isr);
            final StringBuffer buffer = new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            logger.debug(buffer.toString());

        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage());
        }

    }

}
