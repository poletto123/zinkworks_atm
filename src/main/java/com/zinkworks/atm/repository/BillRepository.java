package com.zinkworks.atm.repository;

import com.zinkworks.atm.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BillRepository extends JpaRepository<Bill, Integer> {

    @Query("SELECT quantity FROM Bill WHERE BILL_TYPE = :billType")
    public int getBillQuantity(@Param("billType") String billType);

}
