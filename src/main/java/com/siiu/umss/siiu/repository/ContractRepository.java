/**
 * @author: Edson A. Terceros T.
 */

package com.siiu.umss.siiu.repository;

import com.siiu.umss.siiu.model.Contract;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContractRepository extends JpaRepository<Contract, Long> {
}
