package com.codesync.common;

import com.codesync.common.entity.Configurations;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.inject.Inject;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.AccessControlException;
import java.security.AccessController;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dharmendra.Singh
 */
public class Util {

    private static java.util.logging.Logger LOG = java.util.logging.Logger.getLogger(Constants.LOG_NAME);

    @Inject
    static volatile Configurations configurations;

    private static Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

    public static Configurations initConfigurations(Path file) {
        LOG.info("Util.initConfigurations : initializing configuration from " + file);

        Configurations configurations;

        try {
            //new JsonReader(new InputStreamReader())
            configurations = gson.fromJson(new JsonReader(new FileReader(file.toFile().getAbsolutePath())), Configurations.class);
        } catch (Exception e) {
            throw new RuntimeException("Exception while reading configuration file");
        }

        return configurations;
    }

    public static Configurations initConfigurations(InputStream inputStream) {
        LOG.finest("Util.initConfigurations : initializing configuration from inputStream ");

        Configurations configurations;

        try {
            configurations = gson.fromJson(new JsonReader(new InputStreamReader(inputStream)), Configurations.class);
        } catch (Exception e) {
            throw new RuntimeException("Exception while reading configuration file");
        }

        return configurations;
    }

    public static void main(String[] args) throws Exception {
        //List<String> dir = Arrays.asList( new String[]{"C:/a/b/c", "C:/a/b/d", "C:/a/b/e"});

        List<String> skipClass = new ArrayList<>();
        skipClass.add("$$_javassist");
        skipClass.add("$$EnhancerBySpringCGLIB");

        String classname = "com/dhar/automation/testrunner/TestScheduler$$EnhancerBySpringCGLIB$$121e1e51";
        for(String s : skipClass){
            System.out.println("s = " + s);
            System.out.println("classname = " + classname.contains(s));
        }


        String packageName = "com.codesync";
        String s = packageName.replaceAll("\\.", "/");
        System.out.println(s);

        System.out.println("agent loaded");
        System.out.println(getFileExtension("ac."));
        java.util.logging.ConsoleHandler handler = new java.util.logging.ConsoleHandler();
        handler.setFormatter(new java.util.logging.Formatter() {
            @Override
            public String format(java.util.logging.LogRecord record) {
                StringBuilder builder = new StringBuilder(1000);
                builder.append("[").append(record.getLevel()).append("] - ");
                builder.append("[").append(record.getSourceClassName()).append(".");
                builder.append(record.getSourceMethodName()).append("] - ");
                ;
                builder.append(formatMessage(record)).append("  ");
                if (record.getThrown() != null) {
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    pw.println();
                    record.getThrown().printStackTrace(pw);
                    pw.close();
                    builder.append(sw.toString());
                }
                builder.append("\n");
                return builder.toString();
            }
        });
        handler.setLevel(java.util.logging.Level.FINEST);
        LOG.addHandler(handler);
        LOG.setLevel(java.util.logging.Level.ALL);


    }

    /**
     * Creates the checksum.
     *
     * @param filePath the file path
     * @return the byte[]
     * @throws Exception the exception
     */
    public static byte[] createChecksum(String filePath) throws Exception {
        MessageDigest complete;
        try (InputStream fis = new FileInputStream(filePath)) {


            byte[] buffer = new byte[1024];
            complete = MessageDigest.getInstance("MD5");
            int numRead;
            do {
                numRead = fis.read(buffer);
                if (numRead > 0) {
                    complete.update(buffer, 0, numRead);
                }
            } while (numRead != -1);

            fis.close();
        } catch (Exception e) {
            //TODO
            throw e;
        }
        return complete.digest();
    }

    /**
     * Gets the m d5 checksum.
     *
     * @param filePath the file path
     * @return the m d5 checksum
     * @throws Exception the exception
     */
    public static String getMD5Checksum(String filePath) throws Exception {
        byte[] b = createChecksum(filePath);
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < b.length; i++) {
            result.append(Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1));
        }
        return result.toString();
    }

    /*private FileType getFileType(String fullPath) {
        if (fullPath.endsWith(".class")) {
            return FileType.CLASS;
        }

        if (fullPath.endsWith(".class")) {
            return FileType.CLASS;
        }
        if (fullPath.endsWith(".jsp")) {
            return FileType.JSP;
        }
        if (fullPath.endsWith(".class")) {
            return FileType.CLASS;
        }
        if (fullPath.endsWith(".class")) {
            return FileType.CLASS;
        }

        return FileType.UNKNOWN;
    }*/

    public static Path getRelativePath(Path child) {
        String parentDir = getClosestParent(child);
        return Paths.get(parentDir).relativize(child);
    }

    public static String getClosestParent(Path child) {
        for (String dir : configurations.getDirs()) {
            if (child.startsWith(dir)) {
                return dir;
            }
        }

        return null;
    }

    /**
     * It return extension of file or empty string if cannot determine file extension.
     *
     * @param name
     * @return
     */
    public static String getFileExtension(String name) {

        int lastIndex = name.indexOf(".");

        if (lastIndex != -1) {
            return name.substring(lastIndex + 1, name.length());
        }

        return "";
    }

    public static boolean checkFile(String filePath) {
        boolean isAccess = false;
        try {
            FilePermission fp = new FilePermission("file.txt", "read");
            AccessController.checkPermission(fp);
            isAccess = true;
        } catch (AccessControlException e) {

        }
        return isAccess;
    }

    public static boolean containsInList(String str, List<String> list){
        boolean result = false;

        if(list != null && list.size() > 0){
            for(String s : list){
                if(str.contains(s)){
                    result = true;
                }
            }
        }

        return result;
    }

}
