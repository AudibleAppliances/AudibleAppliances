#!/bin/sh

sudo apt install libimlib2

echo "Binary" &&
rm -f audible-1.0-SNAPSHOT.jar &&
wget http://oh260.user.srcf.net/AudibleAppliances/builds/audible-1.0-SNAPSHOT.jar &&

echo "Raspicam" &&
cd raspicam &&
git pull && # https://github.com/cedricve/raspicam
mkdir -p build &&
cd build &&
cmake .. &&
make -j4 &&
sudo make install &&
cd ../../ &&

# http://search.maven.org/remotecontent?filepath=org/bytedeco/javacv-platform/1.4/javacv-platform-1.4-bin.zip

echo "Main Repo" &&
cd AudibleAppliances &&
git pull &&
# Don't overwrite the config atm
#cp src/test/resources/testConfig.json ~/config.json &&
git pull &&

echo "Daemon" &&
cd daemon &&
mkdir -p build &&
cd build &&
cmake .. &&
make -j4 &&
cd ../../.. &&

echo "Copying scripts" &&
cd AudibleAppliances &&
cp pi_run.sh ~/ &&
cp killstartups ~/ &&
cp runserver ~/ &&
cp runjar ~/ &&
cp runwebserver ~/ &&
