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

        String responseLong = requestMethod + "_" + base.toUpperCase();
        String responseShort = requestMethod + "_" + base.substring(0, base.length() - 1).toUpperCase();

        if (pathParts.length == 2 && "GET".equals(requestMethod)) {

            System.out.println(responseLong);
            return responseLong;
        } else if (pathParts.length == 3 && "GET".equals(requestMethod)) {

            System.out.println(responseShort);
            return responseShort;
        } else if (pathParts.length == 2 && "POST".equals(requestMethod)) {

              return responseShort;
        } else if (pathParts.length == 3 && "DELETE".equals(requestMethod)) {


            return responseShort;
        } else if (pathParts.length == 3 && "POST".equals(requestMethod)) {

            return responseShort;
        }

        return "unknown".toUpperCase();
    }
}
