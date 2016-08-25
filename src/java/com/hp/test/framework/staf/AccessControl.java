
package com.hp.test.framework.staf;

/**
 * 
 * TODO Describe this <code>AccessControl</code> type.
 */
public class AccessControl {

    private final Permission perm;
    private boolean allowed = false;

    public AccessControl(final boolean allowed, final Permission perm) {
        this.perm = perm;
        this.allowed = allowed;
    }

    public boolean isAllowed() {
        return allowed;
    }

    public Permission getPermission() {
        return perm;
    }
}
