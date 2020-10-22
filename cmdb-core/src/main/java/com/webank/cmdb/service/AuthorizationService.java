package com.webank.cmdb.service;

import com.webank.cmdb.constant.Action;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface AuthorizationService extends CmdbService {
    @Override
    default String getName() {
        return "AuthorizationService";
    }

    void authorizeCiData(int ciTypeId, Object ciData, Action action);

    boolean isCiTypePermitted(int ciTypeId, Action action);

    boolean isCiDataPermitted(int ciTypeId, Object ciData, Action action);

    List<Map<String, Set<?>>> getPermittedData(int ciTypeId, Action action);

}
