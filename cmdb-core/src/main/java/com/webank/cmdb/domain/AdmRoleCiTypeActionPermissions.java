package com.webank.cmdb.domain;

import com.webank.cmdb.constant.Action;
import com.webank.cmdb.support.exception.CmdbException;

public interface AdmRoleCiTypeActionPermissions {
    String ENABLED = "Y";

    String getCreationPermission();

    String getRemovalPermission();

    String getModificationPermission();

    String getEnquiryPermission();

    String getExecutionPermission();

    String getGrantPermission();

    default String getActionPermission(Action action) {
        switch (action){
            case Creation:
                return getCreationPermission();
            case Removal:
                return getRemovalPermission();
            case Modification:
                return getModificationPermission();
            case Enquiry:
                return getEnquiryPermission();
            case Execution:
                return getExecutionPermission();
            case Grant:
                return getGrantPermission();
            default:
                throw new CmdbException("Unsupported action code: " + action).withErrorCode("3054", action.getCode());

        }
    }

    default boolean isActionPermissionEnabled(Action action) {
        return ENABLED.equalsIgnoreCase(getActionPermission(action));
    }
}
