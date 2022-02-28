package com.zinkworks.atm.model;

import com.zinkworks.atm.model.Account;
import com.zinkworks.atm.model.Bill;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;

@RunWith(MockitoJUnitRunner.class)
public class BillTest {

    private Bill bill;

    @Before
    public void setup() {
        bill = new Bill();
        bill.setBillType(50);
        bill.setQuantity(20);
    }

    @Test
    public void testGettersSetters() {
        Assert.assertEquals(50, (int) bill.getBillType());
        Assert.assertEquals(20, bill.getQuantity());
    }
}
