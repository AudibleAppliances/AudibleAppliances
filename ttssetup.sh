#!/bin/bash

sudo apt install festival
wget http://www.speech.cs.cmu.edu/cmu_arctic/packed/cmu_us_awb_arctic-0.95-release.tar.bz2
if [[ $(sha256sum cmu_us_awb_arctic-0.95-release.tar.bz2) != "3f42f17fcb7eaec5cac61f7e6b6aa928a6ffb45f51b1da124278fd300d47984a  cmu_us_awb_arctic-0.95-release.tar.bz2" ]] ; then
	echo "Download file corrupted - retry."
	exit 1
fi
tar xf cmu_us_awb_arctic-0.95-release.tar.bz2
rm -r cmu_us_awb_arctic-0.95-release.tar.bz2
sudo mkdir -p /usr/share/festival/voices/us
sudo mv cmu_us_awb_arctic /usr/share/festival/voices/us/cmu_us_awb_arctic_clunits
