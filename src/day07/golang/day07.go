package main

import (
	"bufio"
	"fmt"
	"math"
	"os"
	"strconv"
	"strings"
)

func calcFuelForValue(guess int, crabPos []int, fuelCalc func(int) int, results chan<- int) {
	fuel := 0
	for _, pos := range crabPos {
		fuel += fuelCalc(Abs(guess - pos))
	}
	results <- fuel
}

func caclFuel(lines []string, fuelCalc func(int) int) int {
	crabPosStr := strings.Split(lines[0], ",")
	crabPos := make([]int, len(crabPosStr))
	for i, pos := range crabPosStr {
		crabPos[i], _ = strconv.Atoi(pos)
	}

	minValue := MinOf(crabPos...)
	maxValue := MaxOf(crabPos...)
	results := make(chan int, maxValue-minValue)
	for i := minValue; i < maxValue; i++ {
		go calcFuelForValue(i, crabPos, fuelCalc, results)
	}

	minFuel := math.MaxInt32
	for i := 0; i < maxValue-minValue; i++ {
		minFuel = MinOf(<-results, minFuel)
	}
	return minFuel
}

func part1(lines []string) int {
	return caclFuel(lines, func(nb int) int {
		return nb
	})
}

func part2(lines []string) int {
	return caclFuel(lines, func(nb int) int {
		return nb * (1 + nb) / 2
	})
}

func main() {

	if len(os.Args) < 2 {
		fmt.Println("Usage: go run {file.go} {filename}")
		return
	}

	file, err := os.Open(os.Args[1])
	if err != nil {
		fmt.Println("Unable to open file", err)
		return
	}
	defer file.Close()

	scanner := bufio.NewScanner(file)
	lines := make([]string, 0)
	for scanner.Scan() {
		lines = append(lines, scanner.Text())
	}
	fmt.Printf("part1 => %d\n", part1(lines))
	fmt.Printf("part2 => %d\n", part2(lines))
}
