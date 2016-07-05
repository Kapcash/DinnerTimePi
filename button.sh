#/bin/bash
echo "Launched"
while  (true)
do
	val=`gpio read 0`
	if [[ val -eq 1 ]];then
		echo "BUTTON"
		exit 1
	fi
done
