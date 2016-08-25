
package com.hp.test.framework.staf;

import java.util.Date;

/**
 * 
 * TODO Describe this <code>SimpleZipEntry</code> type.
 * 
 */
public class SimpleZipEntry implements Comparable<SimpleZipEntry> {

    private final String name;
    private final long length;
    private final Date mDate;
    private final long crc32;

    public SimpleZipEntry(final String name, final long length, final Date mDate, final long crc32) {
        this.name = name;
        this.length = length;
        this.mDate = (Date) mDate.clone();
        this.crc32 = crc32;
    }

    public String getName() {
        return name;
    }

    public long getLength() {
        return length;
    }

    public Date getmDate() {
        return (Date) mDate.clone();
    }

    public long getCrc32() {
        return crc32;
    }

    @Override
    public String toString() {
        return "ZipEntry [name="
               + name
               + ", length="
               + length
               + ", mDate="
               + mDate
               + ", crc32="
               + crc32
               + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + (int) (crc32 ^ (crc32 >>> 32));
        result = (prime * result) + (int) (length ^ (length >>> 32));
        result = (prime * result) + ((mDate == null) ? 0 : mDate.hashCode());
        result = (prime * result) + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SimpleZipEntry other = (SimpleZipEntry) obj;

        if (crc32 != other.crc32) {
            return false;
        }
        if (length != other.length) {
            return false;
        }

        if ((mDate == null) && (other.mDate == null)) {
            return true;
        }

        if (((mDate == null) && (other.mDate != null))
            || ((other.mDate == null) && (mDate != null))) {
            return false;
        }

        // Lets not compare seconds since STAF does not return them.
        // NOTE: clunky if added because findbugs appears to be stupid.
        if ((mDate != null) && (other.mDate != null)) {
            final long time = mDate.getTime() / 10000000;
            final long otherTime = other.mDate.getTime() / 10000000;
            if (time != otherTime) {
                return false;
            }
        }

        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }

        return true;
    }

    @Override
    public int compareTo(final SimpleZipEntry arg0) {
        return this.name.compareTo(arg0.name);

    }

}
