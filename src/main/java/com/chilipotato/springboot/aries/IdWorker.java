package com.chilipotato.springboot.aries;

import org.springframework.stereotype.Component;

@Component
public class IdWorker {
    private long epoch;
    private long projectId;
    private long nodeId;

    private final int projectIdBits = 9;
    private final int nodeIdBits = 3;
    private final int sequenceBits = 9;

    private final long maxNodeId = -1L ^ (-1L << nodeIdBits);
    private final long maxProjectId = -1L ^ (-1L << projectIdBits);
    private final long sequenceMask = -1L ^ (-1L << sequenceBits);

    private final int nodeIdShift = sequenceBits;
    private final int projectIdShift = sequenceBits + nodeIdBits;
    private final int timestampLeftShift = sequenceBits + nodeIdBits + projectIdBits;

    private long sequence = 0L;
    private long lastTimestamp = -1L;

    public IdWorker(IdWorkerConfigBean idWorkerProperties) {
        if (idWorkerProperties.getProjectid() > maxProjectId || idWorkerProperties.getProjectid() < 0) {
            throw new IllegalArgumentException(
                    String.format("projectId can't be greater than %d or less than 0.", maxProjectId));
        }
        if (idWorkerProperties.getNodeid() > maxNodeId || idWorkerProperties.getNodeid() < 0) {
            throw new IllegalArgumentException(
                    String.format("nodeId can't be greater than %d or less than 0.", maxNodeId));
        }
        this.epoch = idWorkerProperties.getEpoch();
        this.nodeId = idWorkerProperties.getNodeid();
        this.projectId = idWorkerProperties.getProjectid();
    }

    public synchronized String nextId() {
        long timestamp = System.currentTimeMillis();

        if (timestamp < lastTimestamp) {
            throw new RuntimeException(String.format(
                    "Clock moved backwards. Refusing to generate id for %d milliseconds.", lastTimestamp - timestamp));
        }

        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }

        lastTimestamp = timestamp;

        return String.valueOf(((timestamp - epoch) << timestampLeftShift) | (projectId << projectIdShift)
                | (nodeId << nodeIdShift) | sequence);
    }

    private long tilNextMillis(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }
}
