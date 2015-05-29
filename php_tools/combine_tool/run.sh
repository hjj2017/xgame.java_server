# !/bin/bash

php ./App_Combine.php >> ./combine.log & tail -f ./combine.log

