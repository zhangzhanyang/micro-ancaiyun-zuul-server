package com.ancaiyun.fallback;

import com.alibaba.fastjson.JSONObject;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Component
public class ServerFallback implements FallbackProvider {
    @Override
    public String getRoute() {
        return "*";
    }

    @Override
    public ClientHttpResponse fallbackResponse(String route, Throwable cause) {
        return this.response(HttpStatus.INTERNAL_SERVER_ERROR, cause);
    }

    private ClientHttpResponse response(final HttpStatus status, Throwable cause) {
        return new ClientHttpResponse() {
            @Override
            public HttpStatus getStatusCode() throws IOException {
                return status;
            }

            @Override
            public int getRawStatusCode() throws IOException {
                return status.value();
            }

            @Override
            public String getStatusText() throws IOException {
                return status.getReasonPhrase();
            }

            @Override
            public void close() {
            }

            @Override
            public InputStream getBody() throws IOException {
                JSONObject r = new JSONObject();
                r.put("code", "9999");
                r.put("msg", "请求超时，请稍后再试！");
                return new ByteArrayInputStream(r.toJSONString().getBytes());
            }

            @Override
            public HttpHeaders getHeaders() {
                // headers设定
                HttpHeaders headers = new HttpHeaders();
                MediaType mt = MediaType.APPLICATION_JSON_UTF8;
                headers.setContentType(mt);
                return headers;
            }
        };
    }
}
