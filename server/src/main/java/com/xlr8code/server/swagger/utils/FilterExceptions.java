package com.xlr8code.server.swagger.utils;

import com.xlr8code.server.common.exception.ApplicationException;
import com.xlr8code.server.filter.exception.NoSuchSortableFieldException;
import com.xlr8code.server.filter.exception.PageNumberFormatException;
import com.xlr8code.server.filter.exception.PageSizeFormatException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FilterExceptions {

    public static List<Class<? extends ApplicationException>> getAllFilterExceptions() {
        return List.of(
                PageSizeFormatException.class,
                PageNumberFormatException.class,
                NoSuchSortableFieldException.class
        );
    }

}
