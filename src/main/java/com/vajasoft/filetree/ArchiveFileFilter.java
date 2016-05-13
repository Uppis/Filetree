package com.vajasoft.filetree;

import java.io.File;
import java.io.FileFilter;

public class ArchiveFileFilter implements FileFilter {
    public boolean accept(File f) {
        String name = f.getName().toLowerCase();
        return f.isDirectory() || name.endsWith(".zip") || name.endsWith(".jar");
    }
}
