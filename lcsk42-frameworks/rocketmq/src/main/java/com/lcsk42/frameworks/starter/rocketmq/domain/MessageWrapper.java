package com.lcsk42.frameworks.starter.rocketmq.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public final class MessageWrapper<T> implements Serializable {

    @Serial
    private final static long serialVersionUID = 1L;

    @NonNull
    private String key;

    @NonNull
    private T message;

    private String uuid = UUID.randomUUID().toString();

    private Long timestamp = System.currentTimeMillis();

    public static <T> MessageWrapper<T> of(String key, T message) {
        return new MessageWrapper<>(key, message);
    }
}
