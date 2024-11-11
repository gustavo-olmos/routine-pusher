package com.routine.pusher.service.implementation;

import com.routine.pusher.jobs.ScheduleJob;
import com.routine.pusher.model.dto.LembreteDTO;
import com.routine.pusher.service.interfaces.NotificadorService;
import com.routine.pusher.util.JobScheduleUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.quartz.*;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class NotificadorServiceImpl implements NotificadorService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(NotificadorServiceImpl.class);

    private final Scheduler scheduler;

    public NotificadorServiceImpl( Scheduler scheduler )
    {
        this.scheduler = scheduler;
    }


    public void schedule( final Class jobClass, final LembreteDTO dto )
    {
        final JobDetail jobDetail = JobScheduleUtils.buildJobDetail( jobClass, dto );
        final Trigger trigger = JobScheduleUtils.buildTrigger( jobClass, dto );

        try {
            scheduler.scheduleJob( jobDetail, trigger );
        }
        catch ( SchedulerException e ) {
            LOGGER.error(e.getMessage( ), e);
        }
    }

    @PostConstruct
    public void init()
    {
        try {
            scheduler.start( );
        }
        catch ( SchedulerException e ) {
            LOGGER.error(e.getMessage( ), e);
        }
    }

    @PreDestroy
    public void preDestroy( )
    {
       try {
           scheduler.shutdown( );
       }
       catch ( SchedulerException e ) {
           LOGGER.error(e.getMessage( ), e);
       }
    }

    @Override
    public void notificar( String title, String body )
    {
        LembreteDTO dto = new LembreteDTO();

        schedule( ScheduleJob.class, dto );
    }
}
