# pyphonelib

Wrap Google's phonelib "https://github.com/google/libphonenumber/tree/master/java"
in a very minimal "http://sparkjava.com". in a small runnable jar

To run:
curl -vvv http://localhost:4568/phonelib/+27829946273

*   Trying 127.0.0.1:4568...
* Connected to localhost (127.0.0.1) port 4568 (#0)
> GET /phonelib/+27829946273 HTTP/1.1
> Host: localhost:4568
> User-Agent: curl/7.79.1
> Accept: */*
> 
* Mark bundle as not supporting multiuse
< HTTP/1.1 200 OK
< Date: Fri, 05 Aug 2022 07:37:11 GMT
< Content-Type: text/html;charset=utf-8
< Transfer-Encoding: chunked
< Server: Jetty(9.4.48.v20220622)
< 
* Connection #0 to host localhost left intact
Vodacom,South Africa,MOBILE%       


Load testing: ab - Apache HTTP server benchmarking tool

ab -n 5000 -c 10 http://localhost:4568/phonelib/+27829946473

Result:
Server Software:        Jetty(9.4.48.v20220622)
Server Hostname:        localhost
Server Port:            4568

Document Path:          /phonelib/+27829946470
Document Length:        27 bytes

Concurrency Level:      10
Time taken for tests:   2.680 seconds
Complete requests:      5000
Failed requests:        0
Total transferred:      775000 bytes
HTML transferred:       135000 bytes
Requests per second:    1865.81 [#/sec] (mean)
Time per request:       5.360 [ms] (mean)
Time per request:       0.536 [ms] (mean, across all concurrent requests)
Transfer rate:          282.42 [Kbytes/sec] received

Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:        0    0   0.1      0       2
Processing:     0    5  13.9      1     144
Waiting:        0    5  13.5      1     144
Total:          0    5  13.9      1     145

Percentage of the requests served within a certain time (ms)
  50%      1
  66%      1
  75%      2
  80%      3
  90%     14
  95%     33
  98%     50
  99%     67
 100%    145 (longest request)
 ------
 
