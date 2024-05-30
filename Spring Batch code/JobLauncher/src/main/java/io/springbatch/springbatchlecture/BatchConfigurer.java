package io.springbatch.springbatchlecture;

import org.springframework.batch.core.configuration.BatchConfigurationException;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.batch.BatchDataSource;
import org.springframework.boot.autoconfigure.batch.BatchDataSourceScriptDatabaseInitializer;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@EnableConfigurationProperties(BatchProperties.class)
public class BatchConfigurer extends DefaultBatchConfiguration {

    @Bean
    @ConditionalOnMissingBean(BatchDataSourceScriptDatabaseInitializer.class)
    BatchDataSourceScriptDatabaseInitializer batchDataSourceInitializer(DataSource dataSource,
                                                                        @BatchDataSource ObjectProvider<DataSource> batchDataSource, BatchProperties properties) {
        return new BatchDataSourceScriptDatabaseInitializer(batchDataSource.getIfAvailable(() -> dataSource),
                properties.getJdbc());
    }

    @Override
    public JobLauncher jobLauncher() throws BatchConfigurationException {
        TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();
        jobLauncher.setJobRepository(jobRepository());
        jobLauncher.setTaskExecutor(getTaskExecutor());
        try {
            jobLauncher.afterPropertiesSet();
            return jobLauncher;
        }
        catch (Exception e) {
            throw new BatchConfigurationException("Unable to configure the default job launcher", e);
        }
    }
}
