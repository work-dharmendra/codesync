package com.codesync.receiver.loader;

import com.codesync.common.Constants;
import java.util.logging.Level;
import com.codesync.receiver.dto.LoadFileDTO;

import com.google.inject.Inject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.security.ProtectionDomain;
import java.util.Vector;
import java.util.logging.Logger;
import org.springsource.loaded.ReloadableType;
import org.springsource.loaded.SpringLoaded;
import org.springsource.loaded.TypeRegistry;
import org.springsource.loaded.agent.ClassPreProcessorAgentAdapter;
import org.springsource.loaded.agent.SpringLoadedPreProcessor;

import static java.util.logging.Level.FINE;

/**
 * @author Dharmendra.Singh.
 */
public class SpringClassLoader implements ClassLoad {

    private static Logger LOG = Logger.getLogger(Constants.LOG_NAME);

    @Inject
    ClassPreProcessorAgentAdapter classPreProcessorAgentAdapter;

    @Override
    public boolean loadClass(ClassLoader classLoader, LoadFileDTO dto) {
        LOG.log(FINE, "SpringClassLoader.loadClass starts");
        //SpringLoaded expect className in com.foo.Bar, replace \ with dot and .class with empty string.
        String className = dto.relativePath.replaceAll("\\\\", ".").replace(".class", "");

        String slashedName = dto.relativePath.replace(".class", "");
        String dottedClassName = className.replaceAll("/", ".");
        LOG.log(Level.FINE, "slassed name = {0}, dottedClassName = {1}", new Object[]{slashedName, dottedClassName});

        if (isAlreadyLoaded(classLoader, dottedClassName)) {
            LOG.log(FINE, className + " is already loaded");
            SpringLoaded.loadNewVersionOfType(classLoader, className, dto.data);
        } else {
            LOG.log(FINE, className + " is not loaded earlier");
            try {

                Field field = classPreProcessorAgentAdapter.getClass().getDeclaredField("preProcessor");
                field.setAccessible(true);
                TypeRegistry typeRegistry = TypeRegistry.getTypeRegistryFor(classLoader);
                if (typeRegistry == null) {
                    SpringLoadedPreProcessor f = (SpringLoadedPreProcessor) field.get(classPreProcessorAgentAdapter);

                    throw new RuntimeException("no typeRegistry for this application, please check your codesync.json configuration");
                }
                ReloadableType rtype = typeRegistry.addType(dottedClassName, dto.data);
                //ReloadableType rtype = typeRegistry.getReloadableType(className, true);
                LOG.finest("rtype " + rtype);
                Method method = ClassLoader.class.getDeclaredMethod("defineClass", new Class[]{String.class, ByteBuffer.class, ProtectionDomain.class});
                method.setAccessible(true);
                method.invoke(classLoader, dottedClassName, ByteBuffer.wrap(rtype.bytesLoaded), classLoader.getClass().getProtectionDomain());
                LOG.log(FINE, className + " is successfully loaded.");
            } catch (Exception e) {
                LOG.log(Level.SEVERE, "exception while loading class", e);
            }
        }

        LOG.log(FINE, "SpringClassLoader.loadClass ends");
        return true;
    }

    /**
     * It checks if class is already loaded or not. It expect class name in com.foo.Bar.class.
     *
     * @param classLoader
     * @param className
     * @return
     */
    private static boolean isAlreadyLoaded(ClassLoader classLoader, String className) {
        boolean exists = true;
        try {
            LOG.fine("checking for " + className);

            Class.forName(className, false, classLoader);
        } catch (Exception e) {
            //e.printStackTrace();
            exists = false;
            //printClasses(classLoader);
        }
        return exists;
    }

    public static class CustomClassLoader extends ClassLoader {
        public CustomClassLoader(ClassLoader classLoader) {
            super(classLoader);
        }

        public void loadNewClass(String name, byte[] data) {

            Class<?> cl = defineClass(name, data, 0, data.length, null);
            resolveClass(cl);

            try {
                for (Method m : cl.getMethods()) {
                    System.out.println("m.getName() = " + m.getName());
                }
                System.out.println("----------------------------");
                Class<?> aClass = Class.forName(name);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }

        /*@Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            //LOG.info("WWW"+String.valueOf(super.findLoadedClass(name)));
            return defineClass(name, data, 0, data.length, null);
        }

        @Override
        public Class<?> loadClass(String name) throws ClassNotFoundException {

            return defineClass(name, data, 0, data.length, null);
        }
*/

    }

    public static void printClasses(ClassLoader classLoader) {
        try {
            Field f = classLoader.getClass().getDeclaredField("classes");
            f.setAccessible(true);

            Vector<Class> classes = (Vector<Class>) f.get(classLoader);
            for (Class aClass : classes) {
                LOG.info(aClass.getName());
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {


    }
}
