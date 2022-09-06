package main

import (
	"fmt"

	"github.com/Bridouille/advent-of-code/utils"
)

func part1(lines []string) int {
	total := 0
	answers := make(map[rune]bool)
	for _, line := range lines {
		if len(line) == 0 { // new group
			total += len(answers)
			answers = make(map[rune]bool)
		}
		for _, c := range line {
			answers[c] = true
		}
	}
	return total
}

func part2(lines []string) int {
	total := 0
	groupStartIdx := 0
	answers := make(map[rune]int)
	for idx, line := range lines {
		if len(line) == 0 { // new group
			groupSize := idx - groupStartIdx
			for _, v := range answers {
				if v == groupSize {
					total += 1
				}
			}
			answers = make(map[rune]int)
			groupStartIdx = idx + 1
		}
		for _, c := range line {
			answers[c] += 1
		}
	}
	return total
}

func main() {
	fmt.Println("(exemple) part1=", part1(utils.ReadCompleteFile("./inputs/day06_example.txt")))
	fmt.Println("part1=", part1(utils.ReadCompleteFile("./inputs/day06.txt")))
	fmt.Println("(exemple) part2=", part2(utils.ReadCompleteFile("./inputs/day06_example.txt")))
	fmt.Println("part2=", part2(utils.ReadCompleteFile("./inputs/day06.txt")))
}
