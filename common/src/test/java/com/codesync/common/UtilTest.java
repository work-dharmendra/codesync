package com.codesync.common;

import com.codesync.common.entity.Configurations;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import mockit.Mocked;
import mockit.NonStrictExpectations;
import mockit.integration.junit4.JMockit;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.nio.file.Paths;
import java.util.Arrays;

/**
 * @author Dharmendra.Singh.
 */
@RunWith(JMockit.class)
public class UtilTest {

    @Mocked
    Configurations configurations;

    private Injector injector;

    @Before
    public void setUp() throws Exception {

        injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                requestStaticInjection(Util.class);
            }
        });

        new NonStrictExpectations() {
            {
                configurations.getDirs();
                result = Arrays.asList(new String[]{"C:/a/b/c", "C:/a/b/d", "C:/a/b/e"});
            }
        };

    }

    @Test
    public void testGetClosestParent() {
        Assert.assertEquals("C:/a/b/c", Util.getClosestParent(Paths.get("C:/a/b/c/d/e/f.class")));
        Assert.assertEquals("C:/a/b/d", Util.getClosestParent(Paths.get("C:/a/b/d/d/e/f.jsp")));
        Assert.assertEquals("C:/a/b/e", Util.getClosestParent(Paths.get("C:/a/b/e/d/e/f.htm")));
    }

    @Test
    public void testGetRelativePath(){
        Assert.assertEquals(Paths.get("d/e/f.class"), Util.getRelativePath(Paths.get("C:/a/b/c/d/e/f.class")));
        Assert.assertEquals(Paths.get("d/e/f.jsp"), Util.getRelativePath(Paths.get("C:/a/b/d/d/e/f.jsp")));
        Assert.assertEquals(Paths.get("d/e/f.htm"), Util.getRelativePath(Paths.get("C:/a/b/e/d/e/f.htm")));
    }
}
