<?php

if(isset($_POST["username"])){

$name = $_POST["name"];
$username = $_POST["username"];	
	
//username and psw
$server_name = "localhost:3306";
$dbusername = "root";
$password = "1234";
$dbname = "test_image";


//connect to database
$con = mysqli_connect($server_name,$dbusername,$password,$dbname);

if ($con->connect_error) {
    die("Connection failed: " . $con->connect_error);
} 

$query_select1 = "SET SQL_SAFE_UPDATES = 0;";
$query_select = "Delete FROM photos Where name = '$name' and username = '$username' limit 1;";
$query_select2 = "SET SQL_SAFE_UPDATES = 1;";
$result1 = $con->query($query_select1);
$result = $con->query($query_select);
$result2 = $con->query($query_select2);


if($result){
	echo "success";
	
}else{
	echo "failed";
}
}else{
	echo "something is wrong";
	
}
?>