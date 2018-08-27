<?php


if(isset($_POST["idtoken"])){



$id_token =  $_POST["idtoken"];
$name = $_POST["fullname"];
$email = $_POST["email"];
$username = $_POST["username"];
//$name = "james cheng";
//$email = "cheng-james@hotmail.com";
//$username = "dick";


 
	 $connection = mysqli_connect('localhost:3306', 'root', '1234','test_image');
	// $query = "INSERT INTO usergid(userGIDST,useremail,userfullname,username) values('$userid','$email','$name','$username');";
	$query_search = "INSERT INTO usergid(useremail,userfullname,username,userGIDST) values('$email','$name','$username','$id_token');";
		$result = mysqli_query($connection, $query_search);
		
		if($result){
			$JObj->status = "1";
			$myJson = json_encode($JObj);
			echo $myJson;
		}else{
			$JObj->status = "0";
			$myJson = json_encode($JObj);
			echo $myJson;
		}
		//$row = mysqli_fetch_assoc($result);
		
		//echo $row['Duplicates'];
		
	/*	if($row['id'] == NULL){
			//lead to another page for registering new username
			$JObj->status = "0";
			$myJson = json_encode($JObj);
			echo $myJson;
			
			
		}else{
			$JObj->status = "1";
			$JObj->username = $row['username'];
			$JObj->userfullname = $row['userfullname'];
			$myJson = json_encode($JObj);
			
			echo $myJson;
		}
	 */
  
  // If request specified a G Suite domain:
  //$domain = $payload['hd'];


}
?>