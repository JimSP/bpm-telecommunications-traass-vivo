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
@EqualsAndHashCode(of = { //
		"id" }, callSuper = false)
@ToString(exclude = { "streetName", "streetNumber", "complement" })
@Entity
@Table(indexes = { //
		@Index(columnList = "createDate"), //
		@Index(columnList = "alterDate") //
})
public class AddressEntity extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(length = 50)
	private String addressType;

	@Column(length = 50)
	private String roadType;

	@Column(length = 255)
	private String streetName;

	@Column
	private Integer streetNumber;

	@Column(length = 50)
	private String complement;

	@Column(length = 255)
	private String neighborhood;

	@Column(length = 255)
	private String city;

	@Column(length = 50)
	private String province;

	@Column(length = 50)
	private String country;

	@Column(length = 8)
	private String zipCode;

	@OneToOne(cascade = CascadeType.ALL)
	private SolicitationEntity solicitationEntity;

	@ManyToOne(cascade = CascadeType.ALL)
	private UserEntity userEntity;

	@ManyToOne(cascade = CascadeType.ALL)
	private DonnorEntity donnorEntity;

	@ManyToOne(cascade = CascadeType.ALL)
	private TransfereeEntity transfereeEntity;

}
