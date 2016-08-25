
package com.hp.test.framework.staf;

/**
 * Enumerated list of states for a windows service.
 */
public enum WindowServiceState {
    START_PENDING,
    RUNNING,
    STOP_PENDING,
    STOPPED,
    PAUSE_PENDING,
    PAUSED,
    CONTINUE_PENDING,
    UNKNOWN,
    NOT_INSTALLED
}
