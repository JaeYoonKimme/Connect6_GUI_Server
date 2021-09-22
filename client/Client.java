import java.util.*;
import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
public class Client {
    private int color;
    private int port;
    private InputStream inputStream;
    private OutputStream outputStream;
    public void init(int color, int port){
        try {
            Socket socket = new Socket("localhost", port);
            socket.setTcpNoDelay(true);
                        System.out.println("Connected to server");
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();

                } catch (IOException e){
            System.err.println("catch" + e);
        }
    }
    
    public void recvRedStones(){
        try{
            int sizeOfRedStones = inputStream.read();
            byte[] str_byte = new byte[sizeOfRedStones];
            inputStream.read(str_byte, 0, sizeOfRedStones);
            String redinfo = new String(str_byte);
            System.out.println("Got Red Stone ->" + redinfo);
        }catch (IOException e) {
            System.err.println("recv redstones" + e);
        }
    }

    public int byteToInt(byte[] bytes) {
        return ((bytes[3] & 0xFF) << 24) |
                ((bytes[2] & 0xFF) << 16) |
                ((bytes[1] & 0xFF) << 8 ) |
                ((bytes[0] & 0xFF) << 0 );
    }

    public byte[] intToByte(int intValue) {
        byte[] byteArray = new byte[4];
        byteArray[3] = (byte)(intValue >> 24);
        byteArray[2] = (byte)(intValue >> 16);
        byteArray[1] = (byte)(intValue >> 8);
        byteArray[0] = (byte)(intValue);
        return byteArray;
    }
    
    
    public void echo() {
        try {
            while(true) {
                Scanner scanner = new Scanner(System.in);
                String messageToSend = scanner.next();
                
                ByteBuffer b = ByteBuffer.allocate(4);
                byte[] sizeByteToSend = intToByte(messageToSend.length());
                outputStream.write(sizeByteToSend);
                
                byte[] messageByteToSend = messageToSend.getBytes();
                outputStream.write(messageByteToSend);
                
                byte[] sizeByteToRecv = new byte[4];
                inputStream.read(sizeByteToRecv, 0, 4);
                int sizeToRecv = byteToInt(sizeByteToRecv);
                
                System.out.println("Echo message size from Server :"+sizeToRecv);

                byte[] messageByteToRecv = new byte[sizeToRecv];
                inputStream.read(messageByteToRecv, 0, sizeToRecv);
                String message = new String(messageByteToRecv);
                
                System.out.println("Echo from Server : "+message);
            }
        } catch (IOException e){
            System.err.println("IOException e");
        }
    }
    
    public void sendStones() {
        Scanner s = new Scanner(System.in);
        String stones = s.nextLine();
        int sizeOfStones = stones.length();
        try {
            outputStream.write(intToByte(sizeOfStones));
            outputStream.write(stones.getBytes(), 0, sizeOfStones);
        } catch (IOException e) {
            System.err.println("Send redStone" + e);
        }
    }
    
    public void recvStones() {
        try {
            byte[] byteOfSize = new byte[4];
            inputStream.read(byteOfSize, 0, 4);
            int sizeOfStones = byteToInt(byteOfSize);
            byte[] bytesOfStones = new byte[sizeOfStones];
            inputStream.read(bytesOfStones, 0, sizeOfStones); 
            String stones = new String(bytesOfStones);
            System.out.println("Receive stones " + stones);
        } catch (IOException e) {}
    }
    
    public void start() {
        
        if(color == 2) { // black
            System.out.println("Client is BLACK");
            c.sendStones();
            c.recvStones();
        } else {
            System.out.println("Client is WHITE");
            c.recvStones();
        }
        
        while(true){
            System.out.println("DRAW AND WAIT");
            c.sendStones();
            c.recvStones();
        }
    }


    public static void main(String[] args) {
        Client c = new Client();
        Scanner sc = new Scanner(System.in);
        int color = sc.nextInt();
        int port = sc.nextInt();
        c.init(color, port);
        c.recvRedStones();
        c.start();
        
    }
}
