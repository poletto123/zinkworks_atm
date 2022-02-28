package com.zinkworks.atm.service;

import com.zinkworks.atm.model.Bill;
import com.zinkworks.atm.repository.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class BillService {

    @Autowired
    private BillRepository billRepository;

    public List<Bill> getAllAvailableBills() {
        return billRepository.findAll();
    }

    public List<Bill> createEmptyBillList() {
        List<Bill> bills = new ArrayList<>();
        bills.add(new Bill(50, 0));
        bills.add(new Bill(20, 0));
        bills.add(new Bill(10, 0));
        bills.add(new Bill(5, 0));
        return bills;
    }

}
