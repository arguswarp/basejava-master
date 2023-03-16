package ru.javawebinar.basejava;

import ru.javawebinar.basejava.model.Resume;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainReflection {
    public static void main(String[] args) throws IllegalAccessException, InvocationTargetException {
        Resume resume = new Resume("123");
        Field field = resume.getClass().getDeclaredFields()[0];
        field.setAccessible(true);
        System.out.println(field.getName());
        System.out.println(field.get(resume));
        field.set(resume, "new uuid");
//        System.out.println(field.get(resume));
        //toString invocation via reflection
        Method method = resume.getClass().getDeclaredMethods()[1];
        System.out.println("\n" + method.invoke(resume, null));
    }
}
