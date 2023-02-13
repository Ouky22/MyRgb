![MyRgb Logo](./docs/logo.png)

# MyRgb - Controller for RGB-LED strips

MyRgb allows you to send HTTP-Requests to your RGB-LED controller to control your LED strips.

# Screenshots

<img src="./docs/main_screens.png" width="1000dp">

# Features

- LED Controller
  - set the color and brightness of your LED strips
  - turn certain LED strips on or off
  - show your current LED settings (e.g. current color)
- Rgb Alarm
  - create alarms to turn on your LED strips at a specific time
  - there are two types of alarms:
    - one time alarms are triggered the next time the specified alarm time is reached and then are deactivated
    - recurring alarms are triggered on certain days of the week at a specific time
- Rgb Show
  - start rgb show (e.g. switch between different colors with transition)
  - set speed of rgb show

# Add your rgb controller

- you can easily add a new controller with its local IP address and a meaningful name
- in my case I am using an esp32 microcontroller that executes the commands received via HTTP requests to control the connected LED strips

<img src="./docs/add_rgb_controller_screen.jpg" width="300dp">

# Protocol for commands

Commands are send via HTTP POST requests and have the following structure:

## ccc.abc.def.

- ccc = command identifier (which type of command) or value for red

## 0 <= ccc <= 255 -> change color

- ccc --> red value
- abc --> green value
- def --> blue value

## 300.abc.xxx. - off command

- turn off certain LED strip
- abc --> id of strip

## 400.abc.xxx. - on command

- turn on certain LED strip
- abc --> id of strip

## 500.abc.xxx. - brightness command

- abc --> brightness value

## 600.abc.xxx. - rgb show command

- abc --> speed
- const val rgbShowCommand = 600

## 700.xxx.xxx. - current settings command

- receive all current settings (e.g. color values, brightness, which strip is enabled, status of rgbShow)

## 800.xxx.xxx. - rgb alarm command

- trigger new rgb alarm

# Technologies

This is an Android Studio project. Following libraries are used:

- Room Database for storing rgb alarms
- Proto DataStore for storing ip addresses and names of rgb controller
- Retrofit with moshi converter to send HTTP requests and receive HTTP Responses
- Dagger for dependency injection
- Espresso for UI testing
