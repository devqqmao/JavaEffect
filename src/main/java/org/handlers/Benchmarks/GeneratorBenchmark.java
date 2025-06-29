package org.handlers.Benchmarks;

import org.handlers.Main;

import static org.handlers.handlers.KotlinGeneratorsKt.*;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
@State(Scope.Benchmark)
public class GeneratorBenchmark {

    @Param({"1000"})
    private int size;

    @Benchmark
    public void javaContinuationGenerator(Blackhole blackhole) {
        blackhole.consume(Main.generator(size));
    }

    @Benchmark
    public void kotlinBasicGenerator(Blackhole blackhole) {
        Iterator<Integer> iterator = kotlinSequenceGenerator(size).iterator();
        while (iterator.hasNext()) {
            blackhole.consume(iterator.next());
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(GeneratorBenchmark.class.getName())
                .forks(1)
                .build();

        new Runner(opt).run();
    }
}