package com.vajasoft.filetree;

import java.io.*;

public interface ScanTarget {
    public InputStream getInputStream() throws IOException;
    public String getName();
    public long getSize();
    public long getTime();
}
