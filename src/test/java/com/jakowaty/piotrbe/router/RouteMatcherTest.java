package com.jakowaty.piotrbe.router;

import org.junit.Assert;
import org.junit.Test;

public class RouteMatcherTest {

    @Test
    public void testRouteMatcherMatch()
    {
        RouteMatcher matcher = new RouteMatcher();

        Assert.assertTrue(
        "Those two match 1!",
                matcher.match("/test-param/id", "/test-param/{:param::}")
        );

        Assert.assertTrue(
                "Those two match 2!",
                matcher.match("/param/id", "/param/{:param::}")
        );

        Assert.assertTrue(
                "Those two match 3!",
                matcher.match(
          "/positional/param/test/now",
            "/positional/{:param::}/test/{:param2::}")
        );
    }

    @Test
    public void testRouteMatcherNotMatch()
    {
        RouteMatcher matcher = new RouteMatcher();

        Assert.assertFalse(
                "Those two not match1!",
                matcher.match("/param/param}", "/param/{param}")
        );

        Assert.assertFalse(
                "Those two not match2!",
                matcher.match("/param/{param}/1", "/param/{param}")
        );

        Assert.assertFalse(
                "Those two not match3!",
                matcher.match("param/param", "/param/param")
        );
    }

    @Test
    public void testRouteMatcherExtractParams()
    {

    }
}
