package com.github.nathansgreen.codec;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

/**
 * Let's compare with the built-in {@link Integer#toUnsignedString(int)}.
 */
@State(Scope.Thread)
@Timeout(time = 60)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.AverageTime)
@Warmup(time = 2)
@Measurement(iterations = 10, time = 5)
@Threads(value = 6)
public class IntegerToStringBench {
    static final int MAX = 0x7fff_ffff - 1_000_000;

    // @Benchmark
    @OperationsPerInvocation(1_000_000)
    public String integerToString_10Digit() {
        String s = "";
        for (int i = 0x7fff_ffff; i > MAX; --i) {
            s = Integer.toUnsignedString(i);
        }
        return s;
    }

    // @Benchmark
    @OperationsPerInvocation(1_000_000)
    public String integerToString_8Digit() {
        String s = "";
        for (int i = 11_000_000; i > 10_000_000; --i) {
            s = Integer.toUnsignedString(i);
        }
        return s;
    }

    // @Benchmark
    @OperationsPerInvocation(900_000)
    public String integerToString_6Digit() {
        String s = "";
        for (int i = 999_999; i > 99_999; --i) {
            s = Integer.toUnsignedString(i);
        }
        return s;
    }

    // @Benchmark
    @OperationsPerInvocation(9000)
    public String integerToString_4Digit() {
        String s = "";
        for (int i = 1000; i < 10_000; ++i) {
            s = Integer.toUnsignedString(i);
        }
        return s;
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(".*" + IntegerToStringBench.class.getSimpleName() + ".*")
                .build();
        new Runner(opt).run();
    }
}
