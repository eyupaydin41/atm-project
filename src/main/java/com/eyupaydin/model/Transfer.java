package com.eyupaydin.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"transfer_date"}, allowGetters = true)
@Table(name = "transfers")
public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sender_account_no")
    private String senderAccNo;

    @Column(name = "receiver_account_no")
    private String receiverAccNo;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "transfer_date")
    @CreatedDate
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date transferDate;
}
