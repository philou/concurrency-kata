# concurrency-kata

Didactic walk-through the implementation of a basic chatroom in Java, from crude locks to more abstract concurrency control.

## Motivation

By studying the code here (or even better by redoing the exercice), I hope that one can get a deeper understanding of the various concurrency techniques. I myself learned a lot by preparing this Kata.

The concurrency techniques are (in order of appearance) :

* mono-threading
* unbounded thread pool with synchronized methods
* unbounded thread pool with concurrent collections
* bounded thread pool with concurrent collections
* actors model relying on real OS threads
* actors model with green threads, relying on a bounded OS thread pool
* CSP model with green threads
* bounded thread pool with a mix of concurrent collections and fine grained synchronized methods

## Installation

You will need vagrant with docker support.

Then just start hacking using ```./hack.sh``` the password for vagrant is simply 'vagrant'.

### Troubleshooting

if you are getting errors like

```
/var/run/docker.sock: permission denied. Are you trying to connect to a TLS-enabled daemon without TLS?
```

It means your user is not in the docker group and misses the rights. Please run ```sudo gpasswd -a ${USER} docker``` and restart your machine. See http://techblog.roethof.net/why-running-docker-command-returns-varrundocker-sock-permission-denied/ for details.

## How to Follow this Kata

There are 2 ways to explore this kata : through git or through the IDE.

### Through git

If you have the time, this should teach you all that there is to learn from this kata. You might start from the first MONOTHREAD tag and then study the commits up to master to see how the different implementations were created, refactored and benchmarked. One drawback though is that you might loose some time understanding some implementation details along the way.

### Through the IDE

This should be the most time effective way to go through this kata. Just checkout specific tags and study the various implementations at this stage. Commits of interest are :

* CSP : all implementations have been benchmarked, but the chat room does not support login messages, making the bounded concurrent implementation the best by far.
* BOUNDED-FINE-GRAINED : (or directly master) the login feature has been added, with the required fixes to bounded concurrent (corse and fine grained synchronized methods)

## Takeaways

Concerning Java multithreading, here is what I learned or confirmed :

* If there is not much to parallelize, the mono-thread version will be faster (the chatroom server does not do much, so the benchs are mostly measuring the communication cost, which explains why the monothread version generaly remains the fastest one)
* Spawning an unlimited number of threads will not work. Your system will come to a halt, so unbounded thread pool are only suitable in cases where you know that you will not use too many threads by design.
* Corse grained synchronized blocks are usually slow, but it's an easy way to get thread safety
* If you can get away with a bounded thread pool and concurrent collections, just do it, it's rather simple, and it faster all other implementations hands down
* When things get more complex, it's likely that you'll need to use finer grained synchronized blocks in conjunction with bounded thread pools and concurrent collections. This can still perform very well, but the complexity price is high. This usually becomes very error prone, and there is no way to ensure thread safety (I've heard of horror stories where it took weeks to trigger threading bugs)
* This is when Actors and CSP become an interesting solutions. Actors are really not that difficult to implement in java. Their main advantage being that they are easy to reason about.
* Actors and CSP perform rather well when we implement them with the 'green' threads
* Actors are some kind of special case of CSP
* Refactoring Actors to CSP was an interesting problem in itself, and it made me understand CSP a lot better than before

## Meta Takeaways

Preparing this kata, which I thought would take me a few hours of work, turned out to be a large endeavour ... Summing all the work, it took me at least a full week of work.

I went through a lot of failures and false starts

* the first implementation had no tests, and I got stuck at some point
* the second used TCP, which was more realistic, but hid the pure threading issues behind the network stack and brought too much complexity
* the third was supposed to demonstrate the kata along the git log, but that proved unrealistic when I later discovered early implementation or test faults which would have required to change the whole git history
* eventually, I got the final one with test, no TCP, all and a branch by abstraction model

Here is what I learned about preparing this kind of teaching material

* using branch by abstraction is the best way to compare multiple alternatives in a git repo, because it allows to cope with changes both to the problem and to the implementations at any time
* don't skip test
* be ready to spend a lot of time

Eventually, here is what I could do with all this

* practice and distill what I learned even more in order to be able to present all this in a fluid 2h session of live coding (a real Coding Kata)
* extract a 1 or 2 day training about concurrency