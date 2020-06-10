package eapli.base.processamentoMensagens.application.TiposMensagensNotificacao;

import eapli.base.gestaoproducao.gestaoerrosnotificacao.domain.NotificacaoErro;
import eapli.base.gestaoproducao.gestaolinhasproducao.domain.LinhaProducao;
import eapli.base.gestaoproducao.gestaolinhasproducao.repository.LinhaProducaoRepository;
import eapli.base.gestaoproducao.gestaomensagens.domain.Mensagem;
import eapli.base.gestaoproducao.gestaomensagens.repository.MensagemRepository;
import eapli.base.processamentoMensagens.application.ValidacaoParametrosMensagensServico;

public interface CriacaoNotificacaoStrategy {

    NotificacaoErro validarMensagem(LinhaProducao linhaProducao, LinhaProducaoRepository linhaProducaoRepository, MensagemRepository mensagemRepository, Mensagem mensagem, ValidacaoParametrosMensagensServico validacao);
}
