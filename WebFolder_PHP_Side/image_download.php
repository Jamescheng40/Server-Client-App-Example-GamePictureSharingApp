<?php 
$server_name = "localhost:3306";
$username = "root";
$password = "1234";
$dbname = "test_image";

$con = mysqli_connect($server_name,$username,$password,$dbname);

if ($con->connect_error) {
    die("Connection failed: " . $con->connect_error);
} 

$query_select = "SELECT id FROM test_image.photos ORDER BY id DESC LIMIT 1;";
$result = $con->query($query_select);
if(mysqli_num_rows($result) > 0){
	
		$row =  mysqli_fetch_assoc($result);
		$tempname = $row['name'];
			
		printf ("%s\n", $tempname);	
	}
}
else{
	printf("connection lost \n");
	
}

$con->close();
 ?>