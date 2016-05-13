package filetree;

import com.vajasoft.classfile.ClassFile;
import com.vajasoft.classfile.Reference;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class Util {
    public static boolean getBooleanProperty(String key, boolean defaultValue) {
        boolean ret = defaultValue;
        String p = System.getProperty(key);
        if (p != null) {
            p = p.trim();
            if (p.length() > 0) {
                ret = Boolean.parseBoolean(p);
            } else {
                ret = true;
            }
        }
        return ret;
    }

    public static boolean getBooleanProperty(String key) {
        return getBooleanProperty(key, false);
    }

    public static void loadReferences(String fromPropertyOrFile, Collection<String> to) {
        String ref = System.getProperty(fromPropertyOrFile);
        if (ref == null || ref.length() == 0) {
            ref = fromPropertyOrFile;
        }
        if (ref.charAt(0) == '@') {
            try {
                BufferedReader rdr = new BufferedReader(new FileReader(ref.substring(1)));
                for (String line = rdr.readLine(); line != null; line = rdr.readLine()) {
                    String trimmed = line.trim();
                    if (trimmed.length() > 0) {
                        to.add(prepareReference(line));
                    }
                }
            } catch (IOException ex) {
                System.err.println("Can't read file " + ref.substring(1) + ": " + ex);
            }
        } else {
            to.add(prepareReference(ref));
        }
    }

    public static void loadReferences2(String fromPropertyOrFile, Collection<Reference> to) {
        String ref = System.getProperty(fromPropertyOrFile);
        if (ref == null || ref.length() == 0) {
            ref = fromPropertyOrFile;
        }
        if (ref.charAt(0) == '@') {
            try {
                BufferedReader rdr = new BufferedReader(new FileReader(ref.substring(1)));
                for (String line = rdr.readLine(); line != null; line = rdr.readLine()) {
                    String trimmed = line.trim();
                    if (trimmed.length() > 0) {
                        Reference r = createReference(line);
                        if (r != null) {
                            to.add(r);
                        }
                    }
                }
            } catch (IOException ex) {
                System.err.println("Can't read file " + ref.substring(1) + ": " + ex);
            }
        } else {
            Reference r = createReference(ref);
            if (r != null) {
                to.add(r);
            }
        }
    }

    public static Collection<String> findReferences(Collection<String> refs, ClassFile fromClass) {
        Collection<String> ret = new ArrayList<String>();
        if (refs.size() > 0) {
            String[] crefs = fromClass.getReferences();
            for (String r : refs) {
                int ix = Arrays.binarySearch(crefs, r);
                if (ix >= 0 || (-(ix + 1) < crefs.length && crefs[  -(ix + 1)].startsWith(r))) {
                    ret.add(r);
                }
            }
        }
        return ret;
    }

    public static Collection<Reference> findReferences2(Collection<Reference> refs, ClassFile fromClass) {
        Collection<Reference> ret = new ArrayList<Reference>();
        if (refs.size() > 0) {
            Reference[] crefs = fromClass.getReferences2();
            for (Reference r : refs) {
                int ix = Arrays.binarySearch(crefs, r);
                ix = ix >= 0 ? ix : -(ix + 1);
                while (ix < crefs.length && crefs[ix].matchesTo(r)) {
                    ret.add(crefs[ix++]);
                }
            }
        }
        return ret;
    }

    public static Collection<Reference> findReferences(Collection<Reference> refsToFind, ClassFile fromClass, Collection<Reference> excludes) {
        Collection<Reference> ret = new ArrayList<Reference>();
        if (refsToFind.size() > 0) {
            Reference[] refsFromClass = fromClass.getReferences2();
            for (Reference r : refsToFind) {
                int ix = Arrays.binarySearch(refsFromClass, r);
                ix = ix >= 0 ? ix : -(ix + 1);
                while (ix < refsFromClass.length && refsFromClass[ix].matchesTo(r)) {
                    if (!matchesToFilter(refsFromClass[ix], excludes)) {
                        ret.add(refsFromClass[ix]);
                    }
                    ix++;
                }
            }
        }
        return ret;
    }

    private static boolean matchesToFilter(Reference ref, Collection<Reference> filters) {
        boolean ret = false;
        for (Reference filter : filters) {
            if (ref.matchesTo(filter)) {
                ret = true;
                break;
            }
        }
        return ret;
    }

    public static String prepareReference(String r) {
        return r.replace('.', '/');
    }

    public static Reference createReference(String r) {
        String[] tmp = r.replace('.', '/').split(" ");
        Reference ret = null;
        if (tmp.length == 1) {
            ret = new Reference(tmp[0]);
        } else if (tmp.length == 2) {
            ret = new Reference(tmp[0], tmp[1]);
        } else if (tmp.length == 3) {
            ret = new Reference(tmp[0], tmp[1], tmp[2]);
        } else {
            System.err.println("Illegal reference: " + r);
        }
        return ret;
    }

    private Util() {
    }
}
