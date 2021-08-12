package com.middleware.provider.job;

import com.middleware.lab.EnviromentConfig;
import com.middleware.provider.bs.OrdersBs;
import com.middleware.provider.bs.PricingBs;
import com.middleware.provider.utils.DateMapper;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.logging.log4j.LogManager;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Clase encargada de ejecutar tareas de sincronización de ordenes
 *
 * @author Natalia Manrique
 * @version 1.0
 */
@Component
public class PricesJob implements Job {

    @Autowired
    private PricingBs pricingBs;
    @Autowired
    private EnviromentConfig enviromentConfig;

    private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger(PricesJob.class);

    @Override
    public void execute(JobExecutionContext jec) {
        try {
            JobDetail existingJobDetail = jec.getJobDetail();
            if (existingJobDetail != null) {
                if (enviromentConfig.isRunJobs()) {
                    LOGGER.info("Tarea programada ** " + jec.getJobDetail().getKey().getName() + " ** iniciada a las @ " + jec.getFireTime());
                    //this.syncPricesInVtex(jec.getFireTime());
                }
            }

        } catch (Throwable th) {
            LOGGER.error("PricesJob - FATAL ERROR", th);
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, th);
        } finally {
            LOGGER.info("Siguiente ejecución de la tarea ** " + jec.getJobDetail().getKey().getName() + " ** programada a las @ " + jec.getNextFireTime());
        }
    }

    /**
     * Sincroniza los precio en VTEX
     *
     * @param taskExecutionDate fecha en la que se ejecuta la tarea
     * @return true si la sincronización fue exitosa, false en caso contrario
     */
    private Boolean syncPricesInVtex(Date taskExecutionDate) {
        LOGGER.info("Sincronizando precios a VTEX");
        Boolean successful = false;
        try {
            Date startDate = DateMapper.addDays(taskExecutionDate, -28);
            Date endDate = DateMapper.addDays(taskExecutionDate, 1);
            LOGGER.info("startDate=" + startDate + ", endDate=" + endDate);
            try {
                pricingBs.updates();
            } catch (Exception ex) {
                LOGGER.error("Error sincronizando órdenes generadas en VTEX", ex);
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            }
        } catch (Exception ex) {
            LOGGER.error("syncVtexOrdersInDataBase", ex);
        }
        return successful;
    }
}
