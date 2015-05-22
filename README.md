# concurrency-kata
Walkthrough the implementation of a basic chatroom, from crude locks to more abstract concurrency control

## Installation

You will need vagrant with docker support.

Then just start hacking using ```./hack.sh``` the password for vagrant is simply 'vagrant'.

### Troubleshooting

if you are getting errors like

```
/var/run/docker.sock: permission denied. Are you trying to connect to a TLS-enabled daemon without TLS?
```

It means your user is not in the docker group and misses the rights. Please run ```sudo gpasswd -a ${USER} docker``` and restart your machine. See http://techblog.roethof.net/why-running-docker-command-returns-varrundocker-sock-permission-denied/ for details.