package io.springbatch.springbatchlecture;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class StepBuilderConfiguration {

    @Bean
    public Job batchJob(JobRepository jobRepository, Step step1, Step step2, Step step3) {
        return new JobBuilder("batchJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(step1)
                .next(step2)
                .next(step3)
                .build();
    }

    @Bean
    public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("step1", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("step1 was executed");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step step2(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("step2", jobRepository)
                .<String, String>chunk(3, transactionManager)
                .reader(() -> null)
                .processor(item -> null)
                .writer(chunk -> {

                })
                .build();
    }

    @Bean
    public Step step3(JobRepository jobRepository, Step step1) {
        return new StepBuilder("step3", jobRepository)
                .partitioner(step1)
                .gridSize(2)
                .build();
    }

    @Bean
    public Step step4(JobRepository jobRepository, JobLauncher jobLauncher, Job job) {
        return new StepBuilder("step4", jobRepository)
                .job(job)
                .launcher(jobLauncher)
                .build();
    }

    @Bean
    public Step step5(JobRepository jobRepository, Flow flow) {
        return new StepBuilder("step5", jobRepository)
                .flow(flow)
                .build();
    }

    @Bean
    public Job job(JobRepository jobRepository, Step step1, Step step2, Step step3) {
        return new JobBuilder("job", jobRepository)
                .start(step1)
                .next(step2)
                .next(step3)
                .build();
    }

    @Bean
    public Flow flow(Step step2) {
        FlowBuilder<Flow> flowBuilder = new FlowBuilder<>("flow");
        flowBuilder.start(step2).end();
        return flowBuilder.build();
    }
}
