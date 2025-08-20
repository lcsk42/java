package com.lcsk42.frameworks.starter.common.util;

import com.lcsk42.frameworks.starter.base.Singleton;
import com.lcsk42.frameworks.starter.base.constant.StringConstant;
import com.lcsk42.frameworks.starter.common.snowflake.Snowflake;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Utility class for generating various types of unique identifiers.
 * Provides methods for UUID generation, Snowflake ID generation, and worker/datacenter ID calculation.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class IdUtil {

    /**
     * Generates a random UUID (Universally Unique Identifier) in standard hyphenated format.
     * Example: "550e8400-e29b-41d4-a716-446655440000"
     *
     * @return A randomly generated UUID string with hyphens
     */
    public static String generateStandardUuid() {
        return UUID.randomUUID().toString();
    }

    /**
     * Generates a random UUID without hyphens for more compact representation.
     * Example: "550e8400e29b41d4a716446655440000"
     *
     * @return A randomly generated UUID string without hyphens
     */
    public static String generateCompactUuid() {
        return UUID.randomUUID().toString().replace(StringConstant.DASHED, StringUtils.EMPTY);
    }

    /**
     * Generates a worker ID based on host information or random value if host information is unavailable.
     * Uses hostname and MAC address to create a deterministic ID when possible.
     * Falls back to random value within the specified bit range if host information cannot be obtained.
     *
     * @param workerBits Number of bits allocated for worker ID (typically 5-10 bits)
     * @return A worker ID within the specified bit range (0 to 2^workerBits - 1)
     * @throws RuntimeException if network information cannot be accessed (handled internally)
     */
    public static long generateWorkerId(long workerBits) {
        try {
            String hostname = InetAddress.getLocalHost().getHostName();
            NetworkInterface network = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
            byte[] mac = network.getHardwareAddress();
            int hashCode = hostname.hashCode();
            if (mac != null) {
                for (byte b : mac) {
                    hashCode += b;
                }
            }
            long maxWorkerId = ~(-1L << workerBits);
            return (hashCode & maxWorkerId);
        } catch (Exception e) {
            long maxWorkerId = ~(-1L << workerBits);
            return ThreadLocalRandom.current().nextLong(0, maxWorkerId + 1);
        }
    }

    /**
     * Generates a datacenter ID based on IP address or random value if IP information is unavailable.
     * Uses host IP address to create a deterministic ID when possible.
     * Falls back to random value within the specified bit range if IP information cannot be obtained.
     *
     * @param datacenterBits Number of bits allocated for datacenter ID (typically 5 bits)
     * @return A datacenter ID within the specified bit range (0 to 2^datacenterBits - 1)
     * @throws RuntimeException if network information cannot be accessed (handled internally)
     */
    public static long generateDatacenterId(long datacenterBits) {
        try {
            String ip = InetAddress.getLocalHost().getHostAddress();
            int hashCode = ip.hashCode();
            long maxDatacenterId = ~(-1L << datacenterBits);
            return (hashCode & maxDatacenterId);
        } catch (Exception e) {
            long maxDatacenterId = ~(-1L << datacenterBits);
            return ThreadLocalRandom.current().nextLong(0, maxDatacenterId + 1);
        }
    }

    /**
     * Gets or creates a singleton instance of Snowflake ID generator.
     * If no instance exists in the Singleton registry, creates a new one and registers it.
     *
     * @return Singleton instance of Snowflake ID generator
     */
    public static Snowflake getSnowflake() {
        Snowflake snowflake = Singleton.get(Snowflake.class.getName());
        if (Objects.isNull(snowflake)) {
            snowflake = new Snowflake();
            Singleton.put(snowflake);
        }
        return snowflake;
    }

    /**
     * Generates the next unique ID using the Snowflake algorithm.
     * Convenience method that combines getSnowflake() and nextId() calls.
     *
     * @return Next unique ID as a long value
     */
    public static long getSnowflakeNextId() {
        return getSnowflake().nextId();
    }

    /**
     * Generates the next unique ID using the Snowflake algorithm and returns it as a string.
     * Convenience method that combines getSnowflake() and nextIdString() calls.
     *
     * @return Next unique ID as a string representation
     */
    public static String getSnowflakeNextIdString() {
        return getSnowflake().nextIdString();
    }
}