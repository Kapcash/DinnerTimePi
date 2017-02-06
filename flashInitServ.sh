#/bin/bash

echo "Server started"

for i in 1 2 3 
do
	for j in  {3..6}
	do
		gpio write $j 1
		sleep .2
		gpio write $j 0
		sleep .1
	done
done
