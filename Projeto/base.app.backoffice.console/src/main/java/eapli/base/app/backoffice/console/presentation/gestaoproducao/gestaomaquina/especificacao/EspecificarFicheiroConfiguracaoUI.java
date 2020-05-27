package eapli.base.app.backoffice.console.presentation.gestaoproducao.gestaomaquina.especificacao;

import eapli.base.app.common.console.presentation.formatter.ConsoleTables;
import eapli.base.app.common.console.presentation.interaction.UserInteractionFlow;
import eapli.base.gestaoproducao.gestaomaquina.aplication.dto.MaquinaDTO;
import eapli.framework.presentation.console.AbstractUI;
import eapli.framework.util.Console;

import java.io.IOException;
import java.util.List;

public class EspecificarFicheiroConfiguracaoUI extends AbstractUI {
    @Override
    protected boolean doShow() {
        EspecificarFicheiroConfiguracaoController controller = new EspecificarFicheiroConfiguracaoController();
        List<MaquinaDTO> maquinasSemFicheiroDeConfiguracao = controller.maquinasSemFicheiroDeConfiguracao();
        if (maquinasSemFicheiroDeConfiguracao.isEmpty()) {
            System.out.println("Não há nenhuma maquina sem ficheiro de configuracao.\n");
            UserInteractionFlow.enterToContinue();
            return false;
        }

        String maquinasSemFicheiroDeConfiguracaoDisplay = ConsoleTables.tabelaDeMaquinas(maquinasSemFicheiroDeConfiguracao);
        System.out.println(maquinasSemFicheiroDeConfiguracaoDisplay + "\n\n");

        String idMaquina ;
        boolean continuar = true;
        while (continuar) {
            idMaquina = Console.readNonEmptyLine("Indique o código interno da maquina cuja ficheiro de configuracao deseja alterar.", "Indique um código interno valido.");
            continuar = !controller.selecionarMaquina(idMaquina);
            if (continuar) {
                System.out.println("O código indicado não se encontra registado a uma maquina sem ficheiro de configuracao.\n");
            }
        }
        System.out.println("Registo da ficheiro de configuracao em progresso...\n");
        try {
            String nomeFicheiro= Console.readNonEmptyLine("Insira o nome do ficheiro: ","Nome do ficheiro nao pode ser vazio!");
            String descricao=Console.readNonEmptyLine("Insira as configuracoes no ficheiro: ","Este campo nao pode ser vazio!");
            controller.registar(descricao,nomeFicheiro);
            System.out.println("Ficha de produção registada com sucesso!\n");
        } catch (IOException e) {
            System.out.println("Ocorreu um erro: " + e.getMessage());
        } catch (IllegalArgumentException e){
            System.out.println("Ocorreu um erro: "+ e.getMessage());
        }

        UserInteractionFlow.enterToContinue();
        return false;
    }

    @Override
    public String headline() {
        return null;
    }
}