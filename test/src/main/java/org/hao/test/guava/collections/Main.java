package org.hao.test.guava.collections;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;

public class Main {

    public static void main(String[] args) throws Exception {
        Set<String> set = ImmutableSet.of("test");
        System.out.println();

        Map<String, Integer> maps = Maps.newHashMap();
        List<Long> list = Lists.newArrayList();

        ClassPath cp = ClassPath.from(Thread.currentThread().getContextClassLoader());

        ImmutableSet<ClassInfo> classPath = cp.getAllClasses();
        Iterator<ClassInfo> cpi = classPath.iterator();
        while (cpi.hasNext()) {
            ClassInfo ci = cpi.next();
            System.out.println(ci.getResourceName());
        }
    }
}
