#!/bin/bash

checksum="3f42f17fcb7eaec5cac61f7e6b6aa928a6ffb45f51b1da124278fd300d47984a"
voicezip="voice.tar.bz2"
extractedvoice="cmu_us_awb_arctic"
festivalvoicedir="/usr/share/festival/voices/us"
voicedir="${extractedvoice}_clunits"


sudo apt install festival

if [[ -d "${festivalvoicedir}/${voicedir}" ]] ; then
    echo -e "\n\nVoice already installed. If it's corrupt, then delete \"${festivalvoicedir}/${voicedir}\" and run this script again."
    exit 1
fi

wget http://www.speech.cs.cmu.edu/cmu_arctic/packed/cmu_us_awb_arctic-0.95-release.tar.bz2 -O "$voicezip"
if [[ $(sha256sum "$voicezip") != "$checksum  $voicezip" ]] ; then
	echo "Download file corrupted - retry."
	exit 2
fi
tar xf "$voicezip"
rm -r "$voicezip"
sudo mkdir -p $festivalvoicedir
sudo mv "$extractedvoice" "${festivalvoicedir}/${voicedir}"
