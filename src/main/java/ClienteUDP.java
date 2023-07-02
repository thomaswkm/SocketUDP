import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class ClienteUDP {

    public static void main(String[] args) {

        try {
            InetAddress ipServidor = InetAddress.getByName("localhost");
            DatagramSocket socketUdp = new DatagramSocket();
            System.out.println("Ingresa las coordenadas del pixel, separadas por una coma");
            String coordenadas = new Scanner(System.in).nextLine();
            byte[] bufferEnvio = coordenadas.getBytes();
            byte[] bufferRecepcion = new byte[10000];

            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(200, 200);
            JPanel panel = new JPanel();
            frame.add(panel);
            frame.setVisible(true);

            while (true) {
                DatagramPacket paqueteSaliente = new DatagramPacket(bufferEnvio, bufferEnvio.length, ipServidor, 8888);
                socketUdp.send(paqueteSaliente);

                DatagramPacket paqueteEntrante = new DatagramPacket(bufferRecepcion, bufferRecepcion.length);
                socketUdp.receive(paqueteEntrante);

                String pixelInfo = new String(paqueteEntrante.getData(), paqueteEntrante.getOffset(), paqueteEntrante.getLength());
                System.out.println(pixelInfo);


                String hexValue = pixelInfo.substring(pixelInfo.lastIndexOf("0x") + 2);

                int pixelValue = (int) Long.parseLong(hexValue, 16);

                Color color = new Color(pixelValue);

                panel.setBackground(color);
            }

        } catch (UnknownHostException e) {
            System.out.println("Error al ingresar la IP del cliente");
        } catch (SocketException e) {
            System.out.println("Error al crear el socket");
        } catch (IOException e) {
            System.out.println("Error al enviar o recibir un paquete");
        }
    }
}
