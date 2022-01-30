package com.spring.batch;

import java.nio.file.Files;
import java.nio.file.Path;

import com.google.api.gax.paging.Page;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;

@Configuration
@EnableBatchProcessing
public class JobConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public EmployeeRepository repository;
    
    @Value("${file.input}")
    private String fileInput;

    @Value("gs://bucket-nimble-perigee-337406/employee-list.CSV")
    private Resource gcsFile;

    @Bean
    public FlatFileItemReader<Employee> reader() throws Exception{
        byte[] fileContents = null;
        try {	
            Bucket bucket = getStorage().get("bucket-nimble-perigee-337406");
            Page<Blob> blobs = bucket.list();
            for (Blob blob: blobs.getValues()) {
                fileContents = blob.getContent();
            }
        }catch(IllegalStateException e){
			throw new RuntimeException(e);
		}
        Path tempFile = Files.createTempFile("tempfiles", ".csv");
        Files.write(tempFile,fileContents);
        return new FlatFileItemReaderBuilder<Employee>().name("employeeItemReader")
            .resource(new PathResource(tempFile.toUri()))
            .delimited()
            .names(new String[] { "empname", "emplocation", "empdept" })
            .fieldSetMapper(new BeanWrapperFieldSetMapper<Employee>() {{
                setTargetType(Employee.class);
             }})
            .build();
    }

    @Bean
    public EmployeeItemProcessor processor() {
        return new EmployeeItemProcessor();
    }

    @Bean
    public ItemWriter<Employee> writer() {
        return new RepositoryItemWriterBuilder<Employee>()
                .repository(repository)
                .methodName("save")
                .build();
    }

    @Bean
    public Job importUserJob(JobListener listener, Step step1) {
        return jobBuilderFactory.get("importUserJob")
            .incrementer(new RunIdIncrementer())
            .listener(listener)
            .flow(step1)
            .end()
            .build();
    }

    @Bean
    public Step step1() throws Exception{
        return stepBuilderFactory.get("step1")
            .<Employee, Employee> chunk(2000)
            .reader(reader())
            .processor(processor())
            .writer(writer())
            .build();
    }

    private Storage getStorage() throws Exception{
        return StorageOptions.newBuilder().
                   setCredentials(ServiceAccountCredentials.fromStream(
                    getClass().getResourceAsStream("/service-account.json"))).build()
                   .getService();
       }

}
