<?php
/* ----------------------------------------------------------------------------
 *
 * This example shows how BaseX commands can be performed via the PHP API.
 * The execution time will be printed along with the result of the command.
 *
 * ----------------------------------------------------------------------------
 * (C) Workgroup DBIS, University of Konstanz 2005-10, ISC License
 * ----------------------------------------------------------------------------
 */
include("BaseXClient.php");

//$createDB = "create db <countries><c>Brasil</c><c>Italy</c><c>Germany</c><c>Uruguay</c><c>Argentina</c><c>England</c><c>France</c></countries> wm";

// command to be performed
$cmd = 'for $node in doc("factbook")//country order by xs:int($node/@population) return data($node/@name)';
$cmd2 = 'for $node in doc("factbook")//country order by xs:int($node/@population) return data($node/@population)';

try {
  // create session
  $session = new Session("localhost", 1984, "admin", "admin");
  
  //$session->execute($createDB);
  echo "<table border='0' cellspacing='2' cellpadding='4' width='20%'>
	<tbody><tr style='text-align:center;'><tbody><tr style='text-align:center;'>";
  echo "<td style='text-align:center;background-color:#D7D7D7;border:#ffffff 1px solid;font-size:12pt;'></td>";
  echo "<td style='text-align:center;background-color:#D7D7D7;border:#ffffff 1px solid;font-size:12pt;'>Country</td>";
  echo "<td style='text-align:center;background-color:#D7D7D7;border:#ffffff 1px solid;font-size:12pt;'>Population</td>";
  try {
  $query = $session->query($cmd);
  $query2 = $session->query($cmd2);
  $count = 0;
  while($query->hasNext()) {
  		$next = $query->next();
  		$query2->hasNext();
  		$next2 = $query2->next();
  		$count += 1;
  		if($count%2) {
  		echo "<tr style='text-align:center;'>
  		<td style='text-align:center;'>$count</td><td style='text-align:center;'>$next</td>
          <td style='text-align:center;'>$next2</td></tr>";
  		} else {
  		echo "<tr style='text-align:center; background-color:#eeeeee;'>
  		<td style='text-align:center;'>$count</td><td style='text-align:center;'>$next</td>
          <td style='text-align:center;'>$next2</td></tr>";
  		}
  	}
  $query->close();
  $query2->close();
  } catch (Exception $e) {
  // print exception
  print $e->getMessage();
  }	
  echo "</table>";
  $query->close();
  // close session
  $session->close();

} catch (Exception $e) {
  // print exception
  print $e->getMessage();
}
?>
