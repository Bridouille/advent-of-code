package main

import (
	"fmt"

	"github.com/Bridouille/advent-of-code/utils"
)

func treesOnMap(lines []string, xStep int, yStep int) int {
	width := len(lines[0])
	height := len(lines)
	trees := 0
	for x, y := 0, 0; y < height; y += yStep {
		if lines[y][x] == '#' {
			trees++
		}
		x += xStep
		x %= width
	}
	return trees
}

func part1(lines []string) int {
	return treesOnMap(lines, 3, 1)
}

func part2(lines []string) int {
	return treesOnMap(lines, 1, 1) * treesOnMap(lines, 3, 1) *
		treesOnMap(lines, 5, 1) * treesOnMap(lines, 7, 1) * treesOnMap(lines, 1, 2)
}

func main() {
	fmt.Println("(exemple) part1=", part1(utils.ReadFile("./inputs/day03_example.txt")))
	fmt.Println("part1=", part1(utils.ReadFile("./inputs/day03.txt")))
	fmt.Println("(exemple) part2=", part2(utils.ReadFile("./inputs/day03_example.txt")))
	fmt.Println("part2=", part2(utils.ReadFile("./inputs/day03.txt")))
}
