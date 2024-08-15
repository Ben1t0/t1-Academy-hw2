package ru.t1academy.java.hw2.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class RequestAndResponseFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(RequestAndResponseFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("\n").append("-----------------------------").append("\n");
        sb.append(">>>>>>>>>>REQUEST>>>>>>>>>>>").append("\n");
        sb.append("METHOD: %s".formatted(request.getMethod())).append("\n");
        sb.append("URI: %s".formatted(request.getRequestURI())).append("\n");
        sb.append("----------HEADERS----------").append("\n");
        Collections.list(request.getHeaderNames())
                .forEach(h -> sb.append("HEADER %s : %s".formatted(h, request.getHeader(h))).append("\n"));
        long startTime = System.currentTimeMillis();

        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
        filterChain.doFilter(requestWrapper, responseWrapper);
        long endTime = System.currentTimeMillis();

        logRequestBody(requestWrapper, "BODY: ", sb);
        sb.append("-----------------------------").append("\n");

        sb.append("Execution time: %d".formatted(endTime - startTime)).append("\n");
        sb.append("<<<<<<<<<<RESPONSE<<<<<<<<<<").append("\n");
        sb.append("STATUS: %d".formatted(response.getStatus())).append("\n");
        sb.append("----------HEADERS----------").append("\n");

        StringBuilder responseBodySB = new StringBuilder();
        logResponseBody(responseWrapper, "BODY: ", responseBodySB);

        responseWrapper.copyBodyToResponse();
        responseWrapper.getHeaderNames()
                .forEach(h -> sb.append("HEADER %s : %s".formatted(h, responseWrapper.getHeader(h))).append("\n"));
        sb.append(responseBodySB);
        sb.append("-----------------------------");
        log.info(sb.toString());
    }

    private static void logRequestBody(ContentCachingRequestWrapper request, String prefix, StringBuilder msg) {
        byte[] content = request.getContentAsByteArray();
        if (content.length > 0) {
            logContent(content, request.getContentType(), request.getCharacterEncoding(), prefix, msg);
        }
    }

    private static void logResponseBody(ContentCachingResponseWrapper request, String prefix, StringBuilder msg) {
        byte[] content = request.getContentAsByteArray();
        if (content.length > 0) {
            logContent(content, request.getContentType(), request.getCharacterEncoding(), prefix, msg);
        }
    }

    private static final List<MediaType> READABLE_TYPES = Arrays.asList(
            MediaType.valueOf("text/*"),
            MediaType.APPLICATION_FORM_URLENCODED,
            MediaType.APPLICATION_JSON,
            MediaType.APPLICATION_XML,
            MediaType.valueOf("application/*+json"),
            MediaType.valueOf("application/*+xml"),
            MediaType.MULTIPART_FORM_DATA
    );

    private static void logContent(byte[] content, String contentType, String contentEncoding, String prefix,
                                   StringBuilder sb) {
        MediaType mediaType = MediaType.valueOf(contentType);
        boolean visible = READABLE_TYPES.stream()
                .anyMatch(visibleType -> visibleType.includes(mediaType));
        if (visible) {
            try {
                String contentString = new String(content, contentEncoding);
                Stream.of(contentString.split("\r\n|\r|\n"))
                        .forEach(line -> sb.append(prefix).append(" ").append(line).append("\n"));
            } catch (UnsupportedEncodingException e) {
                sb.append(String.format("%s [%d bytes content]", prefix, content.length)).append("\n");
            }
        } else {
            sb.append(String.format("%s [%d bytes content]", prefix, content.length)).append("\n");
        }
    }
}
