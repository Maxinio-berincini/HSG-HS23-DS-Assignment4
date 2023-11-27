Assignment 4
------------

# Team Members
- Leon Luca Klaus Muscat
- Felix Kappeler
- Max Beringer

# GitHub link to your (forked) repository (if submitting through GitHub)

>https://github.com/Maxinio-berincini/HSG-HS23-DS-Assignment4


# Task 3

1. What is the causal consistency? Explain it using the happened-before relation.
> We say event f happens-before event g (written f -> g) if:
> - f and g occurred at the same node, and f occurred before g in that node's local execution order, or
> - event f is the sending of a message m, and event g is the receipt of that same message m (assuming sent messages are unique), or
> - there exists some event h such that f -> g and g -> h, then also f -> h.
> 
> two distinct operations f and g are concurrent if neither f -> g nor g -> f (written f || g). 
> 
> Taken from physics (relativity):
> - When f -> g, the f might have caused g.
> - When f || g, we know that f cannot have caused g, and g cannot have caused f.
> - Happens before relation encodes potential causality
> 
> Causal consistency is a consistency model that guarantees that causally related operations are seen by all processes in the same order. 
> This means that if two operations are causally related, then all processes will see them in the same order (and therefore with the same happens-before relationship), 
> regardless of which node they are executed on. Though processes may disagree about the order of causally independent (which are denoted as concurrent) operations.
> It is a weaker consistency model than sequential consistency, but stronger than eventual consistency.
> 
> sources: https://www.educative.io/answers/what-is-causal-consistency-in-distributed-systems, lecture slides

2. You are responsible for designing a distributed system that maintains a partial ordering of operations on a data store (for instance, maintaining a time-series log database receiving entries from multiple independent processes/sensors with minimum or no concurrency). When would you choose Lamport timestamps over vector clocks? Explain your argument. 
   What are the design objectives you can meet with both?
>When designing a distributed system that maintains a partial ordering of operations on a data store, such as a time-series log database, the choice between Lamport timestamps and vector clocks depends on the specific requirements of the system.
> 
> Lamport Timestamps:
> - Suitable to establish a partial ordering of events with a minimal overhead
> - Each process maintains a single Lamport Timestamp counter c, events are tagged with the value of this counter
> - The algorithm ensures that if f -> g, c(f) < c(g)
> - given two events it is possible to determine if one event happened before the other, but it is not possible to determine if they are concurrent
> - If c(f) < c(g), then we cannot tell if f -> g or f || g
> 
> Vector Clocks:
> - Distinguish whether two operations are concurrent or one is causally dependent on the other
> - Each process maintains a vector of numbers, with each element corresponding to a process
> - Vector clocks require knowledge of the number of processes in the system and have a higher space complexity compared to Lamport timestamps
> - This is achieved by holding a vector of n logical clocks in each process (where n is the number of processes)
> 
>When to use Lamport Timestamps:
> - when we need simplicit,an efficient algorithm and less overhead
> - when it is enough to know that causally related events maintain proper sequence (Total ordering)
> - when we don't need concurrency detection for independent events
> 
> When to use Vector Clocks:
> - systems demanding precise event ordering and a need to resolve causality across multiple processes
> - when we need to know about the concurrency of dependent and independent events
> - when we need a reliable system that can handle and detect failures
> 
> Design objectives that can be met with both:
> - Consistency: Both mechanisms ensure a synchronized and coherent sequence of events
> - Fault Tolerance: They assist in fault recovery by providing event sequences, aiding in system integrity restoration after failures.  
> - Synchronization and Order: They synchronize events across distributed processes and maintain a consistent event order for system reliability.
> 
> Sources: https://people.cs.rutgers.edu/~pxk/417/notes/clocks/index.html, lecture slides


3. Vector clocks are an extension of the Lamport timestamp algorithm. However, scaling a vector clock to handle multiple processes can be challenging. Propose some solutions to this and explain your argument. 
>Although Vector clocks are a more sophisticated variant which gives us more guarantees, including knowledge of concurrency & causal history, this comes with some major trade-offs:
> - Memory Overhead: The memory consumption for storing vector clocks grows linearly with the number of processes, posing a significant burden on memory-constrained systems. 
> - Communication Bandwidth Consumption: The exchange of vector clocks during communication events adds to the overall network traffic, potentially overwhelming bandwidth-limited networks.
>
> Here are some techniques to scale up vector clocks:
> - Compression Techniques: Implement compression methods like low-density vectors to reduce the size of vector clocks, minimizing the storage and bandwidth requirements. 
> - Cleanup Processes: Remove obsolete or irrelevant entries from vector clocks periodically, ensuring they retain only pertinent information, reducing unnecessary resource usage. 
> - Aggregation Methods: Employ summaries or hierarchies to aggregate vector clocks, allowing for a more efficient representation of relationships between processes while reducing overall complexity. 
> - Approximation Approaches: Utilize probabilistic or interval-based methods to approximate vector clocks, offering a more streamlined representation of causal relationships without relying on extensive storage.
>
> Sources: 
> - https://ieeexplore.ieee.org/stamp/stamp.jsp?arnumber=9234035
> - https://levelup.gitconnected.com/distributed-systems-physical-logical-and-vector-clocks-7ca989f5f780
> 
