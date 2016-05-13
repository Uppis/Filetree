package com.vajasoft.filetree;

import java.io.*;
import java.util.regex.*;

public class RegexFileFilter implements FileFilter {
    private static final String USAGE = "Properties of " + RegexFileFilter.class.getName() + ":\n" +
                                        "\t-Dinclude.pattern=<regex>, default: all included\n" +
                                        "\t-Dexclude.pattern=<regex>, default: nothing excluded\n";
    private Pattern include;
    private Pattern exclude;

    public RegexFileFilter() {
        this(System.getProperty("include.pattern"), System.getProperty("exclude.pattern"));
        if (include == null && exclude == null)
            System.out.println(USAGE);
    }

    public RegexFileFilter(String inc, String exc) {
        if (inc != null)
            include = Pattern.compile(inc);
        if (exc != null)
            exclude = Pattern.compile(exc);
    }
    public boolean accept(File f) {
        return f.isDirectory() ||
                ((include == null || include.matcher(f.getName()).matches()) &&
                 (exclude == null || !exclude.matcher(f.getName()).matches()));
    }
}
