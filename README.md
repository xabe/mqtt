## Ejemplo de uso de Mqtt con Java 

Lo primero es crear la password de mqtt para nuestra aplicación:

Para crear la password ejecutamos el siguiente comando para generar la password

```shell script
docker-compose run mosquitto /bin/sh -c "touch /tmp/passwd && mosquitto_passwd -b /tmp/passwd mosquitto password && cat /tmp/passwd && rm /tmp/passwd" > ./mosquitto/config/mosquitto.passwd
```

Modificamos el fichero de configuración añadiendo el usuario y password en el fichero de mosquitto.conf:

```shell script
user mosquitto
password_file /mosquitto/config/mosquitto.passwd
allow_anonymous true
```

Para comprobar que todo funciona arrancamos el broker:

```shell script
docker-compose up -d
```

Abrimos una terminal nos conectamos al container para escuchar los eventos de mqtt con los siguientes comandos:

```shell script
docker exec -it mosquitto sh
mosquitto_sub -h localhost -t "test" -u "mosquitto" -P "password"
```

Abrimos otra terminal nos conectamos al contianer para enviar un evento en lo topic **test**:

```shell script
docker exec -it mosquitto sh
mosquitto_pub -h localhost -t "test" -u "mosquitto" -P "password" -m "Hello World"
```

Lo siguiente es generar los binarios de productor y consumidor

```shell script
mvn clean install
```

Una vez generado los binarios podemos atacar el api del productor:


> ### Api productor
>
> - Crear un evento de temperatura
>
>```shell script
>curl --request POST \
>  --url http://localhost:8008/producer/sensor/temperature \
>  --header 'content-type: application/json' \
>  --data '{
>	"value" : "10",
>	"unit": "CELSIUS"
>}'
>```
>
> - Crear un evento de humedad
>
>```shell script
>curl --request POST \
>  --url http://localhost:8008/producer/sensor/humidity \
>  --header 'content-type: application/json' \
>  --data '{
>	"value" : "50",
>	"unit": "PERCENTAGE"
>}'
>```

-------

> ### Api consumidor
>
> - Obtener todos los eventos temperatura
>
>```shell script
>curl --request GET \
>  --url http://localhost:8009/consumer/sensor/temperature
>```
> - Obtener todos los eventos humedad
>
>```shell script
>curl --request GET \
>  --url http://localhost:8009/consumer/sensor/humidity
>```