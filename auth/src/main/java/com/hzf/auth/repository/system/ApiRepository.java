package com.hzf.auth.repository.system;

import com.hzf.auth.models.system.Api;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiRepository extends JpaRepository<Api, Long> {

    boolean existsByPathAndMethodAndControllerClassAndMethodName(String path, String method, String controllerClass, String methodName);
}
