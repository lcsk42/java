package com.lcsk42.frameworks.starter.common.snowflake;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the decomposed information of a Snowflake ID.
 * <p>
 * Snowflake ID is a distributed unique ID generation algorithm developed by Twitter,
 * which typically consists of:
 * <ul>
 *   <li><b>timestamp</b> - milliseconds since epoch (custom epoch is often used)</li>
 *   <li><b>datacenterId</b> - identifier of the data center</li>
 *   <li><b>workerId</b> - identifier of the worker machine</li>
 *   <li><b>sequence</b> - incremental sequence number within the same millisecond</li>
 * </ul>
 * This class provides a structured breakdown of these components.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Snowflake_ID">Snowflake ID on Wikipedia</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SnowflakeIdInfo {
    /**
     * The timestamp component of the Snowflake ID (milliseconds since epoch)
     */
    private long timestamp;

    /**
     * The data center identifier component
     */
    private long datacenterId;

    /**
     * The worker machine identifier component
     */
    private long workerId;

    /**
     * The sequence number component (for IDs generated in the same millisecond)
     */
    private long sequence;
}