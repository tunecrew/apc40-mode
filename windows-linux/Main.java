import com.sun.media.sound.MidiOutDeviceProvider;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.sound.midi.*;

public class Main extends Application {
    private static final String APC40_NAME = "APC40";

    public static void main(String[] args)
            throws MidiUnavailableException, InvalidMidiDataException {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);
        for (APC40Mode mode : APC40Mode.values()) {
            Button button = new Button(mode.toString());
            button.setOnAction(new ModeButtonClickHandler(mode));
            hbox.getChildren().add(button);
        }
        Scene scene = new Scene(hbox);
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(new CloseRequestHandler());
    }

    private synchronized void applyMode(APC40Mode mode)
            throws MidiUnavailableException, InvalidMidiDataException {
        MidiDevice midiOutputDevice = getMidiOutputDevice();
        if (midiOutputDevice != null) {
            midiOutputDevice.open();
            Receiver midiOutputReceiver = midiOutputDevice.getReceiver();
            byte[] message = {(byte) 0xf0, 0x47, 0x00, 0x73, 0x60, 0x00, 0x04, mode.getByte(), 0x08, 0x04, 0x01, (byte) 0xf7};
            SysexMessage sysexMessage = new SysexMessage(message, message.length);
            midiOutputReceiver.send(sysexMessage, -1);
            midiOutputReceiver.close();
            midiOutputDevice.close();
            printMessage("\"" + mode.toString() + "\" is turned on");
        } else {
            printMessage(APC40_NAME + " not found");
        }
    }

    private static MidiDevice getMidiOutputDevice()
            throws MidiUnavailableException {
        MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
        MidiOutDeviceProvider provider = new MidiOutDeviceProvider();
        for (MidiDevice.Info info : infos) {
            if (info.getName().contains(APC40_NAME) && provider.isDeviceSupported(info)) {
                return MidiSystem.getMidiDevice(info);
            }
        }
        return null;
    }

    private void printMessage(String message) {
        System.out.println(message);
    }

    private class ModeButtonClickHandler implements EventHandler<ActionEvent> {
        private final APC40Mode mode;

        public ModeButtonClickHandler(APC40Mode mode) {
            this.mode = mode;
        }

        @Override
        public void handle(ActionEvent actionEvent) {
            try {
                applyMode(mode);
            } catch (MidiUnavailableException | InvalidMidiDataException e) {
                printMessage(e.getMessage());
            }
        }
    }

    private class CloseRequestHandler implements EventHandler<WindowEvent> {
        @Override
        public void handle(WindowEvent windowEvent) {
            Platform.exit();
            System.exit(0);
        }
    }
}