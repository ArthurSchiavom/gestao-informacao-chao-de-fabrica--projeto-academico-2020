package eapli.base.solicitarConfiguracaoMaquina.application;

import eapli.base.gestaoproducao.gestaomaquina.domain.CodigoInternoMaquina;
import eapli.base.gestaoproducao.gestaomaquina.domain.Maquina;
import eapli.base.gestaoproducao.gestaomaquina.repository.MaquinaRepository;
import eapli.base.infrastructure.persistence.PersistenceContext;
import eapli.base.solicitarConfiguracaoMaquina.tcp.EnviarConfiguracaoMaquinaTcp;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class SolicitarConfiguracaoMaquinaController {

    public boolean enviarConfigPorTcp(String caminho, String codInterno) {

        MaquinaRepository repo = PersistenceContext.repositories().maquinas();
        CodigoInternoMaquina cod = new CodigoInternoMaquina(codInterno);
        Maquina maq;
        try{
            maq = repo.findByIdentifier(cod).get();
        }catch(Exception ex){
            return false;
        }

        String data;
        try {
           data = getFicheiro(caminho);
        } catch (FileNotFoundException e) {
            return false;
        }

        EnviarConfiguracaoMaquinaTcp.enviarConfiguracaoMaquinaTcp(data,maq);

        return true;
    }


    /**
     * Load do ficheiro config
     * @throws FileNotFoundException caso o ficheiro nao exista
     */
    private String getFicheiro(String caminho) throws FileNotFoundException {
        Scanner in = new Scanner(new File(caminho));
        String data = "";
        while(in.hasNext()){
            data+= in.next();
        }
        return data;
    }
}
