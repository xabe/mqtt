
Crear password

ejecutar el comando para genrar la password

docker-compose run mosquitto /bin/sh -c "touch /tmp/passwd && mosquitto_passwd -b /tmp/passwd mosquitto password && cat /tmp/passwd && rm /tmp/passwd" > ./mosquitto/config/mosquitto.passwd


Modificamos el fichero de configuración añadiendo mosquitto.conf:

user mosquitto
password_file /mosquitto/config/mosquitto.passwd
allow_anonymous true


Hacer una prueba

nos conectamos a una consola del container para escuchar los eventos:

docker exec -it mosquitto sh

mosquitto_sub -h localhost -t "test" -u "mosquitto" -P "password"

Abrimos otra consola del container para escuchar los eventos:

docker exec -it mosquitto sh

mosquitto_pub -h localhost -t "test" -u "mosquitto" -P "password" -m "Hello World"