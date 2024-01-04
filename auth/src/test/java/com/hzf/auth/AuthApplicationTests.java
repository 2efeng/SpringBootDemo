package com.hzf.auth;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Map;

@SpringBootTest
class AuthApplicationTests {

    @Autowired
    WebApplicationContext applicationContext;

    @Test
    protected void hello() {
        RequestMappingHandlerMapping requestMappingHandlerMapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> requestMappingInfoHandlerMethodEntry :handlerMethods.entrySet()){
            RequestMappingInfo requestMappingInfo = requestMappingInfoHandlerMethodEntry.getKey();
            HandlerMethod mappingInfoValue = requestMappingInfoHandlerMethodEntry.getValue();
            RequestMethodsRequestCondition methodsCondition = requestMappingInfo.getMethodsCondition();
            PatternsRequestCondition patternsCondition = requestMappingInfo.getPatternsCondition();

            String requestType = methodsCondition.getMethods().toString();
//            String requestUrl = patternsCondition.getPatterns().iterator().next();
            String controllerName = mappingInfoValue.getBeanType().toString();
            String requestMethodName = mappingInfoValue.getMethod().getName();
            Class<?> [] methodParamsTypes = mappingInfoValue.getMethod().getParameterTypes();

        }

    }

}
