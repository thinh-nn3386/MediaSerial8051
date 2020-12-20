package sample;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import com.fazecast.jSerialComm.SerialPort;

public class Controller implements Initializable {
    static final int TOTAL =3;
    String[] path = {
            "src/fxml/mp3/BigCityBoi.mp3",
            "src/fxml/mp3/DanChauADaDiVoBar.mp3",
            "src/fxml/mp3/NaNaBumChit.mp3"
    };
    String[] name = {"BigCityBoi", "DanChauADaDiBar","NaNaBumKit"};
    Media media;
    MediaPlayer mediaPlayer;
    int currentMp3=2;
    SerialPort sp;
    int remotePlay = 0 ;
    Thread th1;

    @FXML private Button play;
    @FXML private Button pause;
    @FXML private Image img;
    @FXML private Button next;
    @FXML private Button back;
    @FXML private Button volumeUp;
    @FXML private Button volumeDown;
    @FXML private Text time;
    @FXML protected ProgressBar progress;

    class runProgress implements Runnable{
        @Override
        public void run() {
            double per;
            while ((per = mediaPlayer.getCurrentTime().toMillis()/mediaPlayer.getTotalDuration().toMillis()) !=1 ){
                progress.setProgress(per);
            }

        }
    }
    class sendRecv implements Runnable{
        @Override
        public void run() {
                while (true)
                {
                    while (sp.bytesAvailable() == 0) {
                        try {
                            Thread.sleep(20);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    byte[] readBuffer = new byte[sp.bytesAvailable()];
                    int numRead = sp.readBytes(readBuffer, readBuffer.length);
                    System.out.println("Read " + numRead + " bytes." + readBuffer[0]+readBuffer[1]);
                    if (readBuffer[0] == 52 && readBuffer[1] == 52 && remotePlay== 0 ){
                        remotePlay=1;
                        th1 = new Thread(new runProgress());
                        th1.start();
                        mediaPlayer.play();

                    }
                    if (readBuffer[0] == 52 && readBuffer[1] == 52 && remotePlay== 1 ){
                        remotePlay=0;
                        mediaPlayer.pause();
                    }
                    if (readBuffer[0] == 52 && readBuffer[1] == 48 ){
                        mediaPlayer.pause();
                        currentMp3--;
                        if (currentMp3<0) currentMp3=TOTAL-1;
                        media = new Media(new File(path[currentMp3]).toURI().toString());
                        mediaPlayer = new MediaPlayer(media);
                        mediaPlayer.setOnReady(new Runnable() {

                            @Override
                            public void run() {
                                time.setText(String.valueOf((int) media.getDuration().toSeconds()) + " s");
                                // display media's metadata
                                for (Map.Entry<String, Object> entry : media.getMetadata().entrySet()){
                                    System.out.println(entry.getKey() + ": " + entry.getValue());
                                }
                                System.out.println("----------------------------------------------------------");
                                mediaPlayer.play();
                            }

                        });
                    }
                    if (readBuffer[0] == 52 && readBuffer[1] == 51){
                        mediaPlayer.pause();
                        currentMp3++;
                        if (currentMp3>TOTAL-1) currentMp3=0;
                        media = new Media(new File(path[currentMp3]).toURI().toString());
                        mediaPlayer = new MediaPlayer(media);
                        mediaPlayer.setOnReady(new Runnable() {

                            @Override
                            public void run() {
                                time.setText(String.valueOf((int) media.getDuration().toSeconds()) + " s");
                                // display media's metadata
                                for (Map.Entry<String, Object> entry : media.getMetadata().entrySet()){
                                    System.out.println(entry.getKey() + ": " + entry.getValue());
                                }
                                System.out.println("----------------------------------------------------------");
                                mediaPlayer.play();
                            }

                        });
                    }
                }

        }
    }

    @FXML
    protected void handlePlay(ActionEvent event) {
        mediaPlayer.play();
        progress.progressProperty().unbind();
        th1 = new Thread(new runProgress());
        th1.start();

    }
    @FXML
    protected void handlePause(ActionEvent event){
        mediaPlayer.pause();
    }
    @FXML
    protected void handleNext(ActionEvent event){
        mediaPlayer.pause();
        currentMp3++;
        if (currentMp3>TOTAL-1) currentMp3=0;
        media = new Media(new File(path[currentMp3]).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setOnReady(new Runnable() {

            @Override
            public void run() {
                time.setText(String.valueOf((int) media.getDuration().toSeconds()) + " s");
                // display media's metadata
                for (Map.Entry<String, Object> entry : media.getMetadata().entrySet()){
                    System.out.println(entry.getKey() + ": " + entry.getValue());
                }
                System.out.println("----------------------------------------------------------");
                mediaPlayer.play();
            }

        });
    }
    @FXML
    protected void handleBack(ActionEvent event){
        mediaPlayer.pause();
        currentMp3--;
        if (currentMp3<0) currentMp3=TOTAL-1;
        media = new Media(new File(path[currentMp3]).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setOnReady(new Runnable() {

            @Override
            public void run() {
                time.setText(String.valueOf((int) media.getDuration().toSeconds()) + " s");
                // display media's metadata
                for (Map.Entry<String, Object> entry : media.getMetadata().entrySet()){
                    System.out.println(entry.getKey() + ": " + entry.getValue());
                }
                System.out.println("----------------------------------------------------------");
                mediaPlayer.play();
            }

        });
    }
    @FXML
    protected void handleVolumeUp(ActionEvent event){
        System.out.println(mediaPlayer.getVolume());
        mediaPlayer.setVolume(mediaPlayer.getVolume() + 2);
    }
    @FXML
    protected void handleVolumeDown(ActionEvent event){
        mediaPlayer.pause();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String comm = null;
        SerialPort[] ports = SerialPort.getCommPorts();
        if (ports != null && ports.length>0){
            for (SerialPort port: ports) {
                System.out.println(port.getSystemPortName() + " is connected");
                comm = new String(port.getSystemPortName());
            }
        }
        else {
            System.out.println("No serial port is connected");
            return;
        }
        sp = SerialPort.getCommPort(comm);
        sp.setComPortParameters(9600,8,1,0);
        sp.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING,0,0);
        if (sp.openPort()){
            System.out.println(comm +" is opended");
        }
        else {
            System.out.println(comm+ " is not opended");
            return;
        }
        Thread th2 = new Thread(new sendRecv());
        th2.start();

        media = new Media(new File(path[currentMp3]).toURI().toString());
        //progress.setProgress(0);
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setOnReady(new Runnable() {
            @Override
            public void run() {
               time.setText(String.valueOf((int) media.getDuration().toSeconds()) + " s");
              // progress.setMaxWidth(media.getDuration().toSeconds());

                // display media's metadata
                for (Map.Entry<String, Object> entry : media.getMetadata().entrySet()){
                    System.out.println(entry.getKey() + ": " + entry.getValue());
                }
                for (byte i = 0 ; i < name[currentMp3].length()-1 ; i ++){
                    try {
                        sp.getOutputStream().write((byte)name[currentMp3].charAt(i));
                        sp.getOutputStream().flush(); Thread.sleep(100);
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("----------------------------------------------------------");

            }
        });


    }
}
