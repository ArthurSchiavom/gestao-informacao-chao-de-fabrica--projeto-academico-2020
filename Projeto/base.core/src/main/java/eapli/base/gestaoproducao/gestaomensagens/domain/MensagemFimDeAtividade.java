package eapli.base.gestaoproducao.gestaomensagens.domain;

import eapli.base.gestaoproducao.gestaomaquina.domain.CodigoInternoMaquina;
import eapli.base.gestaoproducao.ordemProducao.domain.Identificador;
import eapli.framework.domain.model.AggregateRoot;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.Date;

@Entity
@DiscriminatorValue(value = TipoDeMensagem.Values.FIM_DE_ATIVIDADE)
public class MensagemFimDeAtividade extends Mensagem implements AggregateRoot<Long> {

    //Máquina;TipoMsg;DataHora;OrdemProducao
    public final CodigoInternoMaquina maquinaID;
    public final Date dataHora;
    private Identificador ordemID; // pode ser null

    protected MensagemFimDeAtividade() {
        super();
        maquinaID = null;
        dataHora = null;
    }

    public MensagemFimDeAtividade(CodigoInternoMaquina maquinaID, Date dataHora,
                                  Identificador ordemID) {
        super(TipoDeMensagem.FIM_DE_ATIVIDADE, new TimestampEmissao(dataHora));

        if (maquinaID == null || dataHora == null) {
            throw new IllegalArgumentException("Não pode haver parametros null no Fim de atividade mensagem"); //
            // excepto o Ordem id que pode ser nulo
        }
        this.maquinaID = maquinaID;
        this.dataHora = dataHora;
        this.ordemID = ordemID;
    }
}
