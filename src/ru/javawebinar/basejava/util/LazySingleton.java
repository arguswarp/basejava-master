package ru.javawebinar.basejava.util;

public class LazySingleton {

    int i;
    volatile private static LazySingleton INSTANCE;

    double sim = Math.sin(13.);

    private LazySingleton() {

    }

    private static class  LazySingletonHolder{
        private static final LazySingleton INSTANCE = new LazySingleton();
    }

    public static LazySingleton getInstance() {
        return LazySingletonHolder.INSTANCE;
    }
//
//    public static LazySingleton getInstance() {
//        if (INSTANCE==null) {
//            synchronized (LazySingleton.class) {
//                if (INSTANCE==null) {
//                    int i=13;
//                    INSTANCE = new LazySingleton();
//                }
//            }
//        }
//        return INSTANCE;
//    }
}
