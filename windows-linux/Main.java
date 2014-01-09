import com.sun.media.sound.MidiOutDeviceProvider;

import javax.sound.midi.*;

public class Main {
    private static final String APC40_NAME = "APC40";

    public static void main(String[] args)
            throws MidiUnavailableException, InvalidMidiDataException {
        if (args.length == 1) {
            try {
                APC40Mode mode = APC40Mode.getMode(Integer.parseInt(args[0]));
                System.out.println("Selected mode is \"" + mode + "\"");
                applyMode(mode);
            } catch (IllegalArgumentException e) {
                System.out.println(getUsageMessage());
            }
        } else {
            System.out.println(getUsageMessage());
        }
    }

    private static void applyMode(APC40Mode mode)
            throws MidiUnavailableException, InvalidMidiDataException {
        MidiDevice midiOutputDevice = getMidiOutputDevice();
        if (midiOutputDevice != null) {
            midiOutputDevice.open();
            Receiver midiOutputReceiver = midiOutputDevice.getReceiver();
            byte[] message = {(byte)0xf0, 0x47, 0x00, 0x73, 0x60, 0x00, 0x04, mode.getByte(), 0x08, 0x04, 0x01, (byte)0xf7};
            SysexMessage sysexMessage = new SysexMessage(message, message.length);
            midiOutputReceiver.send(sysexMessage, -1);
            midiOutputReceiver.close();
            midiOutputDevice.close();
        } else {
            System.out.println(APC40_NAME + " not found");
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

    private static String getUsageMessage() {
        StringBuilder sb = new StringBuilder("Usage:\n");
        for (APC40Mode mode : APC40Mode.values()) {
            sb.append("\t").append(mode.getNumber()).append(" - ").append(mode).append("\n");
        }
        return sb.toString();
    }
}