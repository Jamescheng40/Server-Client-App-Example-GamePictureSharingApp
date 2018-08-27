<?php
 //header('Content-type : application/x-www-form-urlencoded; charset=utf-8');
 
 if(isset($_POST["id"])){
	 $id = $_POST["id"];
	 $server_name = "localhost:3306";
$username = "root";
$password = "1234";
$dbname = "test_image";

$con = mysqli_connect($server_name,$username,$password,$dbname);


if ($con->connect_error) {
    die("Connection failed: " . $con->connect_error);
} 
$id = (int)$id;	

$query_select = "SELECT name,games,username FROM test_image.photos ORDER BY id desc Limit $id,1;";
$result = $con->query($query_select);
	 
if(mysqli_num_rows($result) > 0){
	
		$row =  mysqli_fetch_assoc($result);
		$tempid = $row['name'];
		$tempgames = $row['games'];
		$tempusername = $row['username'];
		printf ("%s\n", $tempid);	
		printf ("%s\n", $tempgames);
		printf ("%s\n", $tempusername);
	}

else{
	printf("connection lost \n");
	
}
	 
	 
 }else{
	 
	 echo "error";
 }
 
 ?>