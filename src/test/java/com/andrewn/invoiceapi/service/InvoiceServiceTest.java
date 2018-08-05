package com.andrewn.invoiceapi.service;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.*;

import com.andrewn.invoiceapi.model.invoices.Invoice;
import com.andrewn.invoiceapi.repository.InvoiceRepository;
import com.andrewn.invoiceapi.repository.OffsetLimitRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InvoiceServiceTest {

    @Mock
    private InvoiceRepository invoiceRepository;

    @InjectMocks
    private InvoiceServiceImpl invoiceService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetInvoiceList() {
        List<Invoice> invoices = new ArrayList<Invoice>();
        invoices.add(new Invoice(1, "123", "abc", LocalDate.parse("2018-01-01"), 2000L, new Timestamp(System.currentTimeMillis())));
        invoices.add(new Invoice(2, "1234", "abcd", LocalDate.parse("2018-01-02"), 2000L, new Timestamp(System.currentTimeMillis())));
        invoices.add(new Invoice(3, "12345", "abcde", LocalDate.parse("2018-01-03"), 2000L, new Timestamp(System.currentTimeMillis())));

        Page<Invoice> invoicePage = new PageImpl<Invoice>(invoices);
        when(invoiceRepository.findByOptionalSearchParams(isNull(), isNull(), isNull(), isNull(), any(OffsetLimitRequest.class))).thenReturn(invoicePage);

        List<Invoice> foundInvoices = invoiceService.getInvoiceList(0, 10, null, null, null, null);

        verify(invoiceRepository, times(1)).findByOptionalSearchParams(isNull(), isNull(), isNull(), isNull(), any(OffsetLimitRequest.class));
        verifyNoMoreInteractions(invoiceRepository);
        assertThat(foundInvoices.size()).isEqualTo(3);
    }

    @Test
    public void testGetInvoice() {
        Invoice invoice = new Invoice( 1, "123", "abc", LocalDate.parse("2018-01-01"), 2000L, new Timestamp(System.currentTimeMillis()));
        when(invoiceRepository.findById(1)).thenReturn(java.util.Optional.ofNullable(invoice));

        Invoice foundInvoice = invoiceService.getInvoice(1);

        verify(invoiceRepository, times(1)).findById(1);
        verifyNoMoreInteractions(invoiceRepository);
        assertThat(foundInvoice.getId()).isEqualTo(1);
    }

    @Test
    public void testAddInvoice() {
        Invoice invoice = new Invoice(null, "123", "abc", LocalDate.parse("2018-01-01"), 2000L, new Timestamp(System.currentTimeMillis()));
        Invoice savedInvoice = new Invoice(1, "123", "abc", LocalDate.parse("2018-01-01"), 2000L, new Timestamp(System.currentTimeMillis()));
        when(invoiceRepository.save(invoice)).thenReturn(savedInvoice);
        Invoice saveResult = invoiceService.addInvoice(invoice);

        verify(invoiceRepository, times(1)).save(invoice);
        verifyNoMoreInteractions(invoiceRepository);
        assertThat(saveResult.getId()).isEqualTo(1);
    }

    @Test
    public void testReplaceInvoice() {
        Invoice existingInvoice = new Invoice(1, "123", "abc", LocalDate.parse("2018-01-01"), 2000L, new Timestamp(System.currentTimeMillis()));
        Invoice newInvoice = new Invoice(1, "321", "def", null, 2000L, new Timestamp(System.currentTimeMillis()));
        when(invoiceRepository.findById(1)).thenReturn(java.util.Optional.ofNullable(existingInvoice));
        when(invoiceRepository.save(newInvoice)).thenReturn(newInvoice);

        Invoice savedInvoice = invoiceService.replaceInvoice(1, newInvoice);

        verify(invoiceRepository, times(1)).findById(1);
        verify(invoiceRepository, times(1)).save(newInvoice);
        verifyNoMoreInteractions(invoiceRepository);
        assertThat(savedInvoice.getId()).isEqualTo(1);
        assertThat(savedInvoice.getInvoiceNumber()).isEqualTo("321");
        assertThat(savedInvoice.getPurchaseOrderNumber()).isEqualTo("def");
        assertThat(savedInvoice.getDueDate()).isNull();
        assertThat(savedInvoice.getAmountCents()).isEqualTo(2000L);

    }

    @Test
    public void testUpdateInvoice() {
        Invoice existingInvoice = new Invoice(1, "123", "abc", LocalDate.parse("2018-01-01"), 2000L, new Timestamp(System.currentTimeMillis()));
        Invoice updateInvoice = new Invoice(1, "321", null, null, 2000L, new Timestamp(System.currentTimeMillis()));
        Invoice savedInvoice = new Invoice(1, "321", "abc", LocalDate.parse("2018-01-01"), 2000L, new Timestamp(System.currentTimeMillis()));
        when(invoiceRepository.findById(1)).thenReturn(java.util.Optional.ofNullable(existingInvoice));
        when(invoiceRepository.save(updateInvoice)).thenReturn(savedInvoice);

        Invoice resultInvoice = invoiceService.replaceInvoice(1, updateInvoice);

        verify(invoiceRepository, times(1)).findById(1);
        verify(invoiceRepository, times(1)).save(updateInvoice);
        verifyNoMoreInteractions(invoiceRepository);
        assertThat(resultInvoice.getId()).isEqualTo(1);
        assertThat(resultInvoice.getInvoiceNumber()).isEqualTo("321");
        assertThat(resultInvoice.getPurchaseOrderNumber()).isEqualTo("abc");
        assertThat(resultInvoice.getDueDate()).isNotNull();
        assertThat(resultInvoice.getAmountCents()).isEqualTo(2000L);
    }

    @Test
    public void testDeleteInvoice() {
        Invoice invoice = new Invoice(1, "123", "abc", LocalDate.parse("2018-01-01"), 2000L, new Timestamp(System.currentTimeMillis()));
        when(invoiceRepository.findById(1)).thenReturn(java.util.Optional.ofNullable(invoice));

        invoiceService.deleteInvoice(1);

        verify(invoiceRepository, times(1)).findById(1);
        verify(invoiceRepository, times(1)).delete(invoice);
        verifyNoMoreInteractions(invoiceRepository);
    }

}

