#!/usr/bin/expect

lassign $argv host user user_password mysql_root_password
set timeout 120

spawn ./remote_process.sh $host $user $mysql_root_password


expect {
    "$user@$host's password:" { send "$user_password\r" ; exp_continue }
    expect eof
}

