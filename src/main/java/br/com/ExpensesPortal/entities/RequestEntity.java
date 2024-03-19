package br.com.ExpensesPortal.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.http.HttpMethod;

import br.com.ExpensesPortal.enums.RequestStatus;
import br.com.ExpensesPortal.enums.RequestType;
import br.com.ExpensesPortal.listeners.RequestListener;

@Entity
@Table(name = "requests")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(RequestListener.class)
public class RequestEntity extends BaseEntity{
    
    @Column(nullable = false)
    private UUID entity;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestType requestType;

    @Column(nullable = false)
    private HttpMethod httpMethod;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false, length = 3000)
    private String jsonEntity;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status;
    
    @Column(length = 3000)
    private String returnProcess;
    
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime processDate;
}
