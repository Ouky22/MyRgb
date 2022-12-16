package com.myrgb.ledcontroller.domain

// structure of command: ccc.abc.def.
// ccc = commandIdentifier (see below) or value for red:
// 0 = ccc = 255 --> command which changes color (rrr.ggg.bbb)


// to turn off certain strip
// 300.abc.xxx.   abc --> which strip
// abc = 0 --> all strips off
const val offCommandIdentifier = 300

// to turn on certain strip
// 400.abc.xxx.   abc --> which strip
// abc = 0 --> all strips on
const val onCommandIdentifier = 400

// 500.abc.xxx.    abc --> brightness value
const val brightnessCommandIdentifier = 500

// 600.abc.xxx.     abc --> speed
const val rgbShowCommandIdentifier = 600

// 700.xxx.xxx.
// send all current settings (color values, brightness, which strip is enabled, status of rgbShow)
const val settingsCommandIdentifier = 700

// 800.xxx.xxx.
const val rgbAlarmCommandIdentifier = 800
