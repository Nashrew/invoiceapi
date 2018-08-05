package com.andrewn.invoiceapi.service;

import com.andrewn.invoiceapi.model.invoices.Invoice;

import java.util.List;

public interface InvoiceService {
    Invoice getInvoice(Integer id);
    List<Invoice> getInvoiceList(Integer offset, Integer limit, String invoiceNumber, String purchaseOrderNumber, String dueDateStart, String dueDateEnd);

    Invoice addInvoice(Invoice invoice);
    Invoice replaceInvoice(Integer id, Invoice invoice);
    Invoice updateInvoice(Integer id, Invoice invoice);

    void deleteInvoice(Integer id);
}
