package filetree;

import java.io.*;

public interface ScanHandler {
    public void checkUsage() throws UsageException;
    public void setRoot(File root);
    public boolean scanTarget(ScanTarget target) throws IOException;
}
