package main

import (
	"fmt"

	"github.com/Bridouille/advent-of-code/utils"
)

type Ship struct {
	x, y, direction int
}

var moves = map[rune]func(s *Ship, value int){
	'N': func(s *Ship, value int) { s.y -= value },
	'S': func(s *Ship, value int) { s.y += value },
	'E': func(s *Ship, value int) { s.x += value },
	'W': func(s *Ship, value int) { s.x -= value },
	'L': func(s *Ship, value int) {
		s.direction = s.direction - value
		if s.direction < 0 {
			s.direction = 360 + s.direction
		}
	},
	'R': func(s *Ship, value int) {
		s.direction = (s.direction + value) % 360
	},
}
var forward = map[int]func(s *Ship, value int){
	0:   moves['E'],
	90:  moves['S'],
	180: moves['W'],
	270: moves['N'],
}

func part1(lines []string) int {
	ship := Ship{x: 0, y: 0, direction: 0}
	for _, line := range lines {
		inst := rune(line[0])
		value := utils.ToInt(line[1:])
		if inst == 'F' {
			forward[ship.direction](&ship, value)
		} else {
			moves[inst](&ship, value)
		}
	}
	return utils.Abs(ship.x) + utils.Abs(ship.y)
}

var rotateUp = func(s *Ship) {
	tmp := s.x
	s.x = s.y
	s.y = -tmp
}
var rotateBack = func(s *Ship) {
	s.x *= -1
	s.y *= -1
}
var rotateDown = func(s *Ship) {
	tmp := s.x
	s.x = -s.y
	s.y = tmp
}

// https://www.mometrix.com/academy/rotation/
var rotate = map[string]func(*Ship){
	"R90":  rotateDown,
	"R180": rotateBack,
	"R270": rotateUp,
	"L90":  rotateUp,
	"L180": rotateBack,
	"L270": rotateDown,
}

func part2(lines []string) int {
	ship := Ship{x: 0, y: 0, direction: 0}
	waypoint := Ship{x: 10, y: -1, direction: 0}
	for _, line := range lines {
		inst := rune(line[0])
		value := utils.ToInt(line[1:])
		if inst == 'F' {
			ship.x += value * waypoint.x
			ship.y += value * waypoint.y
		} else if inst == 'R' || inst == 'L' {
			rotate[line](&waypoint)
		} else {
			moves[inst](&waypoint, value)
		}
	}
	return utils.Abs(ship.x) + utils.Abs(ship.y)
}

func main() {
	fmt.Println("(exemple) part1=", part1(utils.ReadFile("./inputs/day12_example.txt")))
	fmt.Println("part1=", part1(utils.ReadFile("./inputs/day12.txt")))
	fmt.Println("(exemple) part2=", part2(utils.ReadFile("./inputs/day12_example.txt")))
	fmt.Println("part2=", part2(utils.ReadFile("./inputs/day12.txt")))
}
