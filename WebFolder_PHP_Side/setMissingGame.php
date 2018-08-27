<?php

if(isset($_POST["game"])){

$game = $_POST["game"];	
	
//username and psw
$server_name = "localhost:3306";
$username = "root";
$password = "1234";
$dbname = "test_image";


//connect to database
$con = mysqli_connect($server_name,$username,$password,$dbname);

if ($con->connect_error) {
    die("Connection failed: " . $con->connect_error);
} 


$query_select = "INSERT INTO test_image.missinggame(games) values('$game');";
$result = $con->query($query_select);

if($result){
	echo "success";
	
}else{
	echo "failed";
}
}else{
	echo "something is wrong";
	
}
?>