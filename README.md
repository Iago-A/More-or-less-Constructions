<img src="https://github.com/user-attachments/assets/fd4bc93f-9236-49e9-a116-d7d554036e81" width=300>

# More or Less: Constructions


## 📝 Sobre la aplicación

More or Less Constructions es una aplicación para móviles Android, desarrollada como proyecto personal, diseñada para poner a prueba las habilidades del usuario en un juego de adivinanza de números (“más o menos”). La app permite jugar partidas individuales, donde se muestran de forma aleatoria varias construcciones tomadas de una base de datos de entre 281 edificaciones como edificios, puentes, estatuas o monumentos, la cual puede ser ampliada.

La aplicación ha sido desarrollada utilizando Android como plataforma principal, con almacenamiento local mediante SharedPreferences para guardar los récords de cada usuario y archivos JSON para la información de las construcciones. Está pensada para cualquier usuario con un dispositivo Android, una vez instalada se puede jugar sin conexión a internet, y ofrece un diseño moderno con fondos responsivos e interfaz intuitiva.

En cuanto al ámbito lingüístico, ofrece soporte en Español, Gallego, Vasco, Catalán, Inglés, Portugués, Francés, Alemán, Italiano, Chino, Japonés y Coreano.

## :iphone: Funcionalidades de la aplicación
- `Juego`: Al iniciar el juego se muestran 2 construcciones, donde solo se muestra la altura de una de las construcciones. El usuario debe elegir cuál cree que es más alta.

  Si acierta, el juego sigue, reemplazando la construcción con menos altura (a no ser que la construcción ganadora lo haya sido por 2 rondas consecutivas, donde se sustituirá esta para que no quede demasiado tiempo en juego).

  Por el contrario si elige la que tenga menos altura, el juego se finaliza, mostrando el número de rondas ganadas.

  Además, se añadió una funcionalidad para que, cualquier construcción jugada, no vuelva a aparecer hasta dentro de 20 rondas, evitando que el juego sea repetitivo o demasiado fácil.
    
- `Extras`: Este apartado fue ideado para alojar funciones útiles como:
  - Reiniciar el récord de victorias.
  - Ver información sobre la aplicación.

- `Soporte de lenguajes`: La propia app cambiará de idioma dependiendo de cual sea el lenguaje definido de nuestro smartphone.

## :camera: Capturas

<img src="https://github.com/user-attachments/assets/474c1671-da80-4e43-a876-aa7d9c69f112" width=290> <img src="https://github.com/user-attachments/assets/edff341b-926e-4031-b216-5cd56d4b9d87" width=290> 
<img src="https://github.com/user-attachments/assets/bd8d6b08-b935-4d50-a0b8-5c8db703b702" width=290>

## :eyes: Probar proyecto

Existen 2 formas de probar la aplicación:
 - `Descargando el proyecto desde GitHub`: Tras descargar el repositorio, abrirlo con tu IDE (por ejemplo Android Studio), y ejecutar el emulador de smartphone.
 - `Descargando la app desde Play Store`: Está previsto que la aplicación se pueda descargar fácilmente desde la plataforma Play Store de Google.

## :computer: Estado del proyecto

:confetti_ball: **Versión 1.0 finalizada** :confetti_ball:

## :heavy_check_mark: Tecnologías utilizadas:

- **Java** - Lenguaje de programación principal
- **XML** - Diseño de interfaces
- **JSON** - Almacenamiento de datos 
- **Android SDK** - Plataforma de desarrollo

## :construction_worker: Desarrolladores del proyecto

| <img src="https://avatars.githubusercontent.com/u/181847143?v=4" width=115><br><sub>Iago Blanco Cañás</sub> 
| :---: | 

Un agradecimiento especial a mi amigo Jesús por ayudarme con la idea del juego, y a todos los propietarios de cada fotografía por tomarlas y compartirlas en internet. :blue_heart:

