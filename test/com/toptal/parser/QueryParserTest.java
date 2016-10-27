package com.toptal.parser;

import org.junit.Assert;
import org.junit.Test;

public class QueryParserTest {

    @Test
    public void shouldParseSimpleAddition() throws Exception {
        // given
        String givenAdditionWithExtraSpacing = " 2 + 1   ";

        // when
        QueryParser parser = new QueryParser();
        String result = parser.parse(givenAdditionWithExtraSpacing);

        // then
        Assert.assertEquals("3.0", result);
    }

}
