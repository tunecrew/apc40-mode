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
*/

import de.humatic.mmj.*;

public class APC40Mode
{
	private static final boolean DEBUG = true;
	private static final String APC40_NAME = "Akai APC40 - Akai APC40";
	private static byte mode = 0x00;

	public static void main (String[] args)
	{
		if ((args != null) && (args.length == 1))
		{
			if (args[0].equals("0")) { mode = 0x40; }
			else if (args[0].equals("1")) { mode = 0x41; }
			else if (args[0].equals("2")) { mode = 0x42; }
		}

		if (mode != 0x00)
		{
			byte[] introductionData = {(byte)0xf0, 0x47, 0x00, 0x73, 0x60, 0x00, 0x04, mode, 0x08, 0x04, 0x01, (byte)0xf7};
		
			MidiOutput midiOutput = getMidiOutput();

			if (midiOutput != null) { midiOutput.sendMidi(introductionData); }
			else { System.out.println("no APC40 found"); }
		}
		else { System.out.println("input error"); }
	}

	private static MidiOutput getMidiOutput()
	{
		MidiOutput midiOutput = null;	
		String[] midiOutputs = de.humatic.mmj.MidiSystem.getOutputs();

		if (DEBUG)
		{
			CoreMidiDevice[] coreMidiDevices = de.humatic.mmj.MidiSystem.getDevices();
			
			for (int i = 0; i < coreMidiDevices.length; i++)
			{
				CoreMidiDevice coreMidiDevice = coreMidiDevices[i];

				System.out.println(coreMidiDevice.getManufacturer());
				System.out.println(coreMidiDevice.getModel());
				System.out.println(coreMidiDevice.getName());
				System.out.println(coreMidiDevice.getID());
				System.out.println(coreMidiDevice.toString());
				System.out.println();
			}

			for (int i = 0; i < midiOutputs.length; i++)
			{
				System.out.println(midiOutputs[i]);
				System.out.println();
			}
		}
	
		for (int i = 0; i < midiOutputs.length; i++)
		{
			if (midiOutputs[i].equals(APC40_NAME))
			{
				if (DEBUG) { System.out.println("found APC40 at output " + i); }

				midiOutput = de.humatic.mmj.MidiSystem.openMidiOutput(i);
			}
		}

		return midiOutput;
	}
}
