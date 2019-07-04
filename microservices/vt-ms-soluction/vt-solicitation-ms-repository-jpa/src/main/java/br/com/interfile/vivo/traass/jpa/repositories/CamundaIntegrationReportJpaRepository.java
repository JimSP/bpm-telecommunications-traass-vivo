package br.com.interfile.vivo.traass.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.interfile.vivo.traass.jpa.entities.CamundaIntegrationReportEntity;

@Repository
public interface CamundaIntegrationReportJpaRepository extends JpaRepository<CamundaIntegrationReportEntity, String>{
	
}
