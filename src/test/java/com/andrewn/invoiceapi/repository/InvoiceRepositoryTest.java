package com.andrewn.invoiceapi.repository;

import com.andrewn.invoiceapi.model.invoices.Invoice;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Java6Assertions.assertThat;


@RunWith(SpringRunner.class)
@DataJpaTest
public class InvoiceRepositoryTest {

    private static final String SORT_FIELD = "createdAt";

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    InvoiceRepository invoiceRepository;

    @Test
    public void testFindByLimitAndOffset_thenReturnInvoices() {

        populateTestData(55, "123", "abc", "2018-01-01");

        OffsetLimitRequest offsetLimitRequest = new OffsetLimitRequest(20, 10, new Sort(Sort.Direction.DESC, SORT_FIELD));
        List<Invoice> found = invoiceRepository.findAll(offsetLimitRequest).getContent();

        assertThat(found.size()).isEqualTo(10);
    }

    @Test
    public void testFindByLimitAndOffsetWithAdditionalParameters_thenReturnInvoices() {

        populateTestData(55, "123", "abc", "2018-01-01");

        List<Invoice> found = getInvoiceByParameters(10, 20, "123", "abc", null, null);

        assertThat(found.size()).isEqualTo(10);
    }

    @Test
    public void testFindByInvoiceNumber_thenReturnInvoices() {

        populateTestData(1, "123", "abc", "2018-01-01");

        List<Invoice> found = getInvoiceByParameters(10, 0, "123", null, null, null);

        assertThat(found.size()).isEqualTo(1);
        for(Invoice i : found) {
            assertThat(i.getInvoiceNumber()).isEqualTo("123");
        }
    }

    @Test
    public void testFindByPurchaseOrderNumber_thenReturnInvoices() {

        populateTestData(1, "123", "abc", "2018-01-01");

        List<Invoice> found = getInvoiceByParameters(10, 0, null, "abc", null, null);

        assertThat(found.size()).isEqualTo(1);
        for(Invoice i : found) {
            assertThat(i.getPurchaseOrderNumber()).isEqualTo("abc");
        }
    }

    @Test
    public void testFindByInvoiceNumberAndPurchaseOrderNumber_thenReturnInvoices() {

        populateTestData(1, "1", "abc", "2018-01-01");
        populateTestData(1, "2", "def", "2018-01-02");
        populateTestData(1, "3", "abc", "2018-01-03");
        populateTestData(1, "4", "def", "2018-01-04");

        List<Invoice> found = getInvoiceByParameters(10, 0, "2", "def", null, null);

        assertThat(found.size()).isEqualTo(1);
        assertThat(found.stream().map(Invoice::getInvoiceNumber).collect(Collectors.toList()).contains("2"));
    }

    @Test
    public void testFindByStartDate_thenReturnInvoices() {

        populateTestData(1, "1", "abc", "2018-01-01");
        populateTestData(1, "2", "def", "2018-01-02");
        populateTestData(1, "3", "abc", "2018-01-03");
        populateTestData(1, "4", "def", "2018-01-04");

        List<Invoice> found = getInvoiceByParameters(10, 0, null, null, LocalDate.parse("2018-01-02"), null);

        assertThat(found.size()).isEqualTo(3);
        assertThat(found.stream().map(Invoice::getInvoiceNumber).collect(Collectors.toList()).contains("2")).isTrue();
        assertThat(found.stream().map(Invoice::getInvoiceNumber).collect(Collectors.toList()).contains("3")).isTrue();
        assertThat(found.stream().map(Invoice::getInvoiceNumber).collect(Collectors.toList()).contains("4")).isTrue();
    }


    @Test
    public void testFindByEndDate_thenReturnInvoices() {

        populateTestData(1, "1", "abc", "2018-01-01");
        populateTestData(1, "2", "def", "2018-01-02");
        populateTestData(1, "3", "abc", "2018-01-03");
        populateTestData(1, "4", "def", "2018-01-04");

        List<Invoice> found = getInvoiceByParameters(10, 0, null, null, null, LocalDate.parse("2018-01-03"));

        assertThat(found.size()).isEqualTo(3);
        assertThat(found.stream().map(Invoice::getInvoiceNumber).collect(Collectors.toList()).contains("1")).isTrue();
        assertThat(found.stream().map(Invoice::getInvoiceNumber).collect(Collectors.toList()).contains("2")).isTrue();
        assertThat(found.stream().map(Invoice::getInvoiceNumber).collect(Collectors.toList()).contains("3")).isTrue();
    }

    @Test
    public void testFindByDateRange_thenReturnInvoices() {

        populateTestData(1, "1", "abc", "2018-01-01");
        populateTestData(1, "2", "def", "2018-01-02");
        populateTestData(1, "3", "abc", "2018-01-03");
        populateTestData(1, "4", "def", "2018-01-04");

        List<Invoice> found = getInvoiceByParameters(10, 0, null, null, LocalDate.parse("2018-01-02"), LocalDate.parse("2018-01-03"));

        assertThat(found.size()).isEqualTo(2);
        assertThat(found.stream().map(Invoice::getInvoiceNumber).collect(Collectors.toList()).contains("2")).isTrue();
        assertThat(found.stream().map(Invoice::getInvoiceNumber).collect(Collectors.toList()).contains("3")).isTrue();
    }

    @Test
    public void testFindByInvoiceNumberAndPurchaseOrderNumberAndDateRange_thenReturnInvoices() {

        populateTestData(1, "1", "abc", "2018-01-01");
        populateTestData(1, "2", "def", "2018-01-02");
        populateTestData(1, "3", "abc", "2018-01-03");
        populateTestData(1, "4", "def", "2018-01-04");

        List<Invoice> found = getInvoiceByParameters(10, 0, "2", "def", LocalDate.parse("2018-01-02"), LocalDate.parse("2018-01-03"));

        assertThat(found.size()).isEqualTo(1);
        assertThat(found.stream().map(Invoice::getInvoiceNumber).collect(Collectors.toList()).contains("2"));
    }

    private List<Invoice> getInvoiceByParameters(int limit, int offset, String invoiceNumber, String def, LocalDate dueDateStart, LocalDate dueDateEnd) {
        OffsetLimitRequest offsetLimitRequest = new OffsetLimitRequest(offset, limit, new Sort(Sort.Direction.DESC, SORT_FIELD));
        return invoiceRepository.findByOptionalSearchParams(invoiceNumber, def, dueDateStart, dueDateEnd, offsetLimitRequest).getContent();
    }

    private void populateTestData(int count, String invoiceNumber, String purchaseOrderNumber, String dueDate) {
        for(int i = 0; i < count; i++) {
            Invoice invoice = new Invoice(i, invoiceNumber, purchaseOrderNumber,
                    LocalDate.parse(dueDate), 200000L, new Timestamp(System.currentTimeMillis()));
            entityManager.merge(invoice);
        }
        entityManager.flush();
    }
}
