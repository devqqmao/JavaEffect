package org.handlers;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

import static org.handlers.Marks.*;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 0, time = 1)
@Measurement(iterations = 1, time = 1)
@Fork(1)
@State(Scope.Benchmark)
public class ContinuationBenchmark {

    @Benchmark
    public void benchmarkContinuationHandling(Blackhole blackhole) {
//        int res = execute_one_effect();
        int res = pure_compute(); // 1000
        blackhole.consume(res);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(ContinuationBenchmark.class.getName())
                .forks(1)
                .build();

        new Runner(opt).run();
    }
}