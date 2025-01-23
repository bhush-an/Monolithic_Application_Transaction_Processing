package com.app.repository;

import com.app.entities.TransactionRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITransactionRecordRepository extends JpaRepository<TransactionRecord, Long> {

}
