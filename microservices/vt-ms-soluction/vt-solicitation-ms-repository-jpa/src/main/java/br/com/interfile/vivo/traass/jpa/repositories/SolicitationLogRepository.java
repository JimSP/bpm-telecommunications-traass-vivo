package br.com.interfile.vivo.traass.jpa.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.interfile.vivo.traass.jpa.entities.SolicitationLogEntity;

@Repository
public interface SolicitationLogRepository extends JpaRepository<SolicitationLogEntity, Long> {

	List<SolicitationLogEntity> findBySolicitationIdOrderById(final Long id);

	List<SolicitationLogEntity> findByCreateDateBetweenAndSolicitationStatusIn(final Date begin,
			final Date end, final String... asList);

}
