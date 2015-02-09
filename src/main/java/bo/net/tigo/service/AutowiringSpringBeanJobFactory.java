package bo.net.tigo.service;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

/**
 * Created by aralco on 2/4/15.
 */
public final class AutowiringSpringBeanJobFactory extends SpringBeanJobFactory  //{
        implements ApplicationContextAware {

    private transient AutowireCapableBeanFactory beanFactory;
//    @Autowired
//    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(final ApplicationContext context) {
        beanFactory = context.getAutowireCapableBeanFactory();
    }

    @Override
    protected Object createJobInstance(final TriggerFiredBundle bundle)
            throws Exception {
//        AutowireCapableBeanFactory beanFactory = applicationContext.getAutowireCapableBeanFactory();
        final Object job = super.createJobInstance(bundle);
        beanFactory.autowireBean(job);
        return job;
    }
}