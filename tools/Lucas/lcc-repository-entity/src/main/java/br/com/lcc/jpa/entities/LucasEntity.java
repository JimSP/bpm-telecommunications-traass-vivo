package br.com.lcc.jpa.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@EqualsAndHashCode(of = { "id" })
@ToString
@Entity
public class LucasEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

}
