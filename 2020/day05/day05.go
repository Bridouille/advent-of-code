package main

import (
	"fmt"
	"sort"

	"github.com/Bridouille/advent-of-code/utils"
)

func getRow(str string, min int, max int) int {
	if len(str) == 0 {
		return min
	}
	half := (max - min) / 2
	if str[0] == 'F' {
		return getRow(str[1:], min, max-half-1)
	}
	return getRow(str[1:], min+half+1, max)
}

func getColumn(str string, min int, max int) int {
	if len(str) == 0 {
		return min
	}
	half := (max - min) / 2
	if str[0] == 'L' {
		return getColumn(str[1:], min, max-half-1)
	}
	return getColumn(str[1:], min+half+1, max)
}

func part1(lines []string) int {
	max := 0
	for _, line := range lines {
		row := getRow(line[:7], 0, 127)
		colum := getColumn(line[7:], 0, 7)
		seatId := row*8 + colum
		if seatId > max {
			max = seatId
		}
	}
	return max
}

func part2(lines []string) int {
	seatIds := make([]int, 0)
	for _, line := range lines {
		row := getRow(line[:7], 0, 127)
		colum := getColumn(line[7:], 0, 7)
		seatId := row*8 + colum
		seatIds = append(seatIds, seatId)
	}
	sort.Ints(seatIds)
	for i := 1; i < len(seatIds); i++ {
		if seatIds[i] == seatIds[i-1]+2 {
			return seatIds[i-1] + 1
		}
	}
	return -1
}

func main() {
	fmt.Println("(exemple) part1=", part1(utils.ReadFile("./inputs/day05_example.txt")))
	fmt.Println("part1=", part1(utils.ReadFile("./inputs/day05.txt")))
	fmt.Println("(exemple) part2=", part2(utils.ReadFile("./inputs/day05_example.txt")))
	fmt.Println("part2=", part2(utils.ReadFile("./inputs/day05.txt")))
}
