package br.com.interfile.vivo.traass.jpa.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = { "id" }, callSuper = false)
@Entity
@Table(indexes = { //
		@Index(columnList = "createDate"), //
		@Index(columnList = "alterDate") //
})
public class DigitalDocumentEntity extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(length = 255, unique = true, nullable = false)
	private String referenceName;

	@Column(length = 2048, unique = true, nullable = false)
	private String url;

	@Column
	private String documentType;
	
	@ManyToOne(cascade=CascadeType.ALL)
	private SolicitationEntity solicitationEntity;
}
