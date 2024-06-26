package io.springbatch.springbatchlecture;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class JobLauncherController {

    private Job job;
    private JobLauncher jobLauncher;
    private BatchConfigurer batchConfigurer;

    public JobLauncherController(Job job, JobLauncher jobLauncher, BatchConfigurer batchConfigurer) {
        this.job = job;
        this.jobLauncher = jobLauncher;
        this.batchConfigurer = batchConfigurer;
    }

    @PostMapping("/batch")
    public String launch(@RequestBody Member member) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("id", member.getId())
                .addDate("date", new Date())
                .toJobParameters();

        JobLauncher launcher = batchConfigurer.jobLauncher();
        this.jobLauncher.run(job, jobParameters);

        return "batch completed";
    }
}
