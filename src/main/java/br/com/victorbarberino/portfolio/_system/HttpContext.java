package br.com.victorbarberino.portfolio._system;

import jakarta.servlet.http.HttpServletRequest;

public class HttpContext {
    private static final ThreadLocal<HttpServletRequest> currentRequest = new ThreadLocal<>();

    public static HttpServletRequest getCurrentRequest() {
        return currentRequest.get();
    }

    public static void setCurrentRequest(HttpServletRequest request) {
        currentRequest.set(request);
    }

    public static void clear() {
        currentRequest.remove();
    }
}
