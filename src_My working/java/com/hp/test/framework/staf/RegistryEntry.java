

package com.hp.test.framework.staf;

/**
 * 
 * TODO Describe this <code>RegistryEntry</code> type.
 * 
 */
public class RegistryEntry {

    private final String key;
    private final String value;
    private final String data;
    private final String type;
    private boolean replication = false;
    private boolean compactor = false;
    private boolean pool = false;

    /**
     * Set a registry entry.
     * 
     * @param key
     *            Registry Key
     * 
     * @param value
     *            Value for that Registry key
     * @param data
     *            Data for the value
     * 
     * @param type
     *            Type of the data
     * @param compactor
     *            True to restart need on cleanup.
     * @param pool
     *            True to restart need on cleanup.
     * @param replication
     *            True to restart need on cleanup.
     */
    public RegistryEntry(
            final String key,
            final String value,
            final String data,
            final String type,
            final boolean compactor,
            final boolean pool,
            final boolean replication) {
        this.key = key;
        this.value = value;
        this.data = data;
        this.type = type;
        this.compactor = compactor;
        this.pool = pool;
        this.replication = replication;
    }

    public RegistryEntry(final String key, final String value, final String data, final String type) {
        this(key, value, data, type, false, false, false);
    }

    public String getWriteCommand() {
        if ((data == null) || (type == null)) {
            throw new IllegalStateException("One needs to specify both data & type in constructor!");
        }
        return String.format("add %s /v %s /t %s /d %s /f", key, value, type, data);
    }

    public String getReadCommand() {
        return "QUERY " + key;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public String getData() {
        return data;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "RegistryEntry [key="
               + key
               + ", value="
               + value
               + ", data="
               + data
               + ", type="
               + type
               + "]";
    }

    public boolean isPool() {
        return pool;

    }

    public boolean isCompactor() {
        return compactor;
    }

    public boolean isReplication() {
        return replication;

    }

}
