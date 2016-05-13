package com.vajasoft.filetree;

import java.io.*;

public class ClassFileFilter implements FileFilter {
    public boolean accept(File f) {
        return f.isDirectory() || f.getName().endsWith(".class");
    }
}
