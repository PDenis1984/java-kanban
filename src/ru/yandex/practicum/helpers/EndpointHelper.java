package ru.yandex.practicum.helpers;

import ru.yandex.practicum.models.Endpoint;

public class EndpointHelper {

    public static Endpoint getEndpoint(String requestPath, String requestMethod, String base) {
        String[] pathParts = requestPath.split("/");

        return switch (pathParts[1]) {
            case "tasks", "epics", "subtasks" -> Endpoint.valueOf(endPointString(base, requestMethod, pathParts));
            case "history", "prioritized" -> Endpoint.valueOf(base.toUpperCase());
            default -> Endpoint.UNKNOWN;
        };
    }

    private static String endPointString(String base, String requestMethod, String[] pathParts) {

        if (pathParts.length == 2 && "GET".equals(requestMethod)) {

            return requestMethod + "_" + base.toUpperCase();
        } else if (pathParts.length == 3 && "GET".equals(requestMethod)) {

            return requestMethod + "_" + base.substring(0, base.length() - 2).toUpperCase();
        }
        return "unknown";
    }
}
