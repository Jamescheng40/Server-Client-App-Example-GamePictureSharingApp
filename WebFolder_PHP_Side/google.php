<?php
//your autolaod file location
	require_once '\autoload.php';

if(isset($_POST["idtoken"])){



$id_token =  $_POST["idtoken"];
$name = $_POST["fullname"];
$email = $_POST["email"];
$username = $_POST["username"];

// your client ID, Specified by Google 
	$CLIENT_ID = "";
   
$client = new Google_Client(['client_id' => $CLIENT_ID]);
$payload = $client->verifyIdToken($id_token);
if ($payload) {
  $userid = $payload['sub'];
  if(isset($userid)){
	 $connection = mysqli_connect('localhost:3306', 'root', '1234','test_image');
	 
	$query_search = "SELECT * FROM test_image.usergid WHERE userGIDST LIKE '$userid';";
		$result = mysqli_query($connection, $query_search);
		$row = mysqli_fetch_assoc($result);
		
		if($row['id'] == NULL){
			//lead to another page for registering new username
			$JObj->status = "0";
			$myJson = json_encode($JObj);
			echo $myJson;
			
			
		}else{
			$JObj->status = "1";
			$JObj->username = $row['username'];
			$JObj->useremail = $row['useremail'];
			$myJson = json_encode($JObj);
			
			echo $myJson;
		}
	 
  }
  // If request specified a G Suite domain:
  //$domain = $payload['hd'];
} else {
	echo "nope";
  // Invalid ID token
}

}
?>