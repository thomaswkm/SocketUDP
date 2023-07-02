import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class ServerUDP {

    public static void main(String[] args) {

        try {
            Robot robot = new Robot();
            DatagramSocket socketUDP = new DatagramSocket(8888);
            Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());

            while (true) {
                byte[] buffer = new byte[10000];
                DatagramPacket paqueteEntrante = new DatagramPacket(buffer, buffer.length);
                socketUDP.receive(paqueteEntrante);
                String coordenadas = new String(paqueteEntrante.getData(), 0, paqueteEntrante.getLength());

                BufferedImage screenshot = robot.createScreenCapture(screenRect);

                int x = Integer.parseInt(coordenadas.split(",")[0].trim());
                int y = Integer.parseInt(coordenadas.split(",")[1].trim());

                int pixelValue = screenshot.getRGB(x, y);

                String pixelInfo = String.format("Pixel en (%d, %d): 0x%08X", x, y, pixelValue);
                buffer = pixelInfo.getBytes();

                int puertoCliente = paqueteEntrante.getPort();
                InetAddress ipCliente = paqueteEntrante.getAddress();

                DatagramPacket paqueteSaliente = new DatagramPacket(buffer, buffer.length, ipCliente, puertoCliente);
                socketUDP.send(paqueteSaliente);
            }

        } catch (SocketException e) {
            System.out.println("Error al crear el socket");
        } catch (IOException e) {
            System.out.println("Error al recibir o enviar un paquete");
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }
}
