<?php
 //header('Content-type : application/x-www-form-urlencoded; charset=utf-8');
 
 if(isset($_POST["position"])){
	 $position = $_POST["position"];
	 $username = $_POST["username"];
	 
//server info	 
$server_name = "localhost:3306";
$DBusername = "root";
$password = "1234";
$dbname = "test_image";

$con = mysqli_connect($server_name,$DBusername,$password,$dbname);


if ($con->connect_error) {
    die("Connection failed: " . $con->connect_error);
} 

//forced conversion
$position = (int)$position;	

$query_select = "SELECT * FROM photos where username LIKE '$username' limit $position,1;";
$result = $con->query($query_select);
	 
if(mysqli_num_rows($result) > 0){
	
		$row =  mysqli_fetch_assoc($result);
		$tempid = $row['name'];
		//$tempgames = $row['games'];
		//$tempusername = $row['username'];
		printf ("%s\n", $tempid);	
		//printf ("%s\n", $tempgames);
		//printf ("%s\n", $tempusername);
	}

else{
	printf("ResultNotFound");
	
}
	 
	 
 }else{
	 
	 echo "error";
 }
 
 ?>