package com.lcsk42.frameworks.starter.web.util;

import com.lcsk42.frameworks.starter.base.constant.StringConstant;
import com.lcsk42.frameworks.starter.convention.errorcode.impl.FileErrorCode;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Utility class for servlet-related operations.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ServletUtil {

    /**
     * Gets all parameters from the request as an unmodifiable map.
     *
     * @param request the servlet request
     * @return map of parameter names to their values (as String arrays)
     */
    public static Map<String, String[]> getParams(ServletRequest request) {
        return Collections.unmodifiableMap(request.getParameterMap());
    }

    /**
     * Gets all parameters from the request as a map with comma-separated values.
     *
     * @param request the servlet request
     * @return map of parameter names to their comma-separated values
     */
    public static Map<String, String> getParamMap(ServletRequest request) {
        return getParams(request).entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> String.join(StringConstant.COMMA, entry.getValue())
                ));
    }

    /**
     * Reads the request body as a string.
     *
     * @param request the servlet request
     * @return the request body as string, or empty string if reading fails
     */
    public static String getBody(ServletRequest request) {
        try (Reader reader = request.getReader()) {
            return IOUtils.toString(reader);
        } catch (IOException e) {
            return StringUtils.EMPTY;
        }
    }

    /**
     * Reads the request body as a byte array.
     *
     * @param request the servlet request
     * @return the request body as byte array, or empty array if reading fails
     */
    public static byte[] getBodyBytes(ServletRequest request) {
        try (InputStream input = request.getInputStream()) {
            return IOUtils.toByteArray(input);
        } catch (IOException e) {
            return ArrayUtils.EMPTY_BYTE_ARRAY;
        }
    }

    /**
     * Gets all headers from the request as a map (single value per header).
     *
     * @param request the HTTP servlet request
     * @return map of header names to their values
     */
    public static Map<String, String> getHeaderMap(HttpServletRequest request) {
        return Collections.list(request.getHeaderNames())
                .stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        request::getHeader,
                        (existing, replacement) -> existing,
                        HashMap::new
                ));
    }

    /**
     * Gets all headers from the request as a map (all values for each header).
     *
     * @param request the HTTP servlet request
     * @return map of header names to their list of values
     */
    public static Map<String, List<String>> getHeadersMap(HttpServletRequest request) {
        return Collections.list(request.getHeaderNames())
                .stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        name -> Collections.list(request.getHeaders(name)),
                        (existing, replacement) -> existing
                ));
    }

    /**
     * Gets all headers from the response as a map.
     *
     * @param response the HTTP servlet response
     * @return map of header names to their list of values
     */
    public static Map<String, List<String>> getHeadersMap(HttpServletResponse response) {
        return response.getHeaderNames()
                .stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        name -> response.getHeaders(name).stream().toList(),
                        (existing, replacement) -> existing,
                        HashMap::new
                ));
    }

    /**
     * Gets a specific header value from the request (UTF-8 encoded).
     *
     * @param request the HTTP servlet request
     * @param name    the header name
     * @return the header value, or empty string if not found
     */
    public static String getHeader(HttpServletRequest request, String name) {
        return getHeader(request, name, StandardCharsets.UTF_8);
    }

    /**
     * Gets a specific header value from the request with specified charset.
     *
     * @param request the HTTP servlet request
     * @param name    the header name
     * @param charset the charset to use for decoding
     * @return the header value, or empty string if not found
     */
    public static String getHeader(HttpServletRequest request, String name, Charset charset) {
        String headerValue = request.getHeader(name);
        if (StringUtils.isBlank(headerValue)) {
            return StringUtils.EMPTY;
        }
        try {
            byte[] bytes = headerValue.getBytes(StandardCharsets.ISO_8859_1);
            return new String(bytes, charset);
        } catch (Exception e) {
            return headerValue;
        }
    }

    /**
     * Checks if the request method is GET.
     *
     * @param request the HTTP servlet request
     * @return true if GET method, false otherwise
     */
    public static boolean isGet(HttpServletRequest request) {
        return HttpMethod.GET.matches(request.getMethod());
    }

    /**
     * Checks if the request method is POST.
     *
     * @param request the HTTP servlet request
     * @return true if POST method, false otherwise
     */
    public static boolean isPost(HttpServletRequest request) {
        return HttpMethod.POST.matches(request.getMethod());
    }

    /**
     * Checks if the request method is PUT.
     *
     * @param request the HTTP servlet request
     * @return true if PUT method, false otherwise
     */
    public static boolean isPut(HttpServletRequest request) {
        return HttpMethod.PUT.matches(request.getMethod());
    }

    /**
     * Checks if the request method is DELETE.
     *
     * @param request the HTTP servlet request
     * @return true if DELETE method, false otherwise
     */
    public static boolean isDelete(HttpServletRequest request) {
        return HttpMethod.DELETE.matches(request.getMethod());
    }

    /**
     * Checks if the request method is OPTIONS.
     *
     * @param request the HTTP servlet request
     * @return true if OPTIONS method, false otherwise
     */
    public static boolean isOptions(HttpServletRequest request) {
        return HttpMethod.OPTIONS.matches(request.getMethod());
    }

    /**
     * Checks if the request is a multipart request.
     *
     * @param request the HTTP servlet request
     * @return true if multipart POST request, false otherwise
     */
    public static boolean isMultipart(HttpServletRequest request) {
        if (!isPost(request)) {
            return false;
        } else {
            String contentType = request.getContentType();
            return StringUtils.isNoneBlank(contentType) && StringUtils.startsWithIgnoreCase("multipart/", contentType);
        }
    }

    /**
     * Gets a specific cookie from the request.
     *
     * @param httpServletRequest the HTTP servlet request
     * @param name               the cookie name
     * @return the Cookie object, or null if not found
     */
    public static Cookie getCookie(HttpServletRequest httpServletRequest, String name) {
        return getCookieMap(httpServletRequest).get(name);
    }

    /**
     * Gets all cookies from the request as a map.
     *
     * @param httpServletRequest the HTTP servlet request
     * @return map of cookie names to Cookie objects
     */
    public static Map<String, Cookie> getCookieMap(HttpServletRequest httpServletRequest) {
        Cookie[] cookies = httpServletRequest.getCookies();
        if (ArrayUtils.isEmpty(cookies)) {
            return Map.of();
        } else {
            return Stream.of(cookies)
                    .collect(Collectors.toMap(
                            Cookie::getName,
                            Function.identity(),
                            (existing, replacement) -> replacement
                    ));
        }
    }

    /**
     * Adds a cookie to the response.
     *
     * @param response the HTTP servlet response
     * @param cookie   the cookie to add
     */
    public static void addCookie(HttpServletResponse response, Cookie cookie) {
        response.addCookie(cookie);
    }

    /**
     * Adds a simple cookie to the response.
     *
     * @param response the HTTP servlet response
     * @param name     the cookie name
     * @param value    the cookie value
     */
    public static void addCookie(HttpServletResponse response, String name, String value) {
        addCookie(response, new Cookie(name, value));
    }

    /**
     * Adds a cookie with max age to the response.
     *
     * @param response        the HTTP servlet response
     * @param name            the cookie name
     * @param value           the cookie value
     * @param maxAgeInSeconds the maximum age of the cookie in seconds
     */
    public static void addCookie(HttpServletResponse response, String name, String value, int maxAgeInSeconds) {
        addCookie(response, name, value, maxAgeInSeconds, StringConstant.SLASH, null);
    }

    /**
     * Adds a fully configurable cookie to the response.
     *
     * @param response        the HTTP servlet response
     * @param name            the cookie name
     * @param value           the cookie value
     * @param maxAgeInSeconds the maximum age of the cookie in seconds
     * @param path            the path for the cookie
     * @param domain          the domain for the cookie
     */
    public static void addCookie(HttpServletResponse response,
                                 String name,
                                 String value,
                                 int maxAgeInSeconds,
                                 String path,
                                 String domain) {
        Cookie cookie = new Cookie(name, value);
        if (StringUtils.isNoneBlank(domain)) {
            cookie.setDomain(domain);
        }

        cookie.setMaxAge(maxAgeInSeconds);
        cookie.setPath(path);
        addCookie(response, cookie);
    }

    /**
     * Writes a file to the response as an attachment.
     *
     * @param response    the HTTP servlet response
     * @param inputStream the input stream of the file
     * @param fileName    the file name to display in download dialog
     * @param contentType the MIME type of the file
     */
    public static void write(HttpServletResponse response,
                             InputStream inputStream,
                             String fileName,
                             String contentType) {
        write(response, inputStream, fileName, contentType, StandardCharsets.UTF_8);
    }

    /**
     * Writes a file to the response as an attachment with specified charset.
     *
     * @param response    the HTTP servlet response
     * @param inputStream the input stream of the file
     * @param fileName    the file name to display in download dialog
     * @param contentType the MIME type of the file
     * @param charset     the charset for encoding the filename
     */
    public static void write(HttpServletResponse response,
                             InputStream inputStream,
                             String fileName,
                             String contentType,
                             Charset charset) {
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                ContentDisposition
                        .attachment()
                        .filename(fileName, charset)
                        .build()
                        .toString()
        );
        response.setCharacterEncoding(charset.toString());
        response.setContentType(contentType);
        write(response, inputStream);
    }

    /**
     * Writes a file to the response to be displayed inline.
     *
     * @param response    the HTTP servlet response
     * @param inputStream the input stream of the file
     * @param fileName    the file name
     * @param contentType the MIME type of the file
     */
    public static void writeToInline(HttpServletResponse response,
                                     InputStream inputStream,
                                     String fileName,
                                     String contentType) {
        writeToInline(response, inputStream, fileName, contentType, StandardCharsets.UTF_8);
    }

    /**
     * Writes a file to the response to be displayed inline with specified charset.
     *
     * @param response    the HTTP servlet response
     * @param inputStream the input stream of the file
     * @param fileName    the file name
     * @param contentType the MIME type of the file
     * @param charset     the charset for encoding the filename
     */
    public static void writeToInline(HttpServletResponse response,
                                     InputStream inputStream,
                                     String fileName,
                                     String contentType,
                                     Charset charset) {
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                ContentDisposition
                        .inline()
                        .filename(fileName, charset)
                        .build()
                        .toString()
        );
        response.setCharacterEncoding(charset.toString());
        response.setContentType(contentType);
        write(response, inputStream);
    }

    /**
     * Writes input stream to response using default buffer size.
     *
     * @param response    the HTTP servlet response
     * @param inputStream the input stream to write
     */
    public static void write(HttpServletResponse response, InputStream inputStream) {
        write(response, inputStream, 8192);
    }

    /**
     * Writes input stream to response using specified buffer size.
     *
     * @param response    the HTTP servlet response
     * @param inputStream the input stream to write
     * @param bufferSize  the buffer size to use for copying
     */
    public static void write(HttpServletResponse response, InputStream inputStream, int bufferSize) {
        ServletOutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            IOUtils.copy(inputStream, outputStream, bufferSize);
            outputStream.flush();
        } catch (IOException ignored) {
            throw FileErrorCode.IO_RUNTIME_EXCEPTION.toException();
        } finally {
            IOUtils.closeQuietly(outputStream);
            IOUtils.closeQuietly(inputStream);
        }
    }
}
