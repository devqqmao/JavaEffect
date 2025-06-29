package org.handlers.Benchmarks;

import org.handlers.Main;
import org.mockito.Mockito;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
@State(Scope.Benchmark)
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

    private TestTarget mockitoTarget;

    @Setup(Level.Iteration)
    public void setup() {
        ValueProvider mockProvider = Mockito.mock(ValueProvider.class);
        Mockito.when(mockProvider.getValue()).thenReturn(1);
        mockitoTarget = new TestTarget(mockProvider);
    }

    @Benchmark
    public void injection_with_mockito(Blackhole blackhole) {
        blackhole.consume(mockitoTarget.getValue());
    }

    @Benchmark
    public void injection_with_effects(Blackhole blackhole) {
        blackhole.consume(Main.perform_effect());
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(DIBenchmark.class.getName())
                .forks(1)
                .build();

        new Runner(opt).run();
    }
}