# Crusader Kings 2 Matchmaker

This is an updated version of the original author badger_ken's [ck2 matchmaker application](http://www.couscouscrabcakes.com/matchmaker/matchmaker.html). This version has been fixed to work with the latest version of CK2 (3.3.3) and has some minor new features. 

See http://www.couscouscrabcakes.com/matchmaker/matchmaker.html for the original website with usage examples etc. To install the updated version please follow the instructions below. 

## Attribution
Many thanks to the original author badger_ken. Ken can be contacted at #TODO INSERT EMAIL# and their website is #WEBSITE#.

## Installation

#### Disclaimer 
This is an alpha release which has not been tested with mods, or many different save files. Your browser, operating system and/or antivirus might detect the program as unregonized and as such dangerous. The code is available in this repo and does nothing dangerous or malicious. Additionally you can compile and run it yourself using gradle if downloading the precompiled binaries is too risky for your tastes. Please follow the compilation from source section below if so.

Please raise any issues you encounter on this repo in the issues tab above or in the [thread on the paradox forum](https://forum.paradoxplaza.com/forum/threads/matchmaker-a-tool-to-help-you-find-that-perfect-noble.601230/) 

#### Steps
1. Download the latest release from the releases page : [ck2_matchmaker_2.0.0-alpha.zip](https://github.com/nigel-gott/ck2_matchmaker/releases/download/v2.0.0-alpha/ck2_matchmaker_2.0.0-alpha.zip)
    1. In chrome you may have to confirm you wish to keep the zip
    2. Click the up arrow next to the discard button and select Keep
2. Extract the zip file. 
3. Navigate into the extracted `ck2_matchmaker_2.0.0-alpha/` folder.
4. In Windows:
    1. Run the `matchmaker_ck2` Application 
    2. Windows might detect it is an unrecognised app.
    3. If so click `More info` and then `Run anyway` 
5. In Linux
    1. Go into the sub folder lib and double click `matchmaker_ck2-2.0-all.jar` to run
    2. If it does not run you may have to install java first, follow https://java.com/en/download/help/linux_x64_install.xml
    3. Once java is installed then run in the terminal: `java -jar matchmaker_ck2-2.0-all.jar`

## Contribution
Please free feel to open feature requests or report bugs as issues. Pull requests are also very welcome! 

## Compile from source
1. Clone this repo 
2. Run `./gradlew createAllExecutables`
3. Find the compiled executables in `build/launch4j/`
