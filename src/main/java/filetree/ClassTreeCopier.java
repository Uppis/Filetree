package filetree;

import com.vajasoft.classfile.ClassFile;
import java.io.*;
import java.nio.channels.*;
import java.util.*;

public class ClassTreeCopier implements ScanHandler {
    private static final String USAGE = "Properties of " + ClassTreeCopier.class.getName() + ":\n" +
                                        "\t-Dcopy.target=<dir name>\n" +
                                        "\t-Dexclude.natives=[true|false]\n" +
                                        "\t-Dexclude.references=<reference | @<filename>>\n";
    private File root;
    private File targetDir;
    private boolean excludeNatives = true;
    private Collection<String> excludeReferences = new ArrayList<String>();

    public ClassTreeCopier() throws IOException {
        File target = new File(System.getProperty("copy.target", "."));
        targetDir = target.getCanonicalFile();
        excludeNatives = Util.getBooleanProperty("exclude.natives");
        Util.loadReferences("exclude.references", excludeReferences);
    }

    public ClassTreeCopier(File target, Collection<String> excludeRefs, boolean noNatives) {
        targetDir = target;
        excludeReferences.addAll(excludeRefs);
        excludeNatives = noNatives;
    }

    public void setRoot(File r) {
        root = r;
    }

    public boolean scanTarget(ScanTarget target) throws IOException {
        boolean ret = true;
        String pathName = target.getName();

        if (pathName.endsWith(".class")) {
            ClassFile cf = new ClassFile(target.getInputStream());
            String cfn = cf.getClassFileName();
            if (excludeNatives && cf.hasNatives()) {
                System.out.println("native:\t" + cfn);
            } else {
                Collection<String> foundRefs = Util.findReferences(excludeReferences, cf);

                if (foundRefs.size() == 0) { // No unwanted references found => include in the copy
                    copyFile(target, cfn);
                } else
                    for (String r : foundRefs) {
                        System.out.println(r + ":\t" + cfn);
                    }
            }
        } else {
            String toName = pathName;
            String rootName = root.getPath();
            if (toName.regionMatches(true, 0, rootName, 0, rootName.length())) {
                toName = toName.substring(rootName.length());
                if (toName.startsWith(File.separator)) {
                    toName = toName.substring(1);
                }
            }
            copyFile(target, toName);
        }

        return ret;
    }

    public void checkUsage() throws UsageException {
        if (excludeReferences.size() == 0)
            throw new UsageException(USAGE);
    }

    private void copyFile(ScanTarget target, String toPathname) throws IOException {
        File toFile = new File(targetDir, toPathname);
        toFile.getParentFile().mkdirs();
        FileChannel in = ((FileInputStream)target.getInputStream()).getChannel();
        FileChannel out = (new FileOutputStream(toFile)).getChannel();
        in.transferTo(0, in.size(), out);
        out.close();
        in.close();
        toFile.setLastModified(target.getTime());
    }
}
