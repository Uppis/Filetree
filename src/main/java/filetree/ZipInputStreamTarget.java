package filetree;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipInputStreamTarget implements ScanTarget {
    ZipInputStream zis;
    ZipEntry entry;

    public ZipInputStreamTarget(ZipInputStream is, ZipEntry e) {
        zis = is;
        entry = e;
    }

    public InputStream getInputStream() throws IOException {
        return zis;
    }

    public String getName() {
        return entry.getName();
    }

    public long getSize() {
        return entry.getSize();
    }

    public long getTime() {
        return entry.getTime();
    }

    @Override
    public String toString() {
        return getName();
    }
}
