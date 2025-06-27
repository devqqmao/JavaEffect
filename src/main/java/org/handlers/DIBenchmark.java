package org.handlers;

import org.mockito.Mockito;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 0, time = 100, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 1, time = 100, timeUnit = TimeUnit.MILLISECONDS)
@Fork(3)
@State(Scope.Benchmark)
@Threads(1)
public class DIBenchmark {

    public interface ValueProvider {
        int getValue();
    }

    public static class TestTarget {
        private final int x;

        public TestTarget(ValueProvider provider) {
            this.x = provider.getValue();
        }

        public int getValue() {
            return x;
        }
    }

    private ValueProvider mockProvider;
    private TestTarget target;

    @Setup(Level.Iteration)
    public void setup() {
        mockProvider = Mockito.mock(ValueProvider.class);
        Mockito.when(mockProvider.getValue()).thenReturn(1);
    }

    @Benchmark
    public void injection_with_mockito(Blackhole blackhole) {
        target = new TestTarget(mockProvider);
        blackhole.consume(target.getValue());
    }

    @Benchmark
    public void injection_baseline(Blackhole blackhole) {
        ValueProvider provider = () -> 1;
        target = new TestTarget(provider);
        blackhole.consume(target.getValue());
    }

    @Benchmark
    public void injection_with_effects(Blackhole blackhole) {
        blackhole.consume(Main.dependency_injection());
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(DIBenchmark.class.getSimpleName())
                .forks(1)
                .build();

        new Runner(opt).run();
    }
}