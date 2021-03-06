#include "comunicacao_sistema_central.h"

#define TEMPO_SLEEP_ENTRE_CHECKS_PRIMEIRA_CONEXAO 1

void definir_ultimo_resultado_handshake_scm(Payload *payload) {
    pthread_mutex_lock(&mutex_ultimo_resultado_handshake_scm);
    if (payload == NULL) {
        sucesso_ultimo_resultado_handshake_scm = FALSE;
    }
    else {
        ultimo_resultado_handshake_scm = *payload;
        sucesso_ultimo_resultado_handshake_scm = TRUE;
    }
    pthread_mutex_unlock(&mutex_ultimo_resultado_handshake_scm);
}

Payload *ler_ultimo_resultado_handshake_scm() {
    if (sucesso_ultimo_resultado_handshake_scm == TRUE) {
        return &ultimo_resultado_handshake_scm;
    }
    else {
        return NULL;
    }
}

void sleep_ate_primeira_conexao_ser_bem_sucedida() {
    while (primeira_conexao_ao_sistema_central_terminada != TRUE) {
        sleep(TEMPO_SLEEP_ENTRE_CHECKS_PRIMEIRA_CONEXAO);
    }
}

/**
 * DEPRECATED
 */
int handshake_sistema_central_legacy() {
    Payload payload;
    int sucesso = handshake_tcp(endereco_sistema_central, PORTA_SISTEMA_CENTRAL, &payload);
    if (sucesso == TRUE) {
        free(payload.data);
    }

    return sucesso;
}

/**
 * DEPRECATED
 */
void reconectar_sistema_central_legacy() {
    char tentar;
    pthread_mutex_lock(&mutex_a_reconectar_ao_sistema_central);
    if (a_reconectar_ao_sistema_central == TRUE) {
        tentar = FALSE;
    }
    else {
        tentar = TRUE;
        a_reconectar_ao_sistema_central = TRUE;
    }
    pthread_mutex_unlock(&mutex_a_reconectar_ao_sistema_central);

    if (tentar == FALSE) {
        return;
    }

    printf("Falha ao tentar contactar o servidor central, a tentar reconectar.\n");

    int success = FALSE;
    int should_sleep = FALSE;
    while (success != TRUE) {
        printf("\n");
        Payload resultado;
        success = handshake_tcp(endereco_sistema_central, PORTA_SISTEMA_CENTRAL, &resultado);
        if (success == FALSE) {
            printf("Servidor central não alcançado. Próxima tentativa em %d segundos.\n",
                   TEMPO_ESPERA_RECONEXAO_SCM_SEGUNDOS);
            should_sleep = TRUE;
        }

        if (success == TRUE) {
            if (resultado.code == REQUEST_CODE_NACK) {
                printf("O servidor central recusou a tentativa de conexão por não reconhecer a máquina.\n");
                if (resultado.data[0] != 0) {
                    printf(": %s", resultado.data);
                }
                printf(". Próxima tentativa em %d segundos.\n", TEMPO_ESPERA_RECONEXAO_SCM_SEGUNDOS);
                should_sleep = TRUE;
            } else if (resultado.code != REQUEST_CODE_ACK) {
                printf("O servidor central recusou a tentativa de conexão por motivos desconhecidos. Próxima tentativa em %d segundos.\n",
                       TEMPO_ESPERA_RECONEXAO_SCM_SEGUNDOS);
                should_sleep = TRUE;
            } else {
                printf("Reconectado ao sistema central.\n");
                success = TRUE;
            }

            free(resultado.data);
        }

        if (should_sleep) {
            sleep(TEMPO_ESPERA_RECONEXAO_SCM_SEGUNDOS);
        }
    }

    a_reconectar_ao_sistema_central = FALSE;
}


int handshake_sistema_central() {
    Payload payload;
    int sucesso = handshake_ssl(endereco_sistema_central, PORTA_SISTEMA_CENTRAL, &payload, TRUE);
    if (sucesso == TRUE) {
        free(payload.data);
        definir_ultimo_resultado_handshake_scm(&payload);
    }
    else {
        definir_ultimo_resultado_handshake_scm(NULL);
    }


    return sucesso;
}

void reconectar_sistema_central() {
    char tentar;
    pthread_mutex_lock(&mutex_a_reconectar_ao_sistema_central);
    if (a_reconectar_ao_sistema_central == TRUE) {
        tentar = FALSE;
    }
    else {
        tentar = TRUE;
        a_reconectar_ao_sistema_central = TRUE;
    }
    pthread_mutex_unlock(&mutex_a_reconectar_ao_sistema_central);

    if (tentar == FALSE) {
        return;
    }

    printf("Falha ao tentar contactar o servidor central, a tentar reconectar.\n");

    int success = FALSE;
    int should_sleep = FALSE;
    while (success != TRUE) {
        printf("\n");
        Payload resultado;
        success = handshake_ssl(endereco_sistema_central, PORTA_SISTEMA_CENTRAL, &resultado, TRUE);
        if (success == FALSE) {
            printf("Falha na tentativa de conexão ao servidor central. Próxima tentativa em %d segundos.\n",
                   TEMPO_ESPERA_RECONEXAO_SCM_SEGUNDOS);
            should_sleep = TRUE;
            definir_ultimo_resultado_handshake_scm(NULL);
        }
        else {
            definir_ultimo_resultado_handshake_scm(&resultado);
        }

        if (success == TRUE) {
            if (resultado.code == REQUEST_CODE_NACK) {
                printf("O servidor central recusou a tentativa de conexão por não reconhecer a máquina\n");
                if (resultado.data[0] != 0) {
                    printf(": %s", resultado.data);
                }
                printf(". Próxima tentativa em %d segundos.\n", TEMPO_ESPERA_RECONEXAO_SCM_SEGUNDOS);
                should_sleep = TRUE;
            } else if (resultado.code != REQUEST_CODE_ACK) {
                printf("O servidor central recusou a tentativa de conexão por motivos desconhecidos. Próxima tentativa em %d segundos.\n",
                       TEMPO_ESPERA_RECONEXAO_SCM_SEGUNDOS);
                should_sleep = TRUE;
            } else {
                printf("Reconectado ao sistema central.\n");
                success = TRUE;
            }

            free(resultado.data);
        }

        if (should_sleep) {
            sleep(TEMPO_ESPERA_RECONEXAO_SCM_SEGUNDOS);
        }
    }

    a_reconectar_ao_sistema_central = FALSE;
}

void do_handshake_sistema_central_ate_sucesso() {
    int sucesso = handshake_sistema_central();
    if (!sucesso) {
        reconectar_sistema_central();
    }

    primeira_conexao_ao_sistema_central_terminada = TRUE;
}

int handshake_sistema_central_ate_sucesso() {
    pthread_t t;
    int success = pthread_create(&t, NULL, (void* (*)(void*)) do_handshake_sistema_central_ate_sucesso, NULL);
    if (success != 0) {
        return TRUE;
    }

    return FALSE;
}
