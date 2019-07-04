package br.com.interfile.vivo.traass.jpa.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

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
@ToString(exclude = { "phoneNumber" })
@Entity
@Table(indexes = { //
		@Index(columnList = "createDate"), //
		@Index(columnList = "alterDate") //
})
public class PhoneEntity extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(length = 10, nullable = true)
	private String phoneType;

	@Column(length = 2, nullable = true)
	private Integer countryCode;

	@Column(length = 2, nullable = true)
	private Integer areaCode;

	@Column(length = 2, nullable = true)
	private Integer operatorCode;

	@Column(length = 25, nullable = false)
	private String phoneNumber;

	@ManyToOne(cascade = CascadeType.ALL)
	private UserEntity userEntity;

	@OneToOne(cascade = CascadeType.ALL)
	private DonnorEntity donnorEntity;

	@OneToOne(cascade = CascadeType.ALL)
	private TransfereeEntity transfereeEntity;
}
