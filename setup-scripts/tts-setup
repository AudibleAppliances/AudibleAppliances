#!/bin/bash

checksum="3f42f17fcb7eaec5cac61f7e6b6aa928a6ffb45f51b1da124278fd300d47984a"
voicezip="voice.tar.bz2"
extractedvoice="cmu_us_awb_arctic"
festivalvoicedir="/usr/share/festival/voices/us"
voicedir="${extractedvoice}_clunits"

# If we're running on raspberry pi, install a dummy MBROLA package to fix dependencies on MBROLA voices
wget http://steinerdatenbank.de/software/mbrola3.0.1h_armhf.deb -O mbrola.deb
sudo dpkg -i mbrola.deb
rm -f mbrola.deb

# Install the festival TTS system
sudo apt update
sudo DEBIAN_FRONTEND=noninteractive apt-get -y install festival festvox-us1

# Check voice isn't already installed
if [[ -d "${festivalvoicedir}/${voicedir}" ]] ; then
    echo -e "\n\nVoice already installed. If it's corrupt, then delete \"${festivalvoicedir}/${voicedir}\" and run this script again."
    exit 1
fi

# Download a higher quality voice file
wget http://www.speech.cs.cmu.edu/cmu_arctic/packed/cmu_us_awb_arctic-0.95-release.tar.bz2 -O "$voicezip"
if [[ $(sha256sum "$voicezip") != "$checksum  $voicezip" ]] ; then
    echo "Download file corrupted - retry."
    exit 2
fi
# Extract the voice to the festival voice directory
echo "Installing voice file..."
tar xf "$voicezip"
rm -f "$voicezip"
sudo mkdir -p $festivalvoicedir
sudo mv "$extractedvoice" "${festivalvoicedir}/${voicedir}"

# Set volume to maximum
amixer set 'PCM' 100%