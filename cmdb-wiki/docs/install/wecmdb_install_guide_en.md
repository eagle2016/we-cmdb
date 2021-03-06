# WeCMDB Install Guide


The WeCMDB runtime environment consists of three components: 

1. wecmdb-app
2. wecmdb-db (MySQL)
3. cas server

The installation packages for these three components are provided as docker images.

This installation guide starts the WeCMDB service through docker-compose. You do not need to install the MySQL and CAS server separately. You can modify some configuration files to use the existing MySQL and CAS server.


## Before installation
1. Linux host with the Internet connection, we recommend a server with 4 cores, 8GB or more memory for better compilation speed.
2. Ubuntu 16.04+ or CentOS 7.3+ is preferred.
3. Docker and docker-compose installed.
     - PLease refer to [docker install guide](https://github.com/WeBankPartners/we-cmdb/blob/master/cmdb-wiki/docs/install/docker_install_guide_en.md) on how to install docker.
     - PLease refer to [docker-compose install guide](https://github.com/WeBankPartners/we-cmdb/blob/master/cmdb-wiki/docs/install/docker-compose_install_guide_en.md) on how to install docker-compose.


## Load images

   Load docker image from files, execute the command as following：

   ```shell script
   docker load --input wecmdb-app.tar
   docker load --input wecmdb-db.tar 
   ```

   Execute command 'docker images'

   ```shell script
	docker images
   ```

   You can see that the image has been imported：

   ![wecmdb_images](images/wecmdb_images.png)

   Take a note of the `image name` and `TAG` in the mirror list, which is useful in the following configuration.

## Configuration
1. Create installation directory and files
	
	On the deployment environment, create an installation directory and following four files within:

	[cmdb.cfg](../../../build/cmdb.cfg)

	[install.sh](../../../build/install.sh)

	[uninstall.sh](../../../build/uninstall.sh)

	[docker-compose-all.tpl](../../../build/docker-compose-all.tpl)

2. Config `cmdb.cfg` as follows

	```
	#cmdb
	cmdb_server_port=8080
	cmdb_image_name=we-cmdb:dev
	cmdb_ip_whitelists={$cmdb_ip_whitelists}

	#database
	database_image_name=cmdb-db:dev
	database_init_password=mysql
	```

	 config items             |desc
	 -------------------------|--------------------
	 cmdb_server_port         |cmdb service port
	 cmdb_image_name          |cmdb docker image name and TAG：we-cmdb:a092a47
	 cmdb_ip_whitelists       |ip whitelists, If the third-party service wants to call the cmdb api, you need to add the access ip of the third-party service into this list. If there are multiple services, the middle is separated by a comma; if not, the default is 127.0.0.1.
	 database_image_name      |cmdb database docker image name and TAG，such as：cmdb-db:dev
	 database_init_password   |cmdb database password


3. Config `install.sh` as follows

	```shell script
	#!/bin/bash
	set -ex
	if ! docker --version &> /dev/null
	then
	    echo "must have docker installed"
	    exit 1
	fi
	
	if ! docker-compose --version &> /dev/null
	then
	    echo  "must have docker-compose installed"
	    exit 1
	fi
	
	source cmdb.cfg
	
	sed "s~{{CMDB_CORE_IMAGE_NAME}}~$cmdb_image_name~" docker-compose-all.tpl > docker-compose.yml
	sed -i "s~{{CMDB_SERVER_PORT}}~$cmdb_server_port~" docker-compose.yml  
	sed -i "s~{{CMDB_DATABASE_IMAGE_NAME}}~$database_image_name~" docker-compose.yml  
	sed -i "s~{{MYSQL_ROOT_PASSWORD}}~$database_init_password~" docker-compose.yml 
	sed -i "s~{{CMDB_IP_WHITELISTS}}~$cmdb_ip_whitelists~" docker-compose.yml
	
	
	docker-compose -f docker-compose.yml up -d
	```

4. Config `uninstall.sh` as follows

	```shell script
	#!/bin/bash
	docker-compose -f docker-compose.yml down -v
	```

5. Config `docker-compose-all.tpl` as follows
	
	The service to be installed is configured in this file：wecmdb-cas、wecmdb-mysql、wecmdb-app。

	If you already have cas and mysql, comment out the two paragraphs in the file. In the environment configuration of the wecmdb, manually modify the cas and database configuration.
	
	Detailed code is as follows:

	```
	version: '2'
	services:
	  wecmdb-mysql:
	    image: {{CMDB_DATABASE_IMAGE_NAME}}
	    restart: always
	    command: [
	            '--character-set-server=utf8mb4',
	            '--collation-server=utf8mb4_unicode_ci',
	            '--default-time-zone=+8:00'
							'--max_allowed_packet=4M'
	    ]
	    volumes:
	      - /etc/localtime:/etc/localtime
	    environment:
	      - MYSQL_ROOT_PASSWORD={{MYSQL_ROOT_PASSWORD}}
	    ports:
	      - 3306:3306
	    volumes:
	      - /data/cmdb/db:/var/lib/mysql
	  wecmdb-app:
	    image: {{CMDB_CORE_IMAGE_NAME}}
	    restart: always
	    volumes:
	      - /data/cmdb/log:/log/
	      - /etc/localtime:/etc/localtime
	    depends_on:
	      - wecmdb-mysql
	    ports:
	      - {{CMDB_SERVER_PORT}}:8080
	    environment:
	      - TZ=Asia/Shanghai
	      - MYSQL_SERVER_ADDR=wecmdb-mysql
	      - MYSQL_SERVER_PORT=3306
	      - MYSQL_SERVER_DATABASE_NAME=cmdb
	      - MYSQL_USER_NAME=root
	      - MYSQL_USER_PASSWORD={{MYSQL_ROOT_PASSWORD}}
	      - CMDB_SERVER_PORT={{CMDB_SERVER_PORT}}
	      - CMDB_IP_WHITELISTS={{CMDB_IP_WHITELISTS}}
	```

## Installation
1. Execute the following command to start the WeCMDB service through docker-compose.
	
	```shell script
	/bin/bash ./install.sh
	```
 
2. Check

	Visit WeCMDB's url `http://cmdb_server_ip:cmdb_server_port/wecmdb` to confirm that the page is accessed normally.
	

## Uninstall
Execute the following command to stop the WeCMDB service through docker-compose.

```shell script
/bin/bash ./uninstall.sh
```

## Restart
Execute the following command to stop the WeCMDB service through docker-compose.

```shell script
/bin/bash ./uninstall.sh
```

Modify the cmdb.cfg configuration file as needed to restart the service.

```shell script
/bin/bash ./install.sh
```