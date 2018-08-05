package com.andrewn.invoiceapi.controller;

import com.andrewn.invoiceapi.model.invoices.Invoice;
import com.andrewn.invoiceapi.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("api/invoices")
public class InvoiceController {

    @Autowired
    InvoiceService invoiceService;

    // Mapping both "" and "/" was nice initially, since it allowed for users to use either one.
    //  However, it causes the method to appear twice in swagger documentation, which is awkward.
    //  Not sure if I should remove one. Finding lots of conflicting opinions on how to handle the situation.
    @RequestMapping(value = {"","/"}, method = RequestMethod.GET)
    public List<Invoice> getInvoices(@RequestParam(required = false, defaultValue = "0") Integer offset,
                                     @RequestParam(required = false, defaultValue = "10") Integer limit,
                                     @RequestParam(required = false) String invoiceNumber,
                                     @RequestParam(required = false) String poNumber,
                                     @RequestParam(required = false) String dueDateStart,
                                     @RequestParam(required = false) String dueDateEnd) {
        return invoiceService.getInvoiceList(offset, limit, invoiceNumber, poNumber, dueDateStart, dueDateEnd);
    }

    @RequestMapping(value = {"/{id}"}, method = RequestMethod.GET)
    public Invoice getInvoice(@PathVariable("id") Integer id) {
        return invoiceService.getInvoice(id);
    }

    @RequestMapping(value = {"","/"}, method = RequestMethod.POST)
    public Invoice addInvoice(@RequestBody Invoice invoice, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_CREATED);
        return invoiceService.addInvoice(invoice);
    }

    @RequestMapping(value = {"/{id}"}, method = RequestMethod.PUT)
    public Invoice replaceInvoice(@RequestBody Invoice invoice, @PathVariable("id") Integer id) {
        return invoiceService.replaceInvoice(id, invoice);
    }

    @RequestMapping(value = {"/{id}"}, method = RequestMethod.PATCH)
    public Invoice updateInvoice(@RequestBody Invoice invoice, @PathVariable("id") Integer id) {
        return invoiceService.updateInvoice(id, invoice);
    }

    @RequestMapping(value = {"/{id}"}, method = RequestMethod.DELETE)
    public void deleteInvoice(@PathVariable("id") Integer id) {
        invoiceService.deleteInvoice(id);
    }
}
