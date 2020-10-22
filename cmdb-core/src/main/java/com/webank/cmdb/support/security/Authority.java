package com.webank.cmdb.support.security;

import com.webank.cmdb.constant.Action;

public interface Authority {

    enum Decision {
        ACCESS_GRANTED, ACCESS_DENIED;

        public boolean isAccessGranted() {
            return this == ACCESS_GRANTED;
        }

        public boolean isAccessDenied() {
            return this == ACCESS_DENIED;
        }
    }

    Decision authorize(Action action, Object dataObject);

    boolean isCiTypePermitted(Action action);

    String getName();

}
