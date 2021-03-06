#include <stdlib.h>
#include "boot.h"
#include "../maquina/informacao_maquina.h"
#include "../comunicacao/informacao_comunicacao.h"
#include "../mensagem/carregamento_mensagem.h"
#include "../utils/const.h"
#include "../comunicacao/scm/comunicacao_sistema_central.h"
#include "../comunicacao/scm/certificado.h"
#include <string.h>

int boot(char *id_maquina_param, char *intervalo_segundos_param, char *caminho_ficheiro_mensagens_param,
         char *endereco_sistema_central_param, char *endereco_smm_param, char *nome_fich_key_pem_param,
         char *id_linha_producao_param) {
    boot_certificado(nome_fich_key_pem_param);
    id_maquina = (unsigned short) atoi(id_maquina_param);
    id_linha_producao=id_linha_producao_param;
    intervalo_entre_mensagens_segundos = atoi(intervalo_segundos_param);
    setup_leitor_mensagens(caminho_ficheiro_mensagens_param);
    endereco_sistema_central = malloc(strlen(endereco_sistema_central_param) + 1);
    strcpy(endereco_sistema_central, endereco_sistema_central_param);
    endereco_smm = malloc(strlen(endereco_smm_param) + 1);
    strcpy(endereco_smm, endereco_smm_param);

    int success = pthread_mutex_init(&mutex_a_reconectar_ao_sistema_central, NULL);
    if (success != 0) {
        perror("Falha ao tentar iniciar semáforo para o sistema de reconexão ao sistema central.");
        return FALSE;
    }

    success = pthread_mutex_init(&mutex_ultimo_resultado_handshake_scm, NULL);
    if (success != 0) {
        perror("Falha ao tentar iniciar semáforo para o último resultado do handshake com o SCM.");
        return FALSE;
    }

    handshake_sistema_central_ate_sucesso();

    return TRUE;
}
