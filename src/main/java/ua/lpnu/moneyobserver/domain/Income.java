package ua.lpnu.moneyobserver.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name="income")
@Getter
@Setter
public class Income {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String description;
    private Timestamp time;
    private Double amount;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;
}
