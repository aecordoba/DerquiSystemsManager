 #!/bin/bash
 
echo -n "Enter database server IP address: "
read host

if [ "$host" != "localhost" ]
then
 
    echo -n "Enter user on $host: "
    read user
    echo -n "Enter $user's password on $host: "
    read -s user_password
    echo ""
    
fi

echo -n "Enter MySQL root's password on $host: "
read -s mysql_root_password
echo -n
echo

if [ "$host" != "localhost" ]
then

    ./automation.sh $host $user $user_password $mysql_root_password  >> /dev/null

else
    
    ./local_process.sh $mysql_root_password
    
fi

echo "Process end."
