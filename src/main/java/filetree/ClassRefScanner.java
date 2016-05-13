package filetree;

import com.vajasoft.classfile.ClassFile;
import java.io.*;
import java.util.*;

public class ClassRefScanner extends DefaultScanHandler {
    private static final String USAGE = "Properties of "+ClassRefScanner.class.getName()+":\n" +
                                        "\t-Dscan.natives=[true|false]\n" +
                                        "\t-Dscan.references=[reference | @<filename>]\n";

    private boolean scanNatives;
    private Collection<String> references = new ArrayList<String>();
    private Writer out;

    public ClassRefScanner() {
        scanNatives = Util.getBooleanProperty("scan.natives");
        Util.loadReferences("scan.references", references);
        out = new OutputStreamWriter(System.out);
    }

    public ClassRefScanner(Collection<String> refs, boolean natives) {
        references.addAll(refs);
        scanNatives = natives;
    }

    public boolean scanTarget(ScanTarget target) throws IOException {
        boolean ret = true;
        String pathName = target.getName();

        if (pathName.endsWith(".class")) {
            ClassFile cf = new ClassFile(target.getInputStream());
            String cfn = cf.getClassFileName();
            if (scanNatives && cf.hasNatives())
                writeln("native:\t" + cfn);
            for (String r : Util.findReferences(references, cf))
                writeln(r + ":\t" + cfn);
        } else
            System.err.println("Not a class file: " + target);
        return ret;
   }

   public void checkUsage() throws UsageException {
       if (references.size() == 0)
           throw new UsageException(USAGE);
   }

   protected Writer write(String s) throws IOException {
       out.append(s).flush();
       return out;
   }

   protected Writer writeln(String s) throws IOException {
       return write(s + "\r\n");
   }
}
