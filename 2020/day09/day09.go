package main

import (
	"fmt"
	"strconv"

	"github.com/Bridouille/advent-of-code/utils"
)

func hasSum(numbers []uint64, needle uint64) bool {
	for i := 0; i < len(numbers)-1; i++ {
		for j := i + 1; j < len(numbers); j++ {
			if numbers[i]+numbers[j] == needle {
				return true
			}
		}
	}
	return false
}

func part1(lines []string, toCheck int) uint64 {
	numbers := utils.Map[string, uint64](lines, func(s string) uint64 {
		ret, _ := strconv.ParseUint(s, 10, 32)
		return ret
	})

	for i := toCheck; i < len(numbers); i++ {
		if !hasSum(numbers[i-toCheck:i], numbers[i]) {
			return numbers[i]
		}
	}
	return uint64(0)
}

func part2(lines []string, toCheck int) uint64 {
	firstInvalid := part1(lines, toCheck)
	numbers := utils.Map[string, uint64](lines, func(s string) uint64 {
		ret, _ := strconv.ParseUint(s, 10, 32)
		return ret
	})
	for i := 0; i < len(numbers)-1; i++ {
		sum := numbers[i]
		arr := make([]uint64, 0)
		arr = append(arr, sum)
		for j := i + 1; j < len(numbers); j++ {
			arr = append(arr, numbers[j])
			sum += numbers[j]
			if sum > firstInvalid {
				break
			}
			if sum == firstInvalid {
				return utils.Min(arr) + utils.Max(arr)
			}
		}
	}
	return uint64(0)
}

func main() {
	fmt.Println("(exemple) part1=", part1(utils.ReadFile("./inputs/day09_example.txt"), 5))
	fmt.Println("part1=", part1(utils.ReadFile("./inputs/day09.txt"), 25))
	fmt.Println("(exemple) part2=", part2(utils.ReadFile("./inputs/day09_example.txt"), 5))
	fmt.Println("part2=", part2(utils.ReadFile("./inputs/day09.txt"), 25))
}
