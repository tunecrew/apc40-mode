/*
 * APC40Mode.java
 * By Christopher Albin Edmonds
 * For more information visit http://www.tunecrew.com/ or mailto:christopher@tunecrew.com
 * 
 * Copyright 2013 Christopher Albin Edmonds
 * 
 * apc40-mode is dual-licensed. For non-commericial use, the GNU Lesser General
 * Public License applies, as described in the following copying permission statement:
 * 
 * This file is part of apc40-mode.
 * 
 * apc40-mode is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * apc40-mode is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with apc40-mode.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * For commercial use, please contact the author for license terms.
 *
 * Third-party licenses included in apc40-mode are licensed under their respective licenses.
*/

package com.tunecrew.midi;

import com.sun.media.sound.MidiOutDeviceProvider;
import de.humatic.mmj.MidiOutput;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.commons.lang3.SystemUtils;

import javax.sound.midi.*;

public class APC40Mode extends Application {
    private static final String APC40_NAME_SUBSTRING = "APC40";
    private final Text myStatusText = new Text("Select mode");

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage)
            throws Exception {
        // TODO msg if os is not supported
        HBox modesBox = new HBox();
        modesBox.setPadding(new Insets(12));
        modesBox.setSpacing(10);
        for (Mode mode : Mode.values()) {
            Button button = new Button(mode.toString());
            button.setOnAction(new ModeButtonClickHandler(mode));
            modesBox.getChildren().add(button);
        }
        VBox statusBox = new VBox();
        statusBox.setPadding(new Insets(5, 12, 5, 12));
        statusBox.setStyle("-fx-background-color: gainsboro");
        statusBox.getChildren().add(myStatusText);

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(modesBox);
        borderPane.setBottom(statusBox);

        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(new CloseRequestHandler());
    }

    private synchronized void applyModeWindowsLinux(Mode mode)
            throws MidiUnavailableException, InvalidMidiDataException {
        MidiDevice midiOutputDevice = getMidiOutputDeviceWindowsLinux(APC40_NAME_SUBSTRING);
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
            printMessage(APC40_NAME_SUBSTRING + " not found");
        }
    }

    private synchronized void applyModeMac(Mode mode) {
        MidiOutput midiOutput = getMidiOutputDeviceMac(APC40_NAME_SUBSTRING);
        if (midiOutput != null) {
            byte[] message = {(byte) 0xf0, 0x47, 0x00, 0x73, 0x60, 0x00, 0x04, mode.getByte(), 0x08, 0x04, 0x01, (byte) 0xf7};
            midiOutput.sendMidi(message);
            printMessage("\"" + mode.toString() + "\" is turned on");
        } else {
            printMessage(APC40_NAME_SUBSTRING + " not found");
        }
    }

    private MidiDevice getMidiOutputDeviceWindowsLinux(String name)
            throws MidiUnavailableException {
        MidiDevice.Info[] infos = javax.sound.midi.MidiSystem.getMidiDeviceInfo();
        MidiOutDeviceProvider provider = new MidiOutDeviceProvider();
        for (MidiDevice.Info info : infos) {
            if (info.getName().contains(name) && provider.isDeviceSupported(info)) {
                return javax.sound.midi.MidiSystem.getMidiDevice(info);
            }
        }
        return null;
    }

    private MidiOutput getMidiOutputDeviceMac(String name) {
        String[] midiOutputs = de.humatic.mmj.MidiSystem.getOutputs();
        for (int i = 0; i < midiOutputs.length; i++) {
            if (midiOutputs[i].contains(name)) {
                return de.humatic.mmj.MidiSystem.openMidiOutput(i);
            }
        }
        return null;
    }

    private void printMessage(String message) {
        System.out.println(message);
        myStatusText.setText(message);
    }

    private enum Mode {
        Generic, AbletonLive, AlternateAbletonLive;

        public byte getByte() {
            switch (this) {
                case Generic:
                    return (byte) 0x40;
                case AbletonLive:
                    return (byte) 0x41;
                case AlternateAbletonLive:
                    return (byte) 0x42;
            }
            throw new RuntimeException("Unsupported enum: " + this);
        }

        @Override
        public String toString() {
            switch (this) {
                case Generic:
                    return "Generic Mode";
                case AbletonLive:
                    return "Ableton Live Mode";
                case AlternateAbletonLive:
                    return "Alternate Ableton Live Mode";
            }
            throw new RuntimeException("Unsupported enum: " + this);
        }
    }

    private class ModeButtonClickHandler implements EventHandler<ActionEvent> {
        private final Mode mode;

        public ModeButtonClickHandler(Mode mode) {
            this.mode = mode;
        }

        @Override
        public void handle(ActionEvent actionEvent) {
            try {
                if (SystemUtils.IS_OS_LINUX || SystemUtils.IS_OS_WINDOWS) {
                    applyModeWindowsLinux(mode);
                } else if (SystemUtils.IS_OS_MAC) {
                    applyModeMac(mode);
                }
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
