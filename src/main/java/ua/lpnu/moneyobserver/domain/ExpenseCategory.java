package ua.lpnu.moneyobserver.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "expense_categories")
@Getter
@Setter
public class ExpenseCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private Double ratio;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @JsonIgnore
    @OneToMany(mappedBy = "expenseCategory",cascade = CascadeType.REMOVE)
    private List<Expense> expenses;

}
