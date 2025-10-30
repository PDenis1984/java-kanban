package ru.yandex.practicum.helpers;

import ru.yandex.practicum.models.Endpoint;

public class EndpointHelper {

    public static Endpoint getEndpoint(String requestPath, String requestMethod, String base) {
        String[] pathParts = requestPath.split("/");

        switch (pathParts[1]) {

            case "tasks":

                if (pathParts.length == 2 && "GET".equals(requestMethod)) {
                    return Endpoint.GET_TASKS;
                } else if (pathParts.length == 3) {

                    return Endpoint.GET_TASK;
                } else {

                    return Endpoint.POST_TASK;
                }
        }
        return Endpoint.UNKNOWN;
    }
}
