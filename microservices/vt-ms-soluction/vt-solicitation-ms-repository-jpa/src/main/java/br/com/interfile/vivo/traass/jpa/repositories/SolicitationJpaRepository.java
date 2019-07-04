package br.com.interfile.vivo.traass.jpa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.interfile.vivo.traass.jpa.entities.SolicitationEntity;

@Repository
public interface SolicitationJpaRepository extends JpaRepository<SolicitationEntity, Long> {

	public List<SolicitationEntity> findByUserEntity_Id(@Param("id") final Long id);

	@Query("select s from SolicitationEntity s where s.id in (:ids)")
	public List<SolicitationEntity> findByIds(@Param("ids") final List<Long> list);

	public List<SolicitationEntity> findByCamundaIntegrationIsNull();

	public List<SolicitationEntity> findByCamundaIntegration_ExecutionId(
			@Param("executionId") final String executionId);

	@Query("update SolicitationEntity set camundaIntegration.id = :camundaIntegrationId where id = :id")
	@Modifying
	public void updateSolicitationIntegrated(@Param("camundaIntegrationId") final String camundaIntegrationId,
			@Param("id") final Long id);

	public List<SolicitationEntity> findByIdOrderByCamundaIntegration_CreateDate(
			@Param("solicitationId") final Long solicitationId);
}
