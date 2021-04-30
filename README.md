# Calculator

* the calculator supports addition, subtraction, multiplication, division, log on floating point numbers
* the calculator can solve simple linear equations with a single variable (namely, x), for simplicity, only addition, subtraction and multiplication operations are allowed
* the calculator supports parentheses in both modes
* the calculator should have a language parser
* do not use any library that can accomplish any of the listed requirements
* the calculator should handle all error cases properly (by carefully indicating the errors to the user)

Currently 0x is regarded as a polynomial, so e.g. log(0x + 1) will throw an exception stating that only linear
equations are supported by the calculatorApplication, it seems to be the more logical behavior. Same goes for e.g. 2x * 0x.

## Examples

```
input: (3+(4-1))*5
output: 30

input: 2 * x + 0.5 = 1
output: x = 0.25

input: 2x + 1 = 2(1-x)
output: x = 0.25
```
