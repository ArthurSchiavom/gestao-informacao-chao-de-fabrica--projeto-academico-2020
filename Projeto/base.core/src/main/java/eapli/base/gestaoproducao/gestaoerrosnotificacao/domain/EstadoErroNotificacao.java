package eapli.base.gestaoproducao.gestaoerrosnotificacao.domain;

import eapli.base.gestaoproducao.gestaoerrosnotificacao.dto.EstadoNotificacaoErroDTO;
import eapli.base.infrastructure.application.ConvertableToDTO;

/**
 * O estado de erro da notificação
 * POR_TRATAR significa que ainda não foi resolvido
 * ARQUIVADO significa que foi resolvido
 */
public enum EstadoErroNotificacao implements ConvertableToDTO<EstadoNotificacaoErroDTO> {
	ATIVO("ativo"), ARQUIVADO("arquivado");

	public final String nomeDisplay;

	EstadoErroNotificacao(String nomeDisplay) {
		this.nomeDisplay = nomeDisplay;
	}

	@Override
	public EstadoNotificacaoErroDTO toDTO() {
		return new EstadoNotificacaoErroDTO(nomeDisplay);
	}

	public static EstadoErroNotificacao actualValueOf(String nome) {
		for (EstadoErroNotificacao estado : values()) {
			if (estado.nomeDisplay.equalsIgnoreCase(nome))
				return estado;
		}

		return null;
	}
}
