package com.vajasoft.filetree;

import java.io.File;
import java.io.IOException;

public class DefaultScanHandler implements ScanHandler {
    public void checkUsage() throws UsageException {
    }
    public void setRoot(File f) {
    }
    public boolean scanTarget(ScanTarget t) throws IOException {
        System.out.println(t);
        return true;
    }
}
