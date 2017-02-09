#/bin/bash

echo "Server started"

gpio mode 3 out
gpio mode 4 out
gpio mode 5 out
gpio mode 6 out

for i in {1..4}
do
	for j in  {3..6}
	do
		gpio write $j 1
		sleep .3
		gpio write $j 0
		sleep .1
	done
done
