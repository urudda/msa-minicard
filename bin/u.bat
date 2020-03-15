SET HOME=C:\java-msa\workspace\msa-minibank
docker rmi --force msa-minibank_minibank-apigateway
docker rmi --force msa-minibank_minibank-eureka
cd minibank_eureka
cmd /c call gradlew build
cd %HOME%
cd minibank_apigateway\
cmd /c call gradlew build
docker-compose up --build