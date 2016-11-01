package com.toptal;

import org.junit.Assert;

import java.util.List;

public class Helpers {

    public static <T> void assertContainsInOrder(List<T> tokens, T ... values) {
        Assert.assertArrayEquals(tokens.toArray(), values);
    }

}
