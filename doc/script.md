# RxJava - Script

## Intro
### Types of Concurrency
**Before** answering *"What is RxJava?"*, **short excursion** into **types of concurrency**.  
**Which types are there?**

* **None at all** - sequential
* **Parallel** - multi-tasking
* **Asynchronous** - interleaving - Without idle time in tasks
* **Sequential** - With idle time in tasks
    - Iterator-based, sequential processing
* **Asynchronous** - interleaving - With idle time in tasks
    - Push-based, sequential but asynchronous processing
**Example**: Web Server, Apache vs Node.js  
Wrong application of asynchrony in **Javascript**: **Callback Hell**

### What is RxJava?
RxJava takes the **Observer Pattern** and **introduces sequences** to it.
RxJava **extends** the Observer Pattern with two things:

* ability to signal that an **error** has occurred
* ability to signal that the producer has **completed**, so that no more data is available

RxJava Observables establishes **feature-parity** between Observables and Iterators, **the only difference is the direction of the data flow**.

**RxJava's objective is to work on discrete values that are emitted over time.**

### Functional Programming
RxJava **combines** observable sequences with functional programming, which means **operations on streams should have no side effects**.

### Java8+ Streams
**Streams exist in Java 8**, but they are:

* Pull-based
* Single-Use
* Don't allow merging
* Don't support time-based operations

Java 8 introduced the **CompletableFuture**, which allows callbacks.  
But it:

* does not have the operators of RxJava
* is single-element based, no streams

TODO:
- Finish Functional Programming section
- set up task 1 section
- set up task 2 section
