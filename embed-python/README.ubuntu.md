# Setting up ubuntu from scratch for java, kotlin, python3.8
Below guide explains hows to setup everything from scratch to make this project work in a fresh Ubuntu 65bit Linux OS

## Assumptions
if you are in root mode and see # prompt then you can use
apt install

if you are in user mode and see $ promp then you can use
sudo apt install

## Update apt
     apt update

## Install Java
     apt install default-jdk
     
## Check java
     java -version
You will get the below output

      openjdk version "11.0.8" 2020-07-14
      OpenJDK Runtime Environment (build 11.0.8+10-post-Ubuntu-0ubuntu120.04)
      OpenJDK 64-Bit Server VM (build 11.0.8+10-post-Ubuntu-0ubuntu120.04, mixed mode, sharing)
      
## Install dependencies for Kotlin native
      apt install git wget unzip
      apt install libncurses5 libncurses5-dev libncursesw5-dev
      apt install gcc-multilib g++-multilib
## install python development libraries
      apt install python3-dev
## Install gradle
      cd /opt
      wget https://services.gradle.org/distributions/gradle-6.5.1-bin.zip
      unzip gradle-6.5.1-bin.zip
## Set PATH for gradle

       cd ~
       export PATH=$PATH:/opt/gradle-6.5.1/bin
## checkout the source code
       git clone https://github.com/dickensas/kotlin-gradle-templates.git
       cd kotlin-gradle-templates/embed-python
       
## set PYTHONPATH
       export PYTHONPATH=/usr/lib/python38.zip:/usr/lib/python3.8:/usr/lib/python3.8/lib-dynload:/usr/local/lib/python3.8/dist-packages:/usr/lib/python3/dist-packages:./
       
## compile
       gradle assemble
## run
       gradle runReleaseExecutableLibgnuplot
## See output
       
       > Task :runReleaseExecutableLibgnuplot
       Result of call: 9
       Today is Sat Aug  1 20:59:17 2020
       Will compute 3 times 3