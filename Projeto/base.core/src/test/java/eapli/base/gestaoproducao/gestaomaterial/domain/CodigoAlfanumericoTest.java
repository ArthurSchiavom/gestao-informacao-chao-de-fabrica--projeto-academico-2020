package eapli.base.gestaoproducao.gestaomaterial.domain;

import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class CodigoAlfanumericoTest {
    @Test(expected = IllegalArgumentException.class)
    public void garantirIdentificadorNaoPodeSerNull() {
        new CodigoAlfanumerico(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void garantirIdentificadorNaoPodeSerStringVazia() {
        new CodigoAlfanumerico("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void garantirIdentificadorNaoSerVazioComEspacos() {
        new CodigoAlfanumerico(" ");
    }

    @Test
    public void garantirNomeAtributoIdentidadeExiste() {
        boolean found = false;
        String identificador = Categoria.identityAttributeName();
        for (Field field : Categoria.class.getDeclaredFields()) {
            if (identificador.equals(field.getName())) {
                found = true;
                break;
            }
        }
        if(found) {
            assertTrue(true);
        } else {
            fail("Verificar metodo identityAttributeName");
        }
    }
}