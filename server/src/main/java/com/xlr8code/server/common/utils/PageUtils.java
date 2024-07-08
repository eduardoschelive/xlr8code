package com.xlr8code.server.common.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PageUtils {

    public static int zeroIndexPage(int page) {
        return page - 1;
    }

    public static int oneIndexPage(int page) {
        return page + 1;
    }

}
