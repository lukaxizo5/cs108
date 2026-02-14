# Assignment 4 - Threads

This assignment focused on practical use of **Java Threads**, synchronization
and concurrent system design through four independent applications.

## Bank
* Simulated a multi-account banking system with concurrent transaction processing
* Implemented **producer-consumer pattern** using `BlockingQueue`
* Thread-safe account updates with synchronization and atomic operations
* Used `CountDownLatch` for worker coordination
* Command-line execution with configurable worker thread count

## Hash Cracker
* Implemented SHA-based password hashing and brute-force cracking tool
* Workload distributed across threads

## The Count
* GUI application with up to four independent counting workers
* Ensured UI responsiveness using **Swing thread rules** and `SwingUtilities.invokeLater`

## WebLoader
* Multithreaded web content downloader with progress tracking GUI
* Implemented **semaphore-based worker limits** and launcher thread pattern
