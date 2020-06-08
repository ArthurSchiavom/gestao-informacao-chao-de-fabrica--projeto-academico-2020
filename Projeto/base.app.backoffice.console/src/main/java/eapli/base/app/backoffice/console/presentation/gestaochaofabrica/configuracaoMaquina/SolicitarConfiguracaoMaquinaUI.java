package eapli.base.app.backoffice.console.presentation.gestaochaofabrica.configuracaoMaquina;

import eapli.base.solicitarConfiguracaoMaquina.application.SolicitarConfiguracaoMaquinaController;
import eapli.framework.presentation.console.AbstractUI;
import eapli.framework.util.Console;

public class SolicitarConfiguracaoMaquinaUI extends AbstractUI {

    SolicitarConfiguracaoMaquinaController controller =
            new SolicitarConfiguracaoMaquinaController();

    @Override
    protected boolean doShow() {
        String caminho = Console.readLine("Introduza o caminho do ficheiro de configuração a carregar: ");

        String codigoInterno = Console.readLine("Insira o código interno da máquina: ");
        try {
            boolean sucesso = controller.enviarConfigPorTcp(caminho, codigoInterno);

            if (sucesso) {
                System.out.println("Operação bem sucedida");
            } else {
                System.out.println("Ocorreu um erro");
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    @Override
    public String headline() {
        return "Configuração máquina";
    }
}