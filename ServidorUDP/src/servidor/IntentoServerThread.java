package servidor;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;


public class IntentoServerThread extends Thread{

	public byte[] archivo;
	public byte[] hash;
	public int numero;

	public InetAddress direccion;
	public int puerto;

	public IntentoServerThread(InetAddress inetAddress, int puerto, int numero) {

		this.direccion = inetAddress;
		this.puerto = puerto;
		this.numero = numero;
	}

	public void cargar(int numero) throws NoSuchAlgorithmException, IOException
	{
		if(numero==1)
		{
			Path fileLocation = Paths.get("/home/s2g2/servidorTCP/Video500.mp4");
			archivo = Files.readAllBytes(fileLocation);

			byte[] hash2 = new byte[61440];
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			hash2 = md.digest(archivo); 
			hash = hash2;
		}
		else 
		{
			Path fileLocation = Paths.get("/home/s2g2/servidorTCP/Video500.mp4");
			archivo = Files.readAllBytes(fileLocation);

			byte[] hash2 = new byte[61440];
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			hash2 = md.digest(archivo); 
			hash = hash2;
		}
	}

	@Override
	public void run() {

		try
		{     
			DatagramSocket socket = new DatagramSocket();
			DatagramPacket paquete;

			if(numero == 1)
			{
				cargar(1);
				String informacion = "8358";
				byte[] info = informacion.getBytes();
				paquete = new DatagramPacket(info, info.length, direccion, puerto);
				socket.send(paquete);

				String tamanoUltimo = "24949";
				byte[] infoultimo = tamanoUltimo.getBytes();
				paquete = new DatagramPacket(infoultimo, infoultimo.length, direccion, puerto);
				socket.send(paquete);

				paquete = new DatagramPacket(hash, hash.length, direccion, puerto);
				socket.send(paquete);

				for(int g = 0; g < 562360320; g += 61440)
				{
					Thread.sleep(25);
					byte[] temp = Arrays.copyOfRange(archivo,g,g+61440);
					paquete = new DatagramPacket(temp, temp.length, direccion, puerto);
					socket.send(paquete);
				}

				byte[] temp = Arrays.copyOfRange(archivo,513540469,513544192);
				paquete = new DatagramPacket(temp, temp.length, direccion, puerto);
				socket.send(paquete);
			}
			else
			{
				cargar(2);
				String informacion = "3936";
				byte[] info = informacion.getBytes();
				paquete = new DatagramPacket(info, info.length, direccion, puerto);
				socket.send(paquete);

				String tamanoUltimo = "47315";
				byte[] infoultimo = tamanoUltimo.getBytes();
				paquete = new DatagramPacket(infoultimo, infoultimo.length, direccion, puerto);
				socket.send(paquete);

				paquete = new DatagramPacket(hash, hash.length, direccion, puerto);
				socket.send(paquete);

				for(int p = 0; p < 241875155; p += 61440)
				{
					Thread.sleep(25);
					byte[] temp2 = Arrays.copyOfRange(archivo,p,p+61440);

					paquete = new DatagramPacket(temp2, temp2.length, direccion, puerto);
					socket.send(paquete);
				}
				byte[] temp2 = Arrays.copyOfRange(archivo,241875155,241876992);
				paquete = new DatagramPacket(temp2, temp2.length, direccion, puerto);
				socket.send(paquete);
			}

			socket.close();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
