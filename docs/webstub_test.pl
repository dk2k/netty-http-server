#!/usr/bin/perl

$counter = 0;
do
{
   $command = '
wget http://127.0.0.1:8200/action --post-file=/home/dk/sample.xml --header="Content-Type: application/soap+xml" --output-document=soapResponse'.$counter.'.xml
';

   print $command;

   $output = `$command`;
   print "$output\n";

   $random_delay = (int( rand(1)) + 1);
   sleep $random_delay;
   print "\ncounter = ".$counter." slept ".$random_delay." sec\n\n";
   $counter+=1;

} while ($counter <10000);