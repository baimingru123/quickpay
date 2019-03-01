#i1=`ps -ef|grep -E "your_process_name"|grep -v grep|awk '{print $2}'` 
i1=`ps -ef|grep -E quickpass-1.jar|grep -v grep|awk '{print $2}'` 
kill -9 $i1