package br.com.lcc.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.lcc.jpa.entities.LucasEntity;

@Repository
public interface LucasJpaRepository extends JpaRepository<LucasEntity, Long> {

}
