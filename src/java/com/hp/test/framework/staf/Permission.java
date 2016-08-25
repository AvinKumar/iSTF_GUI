
package com.hp.test.framework.staf;
/**
 * Enumerated list of file permissions.
 * 
 */
public enum Permission {
    // simple rights
    N("no access"),
    F("full access"),
    M("modify access"),
    RX("read and execute access"),
    R("read-only access"),
    W("write-only access"),
    D("delete access"),

    // specific rights
    DE(false, "delete"),
    RC(false, "read control"),
    WDAC(false, "write DAC"),
    WO(false, "write owner"),
    S(false, "synchronize"),
    AS(false, "access system security"),
    MA(false, "maximum allowed"),
    GR(false, "generic read"),
    GW(false, "generic write"),
    GE(false, "generic execute"),
    GA(false, "generic all"),
    RD(false, "read data/list directory"),
    WD(false, "write data/add file"),
    AD(false, "append data/add subdirectory"),
    REA(false, "read extended attributes"),
    WEA(false, "write extended attributes"),
    X(false, "execute/traverse"),
    DC(false, "delete child"),
    RA(false, "read attributes"),
    WA(false, "write attributes"),
    OI(false, "object inherit"),
    CI(false, "container inherit"),
    IO(false, "inherit only"),
    NP(false, "don't propagate inherit"),
    I(false, "permission inherited from parent container");

    private boolean simpleRight = true;
    private String desc = "";

    Permission(final String description) {
        this(true, description);
    }

    Permission(final Boolean simpleRight, final String description) {
        this.simpleRight = simpleRight;
        desc = description;
    }

    public String getDescription() {
        return desc;
    }

    public String getRight() {
        if (simpleRight) {
            return name();
        }

        // otherwise...
        return "(" + name() + ")";
    }
}
