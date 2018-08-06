package com.andrewn.invoiceapi.controller;

import com.andrewn.invoiceapi.model.invoices.Invoice;
import com.andrewn.invoiceapi.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

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
    public ResponseEntity<List<Invoice>> getInvoices(@RequestParam(required = false, defaultValue = "0") Integer offset,
                                                     @RequestParam(required = false, defaultValue = "10") Integer limit,
                                                     @RequestParam(required = false) String invoiceNumber,
                                                     @RequestParam(required = false) String poNumber,
                                                     @RequestParam(required = false) String dueDateStart,
                                                     @RequestParam(required = false) String dueDateEnd) {
        return new ResponseEntity<List<Invoice>>(invoiceService.getInvoiceList(offset, limit, invoiceNumber, poNumber, dueDateStart, dueDateEnd), HttpStatus.OK);
    }

    @RequestMapping(value = {"/{id}"}, method = RequestMethod.GET)
    public ResponseEntity<Invoice> getInvoice(@PathVariable("id") Integer id) {
        return new ResponseEntity<Invoice>(invoiceService.getInvoice(id), HttpStatus.OK);
    }

    // There are many different ways to manipulate headers and differing opinions
    //  on whether or not to return the entity in post/put/patch responses.
    //  Leaving entity in the response for now because it makes for easy testing of a small demo.
    //  In the real world, it may not make sense or may be too much overhead to include.
    @RequestMapping(value = {"","/"}, method = RequestMethod.POST)
    public ResponseEntity<Invoice> addInvoice(@RequestBody Invoice invoice, UriComponentsBuilder builder) {
        Invoice savedInvoice = invoiceService.addInvoice(invoice);
        HttpHeaders headers = new HttpHeaders();
        headers.add("location", "api/invoices/" + savedInvoice.getId());

        return new ResponseEntity<Invoice>(savedInvoice, headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = {"/{id}"}, method = RequestMethod.PUT)
    public ResponseEntity<Invoice> replaceInvoice(@RequestBody Invoice invoice, @PathVariable("id") Integer id) {
        return new ResponseEntity<Invoice>( invoiceService.replaceInvoice(id, invoice), HttpStatus.OK);
    }

    @RequestMapping(value = {"/{id}"}, method = RequestMethod.PATCH)
    public ResponseEntity<Invoice> updateInvoice(@RequestBody Invoice invoice, @PathVariable("id") Integer id) {
        return new ResponseEntity<Invoice>(invoiceService.updateInvoice(id, invoice), HttpStatus.OK);
    }

    @RequestMapping(value = {"/{id}"}, method = RequestMethod.DELETE)
    public ResponseEntity deleteInvoice(@PathVariable("id") Integer id) {
        invoiceService.deleteInvoice(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
