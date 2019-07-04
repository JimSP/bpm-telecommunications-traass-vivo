package br.com.interfile.vivo.traass.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum SolicitationStatus {

	Open {
		@Override
		public List<SolicitationStatus> possibles() {
			return Arrays.asList(Pending, InAnalysis, InConfiguration, WithProblem, Close);
		}
		
		@Override
		public Boolean visibleForEdit() {
			return Boolean.TRUE;
		}
	},
	Pending {
		@Override
		public List<SolicitationStatus> possibles() {
			return Arrays.asList(InAnalysis, InConfiguration, WithProblem, Close);
		}
		
		@Override
		public Boolean visibleForEdit() {
			return Boolean.TRUE;
		}
	},
	InAnalysis {
		@Override
		public List<SolicitationStatus> possibles() {
			return Arrays.asList(Pending, InConfiguration, WithProblem, Close);
		}
	},
	InConfiguration {
		@Override
		public List<SolicitationStatus> possibles() {
			return Arrays.asList(Pending, InAnalysis, WithProblem, Close);
		}
	},
	WithProblem {
		@Override
		public List<SolicitationStatus> possibles() {
			return Arrays.asList(Pending, InConfiguration, InAnalysis, Close);
		}
	},
	Close {
		@Override
		public List<SolicitationStatus> possibles() {
			return Collections.emptyList();
		}
	},
	Invalid {
		@Override
		public List<SolicitationStatus> possibles() {
			return Collections.emptyList();
		}
	};

	public abstract List<SolicitationStatus> possibles();

	public Boolean visibleForEdit() {
		return Boolean.FALSE;
	}

	public static SolicitationStatus create(final String name) {
		return Arrays.asList(SolicitationStatus.values()) //
				.stream() //
				.filter(predicate -> predicate.name().equals(name)) //
				.findFirst() //
				.orElse(SolicitationStatus.Invalid);
	}

	public static Boolean isOpen(final SolicitationStatus solicitationStatus) {
		return Open == solicitationStatus;
	}
	
	public static Boolean isPending(final SolicitationStatus solicitationStatus) {
		return Pending == solicitationStatus;
	}
	
	public static Boolean isInAnalysis(final SolicitationStatus solicitationStatus) {
		return InAnalysis == solicitationStatus;
	}
	
	public static Boolean isInConfiguration(final SolicitationStatus solicitationStatus) {
		return InConfiguration == solicitationStatus;
	}
	
	public static Boolean isWithProblem(final SolicitationStatus solicitationStatus) {
		return WithProblem == solicitationStatus;
	}
	
	public static Boolean isClose(final SolicitationStatus solicitationStatus) {
		return Close == solicitationStatus;
	}
	
	public static Boolean isInvalid(final SolicitationStatus solicitationStatus) {
		return Invalid == solicitationStatus;
	}
}
