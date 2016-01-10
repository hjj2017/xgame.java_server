<?php
define('START', 2048);
define('OVER', 8192);
define('VALUES_COUNT', 512);

echo "set names 'utf8'; \n\n";

for ($i = START; $i <= OVER; $i++) {
    $insert = '';
    $values = '';
    $prevPunctuation = ", \n";

    if (($i - START) % VALUES_COUNT == 0) {
        if ($i == START) {
            $prevPunctuation = '';
        } else if ($i > START) {
            $prevPunctuation = "; \n";
        } 

        $insert = <<<__sql
insert ignore into t_player ( platform_uid_str, user_name, user_pass, create_time, passbook_create_time, pf ) values \n
__sql;
    }

    $values = <<<__sql
( '${i}', '${i}', '1', '1', '1', 'WEB' )
__sql;


    echo $prevPunctuation . $insert . $values;
}

echo "; \n";

