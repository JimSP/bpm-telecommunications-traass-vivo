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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
@Builder(toBuilder=true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = { "id" }, callSuper=false)
@ToString
@Entity
@Table(indexes = { //
		@Index(columnList = "createDate"), //
		@Index(columnList = "alterDate") //
})
public class DonnorEntity extends BaseEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column
	private String donnorName;

	@Column
	private String donnorDocumentType;

	@Column
	private String donnorDocumentValue;

	@Column
	private String donnorRg;

	@Column
	private String donnorEmail;
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(insertable=true, updatable=true)
	@Fetch(FetchMode.SUBSELECT)
	private List<AddressEntity> donnorAddresses;
	
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(insertable=true, updatable=true)
	private PhoneEntity donnorPhone;
	
	@ManyToOne(cascade=CascadeType.ALL)
	private SolicitationEntity solicitationEntity;

}
