package main

import (
	"fmt"
	"strings"

	"github.com/Bridouille/advent-of-code/utils"
)

const ACC, NOP, JMP string = "acc", "nop", "jmp"

type instruction struct {
	name  string
	value int
}

func parse(lines []string) []instruction {
	return utils.Map(lines, func(s string) instruction {
		inst, value, _ := strings.Cut(s, " ") // dodo t'es le meilleur
		digitValue := utils.ToInt(value)
		return instruction{
			name:  inst,
			value: digitValue,
		}
	})
}

func execute(instructions []instruction) (int, bool) {
	sum := 0
	visited := make(map[int]bool)
	for i := 0; i < len(instructions); i++ {
		if _, ok := visited[i]; ok { // we have seen this line already
			return sum, false
		}
		visited[i] = true
		if instructions[i].name == ACC {
			sum += instructions[i].value
		}
		if instructions[i].name == JMP {
			i += instructions[i].value - 1
		}
	}
	return sum, true
}

func part1(lines []string) int {
	sum, _ := execute(parse(lines))
	return sum
}

func (i *instruction) replace() {
	if i.name == NOP {
		i.name = JMP
	} else if i.name == JMP {
		i.name = NOP
	}
}

func part2(lines []string) int {
	instructions := parse(lines)
	for idx, _ := range instructions {
		for i := 0; i < 2; i++ {
			sum, worked := execute(instructions)
			if worked {
				return sum
			}
			(&instructions[idx]).replace()
		}
	}
	return -1
}

func main() {
	fmt.Println("(exemple) part1=", part1(utils.ReadFile("./inputs/day08_example.txt")))
	fmt.Println("part1=", part1(utils.ReadFile("./inputs/day08.txt")))
	fmt.Println("(exemple) part2=", part2(utils.ReadFile("./inputs/day08_example.txt")))
	fmt.Println("part2=", part2(utils.ReadFile("./inputs/day08.txt")))
}
