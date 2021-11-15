/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.repository;

import com.umss.siiu.model.Contract;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContractRepository extends JpaRepository<Contract, Long> {
}
