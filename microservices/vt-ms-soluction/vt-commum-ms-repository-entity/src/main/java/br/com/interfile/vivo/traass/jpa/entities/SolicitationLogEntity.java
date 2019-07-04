package br.com.interfile.vivo.traass.jpa.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@EqualsAndHashCode(of = { "id" }, callSuper = false)
@Entity
@Table(indexes = { //
		@Index(columnList = "protocolNumber"), //
		@Index(columnList = "operator"), //
		@Index(columnList = "solicitationId"), //
		@Index(columnList = "createDate"), //
		@Index(columnList = "alterDate") //
})
public class SolicitationLogEntity extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column
	private String solicitationStatus;

	@Column
	private Long solicitationId;

	@Column
	private String protocolNumber;

	@Column
	private String channelReception;

	@Column
	private String entryMailbox;

	@Column
	private String comment;

	@Column
	private String operator;

	@Column
	private Long userId;

}
