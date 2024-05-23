package io.github.vinifillos.controllers;

import io.github.vinifillos.operations.MathOperations;
import org.springframework.web.bind.annotation.*;
import java.util.concurrent.atomic.AtomicLong;

import static io.github.vinifillos.operations.Validation.isNumeric;

@RestController
@RequestMapping("/api/v1")
public class MathController {

    private static final AtomicLong counter = new AtomicLong();

    @GetMapping("/sum/{numberOne}/{numberTwo}")
    public Double sum(@PathVariable(value = "numberOne") String numberOne, @PathVariable(value = "numberTwo") String numberTwo) {
        if(!isNumeric(numberOne) || !isNumeric(numberTwo)) throw new UnsupportedOperationException("Please set a numeric value");
        return MathOperations.sum(numberOne, numberTwo);
    }

    @GetMapping("/sub/{numberOne}/{numberTwo}")
    public Double subtract(@PathVariable(value = "numberOne") String numberOne, @PathVariable(value = "numberTwo") String numberTwo) {
        if(!isNumeric(numberOne) || !isNumeric(numberTwo)) throw new UnsupportedOperationException("Please set a numeric value");
        return MathOperations.sub(numberOne, numberTwo);
    }

    @GetMapping("/mul/{numberOne}/{numberTwo}")
    public Double multiply(@PathVariable(value = "numberOne") String numberOne, @PathVariable(value = "numberTwo") String numberTwo) {
        if(!isNumeric(numberOne) || !isNumeric(numberTwo)) throw new UnsupportedOperationException("Please set a numeric value");
        return MathOperations.mul(numberOne, numberTwo);
    }

    @GetMapping("/div/{numberOne}/{numberTwo}")
    public Double divide(@PathVariable(value = "numberOne") String numberOne, @PathVariable(value = "numberTwo") String numberTwo) {
        if(!isNumeric(numberOne) || !isNumeric(numberTwo)) throw new UnsupportedOperationException("Please set a numeric value");
        return MathOperations.div(numberOne, numberTwo);
    }

    @GetMapping("/exp/{numberOne}/{numberTwo}")
    public Double exponential(@PathVariable(value = "numberOne") String numberOne, @PathVariable(value = "numberTwo") String numberTwo) {
        if(!isNumeric(numberOne) || !isNumeric(numberTwo)) throw new UnsupportedOperationException("Please set a numeric value");
        return MathOperations.exp(numberOne, numberTwo);
    }

    @GetMapping("/squareRoot/{numberOne}")
    public Double squareRoot(@PathVariable(value = "numberOne") String numberOne) {
        if(!isNumeric(numberOne)) throw new UnsupportedOperationException("Please set a numeric value");
        return MathOperations.sqr(numberOne);
    }

    @GetMapping("/mean/{numberOne}/{numberTwo}")
    public Double mean(@PathVariable(value = "numberOne") String numberOne, @PathVariable(value = "numberTwo") String numberTwo) {
        if(!isNumeric(numberOne) || !isNumeric(numberTwo)) throw new UnsupportedOperationException("Please set a numeric value");
        return MathOperations.mean(numberOne, numberTwo);
    }
}
