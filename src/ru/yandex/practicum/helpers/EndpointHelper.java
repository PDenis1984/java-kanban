package ru.yandex.practicum.helpers;

import ru.yandex.practicum.models.Endpoint;


public class EndpointHelper {

    public static Endpoint getEndpoint(String requestPath, String requestMethod, String base) {
        String[] pathParts = requestPath.split("/");

        switch (pathParts[1]) {
            case "tasks", "epics", "subtasks": {

                String endpointString = endPointString(base, requestMethod, pathParts);

                return Endpoint.valueOf(endpointString);
            }

            case "history", "prioritized": {

                String endpointString = endPointString(base, requestMethod, pathParts);
                return Endpoint.valueOf(endpointString);
            }
            default:
                return Endpoint.UNKNOWN;
        }
    }

    private static String endPointString(String base, String requestMethod, String[] pathParts) {

        if (pathParts.length == 2 && "GET".equals(requestMethod)) {

            String response = requestMethod + "_" + base.toUpperCase();
            System.out.println(response);
            return response;
        } else if (pathParts.length == 3 && "GET".equals(requestMethod)) {

            String response = requestMethod + "_" + base.substring(0, base.length() - 1).toUpperCase();
            System.out.println(response);
            return response;
        } else if (pathParts.length == 2 && "POST".equals(requestMethod)) {

            String response = requestMethod + "_" + base.substring(0, base.length() - 1).toUpperCase();
            return response;
        }
        return "unknown";
    }
}
