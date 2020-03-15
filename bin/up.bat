SET HOME=C:\java-msa\workspace\msa-minibank
docker rmi --force msa-minibank_minibank-account
docker rmi --force msa-minibank_minibank-apigateway
docker rmi --force msa-minibank_minibank-b2bt
docker rmi --force msa-minibank_minibank-customer
docker rmi --force msa-minibank_minibank-cqrs
docker rmi --force msa-minibank_minibank-eureka
docker rmi --force msa-minibank_minibank-transfer
docker rmi --force msa-minibank_minibank_web
cd minibank_eureka
cmd /c call gradlew build
cd %HOME%
cd minibank_account\
cmd /c call gradlew build
cd %HOME%
cd minibank_apigateway\
cmd /c call gradlew build
cd %HOME%
cd minibank_b2bt\
cmd /c call gradlew build
cd %HOME%
cd minibank_customer\
cmd /c call gradlew build
cd %HOME%
cd minibank_inquiry\
cmd /c call gradlew build
cd %HOME%
cd minibank_transfer\
cmd /c call gradlew build
cd %HOME%
cd minibank_web\
cmd /c call gradlew build
cd %HOME%
docker-compose up --build