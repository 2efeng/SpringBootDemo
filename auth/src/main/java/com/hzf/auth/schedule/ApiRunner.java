package com.hzf.auth.schedule;

import com.hzf.auth.models.system.Api;
import com.hzf.auth.repository.system.ApiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ApiRunner implements CommandLineRunner {

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Autowired
    private ApiRepository apiRepository;

    @Override
    public void run(String... args) {
        Map<String, Api> existingApiPaths = apiRepository.findAll()
                .stream()
                .collect(Collectors.toMap(api -> api.getPath() + "|" + api.getMethod(), api -> api));
        List<Api> needSaveApis = new ArrayList<>();
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
            RequestMappingInfo mappingInfo = entry.getKey();
            HandlerMethod handlerMethod = entry.getValue();
            if (mappingInfo.getPathPatternsCondition() == null) {
                continue;
            }
            String path = mappingInfo.getPathPatternsCondition().getPatterns().iterator().next().toString();
            if (!path.startsWith(("/api"))) {
                continue;
            }
            String name = mappingInfo.getName();
//            List<String> params = mappingInfo
//                    .getParamsCondition()
//                    .getExpressions()
//                    .stream().map(NameValueExpression::getName).toList();
            String method = "";
            if (mappingInfo.getMethodsCondition().getMethods().iterator().hasNext()) {
                method = mappingInfo.getMethodsCondition().getMethods().iterator().next().toString();
            }
            String controllerClass = handlerMethod.getBeanType().getName();
            String methodName = handlerMethod.getMethod().getName();
            Api api = new Api();
            String key = path + "|" + method;
            if (existingApiPaths.containsKey(key)) {
                api.setId(existingApiPaths.get(key).getId());
                existingApiPaths.remove(key);
            }
            api.setName(name);
            api.setPath(path);
            api.setMethod(method);
            api.setControllerClass(controllerClass);
            api.setMethodName(methodName);
            needSaveApis.add(api);
        }
        apiRepository.saveAll(needSaveApis);
        if (!existingApiPaths.isEmpty()) {
            List<Long> needDeletes = existingApiPaths.values().stream().map(Api::getId).toList();
            apiRepository.deleteAllByIdInBatch(needDeletes);
        }
    }
}

