package com.andrewn.invoiceapi.repository;

import com.andrewn.invoiceapi.model.invoices.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface InvoiceRepository extends PagingAndSortingRepository<Invoice, Integer> {

    // Initially, I had used CrudRepository's findBy... system, which is nice.
    //  However, having multiple optional params to search by would necessitate
    //  many different and potentially too verbose findBy methods, and a bloated
    //  service method. So, using @Query instead to handle the optional nature.

//    Page<Invoice> findByInvoiceNumber(String invoiceNumber, Pageable pageable);
//    Page<Invoice> findByPurchaseOrderNumber(String purchaseOrderNumber, Pageable pageable);
//    Page<Invoice> findByInvoiceNumberAndPurchaseOrderNumber(String invoiceNumber, String purchaseOrderNumber, Pageable pageable);
//    Page<Invoice> findByDueDateBetween(OffsetDateTime startDate, OffsetDateTime endDate, Pageable pageable);

    @Query("select invoice from Invoice invoice where "
            + "(:invoiceNumber is null or invoice.invoiceNumber = :invoiceNumber) and"
            + "(:purchaseOrderNumber is null or invoice.purchaseOrderNumber = :purchaseOrderNumber) and"
            + "(:dueDateStart is null or invoice.dueDate >= :dueDateStart) and"
            + "(:dueDateEnd is null or invoice.dueDate <= :dueDateEnd)")
    Page<Invoice> findByOptionalSearchParams(
            @Param("invoiceNumber") String invoiceNumber,
            @Param("purchaseOrderNumber") String purchaseOrderNumber,
            @Param("dueDateStart") LocalDate dueDateStart,
            @Param("dueDateEnd") LocalDate dueDateEnd,
            Pageable pageable);

}
