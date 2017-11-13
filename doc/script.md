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

## RxJava
RxJava takes the **Observer Pattern** and **introduces sequences** to it.
RxJava **extends** the Observer Pattern with two things:

* ability to signal that an **error** has occurred
* ability to signal that the producer has **completed**, so that no more data is available

RxJava Observables establishes **feature-parity** between Observables and Iterators, **the only difference is the direction of the data flow**.

**RxJava's objective is to work on discrete values that are emitted over time.**

### Streams in Java 8+
**Streams exist in Java 8**, but they are:

* Pull-based
* Single-Use
* Don't allow merging
* Don't support time-based operations

Java 8 introduced the **CompletableFuture**, which allows callbacks.  
But it:

* does not have the operators of RxJava
* is single-element based, no streams

### Functional Programming
RxJava **combines** observable sequences with functional programming, which means **operations on streams should have no side effects**.

## Stream Operators
* concat()
* merge()
* map()
* filter()
* concatMap()
* flatMap()
* reduce()
* sum()
* scan()

## Task: Donald's Restaurant
### Short Recap
* Orders are submitted as strings in the format:  
    `"<meal_number>, <servings>"`
* Requests for meals are Lists that contain 1 to n Orders
* Order strings are split at the comma and used to construct a new Order object
* All Orders from one Request are put into an OrderLine object
* All Orders in a Request are assigned an increasing index as the order number
* Orders are turned into Meals while waiting for a time specified in its recipe

### Tasks
1. Create a stream of order strings and print them
2. Parse the order strings into `Orders` and put them into `OrderLine`s, then print those
3. Transform every `Order` in an `OrderLine` into a `Meal` and print it
4. Filter out invalid `Orders`, based on checking their recipe number. Handle exceptions by skipping invalid items
5. Add an incrementing order number to each new `OrderLine`
6. Go multi-threaded by using `parallel()`

## Backpressure
**Cold** Observables emit **on subscription**  
**Hot** Observables emit items **constantly**

### Strategies
TBD

### TweetStream
Experiment with a hot observable, tame the stream.  
Do whatever you want with the tweets.

TODO:

* Finish Functional Programming section
* Finish Backpressure section
* Review projects
* Package projects
* Update project links in presentation
