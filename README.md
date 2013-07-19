apc40-mode 0.2.0

==========
By Christopher Albin Edmonds
For more information visit http://www.tunecrew.com/ or mailto:christopher@tunecrew.com
For copyright information please see the end of this file

==========
apc40-mode is a simple Java utility to switch between the three firmware modes of the Akai APC40 MIDI Controller, as described at http://www.akaipro.com/apc40map and in http://www.akaipro.com/extras/product/apc40/APC40_Communications_Protocol_rev_1.pdf

It is currently Mac OS X only, and because Apple's Java implementation is broken with some aspects of javax.sound.midi, it requires the mmj libraries found here - http://www.humatic.de/htools/mmj.htm

To install the mmj libraries, download the package from http://www.humatic.de/webapps/php/download/pick.php?app=mmj and unzip mmj.zip. Copy libmmj.jnilib and mmj.jar into /Library/Java/Extensions/ and you're done.

To build apc40-mode from scratch, simply use "javac APC40Mode.java". A class file is included, however, so this shouldn't be necessary.

To use apc40-mode, cd to the directory where APC40Mode.class is located and invoke it with "java APC40Mode mode".

mode can be 0, 1 or 2, corresponding to the following modes:

0 - Generic Mode

1 - Ableton Live Mode

2 - Alternate Ableton Live Mode

Note that upon power-up, the APC40 enters Generic Mode. If you open Ableton Live with the APC40 powered and connected, and wait until you see the "red square" around the clips in Live, the APC40 will enter Ableton Live Mode, and will remain in this mode when you quit Live. (This will only occur if the APC40 is configured as a control surface in Live's preferences)

==========
TO-DO

- Make this work on Windows & Linux by using standard javax.sound.midi, and let application auto-detect platform to choose which library to use
- Test on APC20 (does it have different modes too?)
- Add comments to the code
- Add usage message to the code
- DONE - Convert DEBUG flag to input argument
- Package mmj libraries, binary and source into one jar
- Create GUI wrapper
- Add version numbers

==========
Copyright 2013 Christopher Albin Edmonds

apc40-mode is dual-licensed. For non-commericial use, the GNU Lesser General
Public License applies, as described in the following copying permission statement:

This file is part of apc40-mode.

apc40-mode is free software: you can redistribute it and/or modify
it under the terms of the GNU Lesser General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

apc40-mode is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
along with apc40-mode.  If not, see <http://www.gnu.org/licenses/>.

For commercial use, please contact the author for license terms.

Third-party licenses included in apc40-mode are licensed under their respective licenses.

