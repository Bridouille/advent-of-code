package main

import (
	"fmt"
	"strings"

	"github.com/Bridouille/advent-of-code/utils"
)

type DbEntry struct {
	min      int
	max      int
	letter   rune
	password string
}

func (entry DbEntry) isValid() bool {
	occurences := 0
	for _, char := range entry.password {
		if char == entry.letter {
			occurences++
		}
	}
	return occurences >= entry.min && occurences <= entry.max
}

func (entry DbEntry) isValid2() bool {
	if entry.max > len(entry.password) {
		return false
	}
	first := entry.password[entry.min-1]
	second := entry.password[entry.max-1]
	return (first == byte(entry.letter) || second == byte(entry.letter)) && first != second
}

func toDbEntries(lines []string) []DbEntry {
	ret := make([]DbEntry, 0)
	for _, str := range lines {
		part := strings.Split(str, " ")
		ret = append(ret, DbEntry{
			min:      utils.ToInt(strings.Split(part[0], "-")[0]),
			max:      utils.ToInt(strings.Split(part[0], "-")[1]),
			letter:   rune(part[1][0]),
			password: part[2],
		})
	}
	return ret
}

func part1(lines []string) int {
	var valids int = 0
	for _, e := range toDbEntries(lines) {
		if e.isValid() {
			valids++
		}
	}
	return valids
}

func part2(lines []string) int {
	var valids int = 0
	for _, e := range toDbEntries(lines) {
		if e.isValid2() {
			valids++
		}
	}
	return valids
}

func main() {
	fmt.Println("(exemple) part1=", part1(utils.ReadFile("./inputs/day02_example.txt")))
	fmt.Println("part1=", part1(utils.ReadFile("./inputs/day02.txt")))
	fmt.Println("(exemple) part2=", part2(utils.ReadFile("./inputs/day02_example.txt")))
	fmt.Println("part2=", part2(utils.ReadFile("./inputs/day02.txt")))
}
