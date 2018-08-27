<?php
 //header('Content-type : application/x-www-form-urlencoded; charset=utf-8');
 
 if(isset($_POST["id"])){
	 $id = $_POST["id"];
	 $games = $_POST["games"];
	 $server_name = "localhost:3306";
$username = "root";
$password = "1234";
$dbname = "test_image";

$con = mysqli_connect($server_name,$username,$password,$dbname);
$id = (int)$id;

if ($con->connect_error) {
    die("Connection failed: " . $con->connect_error);
} 

$query_select = "SELECT * FROM test_image.photos where games LIKE '$games' limit $id,1;";
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
	printf("ResultNotFound");
	
}
	 
	 
 }else{
	 
	 echo "http error";
 }
 
 ?>