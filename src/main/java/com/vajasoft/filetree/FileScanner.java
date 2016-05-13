package com.vajasoft.filetree;

import java.io.*;

public class FileScanner {
    private FileFilter filter;
    private ScanHandler handler;
    private long handled;

    public FileScanner(FileFilter f, ScanHandler h) {
        filter = f;
        handler = h;
    }

    public void scanDir(File dir) throws IOException {
        File[] files = dir.listFiles(filter);// filter can be null !
        for (File f : files) {
            if (f.isDirectory()) {
                scanDir(f);
            }
            else {
                handler.scanTarget(new FileTarget(f));
                handled++;
            }
        }
    }

    public static void main(String[] args) throws IOException {
        if (args.length > 0) {
            FileFilter filter = (FileFilter) getPlugin(System.getProperty("filetree.filter"));
            ScanHandler handler = (ScanHandler) getPlugin(System.getProperty("filetree.handler", "filetree.DefaultScanHandler"));
            try {
                handler.checkUsage();
                File root = new File(args[0]);
                if (root.isDirectory()) {
                    handler.setRoot(root);
                    FileScanner scanner = new FileScanner(filter, handler);
                    System.out.println("\nScanning files from " + root.getAbsolutePath() + " ...\n");
                    scanner.scanDir(root);
                    System.out.println("\nHandled " + scanner.getHandled() + " files\n");
                } else
                    errorExit("Error: " + args[0] + " is not directory !", 2);
            } catch (UsageException ex) {
                errorExit(usage() + ex.getMessage(), 1);
            }
        } else
            errorExit(usage(), 1);
    }

    private static Object getPlugin(String className) {
        Object ret = null;
        if (className != null) {
            try {
                Class cl = Class.forName(className);
                ret = cl.newInstance();
            } catch (Exception ex) {
                errorExit("Can't create plugin: " + ex, 3);
            }
        }
        return ret;
    }

    private static String usage() {
        return "Usage: java [properties] filetree.FileScanner <dir>\n" +
               "\t-Dfiletree.filter=[java.io.FileFilter class]\n" +
               "\t-Dfiletree.handler=[filetree.Handler class]\n";
   }

    private static void errorExit(String msg, int exitCode) {
        System.err.println(msg);
        System.exit(exitCode);
    }

    public long getHandled() {
        return handled;
    }
}
