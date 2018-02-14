# Audible Appliances - Bravo Group

[![Build Status](https://travis-ci.org/AudibleAppliances/AudibleAppliances.svg?branch=master)](https://travis-ci.org/AudibleAppliances/AudibleAppliances)

Use a Raspberry Pi to read out data from exercise machine screens for the visually impaired.

## Raspberry Pi Configuration

The Raspberry Pi runs Raspbian Stretch Lite (no desktop interface, all CLI. I can switch it to a desktop version if we need it). At the moment it's minimal - for example there's no JRE installed. It does host a network, details are below.

The network details are:

- SSID: `bravo-net`
- Passphrase: `bravobravo`
- Pi's IP: `192.168.0.1`

The login is:

- User: `pi`
- Pass: `raspberry`

After connecting to the network, use eg. `ssh pi@192.168.0.1` to login.

### Raspberry Pi Disk Image

I've taken an image of the raspberry pi, and uploaded it using Git Large File Storage ([installation instructions](https://github.com/git-lfs/git-lfs/wiki/Installation)). After installing, use `git lfs install --skip-smudge` to force it to avoid pulling large files unless explicitly asked to.
To later download the large files, use `git lfs pull`.

### Raspberry Pi Internet

To connect the raspberry pi to a laptop, enable internet sharing over the ethernet link, and connect the laptop to a non-eduroam network. The pi should be able to connect through the laptop (reboot it if necessary).

You can ssh onto the pi from the laptop you're providing internet from by connecting to the IP address of the ethernet interface on the laptop.

### Testing with Maven
Unit tests live in the test directory and must have a name with the following form `*Test.java`. Integration tests live in the it directory and must have the following form `*IT.java`.
To run the unit tests run `mvn clean test`, to run both the unit tests and integration tests run `mvn clean verify`.

### TTS Setup

The TTS component uses the [Festival Sound Synthesis System](http://www.cstr.ed.ac.uk/projects/festival/). The easiest way of setting it up is to run the script `ttssetup.sh`, or by running the following commands:

```bash
sudo apt install festival
wget http://www.speech.cs.cmu.edu/cmu_arctic/packed/cmu_us_awb_arctic-0.95-release.tar.bz2
echo "3f42f17fcb7eaec5cac61f7e6b6aa928a6ffb45f51b1da124278fd300d47984a  cmu_us_awb_arctic-0.95-release.tar.bz2" | sha256sum -c -
tar xf cmu_us_awb_arctic-0.95-release.tar.bz2
sudo mkdir -p /usr/share/festival/voices/us
sudo mv cmu_us_awb_arctic /usr/share/festival/voices/us/cmu_us_awb_arctic_clunits
```

This only works on Debian systems... it won't take much work to port the script to non-Debian systems though.

### Raspberry Pi Internet

To connect the raspberry pi to a laptop, enable internet sharing over the ethernet link, and connect the laptop to a non-eduroam network. The pi should be able to connect through the laptop (reboot it if necessary).

You can ssh onto the pi from the laptop you're providing internet from by connecting to the IP address of the ethernet interface on the laptop.

### Testing with Maven
Unit tests live in the test directory and must have a name with the following form `*Test.java`. Integration tests live in the it directory and must have the following form `*IT.java`.
To run the unit tests run `mvn clean test`, to run both the unit tests and integration tests run `mvn clean verify`.

### OCR Setup

The OCR package is quite simple - it has a dependency on an external program though, `ssocr`. It can be installed (on a `dpkg`-based system) using the `ssocr-setup` script in the top-level repository (on non-`dpkg`-based systems, replace the `make deb` with `make install` in the script and remove the `dpkg` command).
