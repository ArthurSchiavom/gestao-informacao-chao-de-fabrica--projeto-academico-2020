package eapli.base.gestaoproducao.gestaoproduto.domain;

import eapli.base.gestaoproducao.gestaomateriaprima.domain.QuantidadeDeMateriaPrima;
import eapli.base.gestaoproducao.gestaoproduto.application.dto.FichaDeProducaoDTO;
import eapli.base.gestaoproducao.gestaoproduto.application.dto.QuantidadeDeMateriaPrimaDTO;
import eapli.base.infrastructure.application.HasDTO;
import eapli.base.infrastructure.domain.IllegalDomainValueException;
import eapli.base.infrastructure.domain.IllegalDomainValueType;
import eapli.base.utilities.Lists;
import eapli.framework.domain.model.AggregateRoot;
import eapli.framework.domain.model.DomainEntities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class FichaDeProducao implements AggregateRoot<Integer>, HasDTO<FichaDeProducaoDTO> {

    @Version
    private Long version;

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    public final Integer uniqueVal;
    public static String identityAttributeName() {
        return "uniqueVal";
    }

    @ElementCollection
    @CollectionTable
    public List<QuantidadeDeMateriaPrima> quantidadesDeMateriaPrima;

    public FichaDeProducao() {
        uniqueVal = 0;
    }

    protected FichaDeProducao(List<QuantidadeDeMateriaPrima> quantidadesDeMateriaPrima) throws IllegalDomainValueException {
        if (!thisIsValid(quantidadesDeMateriaPrima)) {
            throw new IllegalDomainValueException("A lista de quantidades de matéria prima deve existir e ter pelo menos um elemento.", IllegalDomainValueType.ALREADY_EXISTS);
        }
        this.quantidadesDeMateriaPrima = new ArrayList<>(quantidadesDeMateriaPrima);
        uniqueVal = 0;
    }

    private boolean thisIsValid(List<QuantidadeDeMateriaPrima> quantidadesDeMateriaPrima) {
        return quantidadesDeMateriaPrima != null && quantidadesDeMateriaPrima.size() > 0;
    }

    public static FichaDeProducao valueOf(List<QuantidadeDeMateriaPrima> quantidadesDeMateriaPrima) throws IllegalDomainValueException {
        return new FichaDeProducao(quantidadesDeMateriaPrima);
    }

    @Override
    public boolean equals(final Object o) {
        return DomainEntities.areEqual(this, o);
    }

    @Override
    public int hashCode() {
        return DomainEntities.hashCode(this);
    }

    @Override
    public boolean sameAs(final Object other) {
        return DomainEntities.areEqual(this, other);
    }

    @Override
    public Integer identity() {
        return this.uniqueVal;
    }

    @Override
    public String toString() {
        if (quantidadesDeMateriaPrima.isEmpty())
            return "Ficha de produção vazia";

        return Lists.generateColonSeparatedDisplayList(quantidadesDeMateriaPrima);
    }

    @Override
    public FichaDeProducaoDTO toDTO() {
        List<QuantidadeDeMateriaPrimaDTO> resultado = new ArrayList<>();
        for (QuantidadeDeMateriaPrima quantidade : this.quantidadesDeMateriaPrima) {
            resultado.add(quantidade.toDTO());
        }
        return new FichaDeProducaoDTO(resultado);
    }
}