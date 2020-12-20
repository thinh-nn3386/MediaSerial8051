package Serial;
import com.fazecast.jSerialComm.SerialPort;
public class SendReceive {
    public static void main(String[] args)  throws Exception{
//        String comm = null;
//        SerialPort[] ports = SerialPort.getCommPorts();
//        if (ports != null && ports.length>0){
//            for (SerialPort port: ports) {
//                System.out.println(port.getSystemPortName() + " is connected");
//                comm = new String(port.getSystemPortName());
//            }
//        }
//        else {
//            System.out.println("No serial port is connected");
//            return;
//        }
//        SerialPort sp = SerialPort.getCommPort(comm);
//        sp.setComPortParameters(9600,8,1,0);
//        sp.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING,0,0);
//        if (sp.openPort()){
//            System.out.println(comm +" is opended");
//        }
//        else {
//            System.out.println(comm+ " is not opended");
//            return;
//        }
//
//        try {
//            while (true)
//            {
//                while (sp.bytesAvailable() == 0)
//                    Thread.sleep(20);
//
//                byte[] readBuffer = new byte[sp.bytesAvailable()];
//                int numRead = sp.readBytes(readBuffer, readBuffer.length);
//                System.out.println("Read " + numRead + " bytes." + readBuffer.toString());
//
//            }
//        } catch (Exception e) { e.printStackTrace(); }
//        sp.closePort();
//        System.out.println("Done.");

    }

}
