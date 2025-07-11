package org.handlers.Benchmarks;

import org.handlers.Main;
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
public class EffectHandleBenchmark {

    @Benchmark
    public void perform_effect(Blackhole blackhole) {
        blackhole.consume(Main.pure_effect());
    }

    @Benchmark
    public void pure_compute(Blackhole blackhole) {
        blackhole.consume(Main.pure_compute());
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(EffectHandleBenchmark.class.getName())
                .forks(1)
                .build();

        new Runner(opt).run();
    }
}