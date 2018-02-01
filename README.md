# Audible Appliances - Bravo Group

Use a Raspberry Pi to read out data from exercise machine screens for the visually impaired.

## External Links

[Specification/Project Plan](https://docs.google.com/document/d/1geFa-U47QsEuTCqqtQjdbNEj2eSGh8SEa6GHVU-LnaQ/edit#heading=h.17e4xxor0la1).

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

I've taken an image of the raspberry pi, and uploaded it using Git Large File Storage ([installation instructions](https://github.com/git-lfs/git-lfs/wiki/Installation)).
