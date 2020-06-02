package eapli.base.gestaoproducao.gestaomensagens.domain;

import eapli.base.gestaoproducao.gestaomaquina.domain.CodigoInternoMaquina;
import eapli.base.gestaoproducao.ordemProducao.domain.Identificador;
import eapli.base.gestaoproducao.ordemProducao.domain.OrdemProducao;
import eapli.framework.domain.model.AggregateRoot;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.Date;

@Entity
@DiscriminatorValue(value = TipoDeMensagem.Values.FIM_DE_ATIVIDADE)
public class MensagemFimDeAtividade extends Mensagem implements AggregateRoot<Long> {

    //Máquina;TipoMsg;DataHora;OrdemProducao
    public final Date dataHora;

    protected MensagemFimDeAtividade() {
        super();
        dataHora = null;
    }

    public MensagemFimDeAtividade(CodigoInternoMaquina maquinaID, Date dataHora, OrdemProducao ordem) {
        super(TipoDeMensagem.FIM_DE_ATIVIDADE, new TimestampEmissao(dataHora),maquinaID,ordem);

        if (dataHora == null) {
            throw new IllegalArgumentException("Não pode haver parametros null no Fim de atividade mensagem"); //
            // excepto o Ordem id que pode ser nulo
        }
        this.dataHora = dataHora;
    }
}
