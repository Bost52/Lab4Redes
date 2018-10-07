package servidor;
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class IntentoServer {

	public static int NUMERO_CONEXIONES;
	public static int ARCHIVO;
	public static final int PUERTO_SERVIDOR = 8888;
	public static long inicioConexiones;
	public static long finConexiones;
	public static long inicioThreads;
	public static long finThreads;
	public static long respuestas;
	public static String[] respuestasClientes;

	public static void main(String[] args) throws IOException {

		//Pregunta por cual video enviar 
		Scanner sc = new Scanner(System.in);
		System.out.println("Ingrese el numero del video que desea enviar: (1) para el de 500MB o (2) para el de 250MB");
		int numeroArchivo = sc.nextInt();
		ARCHIVO = numeroArchivo;

		//Pregunta por el numero de clientes a manejar
		System.out.println("Ingrese el numero de clientes que se van a manejar: ");
		int numConexiones = sc.nextInt();
		NUMERO_CONEXIONES = numConexiones;
		sc.close();

		respuestasClientes = new String[NUMERO_CONEXIONES];

		inicioConexiones = System.currentTimeMillis();
		DatagramSocket serverSocket = new DatagramSocket(PUERTO_SERVIDOR);
		byte[] buf;
		DatagramPacket recibido;
		int pos = 0;

		InetAddress[] direcciones = new InetAddress[NUMERO_CONEXIONES];
		int[] puertos = new int[NUMERO_CONEXIONES];

		while(pos < NUMERO_CONEXIONES)
		{
			System.out.println("entra");
			buf = new byte[256];
			recibido = new DatagramPacket(buf, buf.length);
			serverSocket.receive(recibido);
			direcciones[pos] = recibido.getAddress();
			puertos[pos] = recibido.getPort();
			System.out.println(direcciones[pos] + "/" + puertos[pos] + " direccion/puerto");
			pos++;
		}

		finConexiones = System.currentTimeMillis();
		inicioThreads = System.currentTimeMillis();

		//Inicializo todos los threads y los arranco de una. 
		for(int x = 0; x < puertos.length; x++)
		{
			new IntentoServerThread(direcciones[x], puertos[x], ARCHIVO).start();
			System.out.println("Desplegado el thread " + x);
		}
		System.out.println("Desplegados todos los threads");
		finThreads = System.currentTimeMillis();

		//Guardo las respuestas de los clientes
		Integer g = 0;
		while(g < NUMERO_CONEXIONES)
		{
			synchronized (g) {
				buf = new byte[256];
				recibido = new DatagramPacket(buf, buf.length);
				serverSocket.receive(recibido);
				System.out.println(new String(recibido.getData()));
				respuestasClientes[g] = new String(recibido.getData());
				g++;
			}	
		}
		System.out.println("fin guardo respuestas");
		respuestas = System.currentTimeMillis();

		SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm:ss:SS");    

		//Empiezo a escribir en el log todo lo obtenido en la ruta obtenida.
		BufferedWriter writer = new BufferedWriter(new FileWriter("/home/s2g2/ServidoresLab4/ServidorUDP/log.txt",true));

		writer.write("Inicio prueba con " + NUMERO_CONEXIONES+ " cliente(s):");
		writer.newLine();
		Date resultdate = new Date(inicioConexiones);
		writer.write(sdf.format(resultdate));
		writer.newLine();
		writer.write("Fin conexiones: ");
		writer.newLine();
		Date resultdate2 = new Date(finConexiones);
		writer.write(sdf.format(resultdate2));
		writer.newLine();
		writer.write("Inicio Threads: ");
		writer.newLine();
		Date resultdate3 = new Date(inicioThreads);
		writer.write(sdf.format(resultdate3));
		writer.newLine();
		writer.write("Fin Threads: ");
		writer.newLine();
		Date resultdate4 = new Date(finThreads);
		writer.write(sdf.format(resultdate4));
		writer.newLine();
		writer.write("Tiempo de fin de respuestas: ");
		writer.newLine();
		Date resultdate5 = new Date(finThreads);
		writer.write(sdf.format(resultdate5));
		writer.newLine();
		System.out.println("fin log pt1");
		//Escribo las respuestas de los clientes
		for(int m = 0; m<NUMERO_CONEXIONES;m++)
		{
			writer.write("Respuesta del cliente: ");
			writer.newLine();
			writer.write(respuestasClientes[m]);
			writer.newLine();
		}
		System.out.println("fin log pt2");

		writer.close();
		serverSocket.close();
		System.out.println("Fin server");
	}
}