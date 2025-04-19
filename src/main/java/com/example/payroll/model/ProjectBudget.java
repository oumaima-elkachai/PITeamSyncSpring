package com.example.payroll.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.*;

@Getter
@Document(collection = "project_budgets")
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectBudget {

    // Getter et Setter pour id
    @Id
    private String id;  // MongoDB utilise String pour les ObjectId

    // Getter et Setter pour projet
    @DBRef
    private Projet projet;  // Utilisation de DBRef pour référencer une autre entité

    // Getter et Setter pour allocatedFunds
    private double allocatedFunds;
    // Getter et Setter pour usedFunds
    private double usedFunds;

    public void setId(String id) {
        this.id = id;
    }

    public void setProjet(Projet projet) {
        this.projet = projet;
    }

    public void setAllocatedFunds(double allocatedFunds) {
        this.allocatedFunds = allocatedFunds;
    }

    public void setUsedFunds(double usedFunds) {
        this.usedFunds = usedFunds;
    }

    public String getId() {
        return id;
    }

    public Projet getProjet() {
        return projet;
    }

    public double getAllocatedFunds() {
        return allocatedFunds;
    }

    public double getUsedFunds() {
        return usedFunds;
    }
}
