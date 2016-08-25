
package com.hp.test.framework.staf;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * 
 * TODO Describe this <code>FileDetails</code> type.
 */
public final class FileDetails {

    private final String name;
    private final String type;
    private final long size;
    private Date mdate;

    private final SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd-HH:mm:ss");

    public FileDetails(final HashMap<String, String> raw) {
        name = raw.get("name");
        type = raw.get("type");
        size = Long.valueOf(raw.get("size"));
        try {
            mdate = df.parse(raw.get("lastModifiedTimestamp"));
        } catch (final ParseException e) {
            // TODO: No operation done -- this correct?
        }
    }

    /**
     * Get file name.
     * 
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Get file type.
     * 
     * @return
     */
    public String getType() {
        return type;
    }

    /**
     * Get file size.
     * 
     * @return
     */
    public long getSize() {
        return size;
    }

    /**
     * Get file modify date.
     * 
     * @return
     */
    public Date getMdate() {
        return (Date) mdate.clone();
    }

    @Override
    public String toString() {
        return "FileDetails [name="
               + name
               + ", type="
               + type
               + ", size="
               + size
               + ", mdate="
               + mdate
               + "]";
    }
}
