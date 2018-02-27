package com.codesync.receiver;

/**
 * Created by dharmendra on 8/3/17.
 */
public class Main {

    /*public static void main(String[] args) throws Exception {

        ClassLoader classLoader = Main.class.getClassLoader();
        String slashedName = "com/xaxis/xanadu/meta/entity/Test";
        String className = "com.xaxis.xanadu.meta.entity.Test";
        String path = "/Users/dharmendra/xan/release/eng-ma-janus/xanadu-meta/xanadu-metadata-entity/target/classes/com/xaxis/xanadu/meta/entity/Test.class";
        byte[] data = FileUtils.readFileToByteArray(new File(path));
        ClassFileTransformer classFileTransformer = new ClassPreProcessorAgentAdapter();
        Field field = classFileTransformer.getClass().getDeclaredField("preProcessor");
        field.setAccessible(true);
        SpringLoadedPreProcessor f = (SpringLoadedPreProcessor) field.get(classFileTransformer);
        byte[] newbytes = f.preProcess(classLoader, slashedName, classLoader.getClass().getProtectionDomain(), data);

        check(classLoader, className);
        *//*TypeRegistry typeRegistry = TypeRegistry.getTypeRegistryFor(classLoader);
        if (typeRegistry == null) {
            throw new RuntimeException("no typeRegistry for this application, please check your codesync.xml configuration");
        }
        ReloadableType rtype = typeRegistry.addType(className, dto.data);*//*
        Method method = ClassLoader.class.getDeclaredMethod("defineClass", new Class[]{String.class, ByteBuffer.class, ProtectionDomain.class});
        method.setAccessible(true);
        //method.invoke(classLoader, className, ByteBuffer.wrap(rtype.bytesLoaded), classLoader.getClass().getProtectionDomain());

        method.invoke(classLoader, className, ByteBuffer.wrap(newbytes), classLoader.getClass().getProtectionDomain());

        check(classLoader, className);

        method = ClassLoader.class.getDeclaredMethod("defineClass", new Class[]{String.class, ByteBuffer.class, ProtectionDomain.class});
        method.setAccessible(true);
    }

    public static void check(ClassLoader classLoader, String className){
        boolean exist = false;
        try {

            Class.forName(className, false, classLoader);
            exist = true;
        } catch (Exception e) {

        }

        System.out.println("exist = " + exist);
    }*/
}
