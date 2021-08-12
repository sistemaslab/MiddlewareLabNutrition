package com.middleware.provider.job;


import com.middleware.lab.EnviromentConfig;
import com.middleware.provider.bs.OrdersBs;
import com.middleware.provider.utils.DateMapper;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.logging.log4j.LogManager;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Clase encargada de ejecutar tareas de sincronización de ordenes
 * @author Natalia Manrique
 * @version 1.0
 */
@Component
public class ValidateJob implements Job{
       
    @Autowired private OrdersBs orderBs;
    @Autowired private EnviromentConfig enviromentConfig;  
    
    private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger(ValidateJob.class);

    @Override
    public void execute(JobExecutionContext jec) {
        try{
            if(enviromentConfig.isRunJobs()){
                LOGGER.info("Tarea programada ** "+jec.getJobDetail().getKey().getName()+" ** iniciada a las @ "+jec.getFireTime());
               // this.validate(jec.getFireTime());
                
            }
        }catch(Throwable th){
            LOGGER.error("ValidateJob - FATAL ERROR", th);
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, th);
        }finally{
            LOGGER.info("Siguiente ejecución de la tarea ** "+jec.getJobDetail().getKey().getName()+" ** programada a las @ " + jec.getNextFireTime());
        }
    }

  
    
     /**
     * Valida las ordenes que ya pasaron
     * @param taskExecutionDate fecha en la que se ejecuta la tarea
     * @return true si la sincronización fue exitosa, false en caso contrario
     */
    private Boolean validate(Date taskExecutionDate) {
        LOGGER.info("Sincronizando facturas a VTEX");
        Boolean successful = false;
        try{
            Date startDate = DateMapper.addDays(taskExecutionDate, -28);
            Date endDate = DateMapper.addDays(taskExecutionDate, 1);
            LOGGER.info("startDate="+startDate+", endDate="+endDate);
            try {
                orderBs.validateInvoice();
            } catch (Exception ex) {
                LOGGER.error("Error sincronizando órdenes generadas en VTEX", ex);
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            }
        }catch(Exception ex){
            LOGGER.error("syncVtexOrdersInDataBase", ex);
        }
        return successful;
    }
    
    
    
  
    
}
