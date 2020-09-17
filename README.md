# Trabajo Práctico 1: Elecciones

Ubicarse en el directorio del proyecto y ejecutar:
```bash
$> mvn clean install
```

### Servidor
  ```bash
  $> run-server
  ```
  
### Clientes
#### Cliente de Administracion
  ```bash
  $> ./run-management -DserverAddress=xx.xx.xx.xx:yyyy -Daction=actionName
  ```

#### Cliente de Fiscalizacion
  ```bash
  $> ./run-fiscal -DserverAddress=xx.xx.xx.xx:yyyy -Did=pollingNumber -Dparty=partyName
  ```
  
#### Cliente de Consulta
  ```bash
  $> ./run-query -DserverAddress=xx.xx.xx.xx:yyyy [ -Dstate=stateName | -Did=pollingPlaceNumber ] -DoutPath=fileName
  ```
  
#### Cliente de Votacion
  ```bash
  $> ./run-vote -DserverAddress=xx.xx.xx.xx:yyyy -DvotesPath=fileName
  ```
  
