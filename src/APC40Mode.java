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

import de.humatic.mmj.*;
import javax.sound.midi.*;
import org.apache.commons.cli.*;
import org.apache.commons.lang3.*;

public class APC40Mode
{
	private static final String APC40_NAME_MAC = "Akai APC40 - Akai APC40";
	private static final String APC40_NAME_LINUX = "Akai APC40 - Akai APC40";	
	private static Options options;
	private static String os;

	static
	{
		options = new Options();

		Option optionHelp = new Option("h", "help", false, "print usage");
		Option optionList = new Option("l", "list", false, "list available MIDI devices");
		Option optionMode0 = new Option("0", false, "switch firmware to Generic Mode");
		Option optionMode1 = new Option("1", false, "switch firmware to Ableton Live Mode");
		Option optionMode2 = new Option("2", false, "switch firmware to Alternate Ableton Live Mode");

		OptionGroup optionGroupMode = new OptionGroup();

		optionGroupMode.addOption(optionMode0);
		optionGroupMode.addOption(optionMode1);
		optionGroupMode.addOption(optionMode2);

		options.addOption(optionList);
		options.addOption(optionHelp);
		options.addOptionGroup(optionGroupMode);

		os = "other";
		if (SystemUtils.IS_OS_MAC_OSX) { os = "macosx"; }
		else if (SystemUtils.IS_OS_LINUX) { os = "linux"; }
		else if (SystemUtils.IS_OS_WINDOWS) { os = "windows"; }
	}

	public static void main (String[] args)
	{
		if ((args == null) || (args.length == 0)) { printHelp(); }
		else
		{
			CommandLineParser parser = new BasicParser();
			CommandLine line = null;

			try { line = parser.parse(options, args); }
			catch(ParseException e)
			{
				System.out.println( "input argument error: " + e.getMessage() );
				System.out.println();
				printHelp();
			}

			if (line != null)
			{
				if (line.hasOption("l")) { listDevices(); }
				if (line.hasOption("h")) { printHelp(); }
				if (line.hasOption("0")) { changeMode((byte)0x40); }
				if (line.hasOption("1")) { changeMode((byte)0x41); }
				if (line.hasOption("2")) { changeMode((byte)0x42); }
			}
		}
	}
	
	private static void changeMode(byte mode)
	{
		if (os.equals("macosx"))
		{
			byte[] data = {(byte)0xf0, 0x47, 0x00, 0x73, 0x60, 0x00, 0x04, mode, 0x08, 0x04, 0x01, (byte)0xf7};

			MidiOutput midiOutput = getMidiOutput();

			if (midiOutput != null) { midiOutput.sendMidi(data); }
		}
		else if (os.equals("windows") || os.equals("linux"))
		{
			
		}
		else { System.out.println("system unsupported"); }
	}

	private static void listDevices()
	{
		if (os.equals("macosx"))
		{
			CoreMidiDevice[] coreMidiDevices = de.humatic.mmj.MidiSystem.getDevices();

			System.out.println("MIDI devices");
			System.out.println("------------");

			for (CoreMidiDevice coreMidiDevice : coreMidiDevices)
			{
				System.out.println("id:           " + coreMidiDevice.getID());
				System.out.println("manufacturer: " + coreMidiDevice.getManufacturer());
				System.out.println("model:        " + coreMidiDevice.getModel());
				System.out.println("name:         " + coreMidiDevice.getName());
				System.out.println();
			}

			System.out.println("MIDI outputs");
			System.out.println("------------");

			String[] midiOutputs = de.humatic.mmj.MidiSystem.getOutputs();

			for (int i = 0; i < midiOutputs.length; i++) { System.out.println("MIDI output: " + midiOutputs[i]); }
		}
		else if (os.equals("windows") || os.equals("linux"))
		{
			MidiDevice.Info[] midiDeviceInfos = javax.sound.midi.MidiSystem.getMidiDeviceInfo();

			for (MidiDevice.Info midiDeviceInfo : midiDeviceInfos)
			{
				System.out.println("vendor:" + midiDeviceInfo.getVendor());
				System.out.println("name:" + midiDeviceInfo.getName());
				System.out.println("description:" + midiDeviceInfo.getDescription());
				System.out.println("version:" + midiDeviceInfo.getVersion());
			}
		}
		else { System.out.println("system unsupported"); }
	}

	private static void printHelp()
	{
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("APC40Mode <options>", options);
		System.out.println();
	}

	private static MidiOutput getMidiOutput()
	{
		MidiOutput midiOutput = null;	
		String[] midiOutputs = de.humatic.mmj.MidiSystem.getOutputs();

		for (int i = 0; i < midiOutputs.length; i++)
		{
			if (midiOutputs[i].equals(APC40_NAME_MAC)) { midiOutput = de.humatic.mmj.MidiSystem.openMidiOutput(i); }
		}

		if (midiOutput == null) { System.out.println("no APC40 found"); System.out.println(); }

		return midiOutput;
	}
}
