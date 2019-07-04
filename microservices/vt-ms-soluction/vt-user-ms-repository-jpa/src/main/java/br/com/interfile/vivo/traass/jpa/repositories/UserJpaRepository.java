package br.com.interfile.vivo.traass.jpa.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.interfile.vivo.traass.jpa.entities.UserEntity;

@Repository
public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {

	@Query(   "select u" //
			+ "  from UserEntity u" //
			+ " where u.documentValue = :documentValue" //
			+ "  or u.email = :email")
	public Optional<UserEntity> findByDocumentValueOrEmail(@Param("documentValue") final String documentValue,
			@Param("email") final String email);

	public Optional<UserEntity> findById(@Param("id") final Long id);

	@Query("select u from UserEntity u where u.id in (:ids)")
	public List<UserEntity> findByIds(@Param("ids") final List<Long> ids);
}
