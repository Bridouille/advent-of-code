package main

import (
	"bufio"
	"fmt"
	"os"
	"sort"
	"strings"
)

type Point struct {
	X     int
	Y     int
	Value int
}

func getLowPoints(values [][]int) []Point {
	points := make([]Point, 0)

	for y, _ := range values {
		for x, _ := range values[y] {
			top := y == 0 || values[y-1][x] > values[y][x]
			left := x == 0 || values[y][x-1] > values[y][x]
			right := x+1 == len(values[y]) || values[y][x+1] > values[y][x]
			bottom := y+1 == len(values) || values[y+1][x] > values[y][x]

			if top && left && right && bottom {
				points = append(points, Point{x, y, values[y][x]})
				values[y][x] = -1 // mark the current position visited
			}
		}
	}
	return points
}

func part1(values [][]int) int {
	lowPoints := getLowPoints(values)

	sum := 0
	for _, point := range lowPoints {
		sum += 1 + point.Value
	}
	return sum
}

func calcBassinSize(values [][]int, x int, y int) int {
	if x < 0 || y < 0 || y >= len(values) || x >= len(values[y]) || values[y][x] == 9 {
		return 0
	}
	current := values[y][x]
	values[y][x] = -1 // mark the current position visited
	size := 0
	if x > 0 && values[y][x-1] > current { // left
		size += calcBassinSize(values, x-1, y)
	}
	if y > 0 && values[y-1][x] > current { // top
		size += calcBassinSize(values, x, y-1)
	}
	if y+1 < len(values) && values[y+1][x] > current { // bottom
		size += calcBassinSize(values, x, y+1)
	}
	if x+1 < len(values[y]) && values[y][x+1] > current { // rght
		size += calcBassinSize(values, x+1, y)
	}
	return 1 + size
}

func part2(values [][]int) int {
	lowPoints := getLowPoints(copyArray(values))
	basinsSizes := make([]int, 0)

	for _, lowPoint := range lowPoints {
		basinsSizes = append(basinsSizes, calcBassinSize(values, lowPoint.X, lowPoint.Y))
	}

	sort.Slice(basinsSizes, func(i, j int) bool {
		return basinsSizes[j] < basinsSizes[i]
	})
	return basinsSizes[0] * basinsSizes[1] * basinsSizes[2]
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
		l := strings.TrimSpace(scanner.Text())
		if len(l) > 0 {
			lines = append(lines, l)
		}
	}
	values := make([][]int, len(lines))
	for i, line := range lines {
		values[i] = make([]int, len(line))
		for idx, digit := range line {
			values[i][idx] = int(digit - '0')
		}
	}
	values2 := copyArray(values)
	fmt.Printf("part1 => %d\n", part1(values))
	fmt.Printf("part2 => %d\n", part2(values2))
}
