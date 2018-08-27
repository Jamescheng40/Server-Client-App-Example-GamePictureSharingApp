<?php


if(isset($_POST["email"])){
	
$email = $_POST["email"];
$psw = $_POST["psw"];
$psw_sha = sha1($psw);

$connection = mysqli_connect('localhost:3306', 'root', '1234','test_image');
	$query_search_un = "SELECT * FROM usergid WHERE useremail LIKE '$email';";
	$query_search_em = "SELECT * FROM usergid WHERE psw LIKE '$psw_sha';";
	
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
			$Jobj->status = 4;
			if($row['id'] == $row1['id']){
					$Jobj->status = 3;
					$Jobj->username =$row['username'];
					$Jobj->useremail = $row['useremail'];
					
				}
			while($row1 = mysqli_fetch_assoc($result_em)){
				if($row['id'] == $row1['id']){
					$Jobj->status = 3;
					$Jobj->username =$row['username'];
					$Jobj->useremail = $row['useremail'];
					break;
				}
				
			}
		
		}
	 
  
  $myjson = json_encode($Jobj);
  
  echo $myjson;
  
 

}else{
	echo "failed";
}
?>
