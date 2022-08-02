package com.gemini.generic.remote.invocation;

import com.gemini.generic.utils.GemUtils;

public enum RequestType {
    GET {
        @Override
        public Response executeHttpRequest(Request request) {
            return requestReponseManipulation(request);
        }
    },
    PUT {
        @Override
        public Response executeHttpRequest(Request request) {
            return requestReponseManipulation(request);
        }
    },
    POST {
        @Override
        public Response executeHttpRequest(Request request) {
            return requestReponseManipulation(request);
        }
    },
    DELETE {
        @Override
        public Response executeHttpRequest(Request request) {
            return requestReponseManipulation(request);
        }
    },
    PATCH {
        @Override
        public Response executeHttpRequest(Request request) {
            return requestReponseManipulation(request);
        }
    },
    CREATE {
        @Override
        public Response executeHttpRequest(Request request) {
            return requestReponseManipulation(request);
        }
    };

    public abstract Response executeHttpRequest(Request request);

    // Method to manipulate Request and Response
    Response requestReponseManipulation(Request request) {
        return GemUtils.invokeRequestMethod(request);
    }
}
