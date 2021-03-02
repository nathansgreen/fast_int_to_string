## Synopsis

A faster C++ routine to convert 64 bit integers to ASCII form or std::string.

A Java implementation was created to be as algorithmically close as possible to the C++ code.
The initial code supports only 32-bit unsigned integers for simplicity and ease of comparison.
It also supports only direct writes to a `ByteBuffer` to avoid any allocation overhead.

## Example Usage

Just include the source file `fast_int_to_string.hpp` into the translation unit.

In big data processing (e.g. columnar storage engines) even 10% improvement due to algorithmic/macro optimization results in cumulative effect (assuming other parts of the code is competitive enough). In my testing the conversion routine is 30-90% faster than standard counterparts.

## Testing

Added few unit and performance tests.

The Java implementation has both unit tests and microbenchmarks.
The initial code takes a bit more than twice as much time as built-in Java APIs for larger numbers.
A collection of benchmark outcomes are stored in [src/jmh/reports](src/jmh/reports/results.txt).

## License

[Apache 2](http://www.apache.org/licenses/LICENSE-2.0)

## Bugs?
Please report.
