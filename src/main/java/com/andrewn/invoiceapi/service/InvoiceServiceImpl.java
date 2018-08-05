package com.andrewn.invoiceapi.service;

import com.andrewn.invoiceapi.Util;
import com.andrewn.invoiceapi.model.invoices.Invoice;
import com.andrewn.invoiceapi.model.exceptions.NotFoundException;
import com.andrewn.invoiceapi.model.exceptions.BadRequestException;
import com.andrewn.invoiceapi.repository.InvoiceRepository;
import com.andrewn.invoiceapi.repository.OffsetLimitRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private static final String SORT_FIELD = "createdAt";

    @Autowired
    InvoiceRepository invoiceRepository;


    @Override
    public Invoice getInvoice(Integer id) {
        return lookupInvoice(id);
    }

    @Override
    public List<Invoice> getInvoiceList(Integer offset, Integer limit, String invoiceNumber, String purchaseOrderNumber, String dueDateStart, String dueDateEnd) {
        try {
            OffsetLimitRequest offsetLimitRequest = new OffsetLimitRequest(offset, limit, new Sort(Sort.Direction.DESC, SORT_FIELD));
            return invoiceRepository.findByOptionalSearchParams(invoiceNumber, purchaseOrderNumber,
                    dueDateStart != null ? LocalDate.parse(dueDateStart) : null,
                    dueDateEnd != null ? LocalDate.parse(dueDateEnd) : null,
                    offsetLimitRequest).getContent();
        }
        catch(Exception e) {
            throw new BadRequestException(e);
        }
    }

    @Override
    public Invoice addInvoice(Invoice invoice) {
        // TODO throw an exception if id is provided, or allow add anyway and just set to null?
        //  no sure which is better.
        if(invoice.getId() != null)
            invoice.setId(null);
        return saveInvoice(invoice);
    }

    @Override
    public Invoice replaceInvoice(Integer id, Invoice invoice) {
        Invoice existingInvoice = lookupInvoice(id);
        BeanUtils.copyProperties(invoice, existingInvoice, "id");
        return saveInvoice(existingInvoice);
    }

    @Override
    public Invoice updateInvoice(Integer id, Invoice invoice) {
        Invoice existingInvoice = lookupInvoice(id);
        BeanUtils.copyProperties(invoice, existingInvoice, Util.getNullPropertyNames(invoice));
        return saveInvoice(existingInvoice);
    }

    @Override
    public void deleteInvoice(Integer id) {
        Invoice existingInvoice = lookupInvoice(id);
        invoiceRepository.delete(existingInvoice);
    }

    private Invoice lookupInvoice(Integer id) {
        Invoice invoice = invoiceRepository.findById(id).orElse(null);
        if (invoice == null)
            throw new NotFoundException("Invoice {" + id + "} not found.");
        return invoice;
    }

    private Invoice saveInvoice(Invoice invoice) {
        try {
            return invoiceRepository.save(invoice);
        }
        catch(Exception e) {
            throw new BadRequestException(e);
        }
    }
}
