package com.github.nathansgreen.codec;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@Timeout(time = 60)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.AverageTime)
@Warmup(time = 2)
@Measurement(iterations = 10, time = 5)
@Threads(value = 6)
public class IntToByteBufferBench {
    static final int MAX = 0x7fff_ffff - 1_000_000;

    // @Benchmark
    @OperationsPerInvocation(1_000_000)
    public ByteBuffer uint32ToAscii_10Digit() {
        ByteBuffer buffer = ByteBuffer.allocate(16);
        for (int i = 0x7fff_ffff; i > MAX; --i) {
            buffer.clear();  // this is essentially free
            IntToByteBuffer.uint32ToAscii(i, buffer);
        }
        return buffer;
    }

    // @Benchmark
    @OperationsPerInvocation(1_000_000)
    public ByteBuffer uint32ToAscii_8Digit() {
        ByteBuffer buffer = ByteBuffer.allocate(16);
        for (int i = 11_000_000; i > 10_000_000; --i) {
            buffer.clear();  // this is essentially free
            IntToByteBuffer.uint32ToAscii(i, buffer);
        }
        return buffer;
    }

    // @Benchmark
    @OperationsPerInvocation(900_000)
    public ByteBuffer uint32ToAscii_6Digit() {
        ByteBuffer buffer = ByteBuffer.allocate(16);
        for (int i = 999_999; i > 99_999; --i) {
            buffer.clear();  // this is essentially free
            IntToByteBuffer.uint32ToAscii(i, buffer);
        }
        return buffer;
    }

    // @Benchmark
    @OperationsPerInvocation(9000)
    public ByteBuffer uint32ToAscii_4Digit() {
        ByteBuffer buffer = ByteBuffer.allocate(16);
        for (int i = 1000; i < 10_000; ++i) {
            buffer.clear();  // this is essentially free
            IntToByteBuffer.uint32ToAscii(i, buffer);
        }
        return buffer;
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(".*" + IntToByteBufferBench.class.getSimpleName() + ".*")
                .build();
        new Runner(opt).run();
    }
}
