package com.lcsk42.frameworks.starter.common.snowflake;

import com.lcsk42.frameworks.starter.common.util.IdUtil;
import org.apache.commons.lang3.Validate;

import java.io.Serializable;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Twitter Snowflake ID generator implementation.
 * <p>
 * Generates unique 64-bit IDs composed of:
 * <ul>
 *   <li>Timestamp (milliseconds since custom epoch)</li>
 *   <li>Datacenter ID (5 bits)</li>
 *   <li>Worker ID (5 bits)</li>
 *   <li>Sequence number (12 bits)</li>
 * </ul>
 *
 * <p>Structure: [1 unused bit][41-bit timestamp][5-bit datacenter ID][5-bit worker ID][12-bit sequence]
 *
 * <p>Thread-safe implementation that can generate up to 4096 unique IDs per millisecond per worker.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Snowflake_ID">Snowflake ID Algorithm</a>
 */
public class Snowflake implements Serializable, IdGenerator {
    // Custom epoch (2020-01-01 00:00:00 UTC)
    private final static long START_TIMESTAMP = 1577808000000L;
    // Bit allocation configuration
    private final static long WORKER_ID_BITS = 5L;      // Bits allocated for worker ID
    private final static long DATACENTER_ID_BITS = 5L;  // Bits allocated for datacenter ID
    private final static long SEQUENCE_BITS = 12L;      // Bits allocated for sequence number
    // Maximum allowable values
    private final static long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS);          // Maximum worker ID (31)
    private final static long MAX_DATACENTER_ID = ~(-1L << DATACENTER_ID_BITS);  // Maximum datacenter ID (31)
    // Bit shift values for ID composition
    private final static long WORKER_ID_SHIFT = SEQUENCE_BITS;
    private final static long DATACENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;
    private final static long TIMESTAMP_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATACENTER_ID_BITS;
    // Sequence mask (0b111111111111=0xfff=4095)
    private final static long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);
    // Instance configuration
    private final long workerId;       // Worker identifier (0-31)
    private final long datacenterId;   // Datacenter identifier (0-31)
    private long sequence = 0L;        // Sequence number (0-4095)
    private long lastTimestamp = -1L;  // Last timestamp used for ID generation

    /**
     * Default constructor that initializes with automatically generated worker and datacenter IDs.
     * Uses {@link IdUtil} to generate valid IDs within the allowed bit ranges.
     */
    public Snowflake() {
        this(IdUtil.generateWorkerId(WORKER_ID_BITS), IdUtil.generateWorkerId(DATACENTER_ID_BITS));
    }

    /**
     * Constructs a Snowflake ID generator instance.
     *
     * @param workerId     Worker machine ID (0 ≤ workerId ≤ 31)
     * @param datacenterId Datacenter ID (0 ≤ datacenterId ≤ 31)
     * @throws IllegalArgumentException if IDs are out of valid range
     */
    public Snowflake(long workerId, long datacenterId) {
        Validate.isTrue(workerId >= 0 && workerId <= MAX_WORKER_ID,
                "worker Id can't be greater than %d or less than 0", MAX_WORKER_ID);
        Validate.isTrue(datacenterId >= 0 && datacenterId <= MAX_DATACENTER_ID,
                "datacenter Id can't be greater than %d or less than 0", MAX_DATACENTER_ID);
        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }

    /**
     * Generates the next unique ID (thread-safe).
     *
     * @return 64-bit Snowflake ID
     * @throws RuntimeException if system clock moves backwards
     */
    @Override
    public synchronized long nextId() {
        long timestamp = timeGen();
        // Detect clock drift backwards
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(
                    String.format("Clock moved backwards. Refusing to generate id for %d milliseconds",
                            lastTimestamp - timestamp));
        }
        // Handle same-millisecond collisions
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & SEQUENCE_MASK;
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);  // Sequence exhausted, wait for next millisecond
            }
        } else {
            // Initialize sequence with random value to avoid predictable IDs
            sequence = ThreadLocalRandom.current().nextLong(1, 3);
        }
        lastTimestamp = timestamp;
        // Compose ID from components
        return ((timestamp - START_TIMESTAMP) << TIMESTAMP_SHIFT)
                | (datacenterId << DATACENTER_ID_SHIFT)
                | (workerId << WORKER_ID_SHIFT)
                | sequence;
    }

    /**
     * Generates the next unique ID as a string.
     *
     * @return String representation of the Snowflake ID
     */
    @Override
    public synchronized String nextIdString() {
        return Long.toString(nextId());
    }

    /**
     * Blocks until next millisecond when sequence exhausted.
     *
     * @param lastTimestamp The last timestamp used
     * @return Current timestamp in milliseconds
     */
    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * Gets current time in milliseconds.
     *
     * @return Current timestamp in milliseconds
     */
    protected long timeGen() {
        return System.currentTimeMillis();
    }

    /**
     * Decomposes a Snowflake ID into its components.
     *
     * @param id The Snowflake ID to parse
     * @return {@link SnowflakeIdInfo} containing timestamp, datacenterId, workerId and sequence
     */
    public SnowflakeIdInfo parseId(long id) {
        return SnowflakeIdInfo.builder()
                .timestamp((id >> TIMESTAMP_SHIFT) + START_TIMESTAMP)
                .datacenterId((id >> DATACENTER_ID_SHIFT) & MAX_DATACENTER_ID)
                .workerId((id >> WORKER_ID_SHIFT) & MAX_WORKER_ID)
                .sequence(id & SEQUENCE_MASK)
                .build();
    }
}