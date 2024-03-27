package util;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public final class ErrorHandler {
    private ErrorHandler() {
    }

    public static void handleException(HttpServletResponse servletResponse, Throwable throwable) throws IOException {
        servletResponse.setStatus(getCodeStatus(throwable));
        servletResponse.getWriter().write(getJsonResponse(throwable.getMessage()));
    }

    private static int getCodeStatus(Throwable throwable) {
        return switch (throwable.getClass().getSimpleName()) {
            case "DatabaseException" -> 500;
            case "DataExistsException" -> 409;
            case "DataNotFoundException" -> 404;
            case "InvalidDataException" -> 400;
            default -> 0;
        };
    }

    private static String getJsonResponse(String message) {
        return  "{\"message\": \"" + message + "\"}";
    }
}