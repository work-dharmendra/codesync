package com.codesync.receiver.loader;

import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author Dharmendra.Singh.
 */
public class TestLoader {

    public static void main(String []args) throws Exception {

        CustomClassLoader customClassLoader = new CustomClassLoader(TestLoader.class.getClassLoader());

        customClassLoader.setData(Files.readAllBytes(Paths.get("C:\\Users\\dharmendra.singh\\git\\Practise\\spring\\target\\classes\\com\\dhar\\practise\\controller\\TestClass.class")));

        //customClassLoader.loadClass("com.dhar.practise.controller.TestClass");
        //customClassLoader.findClass("com.dhar.practise.controller.TestClass");
        customClassLoader.loadThisClass("com.dhar.practise.controller.TestClass");
        System.out.println();
        Class cl = Class.forName("com.dhar.practise.controller.TestClass", true, customClassLoader);
        for(Method m : cl.getMethods()){
            System.out.println(m.getName());
        }
        System.out.println(customClassLoader.getResourceAsStream("com/dhar/practise/controller/TestClass"));
    }

    public static class CustomClassLoader extends ClassLoader{
        private byte [] data;

        public void setData(byte[] data) {
            this.data = data;
        }

        public CustomClassLoader(ClassLoader classLoader) {
            super(classLoader);
        }

        public void loadThisClass(String name){
            resolveClass(defineClass(name, data, 0 , data.length));
        }

        /*@Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            //LOG.info("WWW"+String.valueOf(super.findLoadedClass(name)));
            return defineClass(name, data, 0, data.length, null);
        }

        @Override
        public Class<?> loadClass(String name) throws ClassNotFoundException {

            return defineClass(name, data, 0, data.length, null);
        }*/


    }
}
