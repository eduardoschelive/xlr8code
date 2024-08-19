package com.xlr8code.server.openapi.utils;

import com.xlr8code.server.common.exception.ApplicationException;
import com.xlr8code.server.filter.exception.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FilterExceptionsUtils {

    public static List<Class<? extends ApplicationException>> getAllFilterExceptions() {
        return List.of(
                BadFilterFormatException.class,
                InvalidBooleanFilterValueException.class,
                InvalidSortDirectionException.class,
                NoMatchingEntitiesFoundException.class,
                NoStrategyDefinedException.class,
                NoSuchFilterableFieldException.class,
                NoSuchSortableFieldException.class,
                PageNumberFormatException.class,
                PageSizeFormatException.class,
                RequiredParamSizeException.class,
                UnsupportedFilterOperationException.class,
                UnsupportedFilterOperationOnFieldException.class,
                InvalidInstantBoundsException.class,
                InvalidInstantFormatException.class
        );
    }

}
