package Mensagens.application.broadcast;

import Mensagens.domain.HelloBroadcast;
import Mensagens.domain.Version;

import java.io.IOException;
import java.net.*;

/**
 * Está encarrege de enviar mensagens HELLO ás máquinas e criar novas threads por cada máquina
 * que responde
 */
public class BroadcastHelloService implements Runnable {
	private static final int TIMEOUT = 100; //em miligsegundos
	private static final int SLEEP = 30; //em segundos
	private static final int PORT = 7194;

	@Override
	public void run() {
		Version version = new Version(0);
		DatagramSocket socket = null;
		try {
			socket = new DatagramSocket();
			socket.setBroadcast(true);
		} catch (SocketException e) {
			System.out.println("Falha a criar socket de broadcast");
		}
		assert socket != null;
		//Envia um broadcast a cada 30 segundos
		//noinspection InfiniteLoopStatement
		while (true) {
			HelloBroadcast helloMsg = null;
			try {
				helloMsg = new HelloBroadcast(version, PORT);
			} catch (UnknownHostException e) {
				System.out.println("Falha a criar pacote de HELLO broadcast");
			}
			assert helloMsg != null;
			try {
				socket.send(helloMsg.getUdpPacket());
			} catch (IOException e) {
				System.out.println("Falha a enviar pacote de HELLO broadcast");
			}
			//Cria X threads até dar timeout de espera de receber respostas
			while (true) {
				try {
					socket.setSoTimeout(TIMEOUT);
					try {
						socket.receive(helloMsg.getUdpPacket());
					} catch (IOException e) {
						System.out.println("Erro a receber pacote");
					}
					ReceiveAcknowledgmentService acknowledgmentService = new ReceiveAcknowledgmentService(helloMsg.getUdpPacket());
					Thread acknowledgeThread  = new Thread(acknowledgmentService);
					acknowledgeThread.start();
					try {
						acknowledgeThread.join();
					} catch (InterruptedException e) {
						System.out.println("Erro a fazer join da thread de acknowledgement");
					}
				} catch (SocketException e) {
					//Quando o socket exception é atirado quer dizer que o tempo do timeout passou
					break;
				}
			}
			//Dorme por 30 segundos
			try {
				Thread.sleep(SLEEP * 1000);
			} catch (InterruptedException e) {
				System.out.println("Erro a esperar durante " + SLEEP + " segundos");
			}
		}
	}
}