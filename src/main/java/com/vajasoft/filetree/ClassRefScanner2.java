package com.vajasoft.filetree;

import com.vajasoft.classfile.ClassFile;
import com.vajasoft.classfile.Reference;
import java.io.*;
import java.util.*;

public class ClassRefScanner2 extends DefaultScanHandler {
    private static final String USAGE = "Properties of "+ClassRefScanner2.class.getName()+":\n" +
                                        "\t-Dscan.natives=[true|false]\n" +
                                        "\t-Dscan.references=[reference | @<filename>]\n";

    private boolean scanNatives;
    private Collection<Reference> references = new ArrayList<Reference>();
    private Writer out;

    public ClassRefScanner2() {
        scanNatives = Util.getBooleanProperty("scan.natives");
        Util.loadReferences2("scan.references", references);
    }

    public ClassRefScanner2(Writer out) {
        this();
        this.out = out;
    }

//    public ClassRefScanner2(Collection<Reference> refs, boolean natives) {
//        references.addAll(refs);
//        scanNatives = natives;
//    }

    public boolean scanTarget(ScanTarget target) throws IOException {
        boolean ret = true;
        String pathName = target.getName();

        if (pathName.endsWith(".class")) {
            ClassFile cf = new ClassFile(target.getInputStream());
            String cfn = cf.getClassFileName();
            if (scanNatives && cf.hasNatives())
                writeln("native:\t" + cfn);
            for (Reference r : Util.findReferences2(references, cf))
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
       Writer w = getWriter();
       w.append(s).flush();
       return w;
   }

   protected Writer writeln(String s) throws IOException {
       return write(s + "\r\n");
   }

   private Writer getWriter() {
       if (out == null)
           out = new OutputStreamWriter(System.out);
       return out;
   }
}
