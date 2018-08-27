<?php
//require_once 'D:\website\vendor\autoload.php';

if(isset($_POST["email"])){




//$name = $_POST["fullname"];
$email = $_POST["email"];
$username = $_POST["username"];
//$name = "james cheng";
//$email = "cheng-james@hotmail.com";
//$username = "dick";
//$CLIENT_ID = "436363084512-t4isk4r7gdc53sgffr31bo0f510fsp9s.apps.googleusercontent.com";
   
//$client = new Google_Client(['client_id' => $CLIENT_ID]);
//$payload = $client->verifyIdToken($id_token);

  //$userid = $payload['sub'];
 
	 $connection = mysqli_connect('localhost:3306', 'root', '1234','test_image');
	// $query = "INSERT INTO usergid(userGIDST,useremail,userfullname,username) values('$userid','$email','$name','$username');";
	$query_search_un = "SELECT * FROM test_image.usergid WHERE username LIKE '$username';";
	$query_search_em = "SELECT * FROM test_image.usergid WHERE useremail LIKE '$email';";
	
		$result_un = mysqli_query($connection, $query_search_un);
		$result_em = mysqli_query($connection, $query_search_em);
		$row = mysqli_fetch_assoc($result_un);
		$row1 = mysqli_fetch_assoc($result_em);
		
		if(($row['id'] == NULL) && ($row1['id'] == NULL)){
			$Jobj->status = 0;
			
			
		}else if(($row['id'] != NULL) && ($row1['id'] == NULL)){
			$Jobj->status = 1;

		}else if(($row['id'] == NULL) && ($row1['id'] != NULL)){
			$Jobj->status = 2;
			
			
		}else{
			$Jobj->status = 3;
			
		}
	 
  
  $myjson = json_encode($Jobj);
  
  echo $myjson;
  
  // If request specified a G Suite domain:
  //$domain = $payload['hd'];


}
?>