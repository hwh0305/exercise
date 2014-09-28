package org.hao.test.guava.collections;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.reflect.ClassPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
//        log.trace("TRACE");
//        log.info("INFO");
//        log.debug("DEBUG");
//        log.warn("WARN");
//        log.error("ERROR");
//
//        new Thread("TEST-LOGBACK-1") {
//
//            @Override
//            public void run() {
//                log.trace("TRACE");
//                log.info("INFO");
//                log.debug("DEBUG");
//                log.warn("WARN");
//                log.error("ERROR");
//            }
//
//        }.start();
//
//        new Thread("TEST-LOGBACK-2") {
//
//            @Override
//            public void run() {
//                log.trace("TRACE");
//                log.info("INFO");
//                log.debug("DEBUG");
//                log.warn("WARN");
//                log.error("ERROR");
//            }
//        }.start();
        Set<String> set = ImmutableSet.of("test");

        Map<String, Integer> maps = Maps.newHashMap();
        List<Long> list = Lists.newArrayList();

        ClassPath cp = ClassPath.from(Thread.currentThread().getContextClassLoader());

        ImmutableSet<ClassPath.ClassInfo> classPath = cp.getAllClasses();
        Iterator<ClassPath.ClassInfo> cpi = classPath.iterator();
        while (cpi.hasNext()) {
        ClassPath.ClassInfo ci = cpi.next();
        System.out.println(ci.getResourceName());
        }
    }
}
