package com.codesync.sender.listener;

import com.codesync.common.Constants;
import com.codesync.common.Util;
import com.codesync.common.entity.Configurations;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.google.inject.Inject;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;



import static java.nio.file.StandardWatchEventKinds.*;

/**
 * @author Dharmendra.Singh
 */
public class FileWatcher {

    private final static Logger LOG = Logger.getLogger(Constants.LOG_NAME);

    @Inject
    Configurations configurations;

    @Inject
    Sender sender;

    private final WatchService watcher;

    public FileWatcher() throws IOException {

        this.watcher = FileSystems.getDefault().newWatchService();
    }

    /**
     * Register the given directory with the WatchService
     */
    private void register(Path dir) throws IOException {
        dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);

    }

    /**
     * Register the given directory, and all its sub-directories, with the
     * WatchService.
     */
    private void registerAll(final Path start) throws IOException {
        // register directory and sub-directories
        Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
                    throws IOException
            {
                //TODO ignore directory like svn
                if (!ignoreDir(dir)) {
                    register(dir);
                    return FileVisitResult.CONTINUE;
                }
                return FileVisitResult.SKIP_SIBLINGS;

            }
        });
    }

    /**
     * Watch all directory specified in codesync.xml. It recursively watch for all sub directory.
     *
     */
    public void watchDir(){

        for(String dir : configurations.getDirs()) {
            try {
                registerAll(Paths.get(dir));
            }catch (IOException e) {
                LOG.log(Level.SEVERE, "Exception sync file", e);

            }
        }
        //This map at a time can hold maximum of 1000 elements to reduce memory consumption.
        //This map holds all file synced for this event. WatchService generate too many events for
        //single file updation. If we find file with same md5 in syncedFile then don't sync that file.
        Map<String, String> syncedFile = new LinkedHashMap<String, String>(1000) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
                return size() > 1000;
            }
        };

        while (true){
            WatchKey key = null;
            try {

                key = watcher.take();
                //Whenever any application Intellij changes any file, it lock file for
                //very brief moment. If we immediately process this file then we will get
                //AccessDeniedException for file. This small sleep is to avoid those exception.
                //TODO this is not elegant and it needs better solution to handle this situation.
                //Thread.sleep(200);

                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();
                    Path parentDir = (Path)key.watchable();
                    Path fullPath = parentDir.resolve((Path) event.context());
                    String filePath = fullPath.toFile().getAbsolutePath();
                    checkFileAccess(filePath);
                    if (kind == ENTRY_CREATE) {
                        try {
                            if (Files.isDirectory(parentDir, LinkOption.NOFOLLOW_LINKS)) {
                                LOG.log(Level.FINEST, "FileWatcher.watchDir : registering dir = " + parentDir +" for watching.");
                                registerAll(parentDir);
                            }
                        } catch (IOException x) {
                            // ignore to keep sample readable
                        }
                    }
                    if(ignoreFile(fullPath) || new File(filePath).isDirectory()){
                        continue;
                    }
                    String md5 = Util.getMD5Checksum(filePath);
                    if (syncedFile.get(filePath) == null || !syncedFile.get(filePath).equals(md5)) {
                        syncedFile.put(filePath, md5);
                        LOG.log(Level.INFO, "syncing file = " + filePath);
                        sender.sync(fullPath);
                    }

                    //TODO if directory is created then register it for watching
                }


            } catch (Exception e) {
                LOG.log(Level.SEVERE, "exception while syncing file", e);
            }finally {
                if (key != null) {
                    key.reset();
                }
            }
        }

        /*for(String dir : configurations.getDirs()){
            try {
                registerAll(Paths.get(dir));

                //This map at a time can hold maximum of 1000 elements to reduce memory consumption.
                //This map holds all file synced for this event. WatchService generate too many events for
                //single file updation. If we find file with same md5 in syncedFile then don't sync that file.
                Map<String, String> syncedFile = new LinkedHashMap<String, String>(1000) {
                    @Override
                    protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
                        return size() > 1000;
                    }
                };
                while (true){
                    WatchKey key = null;
                    try {

                        key = watcher.take();
                        //Whenever any application Intellij changes any file, it lock file for
                        //very brief moment. If we immediately process this file then we will get
                        //AccessDeniedException for file. This small sleep is to avoid those exception.
                        //TODO this is not elegant and it needs better solution to handle this situation.
                        //Thread.sleep(200);

                        for (WatchEvent<?> event : key.pollEvents()) {
                            WatchEvent.Kind<?> kind = event.kind();
                            Path parentDir = (Path)key.watchable();
                            Path fullPath = parentDir.resolve((Path) event.context());
                            String filePath = fullPath.toFile().getAbsolutePath();
                            checkFileAccess(filePath);
                            if (kind == ENTRY_CREATE) {
                                try {
                                    if (Files.isDirectory(parentDir, LinkOption.NOFOLLOW_LINKS)) {
                                        LOG.log(Level.FINEST, "FileWatcher.watchDir : registering dir = " + parentDir +" for watching.");
                                        registerAll(parentDir);
                                    }
                                } catch (IOException x) {
                                    // ignore to keep sample readable
                                }
                            }
                            if(ignoreFile(fullPath) || new File(filePath).isDirectory()){
                                continue;
                            }
                            String md5 = Util.getMD5Checksum(filePath);
                            if (syncedFile.get(filePath) == null || !syncedFile.get(filePath).equals(md5)) {
                                syncedFile.put(filePath, md5);
                                LOG.log(Level.INFO, "syncing file = " + filePath);
                                sender.sync(fullPath);
                            }

                            //TODO if directory is created then register it for watching
                        }


                    } catch (Exception e) {
                        LOG.log(Level.SEVERE, "exception while syncing file", e);
                    }finally {
                        if (key != null) {
                            key.reset();
                        }
                    }
                }
            } catch (IOException e) {
                LOG.log(Level.SEVERE, "Exception sync file", e);

            }
        }*/
    }

    /**
     * Usually IDE/Editors(Sublime) generate extra file temporary whenever we save file.
     * We need to ignore those files and do not send those files for syncing.
     * It check whether file extension is in our allowed list.
     * @param path
     * @return true to ignore file otherwise false
     */
    private boolean ignoreFile(Path path){
        String name = path.getFileName().toString();
        String fileExtension = Util.getFileExtension(name);
        return !Arrays.asList(Constants.ALLOWED_EXTENSION).contains(fileExtension);
    }

    /**
     * Ignore some directory for watching like version control svn.
     * TODO : Need to update this method with more configuration
     * @param path
     * @return true to ignore directory otherwise false
     */
    private boolean ignoreDir(Path path){
        boolean ignore = false;

        String name = path.getFileName().toString();
        if(name.startsWith(".")){
            ignore = true;
        }
        return ignore;
    }

    /**
     * IDE like Intellij etc lock for sometime after compilation.
     * If we try to sync that file immediately then it throw exception.
     * This method check if file is readable or not and the return.
     * It only check for 1 second before returning because 1 sec is more than enough to release lock
     * in my testing.
     * @param filePath
     */
    public void checkFileAccess(String filePath){
        try{
            for(int i = 0; i < 10; i++){
                if(!Util.checkFile(filePath)){
                    Thread.sleep(100);
                }
            }
        }catch(Exception e){

        }
    }
}
