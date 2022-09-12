package main

import (
	"fmt"
	"sort"

	"github.com/Bridouille/advent-of-code/utils"
)

func part1(lines []string) int {
	numbers := utils.Map(lines, utils.ToInt)
	sort.Ints(numbers)
	joltage, oneDiff, threeDiff := 0, 0, 0
	numbers = append(numbers, utils.Max(numbers)+3)
	for i := 0; i < len(numbers); i++ {
		diff := numbers[i] - joltage
		if diff == 1 {
			oneDiff++
		}
		if diff == 3 {
			threeDiff++
		}
		joltage += diff
	}
	return oneDiff * threeDiff
}

func nbWays(memo map[int]int, numbers []int, toFind int) int {
	if toFind < 0 || !utils.Contains(numbers, toFind) {
		return 0
	}
	if toFind == 0 {
		return 1
	}
	if value, exist := memo[toFind]; exist {
		return value
	}
	computed := nbWays(memo, numbers, toFind-1) +
		nbWays(memo, numbers, toFind-2) +
		nbWays(memo, numbers, toFind-3)
	memo[toFind] = computed
	return memo[toFind]
}

func part2(lines []string) int {
	numbers := utils.Map(lines, utils.ToInt)
	numbers = append(numbers, 0)
	memo := make(map[int]int)
	return nbWays(memo, numbers, utils.Max(numbers))
}

func main() {
	fmt.Println("(exemple) part1=", part1(utils.ReadFile("./inputs/day10_example.txt")))
	fmt.Println("part1=", part1(utils.ReadFile("./inputs/day10.txt")))
	fmt.Println("(exemple) part2=", part2(utils.ReadFile("./inputs/day10_example.txt")))
	fmt.Println("(exemple dodo) part2=", part2(utils.ReadFile("./inputs/day10_example_2.txt")))
	fmt.Println("part2=", part2(utils.ReadFile("./inputs/day10.txt")))
}
