package br.com.ExpensesPortal.schedules;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import br.com.ExpensesPortal.entities.RequestEntity;
import br.com.ExpensesPortal.enums.RequestStatus;
import br.com.ExpensesPortal.repositories.RequestRepository;

import java.util.Collection;

@Component
@EnableScheduling
public class RequestSchedule {
    
    @Autowired
    private RequestRepository requestRepository;

    @Scheduled(fixedDelay = (1000 * 60))
    public void reprocessRequest() {
        Collection<RequestEntity> requests = requestRepository.findActiveByStatus(RequestStatus.ERROR);
        requests.forEach(r -> {
            r.setStatus(RequestStatus.PENDING);
            requestRepository.save(r);
        });
    }
}
