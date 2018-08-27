<?php 
$server_name = "localhost:3306";
$username = "root";
$password = "1234";
$dbname = "test_image";

$con = mysqli_connect($server_name,$username,$password,$dbname);



if ($con->connect_error) {
    die("Connection failed: " . $con->connect_error);
} 

$games = $_POST["games"];
//obselete method discarded
    //$query_select = "SELECT id FROM test_image.photos ORDER BY id DESC LIMIT 1;"; 
$query_select = "SELECT count(*) FROM test_image.photos where games LIKE'$games';";
$result = $con->query($query_select);
if(mysqli_num_rows($result) > 0){
		
		$row =  mysqli_fetch_assoc($result);
		$tempid = $row['count(*)'];
			
		printf ("%s\n", $tempid - 1);	
	}

else{
	printf("ResultNotFound");
	
}

$con->close();
 ?>