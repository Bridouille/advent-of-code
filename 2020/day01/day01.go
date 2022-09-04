package main

import (
	"fmt"

	"github.com/Bridouille/advent-of-code/utils"
)

func part1(lines []string) int {
	intSlice := utils.Map[string, int](lines, func(str string) int {
		return utils.ToInt(str)
	})
	for i := range intSlice {
		for j := i + 1; j < len(intSlice); j++ {
			if intSlice[i]+intSlice[j] == 2020 {
				return intSlice[i] * intSlice[j]
			}
		}
	}
	return -1
}

func part2(lines []string) int {
	intSlice := utils.Map[string, int](lines, func(str string) int {
		return utils.ToInt(str)
	})
	for i := range intSlice {
		for j := i + 1; j < len(intSlice); j++ {
			for k := j + 1; k < len(intSlice); k++ {
				if intSlice[i]+intSlice[j]+intSlice[k] == 2020 {
					return intSlice[i] * intSlice[j] * intSlice[k]
				}
			}
		}
	}
	return -1
}

func main() {
	fmt.Println("(exemple) part1=", part1(utils.ReadFile("./inputs/day01_example.txt")))
	fmt.Println("part1=", part1(utils.ReadFile("./inputs/day01.txt")))
	fmt.Println("(exemple) part2=", part2(utils.ReadFile("./inputs/day01_example.txt")))
	fmt.Println("part2=", part2(utils.ReadFile("./inputs/day01.txt")))
}
