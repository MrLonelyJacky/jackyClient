package com.jacky.rpc.studyTest;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Arrays;
import java.util.List;

/**
 * @description:
 * @author: jacky
 * @create: 2022-11-23 11:02
 **/
public class MethodHandlerTest {
    public static void lamadaTest(){
        List<String> s= Arrays.asList("a","b","c");
        s.stream().forEach((String str) ->{System.out.println(str);});
    }

    public static void println(Object obj,String s) throws Throwable{
        MethodType mt=MethodType.methodType(void.class,String.class);
        MethodHandle methodHandle= MethodHandles.lookup().findVirtual(obj.getClass(),"println",mt);
        methodHandle.bindTo(obj).invokeExact(s);
    }

    public static void main(String[] args) throws Throwable{
        String test="Hello World";
        println(new TestA(),test);
        println(new TestB(),test);
        println(System.out,test);

        lamadaTest();
    }
}


class TestA{
    public void println(String s){
        System.out.println("TestA println:"+s);
    }
}

class TestB{
    public void println(String s){
        System.out.println("TestB println:"+s);
    }
}
