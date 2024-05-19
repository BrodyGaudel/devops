package com.brodygaudel.accountservice.query.repository;

import com.brodygaudel.accountservice.query.entity.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OperationRepository extends JpaRepository<Operation, String> {
    /**
     * Retrieves a page of {@link Operation} objects associated with the specified account ID.
     *
     * @param accountId the ID of the account for which to retrieve operations
     * @param pageable  the pagination information
     * @return a {@link Page} containing the operations associated with the specified account ID
     */
    @Query("select o from Operation o where o.account.id = ?1")
    Page<Operation> findByAccountId(String accountId, Pageable pageable);
}
