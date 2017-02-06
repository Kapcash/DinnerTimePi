#/bin/bash

if [ -z "$1" ];then
	echo "Missing argument 1: <gpio pin number>"
	exit 1
fi
if [ -z "$2" ];then
	echo "Missing argument 2 : <flashing time>. 60 by default"
	time=60
else
	time=$2
fi

echo "Flashing"
start=$SECONDS
while  [ $((SECONDS-start)) -le $time ]
do
	gpio write $1 1
	sleep .5
	gpio write $1 0
	sleep .5
done
