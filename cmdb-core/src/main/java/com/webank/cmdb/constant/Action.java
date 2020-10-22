package com.webank.cmdb.constant;

public enum Action {
    None("none"),
    Creation("Creation"),
    Removal("Removal"),
    Modification("Modification"),
    Enquiry("Enquiry"),
    Execution("Execution"),
    Grant("Grant")
    ;

    private String code;

    private Action(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    static public Action fromCode(String code) {
        for (Action AutoFillType : values()) {
            if (None.equals(AutoFillType))
                continue;

            if (AutoFillType.getCode().equals(code)) {
                return AutoFillType;
            }
        }
        return None;
    }
}
