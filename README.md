# Aries
Aries is a service for generating unique IDs at high scale with some simple guarantees. These IDs are unique 64-bit unsigned integers, which are based on time, instead of being sequential. The full ID is composed of a timestamp, a project number, a node number, and a sequence number.

## Timestamp
The 42-bit system timestamp can be used for 139 years. The default value of the epoch timestamp is `1577836800000` (2020-01-01 00:00:00.000).

## Project number
The 9-bit project number supports 512 projects at the same time.

## Node number
The 3-bit node number allows a project to open 8 nodes at the same time.

## Sequence number
The 9-bit sequence number supports a node to generate 512 IDs within one millisecond.