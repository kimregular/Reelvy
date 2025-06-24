package com.reelvy.global.util;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;

@Component
public class PathVariableExtractor {

    private String extract(NativeWebRequest webRequest, String variableName) {

        @SuppressWarnings("unchecked")
        Map<String, String> uriTemplateVars = (Map<String, String>) webRequest.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, NativeWebRequest.SCOPE_REQUEST);

        if(uriTemplateVars == null || !uriTemplateVars.containsKey(variableName)) {
            throw new IllegalArgumentException(variableName + " is not a valid variable name");
        }

        return uriTemplateVars.get(variableName);
    }

    public Long extractAsLong(NativeWebRequest webRequest, String variableName) {
        String value = extract(webRequest, variableName);
        try {
            return Long.parseLong(value);
        }catch (NumberFormatException e) {
            throw new IllegalArgumentException(variableName + " is not a valid variable name");
        }
    }
}
