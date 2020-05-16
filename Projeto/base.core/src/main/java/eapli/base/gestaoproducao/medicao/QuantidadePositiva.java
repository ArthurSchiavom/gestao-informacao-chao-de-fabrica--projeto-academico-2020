package eapli.base.gestaoproducao.medicao;

import eapli.base.infrastructure.domain.IllegalDomainValueException;
import eapli.base.infrastructure.domain.IllegalDomainValueType;
import eapli.framework.domain.model.ValueObject;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class QuantidadePositiva implements ValueObject, Comparable<QuantidadePositiva> {

    private static final long serialVersionUID = 1L;

    /* O hibernate falha se for final. Isto acontece a todos os valores de Embeddables que
    * pertençam outro Embeddable, que por sua vez façam parte de uma @ElementCollection + @CollectionTable
    * */
    public double quantidadeValor;

    protected QuantidadePositiva() {
        quantidadeValor = 0;
    }

    protected QuantidadePositiva(double quantidade) throws IllegalDomainValueException {
        if (quantidade <= 0) {
            throw new IllegalDomainValueException("A quantidade deve ser maior que 0", IllegalDomainValueType.ILLEGAL_VALUE);
        }
        this.quantidadeValor = quantidade;
    }

    public static QuantidadePositiva valueOf(double quantidade) throws IllegalDomainValueException {
        return new QuantidadePositiva(quantidade);
    }

    /* O hibernate falha se não tiver getters e setters */
    public double getQuantidadeValor() {
        return quantidadeValor;
    }

    /* O hibernate falha se não tiver getters e setters */
    public void setQuantidadeValor(double quantidadeValor) {
        this.quantidadeValor = quantidadeValor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuantidadePositiva)) {
            return false;
        }

        final QuantidadePositiva that = (QuantidadePositiva) o;

        return quantidadeValor == that.quantidadeValor;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(quantidadeValor);
    }

    @Override
    public String toString() {
        return "" + quantidadeValor;
    }

    @Override
    public int compareTo(QuantidadePositiva obj) {
        return Double.compare(quantidadeValor, obj.quantidadeValor);
    }
}
