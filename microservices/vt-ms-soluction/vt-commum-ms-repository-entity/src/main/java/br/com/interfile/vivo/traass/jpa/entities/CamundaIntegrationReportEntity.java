package br.com.interfile.vivo.traass.jpa.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
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
@EqualsAndHashCode(of = { "executionId" }, callSuper = false)
@Entity
@Table(indexes = { //
		@Index(columnList = "createDate"), //
		@Index(columnList = "alterDate") //
})
public class CamundaIntegrationReportEntity extends BaseEntity {

	@Id
	private String executionId;

	@Column
	private Integer total;

	@Column
	private Integer qtdSuccess;

	@Column
	private Integer qtdErrors;

	@Column
	private Integer qtdFail;
}
