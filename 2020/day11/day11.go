package main

import (
	"fmt"

	"github.com/Bridouille/advent-of-code/utils"
)

func occupiedV1(seats [][]rune, x int, y int) int {
	total := 0
	for i := -1; i < 2; i++ {
		for j := -1; j < 2; j++ {
			xIdx := x + i
			yIdx := y + j
			if xIdx >= 0 && xIdx < len(seats[0]) &&
				yIdx >= 0 && yIdx < len(seats) && !(i == 0 && j == 0) {
				if seats[yIdx][xIdx] == '#' {
					total++
				}
			}
		}
	}
	return total
}

func occupiedV2(seats [][]rune, x int, y int) int {
	vision := make(map[string]rune)
	for i := 1; i < len(seats[0]); i++ {
		if y-i >= 0 { // top
			top := seats[y-i][x]
			if _, present := vision["top"]; !present {
				if top == 'L' || top == '#' {
					vision["top"] = top
				}
			}
		}
		if y+i < len(seats) { // bottom
			bottom := seats[y+i][x]
			if _, present := vision["bottom"]; !present {
				if bottom == 'L' || bottom == '#' {
					vision["bottom"] = bottom
				}
			}
		}
		if x-i >= 0 { // left
			left := seats[y][x-i]
			if _, present := vision["left"]; !present {
				if left == 'L' || left == '#' {
					vision["left"] = left
				}
			}
		}
		if x+i < len(seats[0]) { // right
			right := seats[y][x+i]
			if _, present := vision["right"]; !present {
				if right == 'L' || right == '#' {
					vision["right"] = right
				}
			}
		}

		// diago
		if y-i >= 0 && x-i >= 0 { // topLeft
			topLeft := seats[y-i][x-i]
			if _, present := vision["topLeft"]; !present {
				if topLeft == 'L' || topLeft == '#' {
					vision["topLeft"] = topLeft
				}
			}
		}
		if y-i >= 0 && x+i < len(seats[0]) { // topRight
			topRight := seats[y-i][x+i]
			if _, present := vision["topRight"]; !present {
				if topRight == 'L' || topRight == '#' {
					vision["topRight"] = topRight
				}
			}
		}
		if y+i < len(seats) && x-i >= 0 { // bottomLeft
			bottomLeft := seats[y+i][x-i]
			if _, present := vision["bottomLeft"]; !present {
				if bottomLeft == 'L' || bottomLeft == '#' {
					vision["bottomLeft"] = bottomLeft
				}
			}
		}
		if y+i < len(seats) && x+i < len(seats[0]) { // bottomRight
			bottomRight := seats[y+i][x+i]
			if _, present := vision["bottomRight"]; !present {
				if bottomRight == 'L' || bottomRight == '#' {
					vision["bottomRight"] = bottomRight
				}
			}
		}
	}
	total := 0
	for _, v := range vision {
		if v == '#' {
			total++
		}
	}
	return total
}

// returns isSame, and the number of occupied seats
func isSame(a [][]rune, b [][]rune) (bool, int) {
	if len(a) != len(b) {
		return false, 0
	}
	totalOccupied := 0
	for y, line := range a {
		for x, _ := range line {
			if a[y][x] != b[y][x] {
				return false, 0
			}
			if a[y][x] == '#' {
				totalOccupied++
			}
		}
	}
	return true, totalOccupied
}

func iterateUntilBlockedAndCount(lines []string, occupiedFunc func([][]rune, int, int) int, occupiedNb int) int {
	grid := utils.Map(lines, func(s string) []rune {
		return []rune(s)
	})
	for {
		tmp := make([][]rune, len(grid))
		for y, line := range grid {
			tmp[y] = make([]rune, len(line))
			for x, _ := range line {
				occupied := occupiedFunc(grid, x, y)
				if grid[y][x] == 'L' && occupied == 0 {
					tmp[y][x] = '#'
				} else if grid[y][x] == '#' && occupied >= occupiedNb {
					tmp[y][x] = 'L'
				} else {
					tmp[y][x] = grid[y][x]
				}
			}
		}
		if bothEquals, occupied := isSame(grid, tmp); bothEquals {
			return occupied
		}
		grid = tmp
	}
}

func part1(lines []string) int {
	return iterateUntilBlockedAndCount(lines, occupiedV1, 4)
}

func part2(lines []string) int {
	return iterateUntilBlockedAndCount(lines, occupiedV2, 5)
}

func main() {
	fmt.Println("(exemple) part1=", part1(utils.ReadFile("./inputs/day11_example.txt")))
	fmt.Println("part1=", part1(utils.ReadFile("./inputs/day11.txt")))
	fmt.Println("(exemple) part2=", part2(utils.ReadFile("./inputs/day11_example.txt")))
	fmt.Println("part2=", part2(utils.ReadFile("./inputs/day11.txt")))
}
