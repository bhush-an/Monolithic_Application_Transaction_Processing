package com.app.repository;

import com.app.dto.TransactionResponseDTO;
import com.app.entities.Credential;
import com.app.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ITransactionRepository extends JpaRepository<Transaction, Long> {
	
	List<Transaction> findBySender(String sender);
	
	List<Transaction> findByReceiver(Credential receiver);
	
	//fetch data from two tables and wrap it up in ResponseDTO
	@Query("select new com.app.dto.TransactionResponseDTO(t.utr, t.sender, t.receiver.username, t.transactionDateTime, "
			+ "tr.credit, tr.debit, tr.balance) from Transaction t "
			+ "join fetch TransactionRecord tr ON t.utr = tr.utr.utr "
			+ "where ((t.sender = :username and tr.debit != 0) or (t.receiver.username = :username and tr.credit != 0))")
	List<TransactionResponseDTO> getAllTransactionsByUsername(@Param("username") String username);
	
	//fetch data from two tables and wrap it up in Object[] -- downcast date to LocalDateTime
//	@Query("select t.utr, t.sender, t.receiver.username, t.transactionDateTime, tr.credit, tr.debit, tr.balance from Transaction t "
//			+ "join fetch TransactionRecord tr ON t.utr = tr.utr.utr "
//			+ "where ((t.sender = :username and tr.debit != 0) or (t.receiver.username = :username and tr.credit != 0))")
//	List<Object[]> getAllTransactionsByUsername(@Param("username") String username);
	
	//fetch data from two tables and wrap it up in Object[] -- NATIVE QUERY MYSQL -- downcast data from TimeStamp to LocalDateTime
//	@Query(value = "select t.utr, t.sender, t.receiver, t.transaction_date_time, tr.credit, tr.debit, tr.balance from transaction t "
//			+ "INNER JOIN transaction_record tr on t.utr=tr.utr "
//			+ "WHERE (t.sender=:username and tr.debit!=0) or (t.receiver=:username and tr.credit!=0)", nativeQuery = true)
//	List<Object[]> getAllTransactionsByUsername(@Param("username") String username);
	
	//fetch data from two tables and wrap it up in ResponseDTO
	@Query("select new com.app.dto.TransactionResponseDTO(t.utr, t.sender, t.receiver.username, t.transactionDateTime, "
			+ "tr.credit, tr.debit, tr.balance) from Transaction t "
			+ "join fetch TransactionRecord tr ON t.utr = tr.utr.utr "
			+ "where (t.transactionDateTime between :startDate and :endDate) and "
			+ "((t.sender = :username and tr.debit != 0) or (t.receiver.username = :username and tr.credit != 0))")
	List<TransactionResponseDTO> getAllTransactionsByDate(@Param("username") String username, 
			LocalDateTime startDate, LocalDateTime endDate);
	
	//fetch data from two tables and wrap it up in ResponseDTO
		@Query("select new com.app.dto.TransactionResponseDTO(t.utr, t.sender, t.receiver.username, t.transactionDateTime, "
				+ "tr.credit, tr.debit, tr.balance) from Transaction t "
				+ "join fetch TransactionRecord tr ON t.utr = tr.utr.utr")
		List<TransactionResponseDTO> getAllTransactions();

}
