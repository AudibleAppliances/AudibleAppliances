# Audible Appliances - Bravo Group

Use a Raspberry Pi to read out data from exercise machine screens for the visually impaired.

## Raspberry Pi Configuration

The Raspberry Pi runs Raspbian Stretch Lite (no desktop interface, all CLI. I can switch it to a desktop version if we need it). At the moment it's minimal - for example there's no JRE installed. It does host a network, details are below.

I've taken a disk image of the current working setup, so if we break stuff at any point it should be pretty easy to restore it.

The network details are:

- SSID:       `bravo-net`
- Passphrase: `bravobravo`
- Pi's IP: `192.168.0.1`

The login is:

- User: `pi`
- Pass: `raspberry`

After connecting to the network, use eg. `ssh pi@192.168.0.1` to login.