package com.crm.project.features.crm;

import com.crm.project.database.JsonDatabase;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CustomerRepository {

    private static final String FILE_CUSTOMER = "data/customers.json";
    private static final Type TIPE_LIST_CUSTOMER = new TypeToken<ArrayList<Customer>>() {
    }.getType();

    public CustomerRepository() {
        JsonDatabase.siapkanFile(FILE_CUSTOMER);
    }

    public List<Customer> ambilSemuaCustomer() {
        List<Customer> hasil = JsonDatabase.bacaSemuaData(FILE_CUSTOMER, TIPE_LIST_CUSTOMER);
        return hasil != null ? hasil : new ArrayList<>();
    }

    public boolean tambahCustomer(Customer customerBaru) {
        List<Customer> semuaCustomer = ambilSemuaCustomer();

        List<Integer> semuaId = semuaCustomer.stream()
                .map(Customer::getId)
                .collect(Collectors.toList());
        int idBaru = JsonDatabase.generateIdBaru(semuaId);

        customerBaru.setId(idBaru);
        semuaCustomer.add(customerBaru);

        return JsonDatabase.simpanSemuaData(FILE_CUSTOMER, semuaCustomer);
    }

    public boolean hapusCustomer(int idCustomer) {
        List<Customer> semuaCustomer = ambilSemuaCustomer();

        boolean berhasilHapus = semuaCustomer.removeIf(
                customer -> customer.getId() == idCustomer);

        if (berhasilHapus) {
            return JsonDatabase.simpanSemuaData(FILE_CUSTOMER, semuaCustomer);
        }
        return false;
    }

    public Customer cariDenganId(int idCustomer) {
        return ambilSemuaCustomer().stream()
                .filter(customer -> customer.getId() == idCustomer)
                .findFirst()
                .orElse(null);
    }
}
