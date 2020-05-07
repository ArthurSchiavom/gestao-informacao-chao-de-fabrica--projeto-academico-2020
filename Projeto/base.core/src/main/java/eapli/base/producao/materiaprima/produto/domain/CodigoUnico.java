package eapli.base.producao.materiaprima.produto.domain;

import eapli.base.infrastructure.domain.IllegalDomainValue;
import eapli.base.infrastructure.domain.IllegalDomainValueType;
import eapli.base.infrastructure.persistence.PersistenceContext;
import eapli.base.infrastructure.persistence.RepositoryFactory;
import eapli.base.producao.materiaprima.produto.persistence.ProdutoRepository;
import eapli.framework.domain.model.ValueObject;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class CodigoUnico implements ValueObject, Serializable, Comparable<CodigoUnico> {

    private static final long serialVersionUID = 1L;

    private final String codigoUnico;

    // Embeddable constructor
    protected CodigoUnico() {
        codigoUnico = null;
    }

    protected CodigoUnico(String codigoUnico) throws IllegalDomainValue {
        if (codigoUnico == null || codigoUnico.isEmpty())
            throw new IllegalArgumentException("O código único deve existir e não ser vazio");
        RepositoryFactory repositoryFactory = PersistenceContext.repositories();
        ProdutoRepository produtoRepository = repositoryFactory.produto();
        if (produtoRepository.produtoOfCodigoUnico(codigoUnico).isPresent()) {
            throw new IllegalDomainValue("O código único indicado já está registado", IllegalDomainValueType.ALREADY_EXISTS);
        }
        this.codigoUnico = codigoUnico;
    }

    public static CodigoUnico valueOf(String codigoUnico) throws IllegalDomainValue {
        return new CodigoUnico(codigoUnico);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CodigoUnico)) {
            return false;
        }

        final CodigoUnico that = (CodigoUnico) o;

        return this.codigoUnico.equals(that.codigoUnico);
    }

    @Override
    public int hashCode() {
        return codigoUnico.hashCode();
    }

    @Override
    public String toString() {
        return codigoUnico;
    }

    @Override
    public int compareTo(CodigoUnico obj) {
        return this.codigoUnico.compareTo(obj.codigoUnico);
    }
}
