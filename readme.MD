
# Oracle (running from an openshift pod to oracle dev database)
* login to openshift with: `oc login` and `winpty oc exec -it -n bro-lv-dev bhrgt-app-validation-v2-1-5c455b4f4d-rds99 -- bash`
* create tar file and copy tar file over to a docker container in openshift

`mci -DskipTests -f jsonblobs && rm jsonblobs.tar && tar cf jsonblobs.tar jsonblobs && oc cp jsonblobs.tar <pod-name>:/tmp --namespace bro-lv-dev`
* untar the archive and then run the OracleJsonTest from inside the openshift pod

`tar xf jsonblobs.tar && cd jsonblobs && mvn -Dtest=org.example.OracleJsonTest test`

# Postgresql with JsonB (running inside local docker)
* boot up local docker images
* run org.example.PostgresJsonbTest

database client available under: http://localhost:5454

# Postgresql with Json (running inside local docker)
* boot up local docker images
* run org.example.PostgresJsonTest

database client available under: http://localhost:5454

# Postgresql with Json and lz4 compression (running inside local docker)
* boot up local docker images
* run org.example.PostgresJsonLz4Test

database client available under: http://localhost:5454

# Postgresql with bytea and lz4 compression (running inside local docker)
* boot up local docker images
* run org.example.PostgresByeaLz4Test

database client available under: http://localhost:5454

# MongoDB (running inside local docker)
* boot up local docker images
* run org.example.MongoTest

database client available under: http://localhost:18081