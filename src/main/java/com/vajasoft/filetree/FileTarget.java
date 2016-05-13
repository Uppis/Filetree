package com.vajasoft.filetree;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileTarget implements ScanTarget {
    File file;
    FileInputStream fis;

    public FileTarget(File f) {
        file = f;
    }

    public FileTarget(File f, FileInputStream is) {
        file = f;
        fis = is;
    }

    public InputStream getInputStream() throws IOException {
        if (fis == null) {
            fis = new FileInputStream(file);
        }
        return fis;
    }

    public String getName() {
        return file.getPath();
    }

    public long getSize() {
        return file.length();
    }

    public long getTime() {
        return file.lastModified();
    }

    @Override
    public String toString() {
        return getName();
    }
}
