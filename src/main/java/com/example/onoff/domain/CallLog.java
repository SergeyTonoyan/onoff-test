package com.example.onoff.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CALL_LOG")
public class CallLog {


    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "ID", length = 45, nullable = false)
    private String id;

    @Column(name = "USER_ID", length = 45, nullable = false)
    private String userId;

    @Column(name = "USERNAME", length = 255, nullable = false)
    private String username;

    @Column(name = "ONOFF_NUMBER", length = 45, nullable = false)
    private String onoffNumber;

    @Column(name = "CONTACT_NUMBER", length = 45, nullable = false)
    private String contactNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", length = 45, nullable = false)
    private CallStatus status;

    @Column(name = "INCOMING", nullable = false)
    private boolean incoming;

    @Column(name = "DURATION", nullable = false)
    private int duration;

    @Column(name = "STARTED_AT", columnDefinition="TIMESTAMP(3)", nullable = false)
    private Instant startedAt;

    @Column(name = "ENDED_AT", columnDefinition="TIMESTAMP(3)", nullable = false)
    private Instant endedAt;
}
