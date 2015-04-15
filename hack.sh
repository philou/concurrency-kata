#!/bin/sh

ssh-keygen -f "~/.ssh/known_hosts" -R [localhost]:2222
vagrant up
ssh -X -p 2222 vagrant@localhost .idea/bin/idea.sh
