package eapli.base.processamentoMensagens.domain;

import eapli.framework.domain.model.ValueObject;

import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlValue;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Embeddable
public class InicioDeProcessamento implements ValueObject, Comparable<InicioDeProcessamento> {
    private static final long serialVersionUID = 1L;

    @XmlValue
    public final Date dataTempoInicio;

    protected InicioDeProcessamento(){
        dataTempoInicio=null;
    }

    public InicioDeProcessamento(String data,String tempo) throws ParseException {
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        String dataTempo=data+'T'+tempo;
        this.dataTempoInicio=format.parse(dataTempo);
    }

    @Override
    public int compareTo(InicioDeProcessamento o) {
        return this.dataTempoInicio.compareTo(o.dataTempoInicio);
    }
}