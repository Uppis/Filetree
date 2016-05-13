package filetree;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipTarget implements ScanTarget {
    ZipFile file;
    ZipEntry entry;

    public ZipTarget(ZipFile f, ZipEntry e) {
        file = f;
        entry = e;
    }

    public InputStream getInputStream() throws IOException {
        return file.getInputStream(entry);
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

    public String toString() {
        return getName();
    }
}
