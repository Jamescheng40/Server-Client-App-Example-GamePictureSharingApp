<?php 

function convertToSpecial($input){
	if($input == NULL){
		return "no input error ";
	}
	$hi = 0;
	$newstring = "%";
while($hi < strlen($input)){
	$newstring = $newstring.$input[$hi]."%";
	$hi = $hi + 1;
	
}
	

	return $newstring;
}


$input = $_POST['keyword'];

$inputToDB = convertToSpecial($input);


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


$query_select = "SELECT * FROM gameslist where games LIKE '$inputToDB';";
$result = $con->query($query_select);
$row =  mysqli_fetch_assoc($result);


if(mysqli_num_rows($result) > 0){
$tempid = $row['id'];
			$tempid = $row['games'];
		printf ("%s\n", $tempid);	

//get the next rows in the result 
while($row =  mysqli_fetch_assoc($result)){
    	$tempid = $row['games'];	
		printf ("%s\n", $tempid);	
}	
	}
else{
	printf("Result Not Found, Click Here To Report This(What You Typed) Missing Game");
}
$con->close();
?>