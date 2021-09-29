package com.middleware.lab;

import com.middleware.provider.job.CatalogJob;
import com.middleware.provider.job.InvoiceJob;
import com.middleware.provider.job.PricesJob;
import com.middleware.provider.job.ValidateJob;
import com.middleware.provider.job.WarehouseJob;
import com.middleware.provider.job.InventoryJob;
import javax.annotation.PostConstruct;
import org.apache.logging.log4j.LogManager;
import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

/**
 *
 * @author Natalia Manrique
 */
@Configuration
public class SchedulerConfig {

    @Autowired
    private ApplicationContext applicationContext;

    @Value("${job.frequency.syncOrders}")
    private int frequencyOrdersJob;
    @Value("${job.frequency.validateInvoice}")
    private int frequencyValidateJob;
    @Value("${job.frequency.syncPrices}")
    private int frequencyPricingJob;
    @Value("${job.frequency.syncWarehouses}")
    private int frequencyWarehouseJob;
    @Value("${job.frequency.syncInventory}")
    private int frequencyInventoryJob;
     @Value("${job.frequency.syncCatalog}")
    private int frequencyCatalogJob;

    @Autowired
    private EnviromentConfig enviromentConfig;

    private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger(SchedulerConfig.class);

    @PostConstruct
    public void init() {
        LOGGER.info("Iniciando SchedulerConfig...");
    }

    @Bean
    public SpringBeanJobFactory springBeanJobFactory() {
        AutoWiringSpringBeanJobFactory jobFactory = new AutoWiringSpringBeanJobFactory();
        LOGGER.info("Configurando fábrica de Jobs...");
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }

    @Bean
    public SchedulerFactoryBean scheduler(JobDetail[] jobDetails, Trigger[] triggers) {
        SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
        if (enviromentConfig.isRunJobs()) {
            schedulerFactory.setConfigLocation(new ClassPathResource("quartz.properties"));
            LOGGER.info("Configurando programación de Jobs Pendings...");
            schedulerFactory.setJobFactory(springBeanJobFactory());
            schedulerFactory.setJobDetails(jobDetails);
            schedulerFactory.setTriggers(triggers);
            // ESPERAR A QUE TERMINE LA EJECUCIÓN DE UN JOB ANTES DE INICIAR EL SIGUIENTE
            schedulerFactory.setWaitForJobsToCompleteOnShutdown(true);
        }
        return schedulerFactory;
    }

    @Bean
    public JobDetail[] jobDetails() {
        JobDetail[] jobDetails = {
            ordersJobDetail().getObject(),
            validateJobDetail().getObject(),
            pricesJobDetail().getObject(),
            warehouseJobDetail().getObject(),
            catalogJobDetail().getObject()
        };
        return jobDetails;
    }

    @Bean
    public Trigger[] triggers() {
        Trigger[] triggers = {
            ordersJobTrigger().getObject(),
            validateJobTrigger().getObject(),
            pricesJobTrigger().getObject(),
            warehouseJobTrigger().getObject(),
            catalogJobTrigger().getObject()
        };
        return triggers;
    }

    @Bean
    public JobDetailFactoryBean ordersJobDetail() {
        JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
        jobDetailFactory.setJobClass(InvoiceJob.class);
        jobDetailFactory.setName("Orders_Job_Detail");
        jobDetailFactory.setDescription("Job de sincronización de órdenes...");
        jobDetailFactory.setDurability(true);
        jobDetailFactory.setRequestsRecovery(true);
        return jobDetailFactory;
    }

    @Bean
    public SimpleTriggerFactoryBean ordersJobTrigger() {
        String name = "Orders_Trigger";
        SimpleTriggerFactoryBean trigger = new SimpleTriggerFactoryBean();
        trigger.setJobDetail(ordersJobDetail().getObject());
        LOGGER.info("Configurando " + name + " para que se ejecute cada " + frequencyOrdersJob + " segundos ");
        trigger.setRepeatInterval(frequencyOrdersJob * 1000);
        trigger.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
        trigger.setName(name);
        return trigger;
    }

    @Bean
    public JobDetailFactoryBean validateJobDetail() {
        JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
        jobDetailFactory.setJobClass(ValidateJob.class);
        jobDetailFactory.setName("Validate_Job_Detail");
        jobDetailFactory.setDescription("Job de sincronización de invoiced...");
        jobDetailFactory.setDurability(true);
        jobDetailFactory.setRequestsRecovery(true);
        return jobDetailFactory;
    }

    @Bean
    public SimpleTriggerFactoryBean validateJobTrigger() {
        String name = "Validate_Trigger";
        SimpleTriggerFactoryBean trigger = new SimpleTriggerFactoryBean();
        trigger.setJobDetail(validateJobDetail().getObject());
        LOGGER.info("Configurando " + name + " para que se ejecute cada " + frequencyValidateJob + " segundos ");
        trigger.setRepeatInterval(frequencyValidateJob * 1000);
        trigger.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
        trigger.setName(name);
        return trigger;
    }

    @Bean
    public JobDetailFactoryBean pricesJobDetail() {
        JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
        jobDetailFactory.setJobClass(PricesJob.class);
        jobDetailFactory.setName("Prices_Job_Detail");
        jobDetailFactory.setDescription("Job de sincronización de precios...");
        jobDetailFactory.setDurability(true);
        jobDetailFactory.setRequestsRecovery(true);
        return jobDetailFactory;
    }

    @Bean
    public SimpleTriggerFactoryBean pricesJobTrigger() {
        String name = "Prices_Trigger";
        SimpleTriggerFactoryBean trigger = new SimpleTriggerFactoryBean();
        trigger.setJobDetail(pricesJobDetail().getObject());
        LOGGER.info("Configurando " + name + " para que se ejecute cada " + frequencyPricingJob + " segundos ");
        trigger.setRepeatInterval(frequencyPricingJob * 1000);
        trigger.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
        trigger.setName(name);
        return trigger;
    }

    @Bean
    public JobDetailFactoryBean warehouseJobDetail() {
        JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
        jobDetailFactory.setJobClass(WarehouseJob.class);
        jobDetailFactory.setName("Warehouse_Job_Detail");
        jobDetailFactory.setDescription("Job de sincronización de warehouses...");
        jobDetailFactory.setDurability(true);
        jobDetailFactory.setRequestsRecovery(true);
        return jobDetailFactory;
    }

    @Bean
    public SimpleTriggerFactoryBean warehouseJobTrigger() {
        String name = "Warehouse_Trigger";
        SimpleTriggerFactoryBean trigger = new SimpleTriggerFactoryBean();
        trigger.setJobDetail(warehouseJobDetail().getObject());
        LOGGER.info("Configurando " + name + " para que se ejecute cada " + frequencyWarehouseJob + " segundos ");
        trigger.setRepeatInterval(frequencyWarehouseJob * 1000);
        trigger.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
        trigger.setName(name);
        return trigger;
    }
    
     @Bean
    public JobDetailFactoryBean inventoryJobDetail() {
        JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
        jobDetailFactory.setJobClass(InventoryJob.class);
        jobDetailFactory.setName("Inventory_Job_Detail");
        jobDetailFactory.setDescription("Job de sincronización de Inventory ...");
        jobDetailFactory.setDurability(true);
        jobDetailFactory.setRequestsRecovery(true);
        return jobDetailFactory;
    }

    @Bean
    public SimpleTriggerFactoryBean inventoryJobTrigger() {
        String name = "Inventory_Trigger";
        SimpleTriggerFactoryBean trigger = new SimpleTriggerFactoryBean();
        trigger.setJobDetail(inventoryJobDetail().getObject());
        LOGGER.info("Configurando " + name + " para que se ejecute cada " + frequencyInventoryJob + " segundos ");
        trigger.setRepeatInterval(frequencyInventoryJob * 1000);
        trigger.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
        trigger.setName(name);
        return trigger;
    }
    
     @Bean
    public JobDetailFactoryBean catalogJobDetail() {
        JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
        jobDetailFactory.setJobClass(CatalogJob.class);
        jobDetailFactory.setName("Catalog_Job_Detail");
        jobDetailFactory.setDescription("Job de sincronización de Catalog ...");
        jobDetailFactory.setDurability(true);
        jobDetailFactory.setRequestsRecovery(true);
        return jobDetailFactory;
    }

    @Bean
    public SimpleTriggerFactoryBean catalogJobTrigger() {
        String name = "Catalog_Trigger";
        SimpleTriggerFactoryBean trigger = new SimpleTriggerFactoryBean();
        trigger.setJobDetail(catalogJobDetail().getObject());
        LOGGER.info("Configurando " + name + " para que se ejecute cada " + frequencyCatalogJob + " segundos ");
        trigger.setRepeatInterval(frequencyCatalogJob * 1000);
        trigger.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
        trigger.setName(name);
        return trigger;
    }

}
