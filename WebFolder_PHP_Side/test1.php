<?php
//testing for encrypt
//$pasw = "1234567";
//echo sha1($pasw);

$pasw = "asadf";
//echo strlen($pasw);
$hi = 0;
$newstring = "%";
while($hi < strlen($pasw)){
	$newstring = $newstring.$pasw[$hi]."%";
	$hi = $hi + 1;
	
}

printf("%s",$newstring);

?>