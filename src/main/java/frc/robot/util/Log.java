package frc.robot.util;



import java.time.Duration;
import java.util.Arrays;
import java.util.function.Supplier;

import org.wpilib.datalog.*;
import org.wpilib.system.DataLogManager;

public class Log<T> {
    private final String name;
    private final Supplier<T> supplier;
    private T value;
    private final Duration delay;
    private long lastUpdate = 0;

    private final DataLogEntry logEntry;

    public Log(String name, Supplier<T> supplier, Duration delay) {
        this.name = name;
        this.supplier = supplier;
        this.delay = delay;

        this.value = supplier.get();

        if (isInteger()) {
            logEntry = new IntegerLogEntry(DataLogManager.getLog(), name);
        } else if (isDouble()) {
            logEntry = new DoubleLogEntry(DataLogManager.getLog(), name);
        } else if (isIntegerArray()) {
            logEntry = new IntegerArrayLogEntry(DataLogManager.getLog(), name);
        } else if (isDoubleArray()) {
            logEntry = new DoubleArrayLogEntry(DataLogManager.getLog(), name);
        } else {
            throw new IllegalArgumentException("Unsupported log type: " + value.getClass());
        }
    }

    public Log(String name, Supplier<T> value) {
        this(name, value, Duration.ofMillis(20));
    }

    public void update() {
        if (System.currentTimeMillis() - lastUpdate > delay.toMillis()) {
            value = supplier.get();
            lastUpdate = System.currentTimeMillis();
            if (isInteger()) {
                ((IntegerLogEntry) logEntry).append((Integer) value);
            } else if (isDouble()) {
                ((DoubleLogEntry) logEntry).append((Double) value);
            } else if (isIntegerArray()) {
                var array = Arrays.stream((Integer[]) value).mapToLong(Integer::longValue).toArray();
                ((IntegerArrayLogEntry) logEntry).append(array);
            } else if (isDoubleArray()) {
                var array = Arrays.stream((Double[]) value).mapToDouble(Double::doubleValue).toArray();
                ((DoubleArrayLogEntry) logEntry).append(array);
            }
        }
    }

    public String getName() {
        return name;
    }

    public Supplier<T> getSupplier() {
        return supplier;
    }

    public T getValue() {
        return value;
    }

    public Duration getDelay() {
        return delay;
    }

    public DataLogEntry getLogEntry() {
        return logEntry;
    }

    private boolean isInteger() {
        return value.getClass() == Integer.class;
    }

    private boolean isDouble() {
        return value.getClass() == Double.class;
    }

    private boolean isIntegerArray() {
        return value.getClass() == Integer[].class;
    }

    private boolean isDoubleArray() {
        return value.getClass() == Double[].class;
    }
}