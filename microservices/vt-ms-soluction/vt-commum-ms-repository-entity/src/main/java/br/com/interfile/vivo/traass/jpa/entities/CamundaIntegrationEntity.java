package br.com.interfile.vivo.traass.jpa.entities;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@EqualsAndHashCode(of = { "id" }, callSuper=false)
@Entity
@Table(indexes = { //
		@Index(columnList = "createDate"), //
		@Index(columnList = "alterDate") //
})
public class CamundaIntegrationEntity extends BaseEntity{

	@Id
	private String id;

	@Column
	private String definitionId;

	@Column
	private String businessKey;

	@Column
	private String caseInstanceId;

	@Column
	private String ended;

	@Column
	private String suspended;

	@Column
	private String tenantId;

	@Column
	private String link;

	@Column
	private String executionId;	
	
	@Column
	private Date createDt;
	
	@ManyToOne(cascade=CascadeType.ALL)
	private SolicitationEntity solicitationEntity;

}