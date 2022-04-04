# Tick - Exchange Live Prices & Websocket

## Installation

```console
git clone https://git.panxora.io/Panxora/Tick.git
cd Tick
mvn clean install
cd local-monolith
```

// Make sure docker is running on the machine

```console
mvn fabric8:build
```

** Running the application
```console
docker run -p 8080:8080 -it --name tick -t tick/local-monolith
```
// If you see prices spooling on the screen you are good
// Browse to http://localhost:8080

** Download tick.tar docker image
```console
docker load -i tick.tar
docker create --name tick -p 8080:8080 --restart unless-stopped tick/local-monolith
docker start tick
```
