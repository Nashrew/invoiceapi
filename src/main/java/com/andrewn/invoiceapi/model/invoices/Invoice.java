package com.andrewn.invoiceapi.model.invoices;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;

@Data
@Entity
@Table(name="invoices")
public class Invoice {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @NotNull
    @Column(name="invoice_number")
    private String invoiceNumber;

    @NotNull
    @Column(name="po_number")
    private String purchaseOrderNumber;

    @Column(name="due_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dueDate;

    @NotNull
    @Column(name="amount_cents")
    private Long amountCents;

    @Column(name="created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssX")
    @CreationTimestamp
    private Timestamp createdAt;

    public Invoice(){}

    public Invoice(Integer id, @NotNull String invoiceNumber, @NotNull String purchaseOrderNumber,
                   @NotNull LocalDate dueDate, @NotNull Long amountCents, Timestamp createdAt) {
        this.id = id;
        this.invoiceNumber = invoiceNumber;
        this.purchaseOrderNumber = purchaseOrderNumber;
        this.dueDate = dueDate;
        this.amountCents = amountCents;
        this.createdAt = createdAt;
    }

    // These getters/setters are added because they are used explicitly by services or tests.
    //  Other getters and setters are provided by @Data
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }
    public String getPurchaseOrderNumber() {
        return purchaseOrderNumber;
    }

    public LocalDate getDueDate() { return dueDate; }

    public @NotNull Long getAmountCents() { return amountCents; }
}
