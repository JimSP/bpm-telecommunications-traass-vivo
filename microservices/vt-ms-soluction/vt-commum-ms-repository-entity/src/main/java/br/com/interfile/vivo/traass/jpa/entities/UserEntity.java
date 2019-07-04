package br.com.interfile.vivo.traass.jpa.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = { "id" }, callSuper = false)
@ToString(exclude = { "password", "documentValue", "email" })
@Entity
@Table(indexes = { //
		@Index(columnList = "createDate"), //
		@Index(columnList = "alterDate") //
})
public class UserEntity extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(length = 255, nullable = false)
	private String name;

	@Column(length = 10, nullable = false)
	private String documentType;

	@Column(length = 14, nullable = false, unique = true)
	private String documentValue;

	@Column(length = 255, nullable = false, unique = true)
	private String email;

	@Column(length = 16, nullable = false)
	private String password;

	@Column(nullable = false)
	private Boolean verified;

	@OneToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
	@JoinColumn(insertable=true, updatable=true)
	@Fetch(FetchMode.SUBSELECT)
	private List<AddressEntity> addressEntitys;

	@OneToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
	@JoinColumn(insertable=true, updatable=true)
	@Fetch(FetchMode.SUBSELECT)
	private List<PhoneEntity> phoneEntitys;
	
	@OneToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
	@JoinColumn(insertable=true, updatable=true)
	@Fetch(FetchMode.SUBSELECT)
	private List<SolicitationEntity> SolicitationEntitys;

}
