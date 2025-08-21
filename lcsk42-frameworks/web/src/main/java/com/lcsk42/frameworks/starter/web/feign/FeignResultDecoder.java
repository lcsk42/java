package com.lcsk42.frameworks.starter.web.feign;

import com.lcsk42.frameworks.starter.convention.errorcode.impl.BaseErrorCode;
import com.lcsk42.frameworks.starter.convention.exception.ServiceException;
import com.lcsk42.frameworks.starter.convention.result.Result;
import feign.Response;
import feign.codec.Decoder;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * Custom Feign response decoder that unwraps Result<T> response wrappers
 * and throws exceptions when the API response indicates a failure.
 */
@RequiredArgsConstructor
public class FeignResultDecoder implements Decoder {

    // The original decoder used by Feign (typically SpringDecoder)
    private final Decoder decoder;

    @Override
    public Object decode(Response response, Type type)
            throws IOException {

        // Retrieve the method metadata from the Feign request template
        final Method method = response.request().requestTemplate().methodMetadata().method();

        // Check if the declared return type is NOT Result, i.e., needs to unwrap from Result<T>
        final boolean isResult = method.getReturnType() != Result.class;

        if (isResult) {
            // Decode the response into a Result object first
            Result<?> result = (Result<?>) this.decoder.decode(response, Result.class);

            // If the response indicates success, return the actual data
            if (BooleanUtils.isTrue(result.isSucceed())) {
                return result.getData();
            } else {
                // Otherwise, throw a custom exception to signal the failure
                throw new ServiceException(result.getMessage(), BaseErrorCode.SERVICE_FEIGN_ERROR);
            }
        }

        // If the method itself returns a Result object, decode it as-is
        return this.decoder.decode(response, type);
    }
}
